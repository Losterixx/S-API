package dev.losterixx.sapi

import dev.losterixx.sapi.utils.async.CoroutineUtils
import kotlinx.coroutines.runBlocking
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.plugin.java.JavaPlugin

object SAPI {
    private lateinit var plugin: JavaPlugin
    private val miniMessage = MiniMessage.miniMessage()

    fun init(plugin: JavaPlugin) {
        this.plugin = plugin
        runBlocking {
            CoroutineUtils.init(plugin)
        }
    }

    fun getPlugin(): JavaPlugin = plugin
    fun getMiniMessage(): MiniMessage = miniMessage
}
