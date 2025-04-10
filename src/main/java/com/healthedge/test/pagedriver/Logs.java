package com.companyname.test.pagedriver;

import org.apache.logging.log4j.Level;

public class Logs {

    /**
     * Retrieves logging level from properties.
     * Listed below in ascending order of logging detail amount; default value is 'info'
     * @return Level to be used for log4j logging
     */
    public static Level getLevel(String logLevel) {
        Level level;
        switch (logLevel.toLowerCase()) {
            case "off":
                level = Level.OFF;
                break;
            case "fatal":
                level = Level.FATAL;
                break;
            case "error":
                level = Level.ERROR;
                break;
            case "warn":
                level = Level.WARN;
                break;
            case "debug":
                level = Level.DEBUG;
                break;
            case "trace":
                level = Level.TRACE;
                break;
            case "all":
                level = Level.ALL;
                break;
            default:
                level = Level.INFO;
        }
        return level;
    }

}
