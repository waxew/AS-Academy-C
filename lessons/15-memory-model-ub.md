# درس 15 - Memory Model و Undefined Behavior

## Lifetime و Storage Duration
Objectها lifetime و storage duration مشخص دارند. Pointer به object پایان یافته dangling است.

## Undefined Behavior
نمونه ها می توانند شامل out-of-bounds access، signed integer overflow در شرایط مربوط، use-after-free و برخی violationهای aliasing باشند.

Compiler می تواند فرض کند UB در برنامه معتبر رخ نمی دهد؛ بنابراین نتیجه UB صرفاً یک crash قابل پیش بینی نیست.

## Alignment و Representation
Typeها alignment requirement دارند و byte representation جزئی از برنامه نویسی سطح پایین است.

## تمرین
چند snippet دارای UB پیدا کنید، علت را بنویسید و نسخه امن ارائه دهید.
