package com.android.szparag.todoist.views.implementations

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.android.szparag.todoist.R
import com.android.szparag.todoist.presenters.contracts.DayPresenter
import com.android.szparag.todoist.views.contracts.DayView
import io.reactivex.Observable

class TodoistDayActivity : TodoistBaseActivity<DayPresenter>(), DayView {
  override fun subscribeUserBackButtonPressed(): Observable<Any> {
    logger.debug("subscribeUserBackButtonPressed")
    return Observable.create {}
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_todoist_day)
  }
}
