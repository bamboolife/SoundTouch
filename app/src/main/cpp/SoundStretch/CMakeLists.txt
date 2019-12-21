
cmake_minimum_required(VERSION 3.4.1)
#添加源码
AUX_SOURCE_DIRECTORY(. LIB_DIRSRCS_WAV)

add_library(
        wavfile
        SHARED
        ${LIB_DIRSRCS})
