package dev.losterixx.sapi.utils.builder.guis

import dev.dejvokep.boostedyaml.block.implementation.Section
import dev.losterixx.sapi.SAPI
import dev.losterixx.sapi.utils.sapi.SLogger
import dev.triumphteam.gui.builder.item.PaperItemBuilder
import dev.triumphteam.gui.guis.GuiItem
import org.bukkit.Color
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.meta.SkullMeta

object GuiItemBuilder {

    private val mm = SAPI.getMiniMessage()

    fun buildGuiItemFromConfig(configSection: Section): GuiItem? {
        val materialName = configSection.getString("material")
        val material = try {
            Material.getMaterial(materialName.uppercase()) ?: run {
                SLogger.sapiWarning("Invalid material '$materialName' in GUI config.")
                return null
            }
        } catch (e: IllegalArgumentException) {
            return null
        }

        var item = PaperItemBuilder.from(material)
            .amount(configSection.getInt("amount", 1))
            .name(mm.deserialize(configSection.getString("name", "config error")))
            .lore(configSection.getStringList("lore").map { line -> mm.deserialize(line) })
            .glow(configSection.getBoolean("glow", false))
            .unbreakable(configSection.getBoolean("unbreakable", false))

        if (configSection.getStringList("enchants") != null) {
            for (enchantString in configSection.getStringList("enchants")) {
                val parts = enchantString.split(":")
                if (parts.size != 2) {
                    SLogger.sapiWarning("Invalid enchant format '$enchantString' in GUI config.")
                    continue
                }

                val enchantName = parts[0].uppercase()
                val enchantLevel = parts[1].toIntOrNull() ?: run {
                    SLogger.sapiWarning("Invalid enchant level in '$enchantString' in GUI config.")
                    continue
                }

                val enchantment = try {
                    Enchantment.getByName(enchantName) ?: run {
                        SLogger.sapiWarning("Invalid enchant name '$enchantName' in GUI config.")
                        continue
                    }
                } catch (e: IllegalArgumentException) {
                    SLogger.sapiWarning("Invalid enchant name '$enchantName' in GUI config.")
                    continue
                }

                item.enchant(enchantment, enchantLevel)
            }
        }

        if (configSection.getString("color") != null) {
            val parts = configSection.getString("color")!!.split(",")
            if (parts.size != 3) {
                SLogger.sapiWarning("Invalid color format '${configSection.getString("color")}' in GUI config.")
            } else {
                val r = parts[0].toIntOrNull()
                val g = parts[1].toIntOrNull()
                val b = parts[2].toIntOrNull()
                if (r == null || g == null || b == null) {
                    SLogger.sapiWarning("Invalid color values in '${configSection.getString("color")}' in GUI config.")
                } else {
                    item.color(Color.fromRGB(r, g, b))
                }
            }
        }

        if (configSection.getString("flags") != null) {
            val flagList = arrayOf<ItemFlag>()
            for (flagString in configSection.getStringList("flags")) {
                val flagName = flagString.uppercase()
                val itemFlag = try {
                    ItemFlag.valueOf(flagName)
                } catch (e: IllegalArgumentException) {
                    SLogger.sapiWarning("Invalid item flag '$flagName' in GUI config.")
                    continue
                }
            }
            item.flags(*flagList)
        }

        if (configSection.getInt("customModelData") != null) {
            if (configSection.getInt("customModelData") < 0) {
                SLogger.sapiWarning("Custom model data cannot be negative in GUI config.")
            } else {
                item.model(configSection.getInt("customModelData"))
            }
        }

        if (configSection.getString("itemModel") != null) {
            val (namespace, key) = configSection.getString("itemModel").split(":")

            val newItem = item.build()
            val meta = newItem.itemMeta
            meta!!.itemModel = NamespacedKey(namespace, key)
            newItem.itemMeta = meta
            item = PaperItemBuilder.from(newItem)
        }

        if (configSection.getString("playerProfile") != null) {
            if (item.build().type != Material.PLAYER_HEAD) {
                SLogger.sapiWarning("Setting playerProfile can only be set on PLAYER_HEAD items in GUI config.")
            } else {
                val profileString = configSection.getString("playerProfile")
                val newItem = item.build()
                val meta = newItem.itemMeta as SkullMeta

                try {
                    meta.playerProfile = SAPI.getPlugin().server.getOfflinePlayer(profileString).playerProfile
                } catch (e: Exception) {
                    SLogger.sapiWarning("Could not set player profile '$profileString' in GUI config: ${e.message}")
                }

                newItem.itemMeta = meta
                item = PaperItemBuilder.from(newItem)
            }
        }

        return item.asGuiItem()
    }

}