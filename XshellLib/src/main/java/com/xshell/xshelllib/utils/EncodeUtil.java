package com.xshell.xshelllib.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by zzy on 2016/8/30.
 * 加密工具
 */
public class EncodeUtil {

    private static String encode(String str, String method) {
        MessageDigest md = null;
        String dstr = null;
        try {
            md = MessageDigest.getInstance(method);
            md.update(str.getBytes());
            dstr = new BigInteger(1, md.digest()).toString(16);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return dstr;
    }

    /**
     * MD5加密
     * @param password 明文
     * @return 密文
     */
    public static String MD5Encode(String password) {
        return encode(password, "MD5");
    }


    /**
     * 用SHA算法进行加密
     * @param str 需要加密的字符串
     * @return SHA加密后的结果
     */
    public static String SHAEncode(String str) {
        return encode(str, "SHA");
    }


    //这里面读写操作比较多，还有截取那两个属性的字符串稍微有点麻烦
    /**
     * int转byte
     * by黄海杰 at：2015-10-29 16:15:06
     * @param iSource
     * @param iArrayLen
     * @return
     */
    public static byte[] toByteArray(int iSource, int iArrayLen) {
        byte[] bLocalArr = new byte[iArrayLen];
        for (int i = 0; (i < 4) && (i < iArrayLen); i++) {
            bLocalArr[i] = (byte) (iSource >> 8 * i & 0xFF);
        }
        return bLocalArr;
    }

    /**
     * byte转int
     * by黄海杰 at：2015-10-29 16:14:37
     * @param bRefArr
     * @return
     */
    // 将byte数组bRefArr转为一个整数,字节数组的低位是整型的低字节位
    public static int toInt(byte[] bRefArr) {
        int iOutcome = 0;
        byte bLoop;

        for (int i = 0; i < bRefArr.length; i++) {
            bLoop = bRefArr[i];
            iOutcome += (bLoop & 0xFF) << (8 * i);
        }
        return iOutcome;
    }

    /**
     * 写入JS相关文件
     * by黄海杰 at:2015-10-29 16:14:01
     * @param output
     * @param str
     */
    public static void writeBlock(OutputStream output, String str) {
        try {
            byte[] buffer = str.getBytes("utf-8");
            int len = buffer.length;
            byte[] len_buffer = toByteArray(len, 4);
            output.write(len_buffer);
            output.write(buffer);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 读取JS相关文件
     * by黄海杰 at:2015-10-29 16:14:19
     * @param input
     * @return
     */
    public static String readBlock(InputStream input) {
        try {
            byte[] len_buffer = new byte[4];
            input.read(len_buffer);
            int len = toInt(len_buffer);
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            int read_len = 0;
            byte[] buffer = new byte[1024];
                while ((read_len = input.read(buffer)) > 0) {
                    len -= read_len;
                    output.write(buffer, 0, read_len);
                    if (len <= 0) {
                        break;
                    }
                }

            buffer = output.toByteArray();
            output.close();
            return new String(buffer,"utf-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    //为了加密我们的html我们把url转成md5
    /**
     * 字符串转MD5
     * by黄海杰 at:2015-10-29 16:15:32
     * @param string
     * @return
     */
    public static String md5(String string) {

        byte[] hash;

        try {

            hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));

        } catch (NoSuchAlgorithmException e) {

            throw new RuntimeException("Huh, MD5 should be supported?", e);

        } catch (UnsupportedEncodingException e) {

            throw new RuntimeException("Huh, UTF-8 should be supported?", e);

        }


        StringBuilder hex = new StringBuilder(hash.length * 2);

        for (byte b : hash) {

            if ((b & 0xFF) < 0x10) hex.append("0");

            hex.append(Integer.toHexString(b & 0xFF));

        }

        return hex.toString();

    }
}
