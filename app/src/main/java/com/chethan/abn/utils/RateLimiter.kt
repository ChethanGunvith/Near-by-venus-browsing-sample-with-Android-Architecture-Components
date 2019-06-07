package com.chethan.demoproject.utils

import android.os.SystemClock
import androidx.collection.ArrayMap
import com.chethan.abn.testing.OpenForTesting

import java.util.concurrent.TimeUnit

/**
 * Created by Chethan on 5/3/2019.
 * Utility class that decides whether we should fetch some data or not.
 */

@OpenForTesting
class RateLimiter<in KEY>(timeout: Int, timeUnit: TimeUnit) {
    private val timestamps = ArrayMap<KEY, Long>()
    private val timeout = timeUnit.toMillis(timeout.toLong())

    @Synchronized
    fun shouldFetch(key: KEY): Boolean {
        val lastFetched = timestamps[key]
        val now = now()
        if (lastFetched == null) {
            timestamps[key] = now
            return true
        }
        if (now - lastFetched > timeout) {
            timestamps[key] = now
            return true
        }
        return false
    }

    private fun now() = SystemClock.uptimeMillis()

    @Synchronized
    fun reset(key: KEY) {
        timestamps.remove(key)
    }
}
