package com.android.szparag.todoist.views.implementations

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.TextView
import com.android.szparag.todoist.R
import com.android.szparag.todoist.dagger.DaggerGlobalScopeWrapper
import com.android.szparag.todoist.presenters.contracts.DayPresenter
import com.android.szparag.todoist.utils.bindView
import com.android.szparag.todoist.utils.invalidLongValue
import com.android.szparag.todoist.views.contracts.DayView
import com.android.szparag.todoist.widgets.WidgetTaskInput
import com.android.szparag.todoist.widgets.adapters.DayTasksAdapter
import io.reactivex.Observable
import io.reactivex.rxkotlin.subscribeBy

private const val INTENT_EXTRAS_KEY_UNIXTIMESTAMP = "todoist.activity.day.timestamp"

class TodoistDayActivity : TodoistBaseActivity<DayPresenter>(), DayView {

  companion object Router {
    fun prepareDayActivityRoutingIntent(packageContext: Context, dayUnixTimestamp: Long) =
        Intent(packageContext, TodoistDayActivity::class.java)
            .putExtra(INTENT_EXTRAS_KEY_UNIXTIMESTAMP, dayUnixTimestamp)
  }

  private val dateHeaderText: TextView by bindView(R.id.dateHeaderText)
  private val dateFullText: TextView by bindView(R.id.dateFullText)
  private val tasksCompletedText: TextView by bindView(R.id.tasksCompletedText)
  private val tasksRemainingText: TextView by bindView(R.id.tasksRemainingText)
  private val tasksOverviewRecycler: RecyclerView by bindView(R.id.tasksOverviewList)
  private val taskInputWidget: WidgetTaskInput by bindView(R.id.taskInputWidget)
  private val tasksOverviewRecyclerAdapter: DayTasksAdapter by lazy {
    DayTasksAdapter().also { tasksOverviewRecycler.adapter = it }
  }

  override fun resolveStartupData() {
    logger.debug("resolveStartupData")
  }

  override fun onStart() {
    super.onStart()
    logger.debug("onStart")
    DaggerGlobalScopeWrapper.getComponent(this).inject(this)
    presenter.attach(view = this, dayUnixTimestamp = intent.getLongExtra(INTENT_EXTRAS_KEY_UNIXTIMESTAMP, invalidLongValue()))
    tasksOverviewRecycler.layoutManager = LinearLayoutManager(this)

  }

  override fun subscribeNewTaskTextAccepted() = taskInputWidget.subscribeTextAccepted()

  override fun onPause() {
    super.onPause()
    logger.debug("onPause")
    presenter.detach()
  }

  override fun subscribeUserBackButtonPressed(): Observable<Any> {
    logger.debug("subscribeUserBackButtonPressed")
    return Observable.create {}
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_todoist_day)
  }

  override fun renderDay(dayNameHeader: CharSequence, dayNumber: Int, monthName: CharSequence, yearNumber: Int) {
    logger.debug("renderDay, dayNameHeader: $dayNameHeader, dayNumber: $dayNumber, monthName: $monthName, yearNumber: $yearNumber")
    dateHeaderText.text = dayNameHeader
    dateFullText.text = "$dayNumber $monthName $yearNumber"
  }

  override fun renderTasks(tasksList: List<CharSequence>, tasksCompletedCount: Int, tasksRemainingCount: Int) {
    logger.debug("renderTasks, tasksList: $tasksList, tasksCompleted: $tasksCompletedCount, tasksRemaining: $tasksRemainingCount")
    tasksOverviewRecyclerAdapter.updateData(tasksList.toMutableList())
    tasksCompletedText.text = tasksCompletedCount.toString()
    tasksRemainingText.text = tasksRemainingCount.toString()
  }
}
