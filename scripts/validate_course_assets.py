#!/usr/bin/env python3
"""Validate the Android Course Package before an APK build."""
from __future__ import annotations
import json
import re
import sys
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
COURSE = ROOT / "app/src/main/assets/course/as-academy-c"
ALLOWED_BLOCKS = {"TITLE","SUBTITLE","PARAGRAPH","LIST","TABLE","IMAGE","DIAGRAM","CODE","OUTPUT","TIP","WARNING","NOTE","IMPORTANT","EXERCISE","QUIZ","PROJECT","REFERENCE"}
MIN_LESSON_BLOCKS = 12
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
branding = load(COURSE / "branding.json")
levels = load(COURSE / "levels.json") or []
chapters = load(COURSE / "chapters.json") or []
require(isinstance(manifest, dict), "manifest.json must be an object")
if isinstance(manifest, dict): require(manifest.get("courseId") == "as-academy-c", "manifest courseId must be as-academy-c")
require(isinstance(branding, dict), "branding.json must be an object")
if isinstance(branding, dict):
    for field in ("primaryColorHex", "secondaryColorHex", "accentColorHex"):
        value = branding.get(field)
        require(isinstance(value, str) and re.fullmatch(r"#[0-9A-Fa-f]{6}", value) is not None, f"branding {field} must be #RRGGBB")
require(len(levels) == 4, f"expected 4 levels, found {len(levels)}")
level_ids = {x.get("id") for x in levels if isinstance(x, dict)}
require(len(level_ids) == len(levels), "duplicate or missing level id")
chapter_ids = set()
for chapter in chapters:
    if not isinstance(chapter, dict): errors.append("chapter entry must be object"); continue
    cid = chapter.get("id"); require(bool(cid), "chapter missing id"); require(cid not in chapter_ids, f"duplicate chapter id: {cid}"); chapter_ids.add(cid)
    require(chapter.get("levelId") in level_ids, f"chapter {cid} has invalid levelId")

lesson_files = sorted((COURSE / "lessons").glob("*.json"))
require(len(lesson_files) == 28, f"expected 28 lesson files, found {len(lesson_files)}")
lesson_ids = set()
for path in lesson_files:
    lesson = load(path)
    if not isinstance(lesson, dict): continue
    lid = lesson.get("id"); require(bool(lid), f"{path.name}: missing lesson id"); require(lid not in lesson_ids, f"duplicate lesson id: {lid}"); lesson_ids.add(lid)
    require(lesson.get("chapterId") in chapter_ids, f"{lid}: invalid chapterId")
    require(int(lesson.get("estimatedMinutes", 0)) >= 40, f"{lid}: estimatedMinutes must be >= 40 for comprehensive lessons")
    blocks = lesson.get("blocks"); require(isinstance(blocks, list), f"{lid}: blocks must be array")
    if isinstance(blocks, list):
        require(len(blocks) >= MIN_LESSON_BLOCKS, f"{lid}: lesson is too shallow ({len(blocks)} blocks; minimum {MIN_LESSON_BLOCKS})")
        block_ids, block_types = set(), set()
        for block in blocks:
            if not isinstance(block, dict): errors.append(f"{lid}: block must be object"); continue
            bid = block.get("id"); require(bool(bid), f"{lid}: block missing id"); require(bid not in block_ids, f"{lid}: duplicate block id {bid}"); block_ids.add(bid)
            btype = block.get("type"); block_types.add(btype); require(btype in ALLOWED_BLOCKS, f"{lid}/{bid}: invalid block type {btype}")
            require(bool(str(block.get("content", "")).strip()), f"{lid}/{bid}: empty content")
        require("CODE" in block_types, f"{lid}: comprehensive lesson must contain CODE")
        require("EXERCISE" in block_types, f"{lid}: comprehensive lesson must contain EXERCISE")
        require("QUIZ" in block_types, f"{lid}: comprehensive lesson must contain QUIZ")

exercises = load(COURSE / "exercises/exercises.json") or []
require(len(exercises) == 28, f"expected 28 exercises, found {len(exercises)}")
exercise_ids, exercise_lessons = set(), set()
for item in exercises:
    if not isinstance(item, dict): errors.append("exercise must be object"); continue
    eid, lid = item.get("id"), item.get("lessonId"); require(bool(eid), "exercise missing id"); require(eid not in exercise_ids, f"duplicate exercise id {eid}"); exercise_ids.add(eid)
    require(lid in lesson_ids, f"exercise {eid}: invalid lessonId {lid}"); exercise_lessons.add(lid)
    require(bool(str(item.get("prompt", "")).strip()), f"exercise {eid}: empty prompt")
    require(isinstance(item.get("acceptance"), list) and len(item.get("acceptance", [])) >= 2, f"exercise {eid}: expected >=2 acceptance criteria")
require(exercise_lessons == lesson_ids, "exercise coverage must include every lesson")

quizzes = load(COURSE / "quizzes/quizzes.json") or []
require(len(quizzes) == 28, f"expected 28 quizzes, found {len(quizzes)}")
quiz_ids, quiz_lessons, question_ids = set(), set(), set()
for quiz in quizzes:
    if not isinstance(quiz, dict): errors.append("quiz must be object"); continue
    qid, lid = quiz.get("id"), quiz.get("lessonId"); require(bool(qid), "quiz missing id"); require(qid not in quiz_ids, f"duplicate quiz id {qid}"); quiz_ids.add(qid)
    require(lid in lesson_ids, f"quiz {qid}: invalid lessonId {lid}"); quiz_lessons.add(lid)
    require(int(quiz.get("passingScorePercent", 0)) >= 70, f"quiz {qid}: passing score must be >=70")
    questions = quiz.get("questions"); require(isinstance(questions, list) and len(questions) >= 3, f"quiz {qid}: expected at least 3 questions")
    if isinstance(questions, list):
        for question in questions:
            if not isinstance(question, dict): errors.append(f"quiz {qid}: question must be object"); continue
            question_id = question.get("id"); require(bool(question_id), f"quiz {qid}: question missing id"); require(question_id not in question_ids, f"duplicate question id {question_id}"); question_ids.add(question_id)
            answers = question.get("answers"); require(isinstance(answers, list) and len(answers) >= 2, f"{question_id}: expected at least 2 answers")
            if isinstance(answers, list): require(sum(1 for a in answers if isinstance(a, dict) and a.get("isCorrect") is True) >= 1, f"{question_id}: no correct answer")
            require(bool(str(question.get("question", "")).strip()), f"{question_id}: empty question")
            require(bool(str(question.get("explanation", "")).strip()), f"{question_id}: empty explanation")
require(quiz_lessons == lesson_ids, "quiz coverage must include every lesson")

projects = load(COURSE / "projects/projects.json") or []
require(len(projects) >= 12, f"expected at least 12 projects, found {len(projects)}")
project_ids = set()
for project in projects:
    if not isinstance(project, dict): errors.append("project must be object"); continue
    pid = project.get("id"); require(bool(pid), "project missing id"); require(pid not in project_ids, f"duplicate project id {pid}"); project_ids.add(pid)
    require(project.get("levelId") in level_ids, f"project {pid}: invalid levelId")
    steps = project.get("steps") or project.get("deliverables") or []
    require(isinstance(steps, list) and len(steps) >= 4, f"project {pid}: expected >=4 milestones/deliverables")

glossary = load(COURSE / "glossary/glossary.json") or []
require(len(glossary) >= 12, f"expected at least 12 glossary entries, found {len(glossary)}")
terms = set()
for entry in glossary:
    if not isinstance(entry, dict): errors.append("glossary entry must be object"); continue
    term = str(entry.get("term", "")).strip(); require(bool(term), "glossary term is empty"); key = term.casefold(); require(key not in terms, f"duplicate glossary term {term}"); terms.add(key)
    require(bool(str(entry.get("definition", "")).strip()), f"glossary {term}: empty definition")

if errors:
    print("COURSE VALIDATION FAILED")
    for error in errors: print(f"- {error}")
    sys.exit(1)
print(f"COURSE VALIDATION PASSED: branding + {len(lesson_ids)} deep lessons, {len(exercises)} exercises, {len(quizzes)} quizzes, {len(question_ids)} questions, {len(projects)} projects, {len(glossary)} glossary entries")
