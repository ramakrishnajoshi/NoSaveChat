package com.example.nosavechat.repository

import android.content.Context
import android.database.Cursor
import android.provider.CallLog
import android.text.format.DateFormat
import com.example.nosavechat.model.CallLogItem
import java.util.Date

/**
 * Repository class to fetch call logs from the device
 */
class CallLogRepository(private val context: Context) {

    /**
     * Fetches recent call logs from the device
     * @return List of CallLogItem objects
     */
    fun getRecentCalls(): List<CallLogItem> {
        val callLogList = mutableListOf<CallLogItem>()
        val projection = arrayOf(
            CallLog.Calls._ID,
            CallLog.Calls.NUMBER,
            CallLog.Calls.CACHED_NAME,
            CallLog.Calls.DATE,
            CallLog.Calls.DURATION,
            CallLog.Calls.TYPE
        )

        // Sort by date in descending order (most recent first)
        val sortOrder = "${CallLog.Calls.DATE} DESC"

        context.contentResolver.query(
            CallLog.Calls.CONTENT_URI,
            projection,
            null,
            null,
            sortOrder
        )?.use { cursor ->
            while (cursor.moveToNext()) {
                val callLogItem = getCallLogFromCursor(cursor)
                callLogList.add(callLogItem)
            }
        }

        return callLogList
    }

    /**
     * Extracts call log data from cursor
     */
    private fun getCallLogFromCursor(cursor: Cursor): CallLogItem {
        val idIndex = cursor.getColumnIndex(CallLog.Calls._ID)
        val numberIndex = cursor.getColumnIndex(CallLog.Calls.NUMBER)
        val nameIndex = cursor.getColumnIndex(CallLog.Calls.CACHED_NAME)
        val dateIndex = cursor.getColumnIndex(CallLog.Calls.DATE)
        val durationIndex = cursor.getColumnIndex(CallLog.Calls.DURATION)
        val typeIndex = cursor.getColumnIndex(CallLog.Calls.TYPE)

        val id = if (idIndex != -1) cursor.getString(idIndex) else ""
        val number = if (numberIndex != -1) cursor.getString(numberIndex) else ""
        val name = if (nameIndex != -1) cursor.getString(nameIndex) else null
        val date = if (dateIndex != -1) cursor.getLong(dateIndex) else 0L
        val duration = if (durationIndex != -1) cursor.getLong(durationIndex) else 0L
        val type = if (typeIndex != -1) cursor.getInt(typeIndex) else 0

        // Format the date
        val formattedDate = DateFormat.format("MMM dd, yyyy hh:mm a", Date(date)).toString()

        return CallLogItem(
            id = id,
            number = number,
            name = name,
            date = date,
            duration = duration,
            type = type,
            formattedDate = formattedDate
        )
    }
}
