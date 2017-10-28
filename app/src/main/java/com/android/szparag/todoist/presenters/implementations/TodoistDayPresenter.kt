package com.android.szparag.todoist.presenters.implementations

import com.android.szparag.todoist.models.contracts.DayModel
import com.android.szparag.todoist.presenters.contracts.DayPresenter
import com.android.szparag.todoist.presenters.contracts.UnixTimestamp
import com.android.szparag.todoist.utils.invalidLongValue
import com.android.szparag.todoist.views.contracts.DayView
import io.reactivex.rxkotlin.subscribeBy
import javax.inject.Inject

class TodoistDayPresenter @Inject constructor(dayModel: DayModel) : TodoistBasePresenter<DayView, DayModel>(dayModel), DayPresenter {

  private var unixTimestamp = 1L

  override fun attach(view: DayView, dayUnixTimestamp: UnixTimestamp) {
    logger.debug("attach")
    check(dayUnixTimestamp >= 0)
    unixTimestamp = dayUnixTimestamp
    super.attach(view)
  }

  override fun attach(view: DayView) {
    super.attach(view)
    logger.debug("attach")
    attach(view, invalidLongValue())
  }

  override fun subscribeModelEvents() {
    logger.debug("subscribeModelEvents")
//    model.getDayData(unixTimestamp)
//        .subscribeBy(
//            onSuccess = { renderDay ->
//              view?.renderDay(renderDay.dayName, "${renderDay.dayNumber} ${renderDay.monthName} ${renderDay.yearNumber}", -1, -100)
//            })


    model.getDayData(unixTimestamp)
        .doOnSubscribe { logger.debug("model.getDayData.doOnSubscribe") }
        .subscribeBy(
            onSuccess = { calendarEvent ->
              logger.debug("model.getDayData.onNext, calendarEvent: $calendarEvent")
              view?.renderDay(
                  dayNameHeader = calendarEvent.dayName,
                  dayNumber = calendarEvent.dayNumber,
                  monthName = calendarEvent.monthName,
                  yearNumber = calendarEvent.yearNumber
              )
            }
        )

    model.subscribeForTasksData(unixTimestamp)
        .doOnSubscribe { logger.debug("model.subscribeForTasksData.doOnSubscribe") }
        .subscribeBy(
            onNext = { tasksEvent ->
              logger.debug("model.subscribeForTasksData.onNext, tasksEvent: $tasksEvent")
              view?.renderTasks(
                  tasksEvent.tasksList,
                  tasksEvent.tasksCompletedCount,
                  tasksEvent.tasksRemaningCount
              )
            }
        )
  }

  override fun subscribeViewUserEvents() {
    logger.debug("subscribeViewUserEvents")
  }

}