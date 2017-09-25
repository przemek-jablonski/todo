package com.android.szparag.todoist.presenters.implementations

import android.support.annotation.CallSuper
import com.android.szparag.todoist.utils.Logger
import com.android.szparag.todoist.utils.add
import com.android.szparag.todoist.presenters.contracts.Presenter
import com.android.szparag.todoist.utils.ui
import com.android.szparag.todoist.views.contracts.View
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy

abstract class TodoistBasePresenter<V : View> : Presenter<V> {

  override lateinit var logger: Logger
  override var view: V? = null
  override lateinit var viewDisposables: CompositeDisposable
  override lateinit var modelDisposables: CompositeDisposable

  override fun attach(view: V) {
    logger = Logger.create(this::class)
    logger.debug("attach, view: $view")
    this.view = view
    onAttached()
  }

  @CallSuper override fun onAttached() {
    logger.debug("onAttached")
    viewDisposables = CompositeDisposable()
    modelDisposables = CompositeDisposable()
    subscribeViewReadyEvents()
    subscribeViewPermissionsEvents()
    subscribeViewUserEvents()
    subscribeModelEvents()
  }


  override final fun detach() {
    logger.debug("detach")
    onBeforeDetached()
    view = null
  }

  @CallSuper override fun onBeforeDetached() {
    logger.debug("onBeforeDetached")
    viewDisposables.clear()
    modelDisposables.clear()
  }

  @CallSuper override fun subscribeViewPermissionsEvents() {
    logger.debug("subscribeViewPermissionsEvents")
  }

  private fun subscribeViewReadyEvents() {
    logger.debug("subscribeViewReadyEvents")
    view
        ?.subscribeOnViewReady()
        ?.ui()
        ?.doOnSubscribe { logger.debug("subscribeViewReadyEvents.sub") }
        ?.filter { readyFlag -> readyFlag }
        ?.subscribeBy(
            onNext = { readyFlag ->
              logger.debug("subscribeViewReadyEvents.onNext, ready: $readyFlag")
              onViewReady()
            },
            onComplete = {
              logger.debug("subscribeViewReadyEvents.onComplete")
            },
            onError = { exc ->
              logger.error("subscribeViewReadyEvents.onError", exc)
            }
        )
  }


  fun Disposable?.toViewDisposable() {
    logger.debug("toViewDisposable: viewDisposables: $viewDisposables, disposed: ${viewDisposables.isDisposed}")
    viewDisposables.takeIf { !it.isDisposed }?.add(this)
  }

  fun Disposable?.toModelDisposable() {
    logger.debug("toModelDisposable: modelDisposables: $modelDisposables, disposed: ${modelDisposables.isDisposed}")
    modelDisposables.takeIf { !it.isDisposed }?.add(this)
  }
}