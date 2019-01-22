package com.hookedroid.rxplayground

import io.reactivex.Flowable
import io.reactivex.processors.PublishProcessor

sealed class AppEvent {
    object Show: AppEvent()
    object Dismiss: AppEvent()

}

val appEventProcessor: PublishProcessor<AppEvent> = PublishProcessor.create()
val appEventFlowable = appEventProcessor as Flowable<AppEvent>