#!/usr/bin/env python3
"""Validate the Android Course Package before an APK build."""
from __future__ import annotations
import json
import sys
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
COURSE = ROOT / "app/src/main/assets/course/as-academy-c"
ALLOWED_BLOCKS = {"TITLE","SUBTITLE","PARAGRAPH","LIST","TABLE","IMAGE","DIAGRAM","CODE","OUTPUT","TIP","WARNING","NOTE","IMPORTANT","EXERCISE","QUIZ","PROJECT","REFERENCE"}
errors: list[str] = []

def load(path: Path):
    try:
        with path.open(encoding="utf-8") as f:
            return json.load(f)
    except Exception as exc:
        errors.append(f"invalid JSON {path.relative_to(ROOT)}: {exc}")
        return None

def require(condition: bool, message: str):
    if not condition:
        errors.append(message)

manifest = load(COURSE / "manifest.json")
levels = load(COURSE / "levels.json") or []
chapters = load(COURSE / "chapters.json") or []
require(isinstance(manifest, dict), "manifest.json must be an object")
if isinstance(manifest, dict):
    require(manifest.get("courseId") == "as-academy-c", "manifest courseId must be as-academy-c")
require(len(levels) == 4, f"expected 4 levels, found {len(levels)}")
level_ids = {x.get("id") for x in levels if isinstance(x, dict)}
require(len(level_ids) == len(levels), "duplicate or missing level id")

chapter_ids = set()
for chapter in chapters:
    if not isinstance(chapter, dict):
        errors.append("chapter entry must be object"); continue
    cid = chapter.get("id")
    require(bool(cid), "chapter missing id")
    require(cid not in chapter_ids, f"duplicate chapter id: {cid}")
    chapter_ids.add(cid)
    require(chapter.get("levelId") in level_ids, f"chapter {cid} has invalid levelId")

lesson_files = sorted((COURSE / "lessons").glob("*.json"))
require(len(lesson_files) == 28, f"expected 28 lesson files, found {len(lesson_files)}")
lesson_ids = set()
for path in lesson_files:
    lesson = load(path)
    if not isinstance(lesson, dict): continue
    lid = lesson.get("id")
    require(bool(lid), f"{path.name}: missing lesson id")
    require(lid not in lesson_ids, f"duplicate lesson id: {lid}")
    lesson_ids.add(lid)
    require(lesson.get("chapterId") in chapter_ids, f"{lid}: invalid chapterId")
    blocks = lesson.get("blocks")
    require(isinstance(blocks, list), f"{lid}: blocks must be array")
    if isinstance(blocks, list):
        require(len(blocks) >= 3, f"{lid}: lesson is too shallow ({len(blocks)} blocks)")
        block_ids = set()
        for block in blocks:
            if not isinstance(block, dict): errors.append(f"{lid}: block must be object"); continue
            bid = block.get("id")
            require(bool(bid), f"{lid}: block missing id")
            require(bid not in block_ids, f"{lid}: duplicate block id {bid}")
            block_ids.add(bid)
            require(block.get("type") in ALLOWED_BLOCKS, f"{lid}/{bid}: invalid block type {block.get('type')}")
            require(bool(str(block.get("content", "")).strip()), f"{lid}/{bid}: empty content")

exercises = load(COURSE / "exercises/exercises.json") or []
require(len(exercises) == 28, f"expected 28 exercises, found {len(exercises)}")
exercise_ids, exercise_lessons = set(), set()
for item in exercises:
    if not isinstance(item, dict): errors.append("exercise must be object"); continue
    eid, lid = item.get("id"), item.get("lessonId")
    require(eid not in exercise_ids, f"duplicate exercise id {eid}"); exercise_ids.add(eid)
    require(lid in lesson_ids, f"exercise {eid}: invalid lessonId {lid}"); exercise_lessons.add(lid)
    require(bool(item.get("prompt")), f"exercise {eid}: empty prompt")
    require(bool(item.get("acceptance")), f"exercise {eid}: missing acceptance criteria")
require(exercise_lessons == lesson_ids, "exercise coverage must include every lesson")

quizzes = load(COURSE / "quizzes/quizzes.json") or []
require(len(quizzes) == 28, f"expected 28 quizzes, found {len(quizzes)}")
quiz_ids, quiz_lessons, question_ids = set(), set(), set()
for quiz in quizzes:
    if not isinstance(quiz, dict): errors.append("quiz must be object"); continue
    qid, lid = quiz.get("id"), quiz.get("lessonId")
    require(qid not in quiz_ids, f"duplicate quiz id {qid}"); quiz_ids.add(qid)
    require(lid in lesson_ids, f"quiz {qid}: invalid lessonId {lid}"); quiz_lessons.add(lid)
    questions = quiz.get("questions")
    require(isinstance(questions, list) and len(questions) >= 3, f"quiz {qid}: expected at least 3 questions")
    if isinstance(questions, list):
        for question in questions:
            if not isinstance(question, dict): errors.append(f"quiz {qid}: question must be object"); continue
            question_id = question.get("id")
            require(question_id not in question_ids, f"duplicate question id {question_id}"); question_ids.add(question_id)
            answers = question.get("answers")
            require(isinstance(answers, list) and len(answers) >= 2, f"{question_id}: expected at least 2 answers")
            if isinstance(answers, list):
                correct = sum(1 for a in answers if isinstance(a, dict) and a.get("isCorrect") is True)
                require(correct >= 1, f"{question_id}: no correct answer")
            require(bool(question.get("question")), f"{question_id}: empty question")
            require(bool(question.get("explanation")), f"{question_id}: empty explanation")
require(quiz_lessons == lesson_ids, "quiz coverage must include every lesson")

projects = load(COURSE / "projects/projects.json") or []
require(len(projects) >= 12, f"expected at least 12 projects, found {len(projects)}")
project_ids = set()
for project in projects:
    if not isinstance(project, dict): errors.append("project must be object"); continue
    pid = project.get("id")
    require(pid not in project_ids, f"duplicate project id {pid}"); project_ids.add(pid)
    require(project.get("levelId") in level_ids, f"project {pid}: invalid levelId")
    require(bool(project.get("steps") or project.get("deliverables")), f"project {pid}: missing steps/deliverables")

glossary = load(COURSE / "glossary/glossary.json") or []
terms = set()
for entry in glossary:
    if not isinstance(entry, dict): errors.append("glossary entry must be object"); continue
    term = str(entry.get("term", "")).strip()
    require(bool(term), "glossary term is empty")
    key = term.casefold()
    require(key not in terms, f"duplicate glossary term {term}"); terms.add(key)
    require(bool(str(entry.get("definition", "")).strip()), f"glossary {term}: empty definition")

if errors:
    print("COURSE VALIDATION FAILED")
    for error in errors:
        print(f"- {error}")
    sys.exit(1)
print(f"COURSE VALIDATION PASSED: {len(lesson_ids)} lessons, {len(exercises)} exercises, {len(quizzes)} quizzes, {len(question_ids)} questions, {len(projects)} projects, {len(glossary)} glossary entries")
