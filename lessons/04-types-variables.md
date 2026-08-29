# درس 04 - متغیرها، Typeها و ثابت ها

## هدف
درک type system پایه C و اهمیت اندازه و محدوده مقادیر.

## Typeهای اصلی
`char`, `short`, `int`, `long`, `long long`, `float`, `double`, `long double` و گونه های signed/unsigned.

## `sizeof`
برای پرسیدن اندازه object/type از `sizeof` استفاده کنید و اندازه ها را hard-code نکنید.

```c
#include <stdio.h>
int main(void) {
    printf("int: %zu\n", sizeof(int));
    return 0;
}
```

## ثابت ها
از `const` برای بیان intent استفاده کنید. Macro و enum نیز نقش های متفاوتی در تعریف constantها دارند.

## Conversion
تبدیل implicit همیشه بی خطر نیست. تبدیل signed/unsigned و narrowing باید آگاهانه انجام شود.

## تمرین
برنامه ای بنویسید که اندازه typeهای اصلی سیستم شما را چاپ کند و نتیجه را با سیستم دیگری مقایسه کنید.
