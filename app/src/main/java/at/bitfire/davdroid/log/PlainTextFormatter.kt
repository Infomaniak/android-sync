/*
 * Copyright © Ricki Hirner (bitfire web engineering).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 */

package at.bitfire.davdroid.log

import org.apache.commons.lang3.exception.ExceptionUtils
import org.apache.commons.lang3.time.DateFormatUtils
import java.util.logging.Formatter
import java.util.logging.LogRecord

class PlainTextFormatter private constructor(
        val logcat: Boolean
): Formatter() {

    companion object {
        @JvmField val LOGCAT = PlainTextFormatter(true)
        @JvmField val DEFAULT = PlainTextFormatter(false)
    }

    override fun format(r: LogRecord): String {
        val builder = StringBuilder()

        if (!logcat)
            builder .append(DateFormatUtils.format(r.millis, "yyyy-MM-dd HH:mm:ss"))
                    .append(" ").append(r.threadID).append(" ")

        val className = shortClassName(r.sourceClassName)
        if (className != r.loggerName)
            builder.append("[").append(className).append("] ")

        builder.append(r.message)

        r.thrown?.let {
            builder .append("\nEXCEPTION ")
                    .append(ExceptionUtils.getStackTrace(it))
        }

        r.parameters?.let {
            for ((idx, param) in it.withIndex())
                builder.append("\n\tPARAMETER #").append(idx).append(" = ").append(param)
        }

        if (!logcat)
            builder.append("\n")

        return builder.toString()
    }

    private fun shortClassName(className: String) = className
            .replace(Regex("^at\\.bitfire\\.(dav|cert4an|dav4an|ical4an|vcard4an)droid\\."), "")
            .replace(Regex("\\$.*$"), "")

}
