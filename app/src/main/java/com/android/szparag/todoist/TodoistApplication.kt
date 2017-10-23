package com.android.szparag.todoist

import android.support.multidex.MultiDexApplication
import com.android.szparag.todoist.models.entities.TodoistDay
import com.android.szparag.todoist.models.entities.TodoistTask
import com.android.szparag.todoist.utils.Logger
import com.android.szparag.todoist.utils.Logger.Companion
import com.android.szparag.todoist.utils.asString
import com.android.szparag.todoist.utils.dayUnixTimestamp
import com.squareup.leakcanary.LeakCanary
import io.realm.Realm
import io.realm.RealmList
import org.joda.time.LocalDate
import java.util.Random

/**
 * Created by Przemyslaw Jablonski (github.com/sharaquss, pszemek.me) on 29/09/2017.
 */
class TodoistApplication : MultiDexApplication() {

  private val logger = Logger.create(TodoistApplication::class.java, hashCode())

  override fun onCreate() {
    super.onCreate()
    if (LeakCanary.isInAnalyzerProcess(this)) {
      return
    }

    LeakCanary.install(this)

    //temp debug only:
    val random = Random()
    Realm.init(this)
    val realm = Realm.getDefaultInstance()
    val currentDay = LocalDate()
    purgeDb(realm)
    randomizeDb(realm, random, currentDay)
    printDb(realm)
  }

  private fun purgeDb(realm: Realm) {
    realm.executeTransaction {
      realm.delete(TodoistDay::class.java)
      realm.delete(TodoistTask::class.java)
    }
  }

  private fun randomizeDb(realm: Realm, random: Random, currentDay: LocalDate) {
    (-28..28).forEachIndexed { index, _ ->
      if (random.nextFloat() > 0.65f) {
        val day = currentDay.plusDays(index)

        val tasks = RealmList<TodoistTask>()

        (0..random.nextInt(6)).forEach {
          tasks.add(TodoistTask(random.nextInt(100).toLong(), getRandomString(random)))
        }

        val todoistDay = TodoistDay(day.dayUnixTimestamp(), tasks)
        realm.executeTransaction {
          realm.insertOrUpdate(todoistDay)
        }

      }
    }
  }

  private fun printDb(realm: Realm) {
    realm.where(TodoistDay::class.java).findAll().forEach {
      logger.info("printdb: $it, ${it.tasks.asString()}")
    }
  }

  private fun getRandomString(random: Random): String {
    val randomStringBuilder = StringBuilder()
    val randomLength = random.nextInt(30)
    var tempChar: Char
    for (i in 0 until randomLength) {
      tempChar = if (random.nextFloat() > 0.93f) {
        127.toChar()
      } else {
        (random.nextInt(25) + 96).toChar()
      }
      randomStringBuilder.append(tempChar)
    }
    return randomStringBuilder.toString()
  }
}