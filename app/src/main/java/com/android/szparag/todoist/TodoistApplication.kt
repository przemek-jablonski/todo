package com.android.szparag.todoist

import android.support.multidex.MultiDexApplication
import com.squareup.leakcanary.LeakCanary
import io.realm.Realm

/**
 * Created by Przemyslaw Jablonski (github.com/sharaquss, pszemek.me) on 29/09/2017.
 */
class TodoistApplication : MultiDexApplication() {

  override fun onCreate() {
    super.onCreate()
    if (LeakCanary.isInAnalyzerProcess(this)) {
      return
    }

    LeakCanary.install(this)

    //temp debug only:
    val realm = Realm.getDefaultInstance()


  }
}