# AS Academy C - فایل build برای کامپایل نمونه های قابل حمل دوره.
cmake_minimum_required(VERSION 3.20)
project(as_academy_c VERSION 1.0.0 LANGUAGES C)

# C23 استاندارد هدف دوره است؛ extensionهای اختصاصی کامپایلر غیرفعال می شوند.
set(CMAKE_C_STANDARD 23)
set(CMAKE_C_STANDARD_REQUIRED ON)
set(CMAKE_C_EXTENSIONS OFF)

# هر مثال مستقل یک executable آموزشی است.
add_executable(c_hello examples/fundamentals/hello.c)
add_executable(c_student_records examples/beginner/student_records.c)
add_executable(c_dynamic_array examples/advanced/dynamic_array.c)
add_executable(c_bit_flags examples/professional/bit_flags.c)

# هشدارهای جدی برای GCC/Clang فعال می شوند تا مثال ها کیفیت آموزشی داشته باشند.
if(CMAKE_C_COMPILER_ID MATCHES "GNU|Clang")
    foreach(target c_hello c_student_records c_dynamic_array c_bit_flags)
        target_compile_options(${target} PRIVATE -Wall -Wextra -Wpedantic -Wconversion)
    endforeach()
endif()
