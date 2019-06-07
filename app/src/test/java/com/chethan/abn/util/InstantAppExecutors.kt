package com.chethan.abn.util

import com.chethan.abn.AppExecutors
import java.util.concurrent.Executor

/**
 * Created by Chethan on 5/3/2019.
 */

class InstantAppExecutors : AppExecutors(instant, instant, instant) {
    companion object {
        private val instant = Executor { it.run() }
    }
}
