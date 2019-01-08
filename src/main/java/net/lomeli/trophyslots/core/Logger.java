package net.lomeli.trophyslots.core;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;

public class Logger {
    private org.apache.logging.log4j.Logger logger;

    public Logger(String name) {
        logger = LogManager.getLogger(name);
    }

    public void log(Level level, Object msg, Object...args) {
        if (args != null && args.length > 0)
            logger.log(level, msg.toString(), args);
        else logger.log(level, msg.toString());
    }

    public void info(Object msg, Object...args) {
        log(Level.INFO, msg, args);
    }

    public void warn(Object msg, Object...args) {
        log(Level.WARN, msg, args);
    }

    public void error(Object msg, Object...args) {
        log(Level.ERROR, msg, args);
    }

    public void exception(Object msg, Exception ex, Object...args) {
        if (args != null && args.length > 0)
            logger.log(Level.ERROR, String.format(msg.toString(), args), ex);
        else logger.log(Level.ERROR, msg.toString(), ex);
    }

    public void debug(Object msg, Object...args) {
        log(Level.DEBUG, msg, args);
    }
}
