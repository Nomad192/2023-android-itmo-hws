package ru.ok.itmo.example

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.io.InterruptedIOException
import java.util.concurrent.TimeUnit

class RxWorker(
    private val counter: Counter,
    private val updateUi: (value: Int) -> Unit,
    private val endUpdateUi: () -> Unit
) : Worker {
    private lateinit var disposable: Disposable

    override fun run(time: Long) {
        disposable = Observable.defer {
            counter.initCounter()
            Observable.just(counter.value)
        }
            .subscribeOn(Schedulers.io())
            .concatWith(
                Observable.interval(time, TimeUnit.MILLISECONDS)
                    .takeWhile {
                        counter.updateCounter()
                    }
                    .flatMap {
                        try {
                            Observable.just(counter.value)
                        } catch (e: InterruptedIOException) {
                            throw InterruptedException()
                        }
                    }
            )
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { value ->
                    updateUi(value)
                },
                { error ->
                    System.err.println("Error: ${error.stackTrace}")
                },
                {
                    endUpdateUi()
                }
            )
    }

    override fun stop() {
        if (::disposable.isInitialized && !disposable.isDisposed)
            disposable.dispose()
    }
}