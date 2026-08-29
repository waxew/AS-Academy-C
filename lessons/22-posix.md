# درس 22 - POSIX و System Programming

این بخش خارج از C استاندارد وارد APIهای سیستم های POSIX می شود.

## مباحث
Process، file descriptor، pipe، signal، environment و filesystem API.

## نکته portability
POSIX بخشی از ISO C نیست؛ کد وابسته به آن باید از لایه portable دوره جدا نگه داشته شود.

## تمرین
برنامه ای بنویسید که child process ایجاد کند و خروجی آن را از pipe دریافت کند.
