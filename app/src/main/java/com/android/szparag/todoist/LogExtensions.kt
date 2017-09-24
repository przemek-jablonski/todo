package com.android.szparag.todoist

import android.util.Log
import kotlin.reflect.KClass

val APPLICATION_TAG = "todoist"

class Logger {
  private lateinit var callerClassString: String
  private val debugLoggingAvailable = true //todo: check if NOT in debug
  private val errorLoggingAvailable = true //todo: check if NOT in debug

  companion object {
    fun create(callerClass : KClass<*>)
        = Logger().apply { this.callerClassString = callerClass.simpleName.toString() }
  }

  init {
    //todo: check if NOT in debug
  }

  fun debug(string: String?) = log(Log.DEBUG, string)
  fun error(string: String?) = log(Log.ERROR, string)
  fun error(string: String?, exception: Exception) = log(Log.ERROR, string, exception)
  fun error(string: String?, throwable: Throwable) = log(Log.ERROR, string, throwable)

  private fun log(level: Int = Log.DEBUG, string: String?, exception: Throwable? = null) {
    exception?.let {
      Log.println(level, APPLICATION_TAG, "$callerClassString: $string $exception")
    } ?: Log.println(level, APPLICATION_TAG, "$callerClassString: $string")

    exception?.let { exception.printStackTrace() }
  }
}