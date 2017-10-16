package com.android.szparag.todoist

import android.support.multidex.MultiDexApplication
import com.squareup.leakcanary.LeakCanary

/**
 * Created by Przemyslaw Jablonski (github.com/sharaquss, pszemek.me) on 29/09/2017.
 */
class TodoistApplication : MultiDexApplication() {

  override fun onCreate() {
    super.onCreate()
    if (LeakCanary.isInAnalyzerProcess(this)) { return }
    LeakCanary.install(this)
  }
}