package ru.ok.itmo.example

import android.app.Activity
import java.lang.ref.WeakReference

class RunnableWorker(
    private val time: Long,
    private val counter: Counter,
    private val updateUi: (value: Int) -> Unit,
    private val endUpdateUi: () -> Unit,
    private val activity: WeakReference<Activity>
) : Runnable {
    private var isRunning = true

    override fun run() {
        counter.initCounter()

        do {
            activity.get()?.runOnUiThread {
                updateUi(counter.value)
            }
            Thread.sleep(time)
        } while (counter.updateCounter() && isRunning)

        activity.get()?.runOnUiThread {
            endUpdateUi()
        }
    }

    fun stop() {
        isRunning = false
    }
}