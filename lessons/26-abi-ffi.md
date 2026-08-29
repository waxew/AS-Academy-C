# درس 26 - ABI، Linkage و FFI

ABI قرارداد سطح binary است: calling convention، alignment/layout، symbol naming و جزئیات platform.

C معمولاً زبان مرزی مناسبی برای FFI است زیرا APIهای C ABI گسترده هستند.

## طراحی API
Public struct layout را بی دلیل expose نکنید. Opaque pointer می تواند سازگاری نسخه ای را بهتر کند.

## تمرین
یک کتابخانه C کوچک با opaque handle طراحی کنید و API create/use/destroy بسازید.
