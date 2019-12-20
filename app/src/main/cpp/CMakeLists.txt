
cmake_minimum_required(VERSION 3.4.1)
#添加头文件
include_directories(include SoundStretch SoundStretch)
#添加源码
AUX_SOURCE_DIRECTORY(. DIRSRCS)
AUX_SOURCE_DIRECTORY(SoundStretch SSH)
AUX_SOURCE_DIRECTORY(SoundTouch STH)
#增加其他目录的源文件到集合变量中
list(APPEND DIRSRCS ${SSH} ${STH})
add_library( # Sets the name of the library.
        soundtouch

        # Sets the library as a shared library.
        SHARED

        # Provides a relative path to your source file(s).
        ${DIRSRCS})

find_library( # Sets the name of the path variable.
        log-lib

        # Specifies the name of the NDK library that
        # you want CMake to locate.
        log)

target_link_libraries( # Specifies the target library.
        soundtouch

        # Links the target library to the log library
        # included in the NDK.
        ${log-lib})