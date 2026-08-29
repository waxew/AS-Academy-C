/* AS Academy - نمونه مدیریت حافظه پویا با کنترل خطا. */
#include <stdio.h>
#include <stdlib.h>

int main(void)
{
    const size_t count = 5;

    /* برای count عدد صحیح حافظه رزرو می کنیم. */
    int *values = malloc(count * sizeof *values);

    /* malloc ممکن است شکست بخورد؛ قبل از dereference بررسی الزامی است. */
    if (values == NULL) {
        fprintf(stderr, "memory allocation failed\n");
        return EXIT_FAILURE;
    }

    /* حافظه تخصیص یافته را مقداردهی می کنیم. */
    for (size_t i = 0; i < count; ++i) {
        values[i] = (int)(i * i);
    }

    /* نتیجه را نمایش می دهیم. */
    for (size_t i = 0; i < count; ++i) {
        printf("%d%s", values[i], i + 1 == count ? "\n" : " ");
    }

    /* مالک این حافظه هستیم، بنابراین مسئول آزادسازی آن نیز هستیم. */
    free(values);
    values = NULL; /* از استفاده تصادفی pointer آزادشده جلوگیری می کند. */

    return EXIT_SUCCESS;
}
