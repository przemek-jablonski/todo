package com.android.szparag.todoist

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.util.DisplayMetrics
import android.view.View
import android.view.Menu
import android.view.MenuItem
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.DecelerateInterpolator
import com.android.szparag.todoist.ItemClickSupport.OnItemClickListener
import io.reactivex.rxkotlin.subscribeBy
import kotterknife.bindView

class TodoistWeekActivity : AppCompatActivity() {

  var recyclerHeight: Int? = null
  lateinit var displayMetrics : DisplayMetrics
  val calendarWeekRecyclerView: RecyclerView by bindView(R.id.recyclerview_calendar_week)

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_todoist_week)
    val toolbar = findViewById(R.id.toolbar) as Toolbar
    setSupportActionBar(toolbar)

    val fab = findViewById(R.id.fab) as FloatingActionButton
    fab.setOnClickListener { view ->
      Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
          .setAction("Action", null).show()
    }
    displayMetrics = DisplayMetrics()
    windowManager.defaultDisplay.getMetrics(displayMetrics)

    calendarWeekRecyclerView.apply {
      recyclerHeight = this.height
      this.adapter = WeekRvAdapter()
      val layoutManager = LinearLayoutManager(this@TodoistWeekActivity)
//          .setupSmoothScrolling(this@TodoistWeekActivity, 10f)
      this.layoutManager = layoutManager
      this.addItemDecoration(DividerItemDecoration(this.context, layoutManager.orientation))
    }

    ItemClickSupport.addTo(calendarWeekRecyclerView).setOnItemClickListener (object: OnItemClickListener {
      override fun onItemClicked(recyclerView: RecyclerView, position: Int, v: View) {
        handleWeekItemClicked(recyclerView, position, v)
      }
    })
  }

  private fun handleWeekItemClicked(recyclerView: RecyclerView, position: Int, view: View) {
//    Snackbar.make(view, "item clicked: $position", Snackbar.LENGTH_LONG).show()
    view
        .resize(targetHeight = recyclerView.height)
        .duration()
        .play()
        .subscribeBy(onNext = { (eventType) ->
          when(eventType) {
            AnimationEvent.AnimationEventType.START -> {}
            AnimationEvent.AnimationEventType.END -> {
              recyclerView.smoothScrollToPosition(position)
            }
            AnimationEvent.AnimationEventType.REPEAT -> {}
          }
        })
  }

  override fun onCreateOptionsMenu(menu: Menu): Boolean {
    menuInflater.inflate(R.menu.menu_todoist_week, menu)
    return true
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    val id = item.itemId
    if (id == R.id.action_settings) {
      return true
    }
    return super.onOptionsItemSelected(item)
  }
}
