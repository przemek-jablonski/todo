package com.android.szparag.todoist.presenters.implementations

import com.android.szparag.todoist.AnimationEvent.AnimationEventType.END
import com.android.szparag.todoist.presenters.contracts.FrontPresenter
import com.android.szparag.todoist.utils.ui
import com.android.szparag.todoist.views.contracts.FrontView
import java.util.concurrent.TimeUnit.MILLISECONDS

class TodoistFrontPresenter: TodoistBasePresenter<FrontView>(), FrontPresenter {


  override fun onAttached() {
    super.onAttached()

  }

  override fun onViewReady() {
    super.onViewReady()
    view?.animateShowBackgroundImage()
        ?.ui()
        ?.filter { (eventType) -> eventType == END}
        ?.flatMap { view?.animateShowQuote()?.ui() }
        ?.filter { (eventType) -> eventType == END }
        ?.flatMap { view?.animatePeekCalendar()?.ui() } //todo: in submodelevents
        ?.filter { (eventType) -> eventType == END}
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