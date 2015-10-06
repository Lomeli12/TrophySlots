package net.lomeli.trophyslots.core

import org.apache.logging.log4j.Level

import net.minecraftforge.fml.common.FMLLog

import net.lomeli.trophyslots.TrophySlots
import org.apache.logging.log4j.LogManager

public object Logger {
    private val logger = LogManager.getLogger(TrophySlots.MOD_NAME)

    public fun log(logLevel:Level, message:Any) {
        val j = message.toString()
        logger.log(logLevel, "[${TrophySlots.MOD_NAME}] : $j")
    }

    public fun logWarning(message:Any) {
        log(Level.WARN, message)
    }

    public fun logInfo(message:Any) {
        log(Level.INFO, message)
    }

    public fun logError(message:Any) {
        log(Level.ERROR, message)
    }
}