package com.android.szparag.todoist.utils

import android.util.Log

val APPLICATION_TAG = "todoist"

class Logger {
  private lateinit var callerClassString: String
  private var callerClassObjectHashcode: Int = -1
  private var debugLoggingAvailable = true //todo: check if NOT in debug
  private var errorLoggingAvailable = true //todo: check if NOT in debug

  companion object {
    fun create(callerClass: Class<*>, hashCode: Int) = create(callerClass.simpleName.toString(), hashCode)

    fun create(callerString: String, hashCode: Int) = Logger().apply {
      this.callerClassString = callerString
      this.callerClassObjectHashcode = hashCode
    }

    fun createInfunctionalStub() = Logger().apply {
      this.debugLoggingAvailable = false
      this.errorLoggingAvailable = false
    }
  }

  init {
    //todo: check if NOT in debug
  }

  fun debug(string: String?) = log(Log.DEBUG, string)
  fun info(string: String?) = log(Log.INFO, string)
  fun warn(string: String?) = log(Log.WARN, string)
  fun error(string: String?) = log(Log.ERROR, string)
  fun error(string: String?, exception: Exception) = log(Log.ERROR, string, exception)
  fun error(string: String?, throwable: Throwable) = log(Log.ERROR, string, throwable)

  //todo: check if app in debug, if not so then don't log anything
  private fun log(level: Int = Log.DEBUG, string: String?, exception: Throwable? = null) {
    if (!debugLoggingAvailable) return
    if (level == Log.ERROR && !errorLoggingAvailable) return
    exception?.let { Log.println(level, APPLICATION_TAG, "$callerClassString[$callerClassObjectHashcode]: $string, exc: $exception") }
        ?: Log.println(level, APPLICATION_TAG, "$callerClassString[$callerClassObjectHashcode]: $string")

    exception?.let { exception.printStackTrace() }
  }
}