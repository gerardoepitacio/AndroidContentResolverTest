package com.kata.formiik.contentprovidertest

import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.database.MatrixCursor
import android.provider.CalendarContract
import io.mockk.every
import io.mockk.mockk
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers
import org.junit.Test

class ExtensionsTest {

    @Test
    fun getGoogleCalendarsTest() {

        // mocking the context
        val mockedContext: Context = mockk(relaxed = true)

        // mocking the content resolver
        val mockedContentResolver: ContentResolver = mockk(relaxed = true)

        val columns: Array<String> = arrayOf(
            CalendarContract.Calendars._ID,
            CalendarContract.Calendars.NAME
        )

        // response to be stubbed, this will help to stub
        // a response from a query in the mocked ContentResolver
        val matrixCursor: Cursor = MatrixCursor(columns).apply {

            this.addRow(arrayOf(1, "username01@gmail.com"))
            this.addRow(arrayOf(2, "name02")) // this row must be filtered by the extension.
            this.addRow(arrayOf(3, "username02@gmail.com"))
        }

        // stubbing content resolver in the mocked context.
        every { mockedContext.contentResolver } returns mockedContentResolver

        // stubbing the query.
        every { mockedContentResolver.query(CalendarContract.Calendars.CONTENT_URI, any(), any(), any(), any()) } returns matrixCursor

        val result: List<Pair<Long, String>> = mockedContext.googleCalendars

        // since googleCalendars extension returns only the calendar name that ends with @gmail.com
        // one row is filtered from the mocked response of the content resolver.
        assertThat(result.isNotEmpty(), Matchers.`is`(true))
        assertThat(result.size, Matchers.`is`(2))
    }
}