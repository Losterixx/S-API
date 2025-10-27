package dev.losterixx.sapi.utils.numutils

import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.Locale
import java.util.concurrent.TimeUnit
import kotlin.math.ln
import kotlin.math.pow

object FormatNums {

    private val defaultDecimalFormat = DecimalFormat("#,##0.###")

    fun formatNumber(number: Double, locale: Locale = Locale.GERMAN, format: DecimalFormat? = null): String {
        val nf = format ?: NumberFormat.getNumberInstance(locale) as NumberFormat
        return nf.format(number)
    }

    fun formatNumber(number: Float, locale: Locale = Locale.GERMAN, format: DecimalFormat? = null): String =
        formatNumber(number.toDouble(), locale, format)

    fun formatNumber(number: Long, locale: Locale = Locale.GERMAN, format: DecimalFormat? = null): String =
        formatNumber(number.toDouble(), locale, format)

    fun formatNumber(number: Int, locale: Locale = Locale.GERMAN, format: DecimalFormat? = null): String =
        formatNumber(number.toDouble(), locale, format)

    fun formatNumberSimple(number: Number, format: DecimalFormat = defaultDecimalFormat): String =
        format.format(number)

    fun formatTimeHMS(seconds: Long, showHours: Boolean = false): String {
        val hrs = TimeUnit.SECONDS.toHours(seconds)
        val mins = TimeUnit.SECONDS.toMinutes(seconds) % 60
        val secs = seconds % 60
        return if (showHours || hrs > 0) "%02d:%02d:%02d".format(hrs, mins, secs)
        else "%02d:%02d".format(mins, secs)
    }

    fun formatTimeDHMS(seconds: Long): String {
        val days = TimeUnit.SECONDS.toDays(seconds)
        val hrs = TimeUnit.SECONDS.toHours(seconds) % 24
        val mins = TimeUnit.SECONDS.toMinutes(seconds) % 60
        val secs = seconds % 60
        return "${days}d ${hrs}h ${mins}m ${secs}s"
    }

    fun formatPercentage(value: Double, decimals: Int = 2, locale: Locale = Locale.GERMAN): String {
        val factor = 10.0.pow(decimals)
        val rounded = (value * factor).toInt() / factor
        val nf = NumberFormat.getNumberInstance(locale)
        return "${nf.format(rounded)}%"
    }

    fun formatBytes(bytes: Long, si: Boolean = false, decimals: Int = 1): String {
        val unit = if (si) 1000 else 1024
        if (bytes < unit) return "$bytes B"
        val exp = (ln(bytes.toDouble()) / ln(unit.toDouble())).toInt()
        val pre = (if (si) "kMGTPE" else "KMGTPE")[exp - 1] + if (si) "" else "i"
        return String.format("%.${decimals}f %sB", bytes / unit.toDouble().pow(exp.toDouble()), pre)
    }
}
