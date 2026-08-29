# نصب و اجرای C

## Windows

پیشنهاد دوره استفاده از GCC از طریق MSYS2/MinGW-w64 یا Clang است. پس از نصب، در Terminal بررسی کنید:

```text
gcc --version
clang --version
```

## Linux

کامپایلر C و ابزار build توزیع خود را نصب کنید و سپس نسخه را بررسی کنید.

## macOS

Command Line Tools ابزارهای لازم برای شروع با Clang را فراهم می کند.

## تست نصب

```text
gcc -std=c23 -Wall -Wextra -Wpedantic examples/fundamentals/hello.c -o hello
./hello
```

در محیطی که GCC حالت `c23` را با نام قدیمی `c2x` ارائه می کند می توان موقتاً از همان نام استفاده کرد، اما محتوای دوره بر استاندارد C23 متمرکز است.

## VS Code

VS Code صرفاً Editor است؛ برای ساخت برنامه همچنان به compiler/toolchain نیاز دارید. افزونه C/C++ می تواند تکمیل کد و تجربه debugging را بهتر کند.

## Debug Build

```text
gcc -std=c23 -Wall -Wextra -Wpedantic -g main.c -o app
```

## Sanitizer Build در toolchainهای سازگار

```text
gcc -std=c23 -Wall -Wextra -g -fsanitize=address,undefined main.c -o app
```
