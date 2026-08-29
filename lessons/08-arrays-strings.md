# درس 08 - Array و String

Array مجموعه ای پیوسته از elementهای هم نوع است. C به صورت خودکار bounds checking انجام نمی دهد.

String متعارف C آرایه ای از `char` است که با `\0` پایان می یابد.

```c
char name[] = "AS Academy";
```

## `string.h`
توابعی مانند `strlen`, `strcmp`, `memcpy` و دیگر ابزارها وجود دارند، اما اندازه buffer و قرارداد هر تابع باید دقیق فهمیده شود.

## Array decay
در بسیاری از expressionها نام array به pointer به اولین element تبدیل می شود، ولی array و pointer یک چیز نیستند.

## تمرین
بدون استفاده از `strlen` تابعی برای محاسبه طول string بنویسید و ورودی `NULL` را در طراحی API خود مشخص کنید.
