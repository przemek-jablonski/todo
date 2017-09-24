package com.android.szparag.todoist.events

import com.android.szparag.todoist.views.contracts.View.PermissionType

data class PermissionEvent(val permissionType: PermissionType, val permissionResponse: PermissionResponse) {
  enum class PermissionResponse {
    PERMISSION_GRANTED,
    PERMISSION_GRANTED_ALREADY,
    PERMISSION_DENIED,
    PERMISSION_DENIED_FIRST_TIME,
    PERMISSION_DENIED_PERMANENTLY
  }
}