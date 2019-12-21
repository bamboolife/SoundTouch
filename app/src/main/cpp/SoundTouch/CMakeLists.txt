
cmake_minimum_required(VERSION 3.4.1)

#添加源码
AUX_SOURCE_DIRECTORY(. LIB_DIRSRCS_SOH)

add_library(
        sound
        SHARED
        ${LIB_DIRSRCS_SOH})