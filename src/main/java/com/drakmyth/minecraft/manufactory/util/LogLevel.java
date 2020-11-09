/*
 *  SPDX-License-Identifier: LGPL-3.0-only
 *  Copyright (c) 2020 Drakmyth. All rights reserved.
 */

package com.drakmyth.minecraft.manufactory.util;

import org.apache.logging.log4j.Level;

// Because Log4j doesn't have a real enum for this >.<
public enum LogLevel {
    OFF(Level.OFF),
    FATAL(Level.FATAL),
    ERROR(Level.ERROR),
    WARN(Level.WARN),
    INFO(Level.INFO),
    DEBUG(Level.DEBUG),
    TRACE(Level.TRACE),
    ALL(Level.ALL);

    private Level level;

    private LogLevel(Level level) {
        this.level = level;
    }

    public Level getLevel() {
        return level;
    }
}
