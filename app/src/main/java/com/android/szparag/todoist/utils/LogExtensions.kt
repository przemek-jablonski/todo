package com.android.szparag.todoist.utils

import android.util.Log
import kotlin.reflect.KClass

val APPLICATION_TAG = "todoist"

class Logger {
  private lateinit var callerClassString: String
  private var callerClassObjectHashcode: Int = -1
  private val debugLoggingAvailable = true //todo: check if NOT in debug
  private val errorLoggingAvailable = true //todo: check if NOT in debug

  companion object {
    fun create(callerClass: Class<*>, hashCode: Int) = create(callerClass.simpleName.toString(), hashCode)

    fun create(callerString: String, hashCode: Int) = Logger().apply {
      this.callerClassString = callerString
      this.callerClassObjectHashcode = hashCode
    }
  }

  init {
    //todo: check if NOT in debug
  }

  fun debug(string: String?) = log(Log.DEBUG, string)
  fun info(string:String?) = log (Log.INFO, string)
  fun warn(string: String?) = log (Log.WARN, string)
  fun error(string: String?) = log(Log.ERROR, string)
  fun error(string: String?, exception: Exception) = log(Log.ERROR, string, exception)
  fun error(string: String?, throwable: Throwable) = log(Log.ERROR, string, throwable)

  private fun log(level: Int = Log.DEBUG, string: String?, exception: Throwable? = null) {
    exception?.let { Log.println(level, APPLICATION_TAG, "$callerClassString[$callerClassObjectHashcode]: $string, exc: $exception") }
        ?: Log.println(level, APPLICATION_TAG, "$callerClassString[$callerClassObjectHashcode]: $string")

    exception?.let { exception.printStackTrace() }
  }
}