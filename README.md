# SoundTouch是什么
 SoundTouch是一个用C++编写的开源的音频处理库，可以改变音频文件或实时音频流的节拍(Tempo)、音调(Pitch)、回放率(Playback Rates)，还支持估算音轨的稳定节拍率(BPM rate)。ST的3个效果互相独立，也可以一起使用。这些效果通过采样率转换、时间拉伸结合实现。
- Tempo节拍 ：通过拉伸时间，改变声音的播放速率而不影响音调。
- Playback Rate回放率 : 以不同的转率播放唱片（DJ打碟？），通过采样率转换实现。
- Pitch音调 ：在保持节拍不变的前提下改变声音的音调，结合采样率转换+时间拉伸实现。如：增高音调的处理过程是：将原音频拉伸时长，再通过采样率转换，同时减少时长与增高音调变为原时长。
## 处理对象
ST处理的对象是PCM（Pulse Code Modulation，脉冲编码调制），.wav文件中主要是这种格式，因此ST的示例都是处理wav音频。mp3等格式经过了压缩，需转换为PCM后再用ST处理。
## 主要特性
- 易于实现：ST为所有支持gcc编译器或者visual Studio的处理器或操作系统进行了编译，支持Windows、Mac OS、Linux、Android、Apple iOS等。
- 完全开源：ST库与示例工程完全开源可下载
- 容易使用：编程接口使用单一的C++类
- 支持16位整型或32位浮点型的单声道、立体声、多通道的音频格式
- 可实现实时音频流处理：
     - 输入/输出延迟约为100ms
     - 实时处理44.1kHz/16bit的立体声，需要133Mhz英特尔奔腾处理器或更好

## SoundTouch公开函数与参数的说明
### 采样：
- setChannels(int) 设置声道，1 = mono单声道, 2 = stereo立体声
- setSampleRate(uint) 设置采样率
### 速率：

- setRate(double) 指定播放速率，原始值为1.0，大快小慢
- setTempo(double) 指定节拍，原始值为1.0，大快小慢
- setRateChange(double)、setTempoChange(double) 在原速1.0基础上，按百分比做增量，取值(-50 .. +100 %)
### 音调：

- setPitch(double) 指定音调值，原始值为1.0
- setPitchOctaves(double) 在原音调基础上以八度音为单位进行调整，取值为[-1.00,+1.00]
- setPitchSemiTones(int) 在原音调基础上以半音为单位进行调整，取值为[-12,+12]

以上调音函数根据乐理进行单位换算，最后进入相同的处理流程calcEffectiveRateAndTempo()。三个函数对参数没有上下界限限制，只是参数过大失真越大。SemiTone指半音，通常说的“降1个key”就是降低1个半音。所以我认为使用SemiTone为单位即可满足需求，并且容易理解。

### 处理：

- putSamples(const SAMPLETYPE *samples, uint nSamples) 输入采样数据
- receiveSamples(SAMPLETYPE *output, uint maxSamples) 输出处理后的数据，需要循环执行
- flush() 冲出处理管道中的最后一组“残留”的数据，应在最后执行

### SoundTouch实时处理音频流

ST对音频的处理是输入函数putSamples()与输出函数receiveSamples()。实时处理音频流的思路就是，循环读取音频数据段，放入ST进行输出，输出处理后的数据段用于播放。

## 相关链接
官网提供了ST的可执行程序、C++源码、说明文档、不同操作系统的示例工程，几个重要链接：
- [SoundTouch官网](https://www.surina.net/soundtouch/)
- [ST处理效果预览](https://www.surina.net/soundtouch/soundstretch.html#examples)（SoundStretch是官网用ST库实现的处理WAV音频的工具）
- [源码编译方法、算法以及参数说明](https://www.surina.net/soundtouch/README.html)
- [常见问题（如实时处理）](http://www.surina.net/soundtouch/faq.html)
## Android中使用SoundTouch
1. 上官网下载源码
  目录结构
  ![image](https://github.com/bamboolife/SoundTouch/blob/master/imgs/soundtouch_dir1.png)
  ![image](https://github.com/bamboolife/SoundTouch/blob/master/imgs/soundtouch_dir2.png)
2. 最关键的就是 include和source两个文件夹，包含需要编译的头文件和源码。需要把include、source中SoundStretch和SoundTouch三个文件夹导入Android c++工程中。
3. 创建Android c++工程（目录结构如下）
 ![image](https://github.com/bamboolife/SoundTouch/blob/master/imgs/project_dir.png)
4. 编写CMake

cpp目录下的CMakeLists.txt
```
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
```
SoundStretch目录下的CMakeLists.txt
```
cmake_minimum_required(VERSION 3.4.1)
#添加源码
AUX_SOURCE_DIRECTORY(. LIB_DIRSRCS_WAV)

add_library( 
        wavfile
        SHARED
        ${LIB_DIRSRCS})
```
SoundTouch目录下的CMakeLists.txt
```
cmake_minimum_required(VERSION 3.4.1)

#添加源码
AUX_SOURCE_DIRECTORY(. LIB_DIRSRCS_SOH)

add_library(
        sound
        SHARED
        ${LIB_DIRSRCS_SOH})
```


  
