package dev.losterixx.sapi.utils.async

import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitTask

object CoroutineUtils {

    private lateinit var plugin: JavaPlugin
    private val initMutex = Mutex()
    private var scope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    suspend fun init(plugin: JavaPlugin) {
        initMutex.withLock {
            if (this::plugin.isInitialized.not()) {
                this.plugin = plugin
            } else if (this.plugin !== plugin) {
                cancelAll()
                this.plugin = plugin
                scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
            }
        }
    }

    suspend fun <T> runSync(block: () -> T): T = suspendCancellableCoroutine { cont ->
        val task: BukkitTask = Bukkit.getScheduler().runTask(plugin, Runnable {
            try {
                if (!cont.isCompleted) cont.resume(block())
            } catch (t: Throwable) {
                if (!cont.isCompleted) cont.resumeWithException(t)
            }
        })
        cont.invokeOnCancellation { task.cancel() }
    }

    fun launchSync(block: suspend CoroutineScope.() -> Unit): Job {
        return scope.launch {
            val deferred = CompletableDeferred<Unit>()
            Bukkit.getScheduler().runTask(plugin, Runnable {
                try {
                    runBlocking {
                        try {
                            block()
                            deferred.complete(Unit)
                        } catch (t: Throwable) {
                            deferred.completeExceptionally(t)
                        }
                    }
                } catch (t: Throwable) {
                    if (!deferred.isCompleted) deferred.completeExceptionally(t)
                }
            })
            deferred.await()
        }
    }

    fun launchAsync(block: suspend CoroutineScope.() -> Unit): Job {
        return scope.launch(Dispatchers.IO) { block() }
    }

    fun cancelAll() {
        val job = scope.coroutineContext[Job]
        job?.cancel()
        scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    }
}