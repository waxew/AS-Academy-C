# درس 11 - File I/O

فایل ها از طریق `FILE *` و توابع `stdio.h` مدیریت می شوند.

```c
FILE *file = fopen("data.txt", "r");
if (file == NULL) {
    /* خطای باز شدن فایل */
}
```

هر resource بازشده باید در مسیرهای مناسب بسته شود. نتیجه عملیات مهم را بررسی کنید.

## Text و Binary
Mode و portability داده binary اهمیت دارد؛ نوشتن مستقیم struct به فایل الزاماً format قابل حمل ایجاد نمی کند.

## تمرین
ابزار شمارش line/word/character بسازید و خطای نبودن فایل را مدیریت کنید.
