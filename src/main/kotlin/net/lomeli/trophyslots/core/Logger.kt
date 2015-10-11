package net.lomeli.trophyslots.core

import net.lomeli.trophyslots.TrophySlots
import org.apache.logging.log4j.Level
import org.apache.logging.log4j.LogManager

public object Logger {
    private val logger = LogManager.getLogger(TrophySlots.MOD_NAME)

    public fun log(logLevel: Level, message: Any) = logger.log(logLevel, "[${TrophySlots.MOD_NAME}] : ${message.toString()}")

    public fun logWarning(message: Any) = log(Level.WARN, message)

    public fun logInfo(message: Any) = log(Level.INFO, message)

    public fun logError(message: Any) = log(Level.ERROR, message)
}