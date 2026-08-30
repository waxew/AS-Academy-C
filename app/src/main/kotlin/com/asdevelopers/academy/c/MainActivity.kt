package com.asdevelopers.academy.c

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.asdevelopers.academy.core.content.LearningExtras
import com.asdevelopers.academy.core.content.LearningExtrasLoader
import com.asdevelopers.academy.core.database.AcademyDatabase
import com.asdevelopers.academy.core.database.ExerciseDraftEntity
import com.asdevelopers.academy.core.database.LearningCompletionEntity
import com.asdevelopers.academy.core.database.LessonProgressEntity
import com.asdevelopers.academy.core.database.ProjectProgressEntity
import com.asdevelopers.academy.core.database.QuizResultEntity
import com.asdevelopers.academy.core.project.ProjectProgress
import com.asdevelopers.academy.core.ui.content.LessonRenderer
import com.asdevelopers.academy.core.ui.screens.AcademyExerciseScreen
import com.asdevelopers.academy.core.ui.screens.AcademyProjectScreen
import com.asdevelopers.academy.core.ui.screens.AcademyQuizScreen
import com.asdevelopers.academy.course.model.Lesson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json

private const val COURSE_ID = "as-academy-c"

/** نقطه ورود اپ C؛ مدل، UI Engine و Database مشترک از AS-Academy-Core مصرف می‌شوند. */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { MaterialTheme { CCourseHost() } }
    }
}

/** Host فقط مقصد محتوای اختصاصی C را نگه می‌دارد و منطق Screenها را از Core می‌گیرد. */
private sealed interface CourseScreen {
    data object Home : CourseScreen
    data class LessonDetail(val id: String) : CourseScreen
    data class ExerciseDetail(val id: String) : CourseScreen
    data class QuizDetail(val id: String) : CourseScreen
    data class ProjectDetail(val id: String) : CourseScreen
}

/** بارگذاری Assetهای دوره و اتصال آن‌ها به Progress/Draft/Assessment مشترک Core. */
@Composable
private fun CCourseHost() {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val database = remember { AcademyDatabase.create(context, "as_academy_c.db") }
    var lessons by remember { mutableStateOf<List<Lesson>>(emptyList()) }
    var extras by remember { mutableStateOf(LearningExtras()) }
    var screen by remember { mutableStateOf<CourseScreen>(CourseScreen.Home) }
    var error by remember { mutableStateOf<String?>(null) }

    DisposableEffect(database) { onDispose { database.close() } }

    LaunchedEffect(Unit) {
        runCatching {
            withContext(Dispatchers.IO) {
                val json = Json { ignoreUnknownKeys = true }
                val base = "course/$COURSE_ID/lessons"
                val loadedLessons = context.assets.list(base).orEmpty()
                    .filter { it.endsWith(".json") }
                    .sorted()
                    .map { fileName ->
                        val raw = context.assets.open("$base/$fileName").bufferedReader(Charsets.UTF_8).use { it.readText() }
                        json.decodeFromString<Lesson>(raw)
                    }
                    .sortedBy { it.id }
                loadedLessons to LearningExtrasLoader(context.assets).load(COURSE_ID)
            }
        }.onSuccess { (loadedLessons, loadedExtras) ->
            lessons = loadedLessons
            extras = loadedExtras
        }.onFailure { error = it.message ?: it.toString() }
    }

    BackHandler(enabled = screen !is CourseScreen.Home) { screen = CourseScreen.Home }

    when {
        error != null -> CourseMessage("خطا در بارگذاری دوره C", error.orEmpty())
        lessons.isEmpty() -> CourseMessage("در حال بارگذاری دوره C", "محتوای آموزشی آماده‌سازی می‌شود…")
        else -> when (val destination = screen) {
            CourseScreen.Home -> CCourseHome(
                lessons = lessons,
                extras = extras,
                database = database,
                onLessonClick = { screen = CourseScreen.LessonDetail(it) },
                onExerciseClick = { screen = CourseScreen.ExerciseDetail(it) },
                onQuizClick = { screen = CourseScreen.QuizDetail(it) },
                onProjectClick = { screen = CourseScreen.ProjectDetail(it) }
            )

            is CourseScreen.LessonDetail -> {
                val lesson = lessons.firstOrNull { it.id == destination.id }
                if (lesson == null) CourseMessage("درس پیدا نشد", destination.id)
                else Scaffold(
                    topBar = { TopBackBar("درس", onBack = { screen = CourseScreen.Home }) },
                    bottomBar = {
                        Button(
                            onClick = {
                                val now = System.currentTimeMillis()
                                scope.launch {
                                    database.progressDao().upsert(
                                        LessonProgressEntity(
                                            courseId = COURSE_ID,
                                            lessonId = lesson.id,
                                            status = "COMPLETED",
                                            progressPercent = 100,
                                            lastBlockIndex = lesson.blocks.lastIndex.coerceAtLeast(0),
                                            studySeconds = 0,
                                            lastOpenedAt = now,
                                            completedAt = now
                                        )
                                    )
                                }
                            },
                            modifier = Modifier.fillMaxWidth().padding(12.dp)
                        ) { Text("ثبت درس به‌عنوان تکمیل‌شده") }
                    }
                ) { padding ->
                    LessonRenderer(
                        lesson = lesson,
                        modifier = Modifier.fillMaxSize().padding(padding).padding(horizontal = 16.dp),
                        onExerciseClick = { screen = CourseScreen.ExerciseDetail(it) },
                        onQuizClick = { screen = CourseScreen.QuizDetail(it) },
                        onProjectClick = { screen = CourseScreen.ProjectDetail(it) }
                    )
                }
            }

            is CourseScreen.ExerciseDetail -> {
                val exercise = extras.exercises.firstOrNull { it.id == destination.id }
                if (exercise == null) CourseMessage("تمرین پیدا نشد", destination.id)
                else {
                    val draft by database.exerciseDraftDao().observe(COURSE_ID, exercise.id).collectAsState(initial = null)
                    Scaffold(topBar = { TopBackBar("تمرین", onBack = { screen = CourseScreen.Home }) }) { padding ->
                        AcademyExerciseScreen(
                            exercise = exercise,
                            modifier = Modifier.fillMaxSize().padding(padding),
                            initialAnswer = draft?.answer.orEmpty(),
                            onDraftChanged = { answer ->
                                scope.launch {
                                    database.exerciseDraftDao().upsert(
                                        ExerciseDraftEntity(COURSE_ID, exercise.id, answer, System.currentTimeMillis())
                                    )
                                }
                            },
                            onCompleted = { answer ->
                                val now = System.currentTimeMillis()
                                scope.launch {
                                    database.exerciseDraftDao().upsert(ExerciseDraftEntity(COURSE_ID, exercise.id, answer, now))
                                    database.learningCompletionDao().upsert(
                                        LearningCompletionEntity(
                                            key = "$COURSE_ID:EXERCISE:${exercise.id}",
                                            courseId = COURSE_ID,
                                            targetType = "EXERCISE",
                                            targetId = exercise.id,
                                            completed = true,
                                            completedAt = now
                                        )
                                    )
                                }
                            }
                        )
                    }
                }
            }

            is CourseScreen.QuizDetail -> {
                val quiz = extras.quizzes.firstOrNull { it.id == destination.id }
                if (quiz == null) CourseMessage("آزمون پیدا نشد", destination.id)
                else Scaffold(topBar = { TopBackBar("آزمون", onBack = { screen = CourseScreen.Home }) }) { padding ->
                    AcademyQuizScreen(
                        quiz = quiz,
                        modifier = Modifier.fillMaxSize().padding(padding),
                        onCompleted = { score ->
                            val now = System.currentTimeMillis()
                            scope.launch {
                                database.quizResultDao().insert(
                                    QuizResultEntity(
                                        attemptId = "$COURSE_ID:${quiz.id}:$now",
                                        courseId = COURSE_ID,
                                        quizId = quiz.id,
                                        scorePercent = score.scorePercent,
                                        correctCount = score.correctQuestionIds.size,
                                        wrongCount = score.wrongQuestionIds.size,
                                        weakTags = score.weakTags.sorted().joinToString("|"),
                                        completedAt = now
                                    )
                                )
                            }
                        }
                    )
                }
            }

            is CourseScreen.ProjectDetail -> {
                val project = extras.projects.firstOrNull { it.id == destination.id }
                if (project == null) CourseMessage("پروژه پیدا نشد", destination.id)
                else {
                    val stored by database.projectProgressDao().observe(COURSE_ID, project.id).collectAsState(initial = null)
                    val progress = stored?.let { entity ->
                        ProjectProgress(
                            courseId = entity.courseId,
                            projectId = entity.projectId,
                            completedMilestoneIds = entity.completedMilestoneIds.split('|').filter(String::isNotBlank).toSet(),
                            draft = entity.draft,
                            updatedAtEpochMillis = entity.updatedAt,
                            completedAtEpochMillis = entity.completedAt
                        )
                    }
                    Scaffold(topBar = { TopBackBar("پروژه", onBack = { screen = CourseScreen.Home }) }) { padding ->
                        AcademyProjectScreen(
                            project = project,
                            progress = progress,
                            modifier = Modifier.fillMaxSize().padding(padding),
                            onProgressChanged = { next ->
                                val completedAt = next.completedAtEpochMillis
                                scope.launch {
                                    database.projectProgressDao().upsert(
                                        ProjectProgressEntity(
                                            courseId = COURSE_ID,
                                            projectId = project.id,
                                            completedMilestoneIds = next.completedMilestoneIds.sorted().joinToString("|"),
                                            draft = next.draft,
                                            updatedAt = next.updatedAtEpochMillis,
                                            completedAt = completedAt
                                        )
                                    )
                                    if (completedAt != null) {
                                        database.learningCompletionDao().upsert(
                                            LearningCompletionEntity(
                                                key = "$COURSE_ID:PROJECT:${project.id}",
                                                courseId = COURSE_ID,
                                                targetType = "PROJECT",
                                                targetId = project.id,
                                                completed = true,
                                                completedAt = completedAt
                                            )
                                        )
                                    }
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

/** Dashboard دوره و دسترسی به تمام فعالیت‌های یادگیری. */
@Composable
private fun CCourseHome(
    lessons: List<Lesson>,
    extras: LearningExtras,
    database: AcademyDatabase,
    onLessonClick: (String) -> Unit,
    onExerciseClick: (String) -> Unit,
    onQuizClick: (String) -> Unit,
    onProjectClick: (String) -> Unit
) {
    val lessonProgress by database.progressDao().observeCourseWithLegacy(COURSE_ID).collectAsState(initial = emptyList())
    val completions by database.learningCompletionDao().observeCourse(COURSE_ID).collectAsState(initial = emptyList())
    val quizResults by database.quizResultDao().observeAll().collectAsState(initial = emptyList())
    val completedLessons = lessons.count { lesson -> lessonProgress.any { it.lessonId == lesson.id && it.status == "COMPLETED" } }
    val completedExercises = extras.exercises.count { exercise -> completions.any { it.targetType == "EXERCISE" && it.targetId == exercise.id && it.completed } }
    val completedProjects = extras.projects.count { project -> completions.any { it.targetType == "PROJECT" && it.targetId == project.id && it.completed } }
    val passedQuizzes = extras.quizzes.count { quiz -> quizResults.any { it.courseId == COURSE_ID && it.quizId == quiz.id && it.scorePercent >= quiz.passingScorePercent } }
    val lessonPercent = if (lessons.isEmpty()) 0 else completedLessons * 100 / lessons.size

    LazyColumn(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text("آموزش جامع زبان C", style = MaterialTheme.typography.headlineMedium)
                Text("از مبانی زبان C تا حافظه، POSIX، شبکه، امنیت و Capstone")
                Text("پیشرفت درس‌ها: $completedLessons از ${lessons.size} — $lessonPercent٪")
                LinearProgressIndicator(progress = { lessonPercent / 100f }, modifier = Modifier.fillMaxWidth())
                Text("تمرین: $completedExercises/${extras.exercises.size} • آزمون قبول‌شده: $passedQuizzes/${extras.quizzes.size} • پروژه: $completedProjects/${extras.projects.size}")
            }
        }
        item { SectionTitle("درس‌ها") }
        items(lessons, key = Lesson::id) { lesson -> LearningCard(lesson.title, lesson.summary) { onLessonClick(lesson.id) } }
        item { SectionTitle("تمرین‌ها") }
        items(extras.exercises, key = { it.id }) { exercise -> LearningCard(exercise.title, "${exercise.difficulty.name} • ${exercise.lessonId}") { onExerciseClick(exercise.id) } }
        item { SectionTitle("آزمون‌ها") }
        items(extras.quizzes, key = { it.id }) { quiz -> LearningCard(quiz.title, "${quiz.questions.size} سؤال • حدنصاب ${quiz.passingScorePercent}٪") { onQuizClick(quiz.id) } }
        item { SectionTitle("پروژه‌ها") }
        items(extras.projects, key = { it.id }) { project -> LearningCard(project.title, "${project.milestones.size} مرحله • ${project.difficulty}") { onProjectClick(project.id) } }
    }
}

@Composable
private fun TopBackBar(title: String, onBack: () -> Unit) {
    Row(Modifier.fillMaxWidth().padding(12.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        OutlinedButton(onClick = onBack) { Text("بازگشت") }
        Text(title, style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(top = 8.dp))
    }
}

@Composable private fun SectionTitle(title: String) { Text(title, style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(top = 8.dp)) }

@Composable
private fun LearningCard(title: String, subtitle: String, onClick: () -> Unit) {
    Card(onClick = onClick, modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(title, style = MaterialTheme.typography.titleMedium)
            Text(subtitle, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
private fun CourseMessage(title: String, message: String) {
    Column(Modifier.fillMaxSize().padding(24.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(title, style = MaterialTheme.typography.headlineSmall)
        Text(message)
    }
}
