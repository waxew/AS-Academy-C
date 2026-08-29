package com.asdevelopers.academy.c

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.asdevelopers.academy.core.ui.AcademyCourseApp

/**
 * نقطه ورود اپ آموزش C.
 * تمام Navigation، Progress، Search، Bookmark و UI عمومی از Core می آید؛
 * این اپ فقط Course Package اختصاصی C را با شناسه ثابت خود بارگذاری می کند.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AcademyCourseApp(courseId = COURSE_ID)
        }
    }

    private companion object {
        const val COURSE_ID = "as-academy-c"
    }
}
