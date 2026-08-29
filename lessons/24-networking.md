# درس 24 - Network و Socket Programming

Socket programming معمولاً به API سیستم عامل متکی است و بخشی از ISO C خالص نیست.

## TCP
Connection-oriented و stream-based است. `send` یا `recv` الزاماً کل buffer درخواستی را یکجا پردازش نمی کند؛ loop و protocol framing لازم است.

## UDP
Datagram-oriented است و trade-off متفاوتی دارد.

## تمرین
Echo client/server بسازید و disconnect، partial I/O و error pathها را مدیریت کنید.
