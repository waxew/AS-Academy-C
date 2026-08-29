# درس 16 - Bitwise Programming

عملگرهای `&`, `|`, `^`, `~`, `<<`, `>>` برای کار با bitها استفاده می شوند.

## Mask
```c
flags |= FEATURE_A;      /* set */
flags &= ~FEATURE_A;     /* clear */
if (flags & FEATURE_A) { /* test */ }
```

برای عملیات bit-level معمولاً unsigned typeها رفتار قابل فهم تری ارائه می کنند.

## تمرین
سیستم permission با READ/WRITE/EXECUTE طراحی کنید.
