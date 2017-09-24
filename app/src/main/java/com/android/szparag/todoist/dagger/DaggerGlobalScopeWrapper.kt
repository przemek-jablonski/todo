package com.android.szparag.todoist.dagger

import android.content.Context

class DaggerGlobalScopeWrapper {

  //todo: make dagger scoped
  companion object {
    private var component: TodoistMainComponent? = null

    fun getComponent(context: Context): TodoistMainComponent =  if (component == null) constructComponent(context) else component!!

    private fun constructComponent(context: Context): TodoistMainComponent {
      component = DaggerTodoistMainComponent.builder()
          .todoistMainModule(TodoistMainModule(context))
          .build()
      return component!!
    }
  }

}