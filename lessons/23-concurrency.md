# درس 23 - Concurrency و Atomics

برنامه همزمان باید data race، synchronization و lifetime resourceها را مدیریت کند.

## مفاهیم
Thread، mutex، atomic operation، race condition، deadlock و memory ordering.

C11 APIها و atomicها بخشی از مسیر مطالعه هستند، ولی availability کتابخانه thread استاندارد را روی platform هدف بررسی کنید.

## تمرین
Counter مشترک را ابتدا ناامن و سپس با synchronization مناسب پیاده سازی و تفاوت را تحلیل کنید.
