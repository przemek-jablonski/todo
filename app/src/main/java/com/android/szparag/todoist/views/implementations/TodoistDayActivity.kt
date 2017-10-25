package com.android.szparag.todoist.views.implementations

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.android.szparag.todoist.R
import com.android.szparag.todoist.presenters.contracts.DayPresenter
import com.android.szparag.todoist.views.contracts.DayView
import io.reactivex.Observable

private const val INTENT_EXTRAS_KEY_UNIXTIMESTAMP= "todoist.activity.day.timestamp"

class TodoistDayActivity : TodoistBaseActivity<DayPresenter>(), DayView {

  companion object Router {
    fun prepareDayActivityRoutingIntent(packageContext: Context, dayUnixTimestamp: Long) =
        Intent(packageContext, TodoistDayActivity::class.java)
            .putExtra(INTENT_EXTRAS_KEY_UNIXTIMESTAMP, dayUnixTimestamp)
  }

  override fun resolveStartupData() {
    logger.debug("resolveStartupData")
  }

  override fun subscribeUserBackButtonPressed(): Observable<Any> {
    logger.debug("subscribeUserBackButtonPressed")
    return Observable.create {}
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_todoist_day)
  }
}
