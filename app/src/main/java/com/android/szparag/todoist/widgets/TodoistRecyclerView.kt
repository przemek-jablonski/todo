package com.android.szparag.todoist.widgets

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.View
import com.android.szparag.todoist.utils.Logger
import com.android.szparag.todoist.utils.abs
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject

typealias RecyclerItemStableId = Long

class TodoistRecyclerView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RecyclerView(context, attrs, defStyleAttr) {

  private val logger = Logger.create(TodoistRecyclerView::class.java, hashCode())

  //todo: snapping to element
  //todo: attrs from xml
  //todo: employ some generic mechanism, so that subscribing for clicks returns ITEM, not item's stableid
  //todo encapsulate adapter here
  var flingVelocityXMultiplier = 1f
  var flingVelocityYMultiplier = 1f
  var flingVelocityXLimit = 25000
  var flingVelocityYLimit = 25000

  private val itemClickSubject: Subject<RecyclerItemStableId> by lazy { PublishSubject.create<RecyclerItemStableId>() }
  private val itemLongClickSubject: Subject<RecyclerItemStableId> by lazy { PublishSubject.create<RecyclerItemStableId>() }
  private var itemClicksListenerAttached = false
  private val itemClicksListener by lazy {
    object : RecyclerView.OnChildAttachStateChangeListener {
      override fun onChildViewDetachedFromWindow(view: View) {
        logger.debug("onChildViewDetachedFromWindow, view: ${view.hashCode()}")
        view.setOnClickListener(null)
        view.setOnLongClickListener(null)
      }
      override fun onChildViewAttachedToWindow(view: View) {
        logger.debug("onChildViewAttachedToWindow, view: ${view.hashCode()}")
        view.setOnClickListener { clickedView ->
          logger.debug("onChildViewAttachedToWindow.setOnClickListener, view: ${view.hashCode()}")
          itemClickSubject.onNext(adapter.getItemId(getChildViewHolder(clickedView).adapterPosition))
        }
        view.setOnLongClickListener { clickedView ->
          logger.debug("onChildViewAttachedToWindow.setOnLongClickListener, view: ${view.hashCode()}")
          itemLongClickSubject.onNext(adapter.getItemId(getChildViewHolder(clickedView).adapterPosition))
          return@setOnLongClickListener itemLongClickSubject.hasObservers()
        }
      }
    }
  }

  fun subscribeItemClicks(): Observable<RecyclerItemStableId> = attachItemClicksSubscriptionListener().run { itemClickSubject }
  fun subscribeItemLongClicks(): Observable<RecyclerItemStableId> = attachItemClicksSubscriptionListener().run { itemLongClickSubject }

  private fun attachItemClicksSubscriptionListener() {
    logger.debug("attachItemClicksSubscriptionListener, itemClicksListenerAttached: $itemClicksListenerAttached")
    if (!itemClicksListenerAttached) {
      logger.debug("attachItemClicksSubscriptionListener, ATTACHING")
      addOnChildAttachStateChangeListener(itemClicksListener)
      itemClicksListenerAttached = true
    }
  }

  fun processFlingX(velocityX: Int) = Math.min((velocityX.abs() * flingVelocityXMultiplier).toInt(), flingVelocityXLimit)
  fun processFlingY(velocityY: Int) = Math.min((velocityY.abs() * flingVelocityYMultiplier).toInt(), flingVelocityYLimit)

  override fun fling(velocityX: Int, velocityY: Int) = super.fling(
      if (velocityX >= 0) processFlingX(velocityX) else -processFlingX(velocityX),
      if (velocityY >= 0) processFlingY(velocityY) else -processFlingY(velocityY))

}