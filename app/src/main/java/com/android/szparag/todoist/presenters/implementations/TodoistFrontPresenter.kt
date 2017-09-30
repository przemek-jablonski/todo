package com.android.szparag.todoist.presenters.implementations

import com.android.szparag.todoist.presenters.contracts.FrontPresenter
import com.android.szparag.todoist.utils.ui
import com.android.szparag.todoist.views.contracts.FrontView
import java.util.concurrent.TimeUnit.MILLISECONDS

class TodoistFrontPresenter: TodoistBasePresenter<FrontView>(), FrontPresenter {


  override fun onAttached() {
    super.onAttached()
    view?.animateShowBackgroundImage()
        ?.ui()
        ?.delaySubscription(1500, MILLISECONDS)
        ?.delay(750, MILLISECONDS)
        ?.flatMap { view?.animateShowQuote()?.ui() }
        ?.flatMap { view?.animatePeekCalendar()?.ui() } //todo: in submodelevents
        ?.subscribe()
        ?.toViewDisposable()
  }

  override fun subscribeModelEvents() {
    logger.debug("subscribeModelEvents")
  }

  override fun subscribeViewUserEvents() {
    logger.debug("subscribeViewUserEvents")
  }


}