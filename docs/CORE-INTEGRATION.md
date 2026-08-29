# اتصال دوره C به AS Academy Core

## اصل معماری

`AS-Academy-C` یک Course Repository است، نه محل پیاده سازی دوباره قابلیت های عمومی اپلیکیشن.

## قابلیت هایی که از AS-Academy-Core می آیند

- Navigation و ساختار صفحات
- Design System و Theme
- Progress Tracking
- Quiz Engine
- Exercise Engine
- Search
- Bookmark
- Settings
- Drawer / Profile
- Content Engine / Updater
- Course schema / contract
- Database و persistence مشترک

## چیزهایی که در این Repository باقی می مانند

- Manifest دوره C
- Catalog و Lesson metadata
- متن و محتوای آموزشی C
- مثال های C
- تمرین ها و پاسخ های آموزشی
- Quiz data
- پروژه های C
- Glossary تخصصی C
- قابلیت ها و محدودیت های خاص C
- مستندات کامپایلر و Toolchain

## قرارداد فعلی

فایل `manifest.json` بر اساس template موجود در `AS-Academy-Core/course/template/manifest.json` ساخته شده و `contentSchemaVersion` آن 1 است.

هر تغییر آینده در schema ابتدا باید در Core تعریف شود و سپس این Repository با نسخه جدید قرارداد هماهنگ شود.
