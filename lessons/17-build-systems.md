# درس 17 - Make و CMake

Build System dependencyها، compiler flags، targetها و build configuration را مدیریت می کند.

این Repository هر دو مسیر را دارد:

```bash
make
```

و:

```bash
cmake -S . -B build
cmake --build build
```

## هدف آموزشی
Make mechanics را شفاف نشان می دهد؛ CMake برای پروژه های چندسکویی و generatorهای مختلف مناسب است.

## تمرین
یک static library بسازید و executable جداگانه را به آن link کنید.
