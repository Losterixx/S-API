package dev.losterixx.sapi.utils.uuid

import com.google.gson.JsonParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL
import java.util.*

object UUIDUtil {
    suspend fun getUUIDByName(name: String): UUID? = withContext(Dispatchers.IO) {
        try {
            val url = URL("https://api.mojang.com/users/profiles/minecraft/$name")
            val conn = url.openConnection() as HttpURLConnection
            conn.requestMethod = "GET"
            conn.connectTimeout = 5000
            conn.readTimeout = 5000

            if (conn.responseCode == 200) {
                val json = conn.inputStream.bufferedReader().readText()
                val id = JsonParser.parseString(json).asJsonObject.get("id").asString
                UUID.fromString(
                    id.replaceFirst(
                        "(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})".toRegex(),
                        "$1-$2-$3-$4-$5"
                    )
                )
            } else null
        } catch (e: Exception) {
            null
        }
    }

    suspend fun getNameByUUID(uuid: UUID): String? = withContext(Dispatchers.IO) {
        try {
            val url = URL("https://sessionserver.mojang.com/session/minecraft/profile/${uuid.toString().replace("-", "")}")
            val conn = url.openConnection() as HttpURLConnection
            conn.requestMethod = "GET"
            conn.connectTimeout = 5000
            conn.readTimeout = 5000

            if (conn.responseCode == 200) {
                val json = conn.inputStream.bufferedReader().readText()
                JsonParser.parseString(json).asJsonObject.get("name").asString
            } else null
        } catch (e: Exception) {
            null
        }
    }
}