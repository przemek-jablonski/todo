package com.android.szparag.todoist

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.View.OnLongClickListener
import com.android.szparag.todoist.R.id
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject


/**
 * Created by Przemyslaw Jablonski (github.com/sharaquss, pszemek.me) on 9/16/2017.
 */
class ItemClickSupport {

  private lateinit var recyclerView : RecyclerView
  private val itemClickSubject: Subject<Pair<View, Int>> by lazy { PublishSubject.create<Pair<View, Int>>() }
  private val itemLongClickSubject: Subject<Pair<View, Int>> by lazy { PublishSubject.create<Pair<View, Int>>() }
  private var consumesLongClicks = false

  private val mAttachListener by lazy {
    object : RecyclerView.OnChildAttachStateChangeListener {
      override fun onChildViewDetachedFromWindow(view: View) {}
      override fun onChildViewAttachedToWindow(view: View) {
        view.setOnClickListener { clickedView ->
          itemClickSubject.onNext(Pair(clickedView, recyclerView.getChildViewHolder(clickedView).adapterPosition))
        }
        view.setOnLongClickListener { clickedView ->
          itemLongClickSubject.onNext(Pair(clickedView, recyclerView.getChildViewHolder(clickedView).adapterPosition))
          return@setOnLongClickListener if (consumesLongClicks) true else itemLongClickSubject.hasObservers()
        }
      }
    }
  }

  fun subscribeItemClick() = itemClickSubject
  fun subscribeItemLongClick(consumesClick: Boolean) = itemLongClickSubject

  fun attach(recycler: RecyclerView) {
    recyclerView = recycler
    recyclerView.let {
      it.setTag(R.id.item_click_support, this)
      it.addOnChildAttachStateChangeListener(mAttachListener)
    }
  }

  fun detach() {
    recyclerView.removeOnChildAttachStateChangeListener(mAttachListener)
    recyclerView.setTag(R.id.item_click_support, null)
  }


}