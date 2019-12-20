
cmake_minimum_required(VERSION 3.4.1)

#添加源码
AUX_SOURCE_DIRECTORY(. LIB_DIRSRCS_SOH)

add_library( # Sets the name of the library.
        souchd

        # Sets the library as a shared library.
        SHARED

        # Provides a relative path to your source file(s).
        ${LIB_DIRSRCS_SOH})