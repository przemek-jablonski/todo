package com.android.szparag.todoist.models.implementations

import com.android.szparag.todoist.models.contracts.DayModel
import com.android.szparag.todoist.models.contracts.UnixTimestamp
import com.android.szparag.todoist.models.entities.RenderDay
import com.android.szparag.todoist.models.entities.TodoistDay
import com.android.szparag.todoist.utils.Logger
import com.android.szparag.todoist.utils.toObservable
import com.android.szparag.todoist.utils.toRenderDay
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.realm.Realm
import org.joda.time.DateTime
import org.joda.time.LocalDate
import java.util.Locale

class TodoistDayModel(private val locale: Locale, private val realm: Realm) : DayModel {

  override val logger = Logger.create(this)
  private lateinit var currentDay: LocalDate

  override fun getDayData(unixTimestamp: UnixTimestamp): Single<RenderDay> {
    logger.debug("getDayData, unixTimestamp: $unixTimestamp")
    check(unixTimestamp >= 0)
    return Single.fromCallable {
      DateTime(unixTimestamp).toLocalDate().toRenderDay(locale)
    }
  }

  override fun subscribeForTasksData(unixTimestamp: UnixTimestamp): Observable<TodoistDay> {
    logger.debug("subscribeForTasksData, unixTimestamp: $unixTimestamp")
    check(unixTimestamp >= 0)
    return realm.where(TodoistDay::class.java).equalTo("unixTimestamp", unixTimestamp).findAll().toObservable()
  }

  override fun subscribeForDayData(unixTimestamp: UnixTimestamp): Observable<RenderDay> {
    logger.debug("subscribeForDayData, unixTimestamp: $unixTimestamp")
    check(unixTimestamp >= 0)
    return Observable.create { }
//    return subscribeForTasksData(unixTimestamp)
//        .doOnSubscribe { logger.debug("subscribeForTasksData.doOnSubscribe")}
//        .doOnEach { logger.debug("subscribeForTasksData.doOnEach, notification: $it")}
//        .map { todoistDay ->
//      getDayData(unixTimestamp).toRenderDay(
//          locale = locale,
//          tasksList = todoistDay?.tasks?.map { it.name } ?: emptyList(),
//          tasksCompletedCount = todoistDay.getDoneTasksCount(), //todo naming inconsistency
//          tasksRemainingCount = todoistDay.getRemainingTasksCount()
//      )
//    }
  }

  override fun attach(): Completable {
    logger.debug("attach")
    return Completable.fromAction {
      currentDay = LocalDate()
    }
  }

  override fun detach(): Completable {
    logger.debug("detach")
    return Completable.fromAction {
      realm.close()
    }
  }


}