package tech.cerberusLabs

import java.net.InetAddress
import java.text.SimpleDateFormat
import java.util.*

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

        val textList = listOf(
            "",
            "LicenseManager v1.0.0",
            "Made by Cerberus-Labs.tech",
            "Authored by Kelvin Bill",
            "License Key: ${config.licenseKey}",
            "User ID: ${config.userId}",
            "Product ID: ${config.productId}",
            ""
        )
        printRectangle(textList)
    }

    private val licenseServerUrl = "https://backend.cerberus-labs.tech/api/v1/license/${config.licenseKey}"
    private val privateUrl = " http://127.0.0.1:25818/api/v1/license/${config.licenseKey}"

    //TODO: add config handler
    private fun loadConfig(): Config {
        this.config = Config("test", 1, 5)
        return this.config
    }

    private fun isValid(): Boolean {
        val license = kotlin.runCatching {
            makeHttpGetRequest<License>(privateUrl)
        }.onFailure {
            println("Error: " + it.message)
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
        if (isValid()) validCallback() else nonValidCallback()
    }

    private fun getCurrentIpAddress(): String {
        val address = InetAddress.getLocalHost()
        println(address.hostAddress)
        return address.hostAddress
    }

}
data class Config(var licenseKey: String, var userId: Int, var productId: Int)
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