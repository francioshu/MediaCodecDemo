package com.teligen.yuvosd;

/**  YUV叠加时间水印native方法
 *
 * @author Created by jiangdongguo on 2017-4-13上午10:42:55
 */
public class YuvUtils {
	

    public native static long YuvUtilsInit(String hzk16Path,String userName,int len);
    
    /** 叠加时间水印
     * @param in 源yuv数据
     * @param width 图像帧的宽
     * @param height 图像帧的高
     * @param out 目的yuv数据
     * @param date 当前时间
     * @param colorFormat 编码器颜色格式
     * @return
     */
    public native static void AddYuvOsd(byte[] in,int width,int height ,byte[] out,String date,int colorFormat);
    
    public native static void setCorrectColorFormat(byte[] in,int width,int height ,byte[] out,int colorFormat);
    
    /** 将YUV图像水平旋转180度
     * @param src   YUV源数据
     * @param width 图像帧的宽
     * @param height 图像帧的高
     * @param isFrontCamera 摄像头类型标志
     * @return
     */
    public native static void YUV420spRotate180ForBack(byte[] src,byte[] dest,int width, int height,int isFrontCamera);
    
    public  static native void Yuv420spRotate270ForFront(byte[] src,byte[] dest,int width, int height);
	
	public static native void Yuv420spRotate90ForBack(byte[] src,byte[] dest,int width, int height);
    
    static{
    	System.loadLibrary("YuvOsd");
    }
}
