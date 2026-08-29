# درس 07 - Function، Scope و Recursion

Function باید مسئولیت مشخص و قرارداد روشن داشته باشد.

```c
static int max_int(int a, int b) {
    return a > b ? a : b;
}
```

`static` در این مثال linkage تابع را به Translation Unit محدود می کند.

## پارامترها
C پارامترها را by value منتقل می کند. برای تغییر object بیرونی معمولاً آدرس آن ارسال می شود.

## Recursion
هر recursion باید base case داشته باشد و هزینه stack آن در نظر گرفته شود.

## تمرین
نسخه iterative و recursive فاکتوریل را بنویسید و محدودیت overflow را توضیح دهید.
