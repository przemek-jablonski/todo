package com.android.szparag.todoist.models.implementations

import com.android.szparag.todoist.models.contracts.TasksModel
import com.android.szparag.todoist.models.entities.TodoistDay
import com.android.szparag.todoist.models.entities.TodoistTask
import com.android.szparag.todoist.utils.Logger
import com.android.szparag.todoist.utils.Logger.Companion
import com.android.szparag.todoist.utils.asFlowable
import hu.akarnokd.rxjava.interop.RxJavaInterop
import io.reactivex.Completable
import io.reactivex.CompletableEmitter
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.realm.Realm
import io.realm.Sort.DESCENDING

class TodoistTasksModel: TasksModel {

  private val realmInstance by lazy { Realm.getDefaultInstance() }
  override val logger = Logger.create(TodoistTasksModel::class, hashCode())


  override fun attach() = Completable.create {
    logger.debug("attach")
    logRealmChanges(realmInstance)
  }

  private fun logRealmChanges(realmInstance: Realm) {
    logger.debug("logRealmChanges, realm: $realmInstance")
    realmInstance.addChangeListener { realm ->
      realm.where(TodoistDay::class.java).findAll().forEach {
        logger.debug("logRealmChanges.listener, todoistDay: $it")
      }
    }
  }

  override fun detach() = Completable.create { emitter ->
    logger.debug("detach")
    realmInstance.close()
  }

//  override fun getTasksForDay(dayMillis: Long): Observable<TodoistTask> {
//    return realmInstance
//        .where(TodoistTask::class.java)
//        .contains("timeCreatedUnix", DESCENDING)
//        .asFlowable()
//        .subscribeOn(AndroidSchedulers.mainThread())
//    logger.debug("getTasksForDay")
//  }


  override fun getDay(dayMillis: Long): Observable<TodoistDay> {
    return RxJavaInterop.toV2Observable(realmInstance
        .where(TodoistDay::class.java)
        .equalTo("timestampDayStartUnix", dayMillis)
        .findFirst()
        .asObservable())
  }

  override fun getTasksForDay(dayMillis: Long): Flowable<List<TodoistTask>> {
    logger.debug("getTasksForDay")
  }
}