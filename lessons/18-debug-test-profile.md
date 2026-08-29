# درس 18 - Debugging، Sanitizer، Test و Profiling

## Debugger
با breakpoint، step، stack frame و variable inspection مشکل را از روی evidence پیدا کنید.

## Sanitizer
در toolchain سازگار:
```bash
-fsanitize=address,undefined -g
```

## Test
Functionهای pure و APIهای کوچک تست پذیری بهتری دارند. assert برای invariantهای برنامه مفید است ولی جای error handling ورودی خارجی را نمی گیرد.

## Profiling
قبل از optimization اندازه گیری کنید. Hot path را پیدا کنید و سپس تغییر را benchmark کنید.

## تمرین
عمداً heap buffer overflow ایجاد کنید، با ASan گزارش را ببینید و سپس bug را رفع کنید.
