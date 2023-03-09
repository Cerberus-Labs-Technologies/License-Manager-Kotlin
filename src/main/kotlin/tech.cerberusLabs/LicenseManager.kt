package tech.cerberusLabs

import kotlinx.serialization.Serializable
import java.net.InetAddress
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

/**
 * LicenseManager is a class that manages the license for a product.
 *
 * This library also includes an option to use its config handler to read the license from a config file
 * and also create a template config file on its first run.
 *
 * Furthermore, this library supports an auto-updater that can be used to update the product.
 * Please use the auto-updater with caution as it can be dangerous if not used properly.
 *
 * The license is checked on every run of the product and on an interval of 1 hour.
 *
 * @author Kelvin Bill
 * @since 1.0.0
 * @version 1.0.0
 */
class LicenseManager(useConfig: Boolean = true, licenseKey: String = "", userId: Int = 0, productId: Int = 0) {

    private var config: Config

    init {
        if (useConfig)
            this.config = loadConfig()
        else
            this.config = Config(licenseKey, userId, productId)

    }

    // TODO: change to actual license server
    private val licenseServerUrl = "https://backend.cerberus-labs.tech/api/v1/license/${config.licenseKey}"
    private val privateUrl = " http://127.0.0.1:25818/api/v1/license/${config.licenseKey}"

    private fun loadConfig(): Config {
        val fileConfig = FileConfig("license")
        fileConfig.createConfig()
        this.config = fileConfig.toLicenseConfig()
        return this.config
    }

    private fun isValid(): Boolean {
        val license = kotlin.runCatching {
            makeHttpGetRequest<License>(privateUrl)
        }.onFailure {
            println(it.stackTraceToString())
            return false
        }.getOrThrow()
        val dateString = license.expires
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        license.expiresAt = dateFormat.parse(dateString)
        if (license.productId != config.productId) return false
        if (license.userId != config.userId) return false
        if (!license.isIpBound(getCurrentIpAddress())) return false
        if (!license.active) return false
        if (!license.permanent && license.expiresAt.before(Date())) return false
        return true
    }

    fun validate(validCallback: () -> Unit, nonValidCallback: () -> Unit) {
        val validity = isValid()
        if (validity) validCallback() else nonValidCallback()
        val textList = listOf(
            "",
            "LicenseManager v1.0.0",
            "Made by Cerberus-Labs.tech",
            "Authored by Kelvin Bill",
            "License Key: ${config.licenseKey}",
            "User ID: ${config.userId}",
            "Product ID: ${config.productId}",
            "Your IP Address: ${getCurrentIpAddress()}",
            "License is: " + if (validity) "valid" else "invalid",
            ""
        )
        printRectangle(textList)
        startLicenseChecker(nonValidCallback)
    }

    private fun startLicenseChecker(nonValidCallback: () -> Unit) {
        val executor = Executors.newScheduledThreadPool(1)
        executor.scheduleAtFixedRate({
            if (!isValid()) {
                nonValidCallback()
            }
        }, 0, 1, TimeUnit.HOURS)
    }

    private fun getCurrentIpAddress(): String {
        val address = InetAddress.getLocalHost()
        return address.hostAddress
    }

}
internal data class License(
    val productId: Int,
    val userId: Int,
    val license: String,
    val boundIPs: List<String>,
    val sponsored: Boolean,
    val active: Boolean,
    val permanent: Boolean,
    val expires: String,
    var expiresAt: Date) {

    fun isIpBound(ip: String): Boolean {
        return boundIPs.contains(ip)
    }
}