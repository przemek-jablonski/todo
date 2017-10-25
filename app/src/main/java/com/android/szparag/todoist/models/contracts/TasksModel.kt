package com.android.szparag.todoist.models.contracts

import com.android.szparag.todoist.models.entities.TodoistDay
import io.reactivex.Single

//todo this needs to be refactored more in reactiveness in mind
interface TasksModel : Model {

  fun getDay(unixTimestamp: Long): TodoistDay?

}