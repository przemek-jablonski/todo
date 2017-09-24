package com.android.szparag.todoist.views

import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.util.DisplayMetrics
import android.view.View
import com.android.szparag.todoist.AnimationEvent
import com.android.szparag.todoist.AnimationEvent.AnimationEventType.END
import com.android.szparag.todoist.AnimationEvent.AnimationEventType.REPEAT
import com.android.szparag.todoist.AnimationEvent.AnimationEventType.START
import com.android.szparag.todoist.R.id
import com.android.szparag.todoist.R.layout
import com.android.szparag.todoist.SmoothScrollLinearLayoutManager
import com.android.szparag.todoist.WeekRvAdapter
import com.android.szparag.todoist.dagger.DaggerGlobalScopeWrapper
import com.android.szparag.todoist.getDisplayMetrics
import com.android.szparag.todoist.presenters.contracts.WeekPresenter
import com.android.szparag.todoist.resize
import com.android.szparag.todoist.setupGranularClickListener
import com.android.szparag.todoist.views.contracts.WeekView
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.rxkotlin.subscribeBy
import kotterknife.bindView
import javax.inject.Inject

class TodoistWeekActivity : TodoistBaseActivity<WeekPresenter>(), WeekView {

  var recyclerHeight: Int? = null
  lateinit var displayMetrics: DisplayMetrics
  val calendarWeekRecyclerView: RecyclerView by bindView(id.recyclerview_calendar_week)

  @Inject override lateinit var presenter: WeekPresenter


  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(layout.activity_todoist_week)
    setSupportActionBar(findViewById(id.toolbar) as Toolbar)
  }

  override fun onStart() {
    super.onStart()
    DaggerGlobalScopeWrapper.getComponent(this).inject(this) //todo: find a way to generify them in Kotlin
    presenter.attach(this) //todo: find a way to generify them in Kotlin
    displayMetrics = getDisplayMetrics()
  }

  override fun setupWeekdaysList() { //todo: should be Completable
    calendarWeekRecyclerView.apply {
      recyclerHeight = this.height
      this.adapter = WeekRvAdapter()
      val layoutManager = SmoothScrollLinearLayoutManager(this@TodoistWeekActivity)
      this.layoutManager = layoutManager
      this.addItemDecoration(DividerItemDecoration(this.context, layoutManager.orientation))
    }
  }

  override fun onUserDayPicked(): Observable<Pair<View, Int>> {
    logger.debug("onUserDayPicked")
    return calendarWeekRecyclerView
        .setupGranularClickListener()
        .subscribeItemClick() // Pair<View, Int>
//        .map { it.second }
  }

  override fun resizeDayToFullscreen(view: View, positionInList: Int): Observable<AnimationEvent> {
    logger.debug("resizeDayToFullscreen, pos: $positionInList, view: $view")
    return view.resize(targetHeight = calendarWeekRecyclerView.height).duration().play()
  }


  override fun fixPositionByScrolling(positionInList: Int): Completable {
    logger.debug("fixPositionByScrolling, pos: $positionInList")
    return Completable.fromAction { calendarWeekRecyclerView.smoothScrollToPosition(positionInList) }
  }

//  private fun handleWeekItemClicked(recyclerView: RecyclerView, position: Int, view: View) {
//    view
//        .resize(targetHeight = recyclerView.height)
//        .duration()
//        .play()
//        .subscribeBy(onNext = { (eventType) ->
//          when (eventType) {
//            START -> {
//            }
//            END -> {
//              recyclerView.smoothScrollToPosition(position)
//            }
//            REPEAT -> {
//            }
//          }
//        })
//  }

}
