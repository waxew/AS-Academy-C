# درس 10 - Struct، Union، Enum و typedef

`struct` چند field را در یک object ترکیب می کند. `union` اعضایی دارد که storage مشترک دارند. `enum` برای مجموعه constantهای نام دار مناسب است و `typedef` نام type را خواناتر می کند.

```c
typedef struct {
    int id;
    double balance;
} Account;
```

## Layout
Padding و alignment می توانند اندازه struct را تغییر دهند؛ layout را بدون قرارداد ABI فرض نکنید.

## تمرین
مدل `Student` شامل id، name و score بسازید و تابع چاپ آن را جداگانه تعریف کنید.
