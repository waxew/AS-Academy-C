# درس 02 - نصب Toolchain و محیط توسعه

## هدف
آماده کردن محیط واقعی ساخت و اجرای برنامه C.

## ابزارهای اصلی
- Compiler: GCC یا Clang
- Build tool: Make و سپس CMake
- Debugger: GDB یا LLDB
- Editor/IDE: VS Code یا محیط دلخواه

## تست Compiler
```bash
gcc --version
clang --version
```

## اولین Build
```bash
gcc -std=c23 -Wall -Wextra -Wpedantic hello.c -o hello
```

## نکته مهم
VS Code کامپایلر نیست. Editor و extension فقط تجربه کاربری را بهتر می کنند؛ ساخت واقعی توسط Toolchain انجام می شود.

## خطاهای رایج
- اضافه نبودن compiler به PATH
- اجرای command در پوشه اشتباه
- تفاوت نام executable در Windows و Unix-like systems
- استفاده از standard flag نامعتبر در compiler قدیمی

## تمرین
فایل `examples/fundamentals/hello.c` را با GCC و در صورت دسترسی با Clang کامپایل کنید و warningها را بررسی کنید.
