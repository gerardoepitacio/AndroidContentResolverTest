package com.kata.formiik.contentprovidertest

import android.content.Context
import android.database.Cursor
import android.provider.CalendarContract

/**
 *
 * Returns the calendar id and name of a calendar that contains @gmail.com and it's from a google account.
 */
val Context.googleCalendars: List<Pair<Long, String>>
    get() {

        val result: MutableList<Pair<Long, String>> = mutableListOf()

        val projection: Array<String> = arrayOf(CalendarContract.Calendars._ID, CalendarContract.Calendars.NAME)

        // Selecting only the visible accounts.
        // this will return only the Google Accounts from the calendars table.
        val selection = "${CalendarContract.Calendars.VISIBLE} = ? AND ${CalendarContract.Calendars.ACCOUNT_TYPE} = ?"

        val selectionsArgs: Array<String> = arrayOf("1", "com.google")

        val ordering = CalendarContract.Calendars._ID

        val calendarCursor: Cursor? = this.contentResolver.query(CalendarContract.Calendars.CONTENT_URI, projection, selection, selectionsArgs, ordering)

        calendarCursor?.use {

            while (it.moveToNext()) {

                // if the calendar name matches with a google domain we are assuming that it's a calendar created by a Google Account.
                val calendarName: String = calendarCursor.getString(1)

                if (calendarName.endsWith("@gmail.com")) {

                    val calendarId: Long = calendarCursor.getLong(0)

                    result.add(Pair(calendarId, calendarName))
                }
            }
        }

        return result
    }