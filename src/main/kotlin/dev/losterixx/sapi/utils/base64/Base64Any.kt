package dev.losterixx.sapi.utils.base64

import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder
import java.io.*

object Base64Any {

    @Throws(IOException::class)
    fun arrayFromBase64(data: String?): Array<Any?> {
        if (data.isNullOrEmpty()) return emptyArray()

        try {
            ByteArrayInputStream(Base64Coder.decodeLines(data)).use { inputStream ->
                ObjectInputStream(inputStream).use { dataInput ->
                    val size = dataInput.readInt()
                    return Array(size) { dataInput.readObject() }
                }
            }
        } catch (e: ClassNotFoundException) {
            throw IOException("Unable to decode class type.", e)
        }
    }

    @Throws(IllegalStateException::class)
    fun arrayToBase64(items: Array<Any?>): String {
        try {
            ByteArrayOutputStream().use { outputStream ->
                ObjectOutputStream(outputStream).use { dataOutput ->
                    dataOutput.writeInt(items.size)
                    items.forEach { dataOutput.writeObject(it) }
                }
                return Base64Coder.encodeLines(outputStream.toByteArray())
            }
        } catch (e: Exception) {
            throw IllegalStateException("Unable to save variables.", e)
        }
    }

    @Throws(IOException::class)
    fun fromBase64(data: String?): Any? {
        return arrayFromBase64(data).firstOrNull()
    }

    @Throws(IllegalStateException::class)
    fun toBase64(item: Any?): String {
        return arrayToBase64(arrayOf(item))
    }
}
