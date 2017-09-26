package com.android.szparag.todoist.models.entities

data class RenderWeekDay(
    val dayName: String,
    val dayNumber: Int,
    val monthNumber: Int,
    val monthName: String,
    val yearNumber: Int,
    val tasksDoneCount: Int,
    val tasksRemainingCount: Int)