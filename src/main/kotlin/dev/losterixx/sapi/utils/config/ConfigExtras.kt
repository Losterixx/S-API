package dev.losterixx.sapi.utils.config

import dev.losterixx.sapi.SAPI
import java.nio.file.Files

object ConfigExtras {

    private val plugin = SAPI.getPlugin()
    private val logger = plugin.logger

    fun loadConfigFiles() {
        val config = ConfigManager.createConfig("config", "config.yml")

        loadLangFiles()
        val langFile = ConfigManager.getConfig("config").getString("langFile", null)
        if (langFile == null) {
            logger.warning("No language file specified in config.yml! Defaulting to english.yml.")
            config.set("langFile", "english")
            ConfigManager.saveConfig("config")
        }
        ConfigManager.createConfig(langFile, "lang/$langFile.yml", "lang")
        logger.info("Using language file: $langFile.yml")
    }

    fun loadLangFiles(defaultLangFiles: List<String> = listOf("english.yml", "german.yml"), langFolder: String = "lang") {
        val langDirectory = plugin.dataFolder.toPath().resolve("lang")

        if (!Files.exists(langDirectory)) {
            Files.createDirectories(langDirectory)
        }

        defaultLangFiles.forEach { fileName ->
            val langConfig = fileName.removeSuffix(".yml")
            ConfigManager.createConfig(langConfig, "$langFolder/$fileName", langFolder)
        }

        Files.list(langDirectory).filter { it.toString().endsWith(".yml") }.forEach { langFile ->
            val langConfig = langFile.fileName.toString().removeSuffix(".yml")
            if (!ConfigManager.existsConfig(langConfig)) {
                ConfigManager.createConfig(langConfig, "$langFolder/${langFile.fileName}", langFolder)
            }
        }
    }

}