package com.easyhz.picly.util.exception

class PiclyUncaughtExceptionHandler(
    private val uncaughtExceptionHandler: Thread.UncaughtExceptionHandler
) : Thread.UncaughtExceptionHandler {

    override fun uncaughtException(thread: Thread, exception: Throwable) {
        if (shouldAbsorb(exception)) {
            return
        }
        uncaughtExceptionHandler.uncaughtException(thread, exception)
    }

    /**
     * Evaluate whether to silently absorb uncaught crashes such that they
     * don't crash the app. We generally want to avoid this practice - we would
     * rather know about them. However in some cases there's nothing we can do
     * about the crash (e.g. it is an OS fault) and we would rather not have them
     * pollute our reliability stats.
     */
    private fun shouldAbsorb(exception: Throwable): Boolean {
        return when (exception::class.simpleName) {
            "CannotDeliverBroadcastException" -> true
            else -> false
        }
    }

}