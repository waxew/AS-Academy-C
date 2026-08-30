package com.asdevelopers.academy.c

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.asdevelopers.academy.core.ui.content.LessonRenderer
import com.asdevelopers.academy.course.model.Lesson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json

/**
 * نقطه ورود اپ آموزش C.
 * مدل Lesson و Renderer از Core/Course مشترک می‌آیند و این Host فقط Asset اختصاصی C را انتخاب می‌کند.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                CCourseHost()
            }
        }
    }
}

/** Host سبک دوره تا زمان اتصال کامل CourseBundle، بدون کپی کردن Lesson renderer. */
@Composable
private fun CCourseHost() {
    val context = LocalContext.current
    var lessons by remember { mutableStateOf<List<Lesson>>(emptyList()) }
    var selectedLesson by remember { mutableStateOf<Lesson?>(null) }
    var error by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        runCatching {
            withContext(Dispatchers.IO) {
                val json = Json { ignoreUnknownKeys = true }
                val base = "course/as-academy-c/lessons"
                context.assets.list(base).orEmpty()
                    .filter { it.endsWith(".json") }
                    .sorted()
                    .map { fileName ->
                        val raw = context.assets.open("$base/$fileName")
                            .bufferedReader(Charsets.UTF_8)
                            .use { it.readText() }
                        json.decodeFromString<Lesson>(raw)
                    }
                    .sortedBy { it.order }
            }
        }.onSuccess { lessons = it }
            .onFailure { error = it.message ?: it.toString() }
    }

    when {
        error != null -> CourseMessage("خطا در بارگذاری دوره C", error.orEmpty())
        selectedLesson != null -> LessonRenderer(
            lesson = requireNotNull(selectedLesson),
            modifier = Modifier.fillMaxSize().padding(16.dp)
        )
        lessons.isEmpty() -> CourseMessage("در حال بارگذاری دوره C", "محتوای آموزشی آماده‌سازی می‌شود…")
        else -> CCourseHome(lessons = lessons, onLessonClick = { selectedLesson = it })
    }
}

/** صفحه فهرست واقعی ۲۸ درس C؛ جزئیات هر درس توسط Renderer مشترک Core نمایش داده می‌شود. */
@Composable
private fun CCourseHome(lessons: List<Lesson>, onLessonClick: (Lesson) -> Unit) {
    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text("آموزش جامع زبان C", style = MaterialTheme.typography.headlineMedium)
                Text("از مبانی زبان C تا حافظه، POSIX، شبکه، امنیت و Capstone")
                Text("${lessons.size} درس آموزشی")
            }
        }
        items(lessons, key = Lesson::id) { lesson ->
            Card(onClick = { onLessonClick(lesson) }) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(lesson.title, style = MaterialTheme.typography.titleMedium)
                    Text(lesson.summary, style = MaterialTheme.typography.bodyMedium)
                    Text("${lesson.estimatedMinutes} دقیقه", style = MaterialTheme.typography.labelMedium)
                }
            }
        }
    }
}

/** پیام Loading/Error یکپارچه و قابل خواندن برای Host دوره. */
@Composable
private fun CourseMessage(title: String, message: String) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(title, style = MaterialTheme.typography.headlineSmall)
        Text(message)
    }
}
