package net.lomeli.trophyslots.core

import net.lomeli.trophyslots.TrophySlots
import org.apache.logging.log4j.Level
import org.apache.logging.log4j.LogManager

class Logger {
    private val logger = LogManager.getLogger(TrophySlots.MOD_NAME)

    fun log(logLevel: Level, message: Any) = logger.log(logLevel, "[${TrophySlots.MOD_NAME}]: $message")

    fun logWarning(message: Any) = log(Level.WARN, message)

    fun logInfo(message: Any) = log(Level.INFO, message)

    fun logError(message: Any) = log(Level.ERROR, message)
}