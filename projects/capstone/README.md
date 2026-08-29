# AS C Capstone - File-backed Key-Value Store

## هدف
ساخت یک storage engine کوچک برای تمرین architecture، binary/text persistence، memory ownership، error handling و testing.

## فرمان های پیشنهادی
`set key value`, `get key`, `delete key`, `list`, `compact`, `exit`.

## ساختار پیشنهادی
- `src/main.c`: CLI
- `src/store.c`: منطق storage
- `include/store.h`: API عمومی
- `tests/`: تست ها

## Milestone
1. In-memory store
2. File persistence
3. Reload
4. Delete/tombstone
5. Compaction
6. Tests and sanitizer
7. Documentation and release
