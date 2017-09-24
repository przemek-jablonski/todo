package com.android.szparag.todoist.views

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.util.DisplayMetrics
import android.view.View
import com.android.szparag.todoist.AnimationEvent.AnimationEventType.END
import com.android.szparag.todoist.AnimationEvent.AnimationEventType.REPEAT
import com.android.szparag.todoist.AnimationEvent.AnimationEventType.START
import com.android.szparag.todoist.R.id
import com.android.szparag.todoist.R.layout
import com.android.szparag.todoist.SmoothScrollLinearLayoutManager
import com.android.szparag.todoist.WeekRvAdapter
import com.android.szparag.todoist.presenters.WeekPresenter
import com.android.szparag.todoist.resize
import com.android.szparag.todoist.setupGranularClickListener
import com.android.szparag.todoist.views.contracts.WeekView
import io.reactivex.Observable
import io.reactivex.rxkotlin.subscribeBy
import kotterknife.bindView

class TodoistWeekActivity : TodoistBaseActivity<WeekPresenter>(), WeekView {

  var recyclerHeight: Int? = null
  lateinit var displayMetrics: DisplayMetrics
  val calendarWeekRecyclerView: RecyclerView by bindView(id.recyclerview_calendar_week)

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(layout.activity_todoist_week)
    val toolbar = findViewById(id.toolbar) as Toolbar
    setSupportActionBar(toolbar)

    val fab = findViewById(id.fab) as FloatingActionButton
    fab.setOnClickListener { view ->
      Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
          .setAction("Action", null).show()
    }
    displayMetrics = DisplayMetrics()
    windowManager.defaultDisplay.getMetrics(displayMetrics)

    calendarWeekRecyclerView.apply {
      recyclerHeight = this.height
      this.adapter = WeekRvAdapter()
      val layoutManager = SmoothScrollLinearLayoutManager(this@TodoistWeekActivity)
      this.layoutManager = layoutManager
      this.addItemDecoration(DividerItemDecoration(this.context, layoutManager.orientation))
    }

    calendarWeekRecyclerView
        .setupGranularClickListener()
        .subscribeItemClick()
        .subscribeBy(
            onNext = {
              logger.debug("ItemClickSupport.subscribeItemClick.onNext: pos: ${it.second}, view: ${it.first}")
              handleWeekItemClicked(calendarWeekRecyclerView, it.second, it.first)
            },
            onError = {
              logger.error("ItemClickSupport.subscribeItemClick.onError: exc: $it")
            },
            onComplete = {
              logger.debug("ItemClickSupport.subscribeItemClick.onComplete")
            }
        )
  }

  override fun onUserDayPicked(): Observable<Any> {
    logger.debug("onUserDayPicked")
    return Observable.create { }
  }


  private fun handleWeekItemClicked(recyclerView: RecyclerView, position: Int, view: View) {
    view
        .resize(targetHeight = recyclerView.height)
        .duration()
        .play()
        .subscribeBy(onNext = { (eventType) ->
          when (eventType) {
            START -> {
            }
            END -> {
              recyclerView.smoothScrollToPosition(position)
            }
            REPEAT -> {
            }
          }
        })
  }

//  override fun onCreateOptionsMenu(menu: Menu): Boolean {
//    menuInflater.inflate(R.menu.menu_todoist_week, menu)
//    return true
//  }

}
