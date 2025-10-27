package dev.losterixx.sapi.premade.commands

import com.mojang.brigadier.builder.LiteralArgumentBuilder
import dev.losterixx.sapi.SAPI
import dev.losterixx.sapi.utils.config.ConfigManager
import io.papermc.paper.command.brigadier.CommandSourceStack
import io.papermc.paper.command.brigadier.Commands
import kotlin.getOrElse
import kotlin.runCatching
import kotlin.system.measureTimeMillis

object DefaultMainCommand {

    private val plugin = SAPI.getPlugin()
    private val mm = SAPI.getMiniMessage()
    private val messagesConfig get() = ConfigManager.getConfig(ConfigManager.getConfig("config").getString("langFile", "english"))

    fun create(command: String, permRoot: String, prefix: String, reloadExtras: () -> Unit = {}): LiteralArgumentBuilder<CommandSourceStack> {
        return Commands.literal(command)
            .requires { ctx -> ctx.sender.hasPermission("$permRoot.admin.about") || ctx.sender.hasPermission("$permRoot.admin.reload") }
            .executes { ctx ->
                val sender = ctx.source.sender

                sender.sendMessage(mm.deserialize(prefix + messagesConfig.getString("commands.$command.usage", "<red>Usage: /$command <about/reload>")))
                return@executes 1
            }
            .then(Commands.literal("about")
                .requires { ctx -> ctx.sender.hasPermission("$permRoot.admin.about") }
                .executes { ctx ->
                    val sender = ctx.source.sender

                    sender.sendMessage(mm.deserialize(prefix + messagesConfig.getString("commands.$command.about", "<gray>%plugin% v%version% <dark_gray>- <gray>%authors%")
                        .replace("%plugin%", plugin.pluginMeta.name)
                        .replace("%version%", plugin.pluginMeta.version)
                        .replace("%authors%", plugin.pluginMeta.authors.joinToString(", "))))

                    return@executes 1
                }
            )
            .then(Commands.literal("reload")
                .requires { ctx -> ctx.sender.hasPermission("$permRoot.admin.reload") }
                .executes { ctx ->
                    val sender = ctx.source.sender

                    sender.sendMessage(mm.deserialize(prefix + messagesConfig.getString("commands.$command.reload.reloading", "<gray>Reloading plugin...")))

                    val elapsedTime = runCatching {
                        measureTimeMillis {
                            ConfigManager.reloadAllConfigs()
                            reloadExtras()
                        }
                    }.getOrElse {
                        sender.sendMessage(mm.deserialize(prefix + messagesConfig.getString("commands.$command.reload.error", "<red>An error occurred while reloading the plugin.")))
                        plugin.server.consoleSender.sendMessage(mm.deserialize(prefix + "<dark_red>An error occurred while reloading the plugin: <red>${it.message}"))
                        it.printStackTrace()
                        return@executes 0
                    }

                    sender.sendMessage(mm.deserialize(prefix + messagesConfig.getString("commands.$command.reload.success", "<gray>Plugin <green>reloaded successfully <gray>in %time%ms")
                        .replace("%time%", (System.currentTimeMillis() - elapsedTime.toDouble() / 1000).toString())))

                    return@executes 1
                }
            )
    }
}