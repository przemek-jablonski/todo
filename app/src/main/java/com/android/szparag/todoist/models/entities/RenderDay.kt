package com.android.szparag.todoist.models.entities

data class RenderDay(
    val unixTimestamp: Long,
    val dayName: String,
    val dayNumber: Int,
    val monthNumber: Int,
    val monthName: String,
    val yearNumber: Int,
    val tasksCompletedCount: Int,
    val tasksRemainingCount: Int
) {

  override fun equals(other: Any?): Boolean = other.takeIf { it is RenderDay }
      ?.let { (it as RenderDay).unixTimestamp == this.unixTimestamp }
      ?: false
}