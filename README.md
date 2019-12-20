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
