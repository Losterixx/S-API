package dev.losterixx.sapi.utils.scheduling

import dev.losterixx.sapi.SAPI

object SchedulerUtil {

    private val plugin = SAPI.getPlugin()

    fun runLater(task: () -> Unit, ticks: Long) {
        plugin.server.scheduler.runTaskLater(plugin, task, ticks)
    }

    fun runRepeating(task: () -> Unit, intervalTicks: Long, startDelayTicks: Long = 0L) {
        plugin.server.scheduler.runTaskTimer(plugin, task, startDelayTicks, intervalTicks)
    }

    fun runAsync(task: () -> Unit) {
        plugin.server.scheduler.runTaskAsynchronously(plugin, task)
    }

    fun runAsyncLater(task: () -> Unit, ticks: Long) {
        plugin.server.scheduler.runTaskLaterAsynchronously(plugin, task, ticks)
    }

    fun runAsyncRepeating(task: () -> Unit, intervalTicks: Long, startDelayTicks: Long = 0L) {
        plugin.server.scheduler.runTaskTimerAsynchronously(plugin, task, startDelayTicks, intervalTicks)
    }
}
