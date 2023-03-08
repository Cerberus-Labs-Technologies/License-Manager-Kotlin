package tech.cerberusLabs

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.*
import java.io.File
import kotlin.reflect.KClass

class FileConfig(name: String) {

    private val file = File("license/$name.json")

    fun createConfig() {
          // Create the directory if it doesn't exist
        if (!file.parentFile.exists()) {
            file.parentFile.mkdirs()
            file.createNewFile()
            val newConfig = JsonObject(emptyMap())
            val emptyJson = Json.encodeToString(newConfig)

            // Write the JSON object to the file
            file.writeText(emptyJson)
            // Create a new empty JSON object


            set("licenseKey", "KEY HERE")
            set("userId", 0)
            set("productId", 1)
        } else {
            println("Config already exists. Skipping creation...")
        }
    }

    fun getString(key: String): String? {
        // Read the JSON string from the file with the given configName
        val jsonString = file.readText()
        // Parse the JSON string into a dynamic JsonElement
        val jsonElement = Json.parseToJsonElement(jsonString)
        // Get the value associated with the given key as a string, or null if the key doesn't exist
        return jsonElement.jsonObject[key]?.jsonPrimitive?.content
    }

    fun getBoolean(key: String): Boolean? {
        // Read the JSON string from the file with the given configName
        val jsonString = file.readText()
        // Parse the JSON string into a dynamic JsonElement
        val jsonElement = Json.parseToJsonElement(jsonString)
        // Get the value associated with the given key as a boolean, or null if the key doesn't exist
        return jsonElement.jsonObject[key]?.jsonPrimitive?.boolean
    }

    fun getInt(key: String): Int? {
        // Read the JSON string from the file with the given configName
        val jsonString = file.readText()
        // Parse the JSON string into a dynamic JsonElement
        val jsonElement = Json.parseToJsonElement(jsonString)
        // Get the value associated with the given key as an int, or null if the key doesn't exist
        return jsonElement.jsonObject[key]?.jsonPrimitive?.int
    }

    fun getLong(key: String): Long? {
        // Read the JSON string from the file with the given configName
        val jsonString = file.readText()
        // Parse the JSON string into a dynamic JsonElement
        val jsonElement = Json.parseToJsonElement(jsonString)
        // Get the value associated with the given key as a long, or null if the key doesn't exist
        return jsonElement.jsonObject[key]?.jsonPrimitive?.long
    }

    fun getDouble(key: String): Double? {
        // Read the JSON string from the file with the given configName
        val jsonString = file.readText()
        // Parse the JSON string into a dynamic JsonElement
        val jsonElement = Json.parseToJsonElement(jsonString)
        // Get the value associated with the given key as a double, or null if the key doesn't exist
        return jsonElement.jsonObject[key]?.jsonPrimitive?.double
    }

    fun getFloat(key: String): Float? {
        // Read the JSON string from the file with the given configName
        val jsonString = file.readText()
        // Parse the JSON string into a dynamic JsonElement
        val jsonElement = Json.parseToJsonElement(jsonString)
        // Get the value associated with the given key as a float, or null if the key doesn't exist
        return jsonElement.jsonObject[key]?.jsonPrimitive?.float
    }

    fun getStringArray(key: String): List<String>? {
        // Read the JSON string from the file with the given configName
        val jsonString = file.readText()
        // Parse the JSON string into a dynamic JsonElement
        val jsonElement = Json.parseToJsonElement(jsonString)
        // Get the value associated with the given key as a string array, or null if the key doesn't exist
        return jsonElement.jsonObject[key]?.jsonArray?.map { it.jsonPrimitive.content }
    }

    fun getBooleanArray(key: String): List<Boolean>? {
        // Read the JSON string from the file with the given configName
        val jsonString = file.readText()
        // Parse the JSON string into a dynamic JsonElement
        val jsonElement = Json.parseToJsonElement(jsonString)
        // Get the value associated with the given key as a boolean array, or null if the key doesn't exist
        return jsonElement.jsonObject[key]?.jsonArray?.map { it.jsonPrimitive.boolean }
    }

    fun getIntArray(key: String): List<Int>? {
        // Read the JSON string from the file with the given configName
        val jsonString = file.readText()
        // Parse the JSON string into a dynamic JsonElement
        val jsonElement = Json.parseToJsonElement(jsonString)
        // Get the value associated with the given key as an int array, or null if the key doesn't exist
        return jsonElement.jsonObject[key]?.jsonArray?.map { it.jsonPrimitive.int }
    }

    fun getLongArray(key: String): List<Long>? {
        // Read the JSON string from the file with the given configName
        val jsonString = file.readText()
        // Parse the JSON string into a dynamic JsonElement
        val jsonElement = Json.parseToJsonElement(jsonString)
        // Get the value associated with the given key as a long array, or null if the key doesn't exist
        return jsonElement.jsonObject[key]?.jsonArray?.map { it.jsonPrimitive.long }
    }

    fun getDoubleArray(key: String): List<Double>? {
        // Read the JSON string from the file with the given configName
        val jsonString = file.readText()
        // Parse the JSON string into a dynamic JsonElement
        val jsonElement = Json.parseToJsonElement(jsonString)
        // Get the value associated with the given key as a double array, or null if the key doesn't exist
        return jsonElement.jsonObject[key]?.jsonArray?.map { it.jsonPrimitive.double }
    }

    fun getFloatArray(key: String): List<Float>? {
        // Read the JSON string from the file with the given configName
        val jsonString = file.readText()
        // Parse the JSON string into a dynamic JsonElement
        val jsonElement = Json.parseToJsonElement(jsonString)
        // Get the value associated with the given key as a float array, or null if the key doesn't exist
        return jsonElement.jsonObject[key]?.jsonArray?.map { it.jsonPrimitive.float }
    }

    fun getJsonObject(key: String): JsonObject? {
        // Read the JSON string from the file with the given configName
        val jsonString = file.readText()
        // Parse the JSON string into a dynamic JsonElement
        val jsonElement = Json.parseToJsonElement(jsonString)
        // Get the value associated with the given key as a JSON object, or null if the key doesn't exist
        return jsonElement.jsonObject[key]?.jsonObject
    }

    internal inline fun <reified T> getArray(key: String): List<T>? {
        // Read the JSON string from the file with the given configName
        val jsonString = file.readText()
        // Parse the JSON string into a dynamic JsonElement
        val jsonElement = Json.parseToJsonElement(jsonString)
        // Get the value associated with the given key as a JSON array, or null if the key doesn't exist
        return jsonElement.jsonObject[key]?.jsonArray?.map { Json.decodeFromString(it.toString()) }
    }

    fun set(key: String, value: Any) {
        // Read the JSON string from the file with the given configName
        val jsonString = file.readText()

        // Parse the JSON string into a dynamic JsonElement
        val jsonElement = Json.parseToJsonElement(jsonString)

        // Create a new JSON object with the updated value
        val newConfig = jsonElement.jsonObject.toMutableMap()
        when (value) {
            is String -> newConfig[key] = JsonPrimitive(value)
            is Number -> newConfig[key] = JsonPrimitive(value)
            is Boolean -> newConfig[key] = JsonPrimitive(value)
            else -> throw IllegalArgumentException("Invalid value type: ${value.javaClass}")
        }
        val updatedJson = JsonObject(newConfig)

        // Serialize the updated JSON object to a string
        val updatedJsonString = Json.encodeToString(updatedJson)
        // Write the string back to the file
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