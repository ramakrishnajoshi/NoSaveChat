package com.example.nosavechat.model

import android.provider.CallLog

/**
 * Data class representing a call log entry
 */
data class CallLogItem(
    val id: String,
    val number: String,
    val name: String?,
    val date: Long,
    val duration: Long,
    val type: Int,
    val formattedDate: String
) {
    val callTypeString: String
        get() = when (type) {
            CallLog.Calls.INCOMING_TYPE -> "Incoming"
            CallLog.Calls.OUTGOING_TYPE -> "Outgoing"
            CallLog.Calls.MISSED_TYPE -> "Missed"
            CallLog.Calls.REJECTED_TYPE -> "Rejected"
            CallLog.Calls.BLOCKED_TYPE -> "Blocked"
            else -> "Unknown"
        }
}