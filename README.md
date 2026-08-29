# AS Academy - C Programming

دوره فارسی جامع زبان C از مبانی تا برنامه نویسی تخصصی سیستم، شبکه و Embedded.

**Course version:** 1.0.0  
**Target standard:** C23  
**Core contract:** AS-Academy-Core / content schema v1

## مسیر دوره

- مبانی: Toolchain، Syntax، Type، I/O، Operator، Control Flow
- مقدماتی: Function، Array، String، Pointer، Struct، File، Preprocessor
- پیشرفته: Dynamic Memory، Function Pointer، Memory Model، Bitwise، Make/CMake، Debug/Test/Profile
- تخصصی: C23، Data Structures، Algorithms، POSIX، Concurrency، Networking، Embedded C، ABI/FFI، Secure C و Capstone

## وضعیت نسخه 1.0

- 4 سطح آموزشی
- 28 ماژول اصلی
- 28 Lesson entry
- Quiz bank اولیه
- 12 تمرین درجه بندی شده
- 12 پروژه مرحله ای
- Glossary تخصصی
- مثال های C کامنت گذاری شده
- CMake و Make build
- CI برای GCC و Clang
- مستند نصب و Toolchain
- مستند اتصال به AS Academy Core

## ساختار

```text
AS-Academy-C/
├── .github/workflows/ci.yml
├── content/
│   ├── catalog.json
│   ├── lessons.json
│   ├── quizzes.json
│   ├── exercises.json
│   ├── projects.json
│   └── glossary.json
├── docs/
│   ├── COURSE-ROADMAP.md
│   ├── CORE-INTEGRATION.md
│   └── INSTALLATION.md
├── examples/
│   ├── fundamentals/
│   ├── beginner/
│   ├── advanced/
│   └── professional/
├── CMakeLists.txt
├── Makefile
├── manifest.json
├── course.json
├── CONTRIBUTING.md
└── VERSION
```

## Build

با CMake:

```bash
cmake -S . -B build
cmake --build build
```

یا با Make:

```bash
make
make run
```

برای یک فایل مستقل:

```bash
gcc -std=c23 -Wall -Wextra -Wpedantic main.c -o app
```

## ارتباط با AS-Academy-Core

این Repository فقط **Course/Content/Capability اختصاصی C** را نگهداری می کند. Navigation، Design System، Database، Progress، Quiz Engine، Exercise Engine، Search، Bookmark، Settings، Drawer/Profile، Content Engine/Updater و قرارداد Course Package متعلق به `AS-Academy-Core` هستند.

بنابراین هر قابلیت عمومی جدید ابتدا در Core پیاده سازی می شود و دوره C فقط داده و تنظیمات لازم برای استفاده از آن را ارائه می کند.

## قرارداد Course Package

`manifest.json` مطابق template فعلی `AS-Academy-Core/course/template/manifest.json` است و قابلیت های code runner، terminal examples، diagrams، quizzes، exercises، projects و glossary را اعلام می کند.

## کیفیت سورس آموزشی

مثال های دوره باید توضیحات کامنتی کافی، error handling متناسب با سطح درس و warningهای کامپایلر را رعایت کنند. CI مثال های اصلی را با GCC و Clang می سازد.

## License / Content

تا زمان تعیین License نهایی توسط مالک پروژه، هیچ License عمومی به Repository اضافه نشده است.
