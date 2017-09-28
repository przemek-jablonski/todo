package com.android.szparag.todoist.views.implementations

import android.app.ActionBar.LayoutParams
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.widget.ImageView
import android.widget.TextView
import com.android.szparag.todoist.R
import com.android.szparag.todoist.dagger.DaggerGlobalScopeWrapper
import com.android.szparag.todoist.models.entities.RenderDay
import com.android.szparag.todoist.presenters.contracts.DayPresenter
import com.android.szparag.todoist.utils.bindView
import com.android.szparag.todoist.utils.getDisplayMetrics
import com.android.szparag.todoist.views.contracts.DayView
import io.reactivex.Completable
import io.reactivex.Observable
import javax.inject.Inject

class TodoistDayActivity : TodoistBaseActivity<DayPresenter>(), DayView {

  @Inject override lateinit var presenter: DayPresenter
  lateinit var displayMetrics: DisplayMetrics

  internal val calendarWeekGraph: ImageView by bindView(R.id.calendarWeekGraph)
  internal val calendarWeekDay: TextView by bindView(R.id.calendarWeekDay)
  internal val calendarWeekDate: TextView by bindView(R.id.calendarWeekDate)
  internal val calendarWeekTasksDone: TextView by bindView(R.id.calendarWeekTasksDone)
  internal val calendarWeekTasksRemaining: TextView by bindView(R.id.calendarWeekTasksRemaining)
  internal val calendarWeekAlarm: ImageView by bindView(R.id.calendarWeekAlarm)
  internal val calendarWeekAlarmHour: TextView by bindView(R.id.calendarWeekAlarmHour)

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_todoist_day)
  }

  override fun onStart() {
    super.onStart()
    logger.debug("onStart")
    DaggerGlobalScopeWrapper.getComponent(this).inject(this) //todo: find a way to generify them in Kotlin
    presenter.attach(this) //todo: find a way to generify them in Kotlin
    displayMetrics = getDisplayMetrics()
  }

  override fun onStop() {
    super.onStop()
    logger.debug("onStop")
    presenter.detach()
  }

  override fun setupCalendarHeader(headerImageUrl: String): Completable {
    logger.debug("setupCalendarHeader")
    return Completable.create {  }
  }

  override fun setupCalendarQuote(quoteText: String): Completable {
    logger.debug("setupCalendarQuote")
    return Completable.create {  }
  }

  override fun setupCalendarExtras(renderDay: RenderDay): Completable {
    logger.debug("setupCalendarExtras")
    return Completable.create {  }
  }

  override fun setupCalendarCheckList(renderDay: RenderDay): Completable {
    logger.debug("setupCalendarCheckList")
    return Completable.create {  }
  }

  override fun setupCalendarBackingView(renderDay: RenderDay): Completable {
    logger.debug("setupCalendarBackingView")
    return Completable.create {
      calendarWeekDay.text = renderDay.dayName
      calendarWeekDate.text = "${renderDay.dayNumber} ${renderDay.monthName} ${renderDay.yearNumber}"
      calendarWeekTasksDone.text = "${renderDay.tasksDoneCount} tasks done"
      calendarWeekTasksRemaining.text = "${renderDay.tasksRemainingCount} tasks remaining"
    }
  }

  override fun subscribeUserAddButtonClicked(): Observable<Any> {
    logger.debug("subscribeUserAddButtonClicked")
    return Observable.create {  }
  }

  override fun subscribeUserChecklistItemCheck(): Observable<Any> {
    logger.debug("subscribeUserChecklistItemCheck")
    return Observable.create {  }
  }

  override fun subscribeUserChecklistItemLongPressed(): Observable<Any> {
    logger.debug("subscribeUserChecklistItemLongPressed")
    return Observable.create {  }
  }

  override fun animateCalendarHeader(): Completable {
    logger.debug("animateCalendarHeader")
    return Completable.create {  }
  }

  override fun animateCalendarQuote(): Completable {
    logger.debug("animateCalendarQuote")
    return Completable.create {  }
  }

  override fun animateCalendarChecklist(): Completable {
    logger.debug("animateCalendarChecklist")
    return Completable.create {  }
  }

  override fun animateCalendarAddButton(): Completable {
    logger.debug("animateCalendarAddButton")
    return Completable.create {  }
  }

  override fun subscribeUserBackButtonPressed(): Observable<Any> {
    logger.debug("subscribeUserBackButtonPressed")
    return Observable.create {  }
  }

}
