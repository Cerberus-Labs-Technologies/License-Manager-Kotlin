package tech.cerberusLabs

import com.google.gson.Gson
import java.net.HttpURLConnection
import java.net.URL

internal inline fun <reified T> makeHttpGetRequest(url: String): T {
    val connection = URL(url).openConnection() as HttpURLConnection
    connection.requestMethod = "GET"
    connection.connectTimeout = 5000
    connection.readTimeout = 5000

    val responseCode = connection.responseCode
    if (responseCode != HttpURLConnection.HTTP_OK) {
        when (responseCode) {
            LicenseStatus.INACTIVE.httpCode -> {
                throw Exception("The license is inactive!")
            }

            LicenseStatus.BLOCKED.httpCode -> {
                throw Exception("The license is blocked!")
            }

            LicenseStatus.EXPIRED.httpCode -> {
                throw Exception("The license is expired!")
            }

            LicenseStatus.NOT_FOUND.httpCode -> {
                throw Exception("The license is not found!")
            }
        }
        throw Exception("An error occurred while trying to connect to the license server!")

    }

    val inputStream = connection.inputStream
    val content = inputStream.bufferedReader().use { it.readText() }
    inputStream.close()

    connection.disconnect()

    val gson = Gson()
    val result = gson.fromJson(content, RestResult::class.java)
    val data = gson.toJson(result.data)
    return gson.fromJson(data, T::class.java)
}

internal data class RestResult(val data: Any, val success: Boolean)

internal enum class LicenseStatus(val httpCode: Int) {
    INACTIVE(403),
    BLOCKED(401),
    EXPIRED(406),
    NOT_FOUND(404)
}

data class Config(var licenseKey: String, var userId: Int, var productId: Int)

internal fun List<String>.printRectangle() {
    val maxLen = this.maxOfOrNull { it.length }?.plus(7) ?: 0
    println("#".repeat(maxLen + 1))
    for ((index, text) in this.withIndex()) {
        val paddingSize = maxLen - text.length - 2
        val paddingStart = paddingSize / 2
        val paddingEnd = paddingSize - paddingStart
        when (index) {
            0 -> {
                println("# ${" ".repeat(paddingStart)}$text${" ".repeat(paddingEnd)}#")
            }
            this.lastIndex -> {
                println("#${" ".repeat(paddingStart)}$text${" ".repeat(paddingEnd)} #")
            }
            else -> {
                println("# ${" ".repeat(paddingStart)}$text${" ".repeat(paddingEnd)}#")
            }
        }
    }
    println("#".repeat(maxLen + 1))
}



