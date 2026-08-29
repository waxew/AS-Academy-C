/* AS Academy - نمونه آموزشی struct و آرایه در C. */
#include <stdio.h>

/* یک نوع داده برای نگهداری مشخصات دانشجو تعریف می کنیم. */
typedef struct {
    char name[40]; /* فضای ثابت برای نام؛ در پروژه واقعی bounds باید کنترل شود. */
    int score;     /* امتیاز دانشجو. */
} Student;

int main(void)
{
    /* آرایه نمونه برای تمرکز درس روی struct است. */
    Student students[] = {{"Sara", 92}, {"Ali", 87}, {"Nima", 95}};
    const size_t count = sizeof students / sizeof students[0];

    /* تمام رکوردها را پیمایش و چاپ می کنیم. */
    for (size_t i = 0; i < count; ++i) {
        printf("%s: %d\n", students[i].name, students[i].score);
    }

    return 0;
}
