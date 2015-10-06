package net.lomeli.trophyslots.core

import org.apache.logging.log4j.Level

import net.minecraftforge.fml.common.FMLLog

import net.lomeli.trophyslots.TrophySlots

public object Logger {
    public fun log(logLevel:Level, message:Any) {
        FMLLog.log(TrophySlots.MOD_ID, logLevel, message.toString())
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