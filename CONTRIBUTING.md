# Contribution Guide

## قوانین محتوای آموزشی

1. هر مثال باید هدف آموزشی مشخص داشته باشد.
2. سورس C باید کامنت توضیحی کافی داشته باشد تا هنرجو نقش بخش های مهم را بفهمد.
3. مثال ها باید تا حد امکان با C23 و بدون compiler extension نوشته شوند.
4. warning جدید نباید بدون دلیل وارد پروژه شود.
5. ورودی، bounds، تخصیص حافظه و return valueهای مهم باید کنترل شوند.
6. منطق مشترک اپ AS Academy نباید در این Repository تکرار شود؛ آن تغییر متعلق به `AS-Academy-Core` است.
7. Lesson جدید باید در catalog/lesson metadata ثبت شود.
8. Exercise و Quiz باید پاسخ یا explanation قابل بررسی داشته باشند.

## بررسی محلی

```text
cmake -S . -B build
cmake --build build
```

یا:

```text
make
make run
```

## Commit

پیام commit باید کوتاه و مشخص باشد، مانند:

```text
feat: add pointer exercises
docs: expand memory model lesson
fix: handle allocation failure in vector example
```
