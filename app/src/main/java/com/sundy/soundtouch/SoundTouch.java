////////////////////////////////////////////////////////////////////////////////
///
/// Example class that invokes native SoundTouch routines through the JNI
/// interface.
///
/// Author        : Copyright (c) Olli Parviainen
/// Author e-mail : oparviai 'at' iki.fi
/// WWW           : http://www.surina.net
///
////////////////////////////////////////////////////////////////////////////////

package com.sundy.soundtouch;

import androidx.annotation.FloatRange;

public final class SoundTouch
{
    /**
     * 获取SoundTouch版本
     * @return
     */
    public native final static String getVersionString();
     //速率

    /**
     * 指定节拍，原始值为1.0，大快小慢
     * @param handle
     * @param tempo
     */
    private native final void setTempo(long handle, float tempo);

    /**
     * 指定播放速率，原始值为1.0，大快小慢
     * @param handle
     * @param speed
     */
    private native final void setSpeed(long handle, float speed);

   // 音调：

    /**
     *在原音调基础上以半音为单位进行调整，取值为[-12.0,+12.0]
     * @param handle
     * @param pitch
     */
    private native final void setPitchSemiTones(long handle,@FloatRange(from = -12.0,to = 12.0) float pitch);

    /**
     * 指定音调值，原始值为1.0
     * @param handle
     * @param pitch
     */
    private native final void setPitch(long handle,float pitch);

    /**
     * 在原音调基础上以八度音为单位进行调整，取值为[-1.00,+1.00]
     * @param handle
     * @param pitch
     */
    private native final void setPitchOctaves(long handle,@FloatRange(from = -1.0,to = 1.0) float pitch);

    /**
     * 指定wav源文件和转化的输出文件
     * @param handle
     * @param inputFile
     * @param outputFile
     * @return
     */
    private native final int processFile(long handle, String inputFile, String outputFile);

    /**
     * 错误信息打印
     * @return
     */
    public native final static String getErrorString();

    /**
     * 实例化SoundTouch对象
     * @return
     */
    private native final static long newInstance();

    /**
     * 销毁SoundTouch对象
     * @param handle
     */
    private native final void deleteInstance(long handle);
    
    long handle = 0;
    
    
    public SoundTouch()
    {
    	handle = newInstance();    	
    }
    
    
    public void close()
    {
    	deleteInstance(handle);
    	handle = 0;
    }


    public void setTempo(float tempo)
    {
    	setTempo(handle, tempo);
    }


    public void setPitchSemiTones(float pitch)
    {
    	setPitchSemiTones(handle, pitch);
    }

    
    public void setSpeed(float speed)
    {
    	setSpeed(handle, speed);
    }


    public int processFile(String inputFile, String outputFile)
    {
    	return processFile(handle, inputFile, outputFile);
    }

    
    // Load the native library upon startup
    static
    {
        System.loadLibrary("soundtouch");
    }
}
