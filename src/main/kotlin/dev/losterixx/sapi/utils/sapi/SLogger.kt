package dev.losterixx.sapi.utils.sapi

import dev.losterixx.sapi.SAPI

object SLogger {

    private val logger = SAPI.getPlugin().logger

    fun info(message: String) {
        logger.info(message)
    }

    fun warning(message: String) {
        logger.warning(message)
    }

    fun error(message: String) {
        logger.severe(message)
    }

    fun sapiInfo(message: String) {
        logger.info("[S-API] $message")
    }

    fun sapiWarning(message: String) {
        logger.warning("[S-API] $message")
    }

    fun sapiError(message: String) {
        logger.severe("[S-API] $message")
    }

}