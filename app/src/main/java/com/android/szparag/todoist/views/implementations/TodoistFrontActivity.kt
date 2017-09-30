package com.android.szparag.todoist.views.implementations

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.android.szparag.todoist.R
import com.android.szparag.todoist.events.PermissionEvent
import com.android.szparag.todoist.presenters.contracts.FrontPresenter
import com.android.szparag.todoist.views.contracts.FrontView
import com.android.szparag.todoist.views.contracts.View.Screen
import io.reactivex.Observable

class TodoistFrontActivity : TodoistBaseActivity<FrontPresenter>(), FrontView {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_todoist_front)
  }


  override fun subscribeUserBackButtonPressed(): Observable<Any> {
    logger.debug("subscribeUserBackButtonPressed")
    return Observable.create { }
  }

}
