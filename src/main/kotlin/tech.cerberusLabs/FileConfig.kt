package tech.cerberusLabs

import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.*
import java.io.File

class FileConfig(name: String) {

    private val file = File("license/$name.json")

    fun createConfig() {
        if (!file.parentFile.exists()) {
            file.parentFile.mkdirs()
            this.createFile()
        } else {
            this.createFile()
            val configs = listOf(Config("key", 1, 1), Config("key2", 2, 2))
            set("configs", configs)
            delete("configs")
        }
    }

    private fun createFile() {
        if (!file.exists()) {
            file.createNewFile()
            val newConfig = JsonObject(emptyMap())
            val emptyJson = Json.encodeToString(newConfig)
            file.writeText(emptyJson)
            set("licenseKey", "KEY HERE")
            set("userId", 0)
            set("productId", 1)
        }
    }

    fun getString(key: String): String? {
        val jsonString = file.readText()
        val jsonElement = Json.parseToJsonElement(jsonString)
        return jsonElement.jsonObject[key]?.jsonPrimitive?.content
    }

    fun getBoolean(key: String): Boolean? {
        val jsonString = file.readText()
        val jsonElement = Json.parseToJsonElement(jsonString)
        return jsonElement.jsonObject[key]?.jsonPrimitive?.boolean
    }

    fun getInt(key: String): Int? {
        val jsonString = file.readText()
        val jsonElement = Json.parseToJsonElement(jsonString)
        return jsonElement.jsonObject[key]?.jsonPrimitive?.int
    }

    fun getLong(key: String): Long? {
        val jsonString = file.readText()
        val jsonElement = Json.parseToJsonElement(jsonString)
        return jsonElement.jsonObject[key]?.jsonPrimitive?.long
    }

    fun getDouble(key: String): Double? {
        val jsonString = file.readText()
        val jsonElement = Json.parseToJsonElement(jsonString)
        return jsonElement.jsonObject[key]?.jsonPrimitive?.double
    }

    fun getFloat(key: String): Float? {
        val jsonString = file.readText()
        val jsonElement = Json.parseToJsonElement(jsonString)
        return jsonElement.jsonObject[key]?.jsonPrimitive?.float
    }

    fun getStringArray(key: String): List<String>? {
        val jsonString = file.readText()
        val jsonElement = Json.parseToJsonElement(jsonString)
        return jsonElement.jsonObject[key]?.jsonArray?.map { it.jsonPrimitive.content }
    }

    fun getBooleanArray(key: String): List<Boolean>? {
        val jsonString = file.readText()
        val jsonElement = Json.parseToJsonElement(jsonString)
        return jsonElement.jsonObject[key]?.jsonArray?.map { it.jsonPrimitive.boolean }
    }

    fun getIntArray(key: String): List<Int>? {
        val jsonString = file.readText()
        val jsonElement = Json.parseToJsonElement(jsonString)
        return jsonElement.jsonObject[key]?.jsonArray?.map { it.jsonPrimitive.int }
    }

    fun getLongArray(key: String): List<Long>? {
        val jsonString = file.readText()
        val jsonElement = Json.parseToJsonElement(jsonString)
        return jsonElement.jsonObject[key]?.jsonArray?.map { it.jsonPrimitive.long }
    }

    fun getDoubleArray(key: String): List<Double>? {
        val jsonString = file.readText()
        val jsonElement = Json.parseToJsonElement(jsonString)
        return jsonElement.jsonObject[key]?.jsonArray?.map { it.jsonPrimitive.double }
    }

    fun getFloatArray(key: String): List<Float>? {
        val jsonString = file.readText()
        val jsonElement = Json.parseToJsonElement(jsonString)
        return jsonElement.jsonObject[key]?.jsonArray?.map { it.jsonPrimitive.float }
    }

    fun getJsonObject(key: String): JsonObject? {
        val jsonString = file.readText()
        val jsonElement = Json.parseToJsonElement(jsonString)
        return jsonElement.jsonObject[key]?.jsonObject
    }

    internal inline fun <reified T> getArray(key: String): List<T>? {
        val jsonString = file.readText()
        val jsonElement = Json.parseToJsonElement(jsonString)
        return jsonElement.jsonObject[key]?.jsonArray?.map { Json.decodeFromString(it.toString()) }
    }

    fun set(key: String, value: Any) {
        val jsonString = file.readText()
        val jsonObject = JsonParser.parseString(jsonString).asJsonObject
        when (value) {
            is String -> {
                jsonObject.addProperty(key, value)
            }

            is Boolean -> {
                jsonObject.addProperty(key, value)
            }

            is Number -> {
                jsonObject.addProperty(key, value)
            }

            is List<*>, is Array<*> -> {
                val gson = GsonBuilder().create()
                val jsonArray = gson.toJsonTree(value).asJsonArray
                jsonObject.add(key, jsonArray)
            }

            else -> {
                throw IllegalArgumentException("Unsupported value type: ${value.javaClass}")
            }
        }
        val gson = GsonBuilder().setPrettyPrinting().create()
        val newJsonString = gson.toJson(jsonObject)
        file.writeText(newJsonString)
    }

    fun delete(key: String) {
        val jsonString = file.readText()
        val jsonElement = Json.parseToJsonElement(jsonString)
        val newConfig = jsonElement.jsonObject.toMutableMap()
        newConfig.remove(key)
        val updatedJson = Json.encodeToJsonElement(JsonObject(newConfig))
        val updatedJsonString = Json.encodeToString(updatedJson)
        file.writeText(updatedJsonString)
    }


    fun toLicenseConfig(): Config {
        return Config(
            getString("licenseKey")!!,
            getInt("userId")!!,
            getInt("productId")!!
        )
    }
}