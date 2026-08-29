# درس 03 - مسیر Source تا Executable

## هدف
شناخت دقیق Preprocess، Compile، Assemble و Link.

## 1. Preprocessing
Directiveهایی مثل `#include` و `#define` پردازش می شوند و Translation Unit شکل می گیرد.

## 2. Compilation
کد C تحلیل می شود و معمولاً به Assembly یا نمایش میانی compiler تبدیل می شود.

## 3. Assembly
Assembly به Object File تبدیل می شود.

## 4. Linking
Object Fileها و Libraryها به خروجی نهایی متصل می شوند.

## مثال مرحله ای GCC
```bash
gcc -E main.c -o main.i
gcc -S main.i -o main.s
gcc -c main.s -o main.o
gcc main.o -o app
```

## خطاها را تفکیک کنید
Syntax Error با Link Error یکسان نیست. اگر تابع declare شده ولی definition آن هنگام link پیدا نشود، مشکل linker است نه parser.

## تمرین
یک تابع را در فایل جدا تعریف کنید، سپس عمداً فایل دوم را از link حذف کنید و پیام linker را تحلیل کنید.
