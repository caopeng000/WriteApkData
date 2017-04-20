package com.writeapkdata;

import android.os.Environment;

import java.io.File;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/******************************************
 * 类名称：FileUtil
 * 类描述：
 *
 * @version: 2.3.1
 * @author: caopeng
 * @time: 2017/4/20 15:50
 ******************************************/
public class FileUtil {
    /**
     * short转换成字节数组（小端序）
     *
     * @param
     * @return
     */
    public static short stream2Short(byte[] stream, int offset) {
        ByteBuffer buffer = ByteBuffer.allocate(2);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.put(stream[offset]);
        buffer.put(stream[offset + 1]);
        return buffer.getShort(0);
    }

    /**
     * 字节数组转换成short（小端序）
     *
     * @param
     * @param
     * @return
     */
    public static byte[] short2Stream(short data) {
        ByteBuffer buffer = ByteBuffer.allocate(2);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.putShort(data);
        buffer.flip();
        return buffer.array();
    }

    /**
     * 判断SDCard是否存在 [当没有外挂SD卡时，内置ROM也被识别为存在sd卡]
     *
     * @return
     */
    public static boolean isSdCardExist() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
    }

    /**
     * 获取sd卡根路径
     *
     * @return
     */
    public static String getSdCardPath() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File sdFile = Environment.getExternalStorageDirectory();
            if (sdFile != null) {
                String dir = sdFile.getPath();
                if (dir.endsWith("/")) {
                    return dir;
                } else {
                    return dir + "/";
                }
            }
            return null;
        }
        return null;
    }


}
