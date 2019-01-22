package com.hookedroid.rxplayground

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var compositeDisposable = CompositeDisposable()
    var mainFragment: MainFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main).apply {
            show_button.setOnClickListener {
                appEventProcessor.onNext(AppEvent.Show)
                it.visibility = View.INVISIBLE
            }
        }

        compositeDisposable.add(createAppEventsSubcription())
    }

    override fun onStop() {
        super.onStop()
        compositeDisposable.clear()
        compositeDisposable = CompositeDisposable()
    }

    private fun createAppEventsSubcription(): Disposable = appEventFlowable.doOnNext {
        when (it) {
            AppEvent.Show -> {
                if (mainFragment == null) {
                    mainFragment = MainFragment().apply {
                        supportFragmentManager
                            .beginTransaction()
                            .add(R.id.content_frame, this)
                            .commit()
                    }
                }
            }
            AppEvent.Dismiss -> {
                mainFragment?.let {
                    supportFragmentManager
                        .beginTransaction()
                        .remove(it)
                        .commit()

                    show_button.visibility = View.VISIBLE
                }

                mainFragment = null
            }
        }
    }.subscribe()
}