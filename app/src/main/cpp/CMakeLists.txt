
cmake_minimum_required(VERSION 3.4.1)
#添加头文件
include_directories(include SoundStretch SoundStretch)
#添加源码
AUX_SOURCE_DIRECTORY(. DIRSRCS)
AUX_SOURCE_DIRECTORY(SoundStretch SSH)
AUX_SOURCE_DIRECTORY(SoundTouch STH)
#增加其他目录的源文件到集合变量中
list(APPEND DIRSRCS ${SSH} ${STH})
add_library(
        soundtouch
        SHARED
        ${DIRSRCS})

find_library(
        log-lib
        log)

target_link_libraries(
        soundtouch
        ${log-lib})