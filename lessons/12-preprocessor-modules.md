# درس 12 - Preprocessor و طراحی چندفایلی

Header قرارداد public ماژول را بیان می کند و source implementation را نگه می دارد.

```c
#ifndef AS_VECTOR_H
#define AS_VECTOR_H
/* declarations */
#endif
```

## Macro
Macro type-safe نیست و argument آن ممکن است بیش از یک بار evaluate شود. در بسیاری موارد function یا `static inline` انتخاب بهتری است.

## تمرین
ماشین حساب را به `calculator.h`, `calculator.c`, `main.c` تقسیم کنید.
