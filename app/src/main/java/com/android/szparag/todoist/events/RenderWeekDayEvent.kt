package com.android.szparag.todoist.events

import com.android.szparag.todoist.models.entities.RenderWeekDay

data class RenderWeekDayEvent(
    val weekList: List<RenderWeekDay>
)