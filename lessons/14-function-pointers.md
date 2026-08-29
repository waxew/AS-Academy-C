# درس 14 - Function Pointer و Callback

Function pointer امکان ارسال behavior به function دیگر را می دهد.

```c
typedef int (*CompareFn)(int, int);
```

Callback در sorting، event handling و APIهای generic کاربرد دارد.

## تمرین
تابعی بسازید که روی آرایه پیمایش کند و callback دلخواه را برای هر element اجرا کند.
