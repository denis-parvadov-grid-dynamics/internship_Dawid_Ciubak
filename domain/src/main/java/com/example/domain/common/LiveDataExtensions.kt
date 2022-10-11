package com.example.domain.common

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

// extension written mainly for testing purposes
// (and to avoid typing this in almost every test that includes liveData in some way)
// tries to get the value from liveData by creating temporary observer, if the value isn't returned
// within 2 seconds, throws TimeoutException
fun <T> LiveData<T>.getOrAwaitValue(
    time: Long = 2,
    timeUnit: TimeUnit = TimeUnit.SECONDS
): T {
    var data: T? = null
    val latch = CountDownLatch(1)
    val observer = object : Observer<T> {
        override fun onChanged(o: T?) {
            data = o
            latch.countDown()
            this@getOrAwaitValue.removeObserver(this)
        }
    }

    this.observeForever(observer)

    // Don't wait indefinitely if the LiveData is not set.
    if (!latch.await(time, timeUnit)) {
        throw TimeoutException("LiveData value was never set.")
    }

    // not the best practice I am aware, but I am sure this cast will work
    @Suppress("UNCHECKED_CAST")
    return data as T
}
