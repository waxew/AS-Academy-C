# درس 27 - Secure C

امنیت C عمدتاً به مدیریت دقیق memory، integer، input و resource وابسته است.

## Checklist
- اندازه buffer را بدانید.
- return valueها را بررسی کنید.
- allocation size overflow را در نظر بگیرید.
- ورودی خارجی را validate کنید.
- ownership را مستند کنید.
- secret یا credential را در repository قرار ندهید.
- warning و sanitizer را در توسعه فعال کنید.

## تمرین
یک parser ورودی بسازید و test caseهای malformed، empty، oversized و boundary را اضافه کنید.
