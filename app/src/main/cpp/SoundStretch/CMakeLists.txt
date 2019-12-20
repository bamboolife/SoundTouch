
cmake_minimum_required(VERSION 3.4.1)

#添加源码
AUX_SOURCE_DIRECTORY(. LIB_DIRSRCS_WAV)

add_library( # Sets the name of the library.
        wavfile

        # Sets the library as a shared library.
        SHARED

        # Provides a relative path to your source file(s).
        ${LIB_DIRSRCS})
