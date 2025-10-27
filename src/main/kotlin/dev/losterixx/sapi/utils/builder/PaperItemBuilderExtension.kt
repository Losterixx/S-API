package dev.losterixx.sapi.utils.builder

import dev.triumphteam.gui.builder.item.PaperItemBuilder
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.OfflinePlayer
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.SkullMeta
import org.bukkit.persistence.PersistentDataType

fun PaperItemBuilder.material(material: Material): PaperItemBuilder {
    val item = build()
    item.type = material
    return PaperItemBuilder.from(item)
}

fun PaperItemBuilder.amount(amount: Int): PaperItemBuilder {
    val item = build()
    item.amount = amount
    return PaperItemBuilder.from(item)
}

fun PaperItemBuilder.skullOwner(owner: OfflinePlayer): PaperItemBuilder {
    val item = build()
    if (item.type != Material.PLAYER_HEAD) return this
    val meta = item.itemMeta as SkullMeta ?: return this
    meta.playerProfile = owner.playerProfile
    item.itemMeta = meta
    return PaperItemBuilder.from(item)
}

fun <T : Any, Z : PersistentDataType<*, T>> PaperItemBuilder.addPcd(key: String, value: T, type: Z): PaperItemBuilder {
    return pdc { it.set(NamespacedKey("sapi", key), type, value) }
}
fun <T : Any, Z : PersistentDataType<*, T>> PaperItemBuilder.getPcd(key: String, type: Z): T? {
    val meta = build().itemMeta ?: return null
    return meta.persistentDataContainer.get(NamespacedKey("sapi", key), type)
}
fun PaperItemBuilder.getPcdBoolean(key: String): Boolean? = getPcd(key, PersistentDataType.BOOLEAN)
fun PaperItemBuilder.hasPcd(key: String): Boolean {
    val meta = build().itemMeta ?: return false
    val container = meta.persistentDataContainer
    return container.has(NamespacedKey("sapi", key), PersistentDataType.STRING) ||
            container.has(NamespacedKey("sapi", key), PersistentDataType.INTEGER) ||
            container.has(NamespacedKey("sapi", key), PersistentDataType.DOUBLE) ||
            container.has(NamespacedKey("sapi", key), PersistentDataType.BOOLEAN)
}
fun PaperItemBuilder.removePcd(key: String): PaperItemBuilder {
    return pdc { it.remove(NamespacedKey("sapi", key)) }
}

fun PaperItemBuilder.cloneItem(): ItemStack = build().clone()
