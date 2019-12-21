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
## 和声音相关的基础知识
### 声音相关概念
声音是由物体震动产生的，我们可以把从感知的角度分为三种属性：
- 响度(Loudness)：音量，与声波的振幅有关
- 音调(Pitch)：音调与声音的频率有关——声音频率越大时，音调就越高，否则就越低
- 音色(Quality)：由物体结构特性所决定，所以使用不同的材质来制作，所表现出来的音色效果是不一样的。

响度和音调只要联想到正弦波非常容易理解，然而音色是什么？
```
音色 = 基频 + 泛音（多个）
```

一个物体发生的同时，会发出很多不同频率的波（谐波）。这许多不同频率的波由于相位差很小（也就是相隔时间很短），人是无法单独分辨的，所以这些波会混合起来一起给人一个整体的感受，而这个感受就叫做音色。

想想就很容易理解了，人的喉咙是立体的，发声时喉咙内每一部分都会产生振动，不同部位产生的振动频率就存在差异。其中频率的相对量最大的决定了声音的音调，其它的频率即泛音。当然人说话时还有鼻子和嘴来协助，另外即便是乐器或其它任何发声物体也往往是整体产生共鸣的结果。

看到一个这样的比喻：如果一个声音中从1到20K赫兹频率的波都有，并且都是1:1的关系，即相对强度都相同。这样一个声音就称为白噪音，听起来就和收音机收不信号时的音色一样。如果我有2万只音箱，每一个音箱分别对应放从1到20k赫兹不同频率的声波。那么我通过开关不同的音箱，调节每个音箱的音量，从理论上讲我就可以得到任何我想要的音色。不论是韩红的声音还是孙楠的声音，小提琴的声音。

### A/D转换（Analog-to-Digital Converter）
样本sample：声波
<br>→ 采样sampling
<br>→ 量化quantization：将连续值离散化
<br>→ 编码coding：可由软件或硬件芯片完成
<br>→ （压缩compress）：mp3等格式
<br>→ 二进制1010…10
整个流程如下图
![image](https://github.com/bamboolife/SoundTouch/blob/master/imgs/sampler.png)
### 声音采集
将模拟信号数字化，分为取样和量化两部分，即通常的 PCM(Pulse-code modulation) 脉冲编码调制技术。

- 采样速率(Sampling Rate)

人耳所能辨识的声音范围是 20-20KHZ，根据奈奎斯特抽样定理(要从抽样信号中无失真地恢复原信号，抽样频率应大于 2 倍信号最高频率)，所以人们一般都选用 44.1KHZ(CD)、48KHZ 或者96KHZ 来做为采样速率。

- 采样深度(Bit Depth)

量化(Quantization) 是将连续值近似为某个范围内有限多个离散值的处理过程，这个范围的宽度离散值的数量表达，会直接影响到音频采样的准确性。一般 8位（256），和 16位（65536）来表示。

- PCM 文件大小
```
存储量 = (采样频率 · 采样位数 · 声道 · 时间)／8 (单位：字节数)
```

 - 采样频率：在16位声卡中有22KHz、44KHz等几级，其中，22KHz相当于普通FM广播的音质，44KHz已相当于CD音质了，目前的常用采样频率都不超过48KHz。
 - 采样位数：在计算机中采样位数一般有8位和16位之分，8位不是说把纵坐标分成8份，而是分成2的8次方即256份； 同理16位是把纵坐标分成2的16次方65536份。
 - 声道数：单声道的声音只能使用一个喇叭发声，立体声的pcm可以使两个喇叭都发声，更能感受到空间效果。
- 声道和立体声

   - mono,Monaural (单声道)
   - stereo,Stereophonic(立体声)
   - 4.1 Surround Sound(4.1环绕立体声)4.1环绕立体声：左前+右前+左后+右后+低音炮
   - 5.1 Surround Sound(5.1环绕立体声)5.1环绕立体声，如杜比数字技术：左前+中置+右前+左后环绕+右后环绕+低音炮
- 音频的几种文件格式
   
    - 不压缩的格式(UnCompressed Audio Format)：PCM数据，wav, aiff
    - 无损压缩格式(Lossless Compressed Audio Format)：FLAC, APE, WV, m4a
    - 有损压缩格式(Lossy Compressed Audio Format)：mp3, aac
> 常见的 wav 格式的音频数据其实是 pcm 文件 + 46字节的头信息，头信息记录了 PCM 文件的采样率、采样深度、声道数等信息，可方便播放进行解码。

### 变声原理
变声即是对 PCM 数据进行的处理，如果是其它格式（如：MP3）也需要先解压成 PCM 格式再进行处理。

常用的变声，如女生、男生、小黄人都是对音调（即频率）进行的处理。当音调高时就是女声，低时即男声，常常听到的女声比男声高八度还是有点道理的。

另外还有一些对声音的高级处理，如：混响（Reverb）、回声（Echo）、EQ、锯齿（Flange）等。下面重点说一下混响：

> Reverb（或残响）是Reverberation的简写，当一个声音发出后，当它碰到障碍物后会反射，碰到下一个障碍物会再反射，不停反射直至它的能量消失为止。这个持续在空间中反覆反射动作形成的声音集成，就是残响。不是每个频率衰减的速度都一样。同样的声音在同个空间不同位置，到达人耳所经过的反射次数、时间都是不同的，混音时使用 reverb 器材或插件可重新塑造声音的立体空间感，让声音有远近等不同距离的层次。

> 混音常用的Reverb效果器大概分为两大类。一类是靠电脑程式运算出来的演算式残响（Algorithmic Reverb）;另一类是取样式残响(Convolution Reverb)。演算式残响就是利用程式运算，模拟空间的各种反应参数，是人工制造出来的残响。取样式残响是在真实空间中做声音脉冲反应的取样（impulse response），加到欲使用的声音上。

这里区分下 Reverb 和 Echo 的区别：

> 通常Echo是指声音发出后，要较长时间才会收到反射音的状态，就像我们对着远方的山大喊；「喂～」我们不会马上听到反射回来的声音，通常是喊完后隔了一小段时间才会听到明显反射回来的「喂～喂～～喂～～～」，这种称之为Echo，Echo算是reverb的一种，但 reverb 是个更大的概念。
当回声与原始声音直接的间隔较大时，如 >200ms，我们耳朵能分辨出两个声音的就是 Echo。如果两个声音直接的间隔比较小，通常我们无法分辨出来，与原始声音产生了共鸣的叫 Reverb。

  
