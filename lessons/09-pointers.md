# درس 09 - Pointerها

Pointer آدرس object یا function را نگهداری می کند.

```c
int value = 42;
int *ptr = &value;
printf("%d\n", *ptr);
```

`&` آدرس می گیرد و `*` در expression مقدار object اشاره شده را dereference می کند.

## Pointer معتبر
Pointer باید به object زنده و مناسب اشاره کند یا مقدار null شناخته شده داشته باشد. Dereference pointer نامعتبر می تواند Undefined Behavior باشد.

## Pointer arithmetic
محاسبات pointer بر اساس elementهای type مقصد انجام می شود و فقط در محدوده های مجاز object/array معنا دارد.

## تمرین
تابع swap برای دو `int` با pointer بنویسید و توضیح دهید چرا ارسال by value به تنهایی کافی نیست.
