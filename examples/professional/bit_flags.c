/* AS Academy - نمونه Bit Mask مناسب مباحث System/Embedded C. */
#include <stdbool.h>
#include <stdio.h>

/* هر permission فقط یک bit را اشغال می کند. */
enum Permission {
    PERM_READ  = 1u << 0,
    PERM_WRITE = 1u << 1,
    PERM_EXEC  = 1u << 2
};

/* بررسی می کنیم تمام bitهای requested در flags فعال باشند. */
static bool has_permission(unsigned flags, unsigned requested)
{
    return (flags & requested) == requested;
}

int main(void)
{
    /* READ و WRITE را با OR ترکیب می کنیم. */
    const unsigned user_permissions = PERM_READ | PERM_WRITE;

    printf("read: %s\n", has_permission(user_permissions, PERM_READ) ? "yes" : "no");
    printf("exec: %s\n", has_permission(user_permissions, PERM_EXEC) ? "yes" : "no");
    return 0;
}
