# درس 13 - Dynamic Memory

خانواده `malloc`, `calloc`, `realloc`, `free` برای مدیریت storage پویا استفاده می شوند.

## قانون مالکیت
برای هر allocation مشخص کنید چه کسی owner است و چه زمانی باید آن را آزاد کند.

```c
int *items = malloc(count * sizeof *items);
if (items == NULL) {
    /* allocation failure */
}
```

## خطرها
Memory leak، use-after-free، double-free، overflow در محاسبه size و dereference null.

## `realloc`
نتیجه را مستقیم روی تنها pointer موجود overwrite نکنید اگر failure باعث از دست رفتن آدرس قبلی می شود؛ از temporary pointer استفاده کنید.

## تمرین
Dynamic Vector با capacity و size پیاده سازی کنید.
