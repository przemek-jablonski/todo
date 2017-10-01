package com.android.szparag.todoist.views.implementations

import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.util.DisplayMetrics
import android.view.View
import com.android.szparag.todoist.AnimationEvent
import com.android.szparag.todoist.R.id
import com.android.szparag.todoist.R.layout
import com.android.szparag.todoist.SmoothScrollLinearLayoutManager
import com.android.szparag.todoist.WeekRvAdapter
import com.android.szparag.todoist.dagger.DaggerGlobalScopeWrapper
import com.android.szparag.todoist.models.entities.RenderWeekDays
import com.android.szparag.todoist.presenters.contracts.WeekPresenter
import com.android.szparag.todoist.utils.bindView
import com.android.szparag.todoist.utils.getDisplayMetrics
import com.android.szparag.todoist.utils.resize
import com.android.szparag.todoist.utils.setupGranularClickListener
import com.android.szparag.todoist.views.contracts.WeekView
import io.reactivex.Completable
import io.reactivex.Observable
import javax.inject.Inject

class TodoistWeekActivity : TodoistBaseActivity<WeekPresenter>(), WeekView {

  var recyclerHeight: Int? = null
  lateinit var displayMetrics: DisplayMetrics
  val calendarWeekRecyclerView: RecyclerView by bindView(id.recyclerview_calendar_week)
  val calendarWeekRecyclerViewLayoutManager by lazy { SmoothScrollLinearLayoutManager(this) }
  val calendarWeekRecyclerViewDecor by lazy {
    DividerItemDecoration(calendarWeekRecyclerView.context, calendarWeekRecyclerViewLayoutManager.orientation)
  }

  @Inject override lateinit var presenter: WeekPresenter


  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    logger.debug("onCreate, bundle $savedInstanceState")
    setContentView(layout.activity_todoist_week)
    setSupportActionBar(findViewById<View>(id.toolbar) as Toolbar)
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


  override fun setupWeekList(renderWeekDays: RenderWeekDays): Completable {
    logger.debug("setupWeekList, event: $renderWeekDays")
    return Completable.fromAction {
      calendarWeekRecyclerView.apply {
        recyclerHeight = this.height
        this.adapter = WeekRvAdapter(renderWeekDays.list)
        this.layoutManager = calendarWeekRecyclerViewLayoutManager
        this.addItemDecoration(calendarWeekRecyclerViewDecor)
      }
    }
  }

  override fun disposeWeekList(): Completable {
    logger.debug("disposeWeekList")
    return Completable.fromAction {
      logger.debug("disposeWeekList.run")
      calendarWeekRecyclerView.removeItemDecoration(calendarWeekRecyclerViewDecor)
    }
  }

  override fun subscribeUserDayPicked(): Observable<Pair<View, Int>> {
    logger.debug("subscribeUserDayPicked")
    return calendarWeekRecyclerView
        .setupGranularClickListener()
        .subscribeItemClick() // Pair<View, Int>
//        .map { it.second }
  }

  override fun animateWeekdayToFullscreen(view: View, positionInList: Int): Observable<AnimationEvent> {
    logger.debug("animateWeekdayToFullscreen, pos: $positionInList, view: $view")
    return view.resize(targetHeight = calendarWeekRecyclerView.height).duration().play()
  }


  override fun animateShiftItemOnScreenPosition(positionInList: Int): Completable {
    logger.debug("animateShiftItemOnScreenPosition, pos: $positionInList")
    return Completable.fromAction {
      logger.debug("animateShiftItemOnScreenPosition.run, pos: $positionInList")
      calendarWeekRecyclerView.smoothScrollToPosition(positionInList)
    }
  }


  override fun subscribeUserBackButtonPressed(): Observable<Any> {
    logger.debug("subscribeUserBackButtonPressed")
    return Observable.create { }
  }


}
