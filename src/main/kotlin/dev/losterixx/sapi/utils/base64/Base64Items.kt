package dev.losterixx.sapi.utils.base64

import org.bukkit.inventory.ItemStack
import org.bukkit.util.io.BukkitObjectInputStream
import org.bukkit.util.io.BukkitObjectOutputStream
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.IOException

object Base64Items {

    @Throws(IOException::class)
    fun itemStackArrayFromBase64(data: String?): Array<ItemStack?> {
        if (data.isNullOrEmpty()) return emptyArray()

        try {
            ByteArrayInputStream(Base64Coder.decodeLines(data)).use { inputStream ->
                BukkitObjectInputStream(inputStream).use { dataInput ->
                    val size = dataInput.readInt()
                    return Array(size) { dataInput.readObject() as ItemStack? }
                }
            }
        } catch (e: ClassNotFoundException) {
            throw IOException("Unable to decode class type.", e)
        }
    }

    @Throws(IllegalStateException::class)
    fun itemStackArrayToBase64(items: Array<ItemStack?>): String {
        try {
            ByteArrayOutputStream().use { outputStream ->
                BukkitObjectOutputStream(outputStream).use { dataOutput ->
                    dataOutput.writeInt(items.size)
                    items.forEach { dataOutput.writeObject(it) }
                }
                return Base64Coder.encodeLines(outputStream.toByteArray())
            }
        } catch (e: Exception) {
            throw IllegalStateException("Unable to save item stacks.", e)
        }
    }

    @Throws(IOException::class)
    fun itemStackFromBase64(data: String?): ItemStack? {
        val array = itemStackArrayFromBase64(data)
        return array.firstOrNull()
    }

    @Throws(IllegalStateException::class)
    fun itemStackToBase64(item: ItemStack?): String {
        return itemStackArrayToBase64(arrayOf(item))
    }
}
