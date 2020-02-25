package com.xshell.xshelllib.utils;

import android.os.Environment;

import com.xshell.xshelllib.application.AppConfig;
import com.xshell.xshelllib.application.AppContext;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class FileUtil {
    private static final Object lockObj = new Object();
    private static FileUtil instance;
    private static String SDCardRoot;
    private static int WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 1;
    private String SDStateString;

    public static FileUtil getInstance() {

        if (instance == null) {
            synchronized (lockObj) {
                if (instance == null) {
                    instance = new FileUtil();
                }
            }
        }
        return instance;
    }

    private FileUtil() {
        // 得到当前外部存储设备的目录 /SDCARD
        SDCardRoot = Environment.getExternalStorageDirectory() + "/";
        // 获取扩展SD卡设备状态
        SDStateString = Environment.getExternalStorageState();
    }


    public String getSDCardRoot() {
        return SDCardRoot;
    }

    /**
     * 获取手机本地储存的目录，如果手机有SD卡返回SD卡的存储目录，如果没有则返回data/data目录
     *
     * @return
     */
    public String getStorageDirectory() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED) ? Environment
                .getExternalStorageDirectory().getPath() : AppContext.CONTEXT
                .getFilesDir().getAbsolutePath();
    }

    /**
     * 在SD卡上创建目录
     */
    public File createDirInSDCard(String dirName) {
        File dirFile = null;
        if (dirName.contains("/")) {
            String[] strs = dirName.split("/");
            for (int i = 0; i < strs.length; i++) {
                if (i == 0) {
                    dirFile = new File(SDCardRoot + strs[i]);
                } else {
                    dirFile = createDirInDir(dirFile, strs[i]);
                }
                if (!dirFile.exists()) {
                    dirFile.mkdirs();
                }
            }
        } else {
            dirFile = new File(getStorageDirectory() + "/" + dirName);
            if (!dirFile.exists()) {
                dirFile.mkdirs();
            }
        }
        return dirFile;
    }

    /**
     * 在SD卡上创建目录
     */
    public File createDirInSDCard(String dirName, String... strs) {
        File dirFile = null;
        for (int i = 0; i < strs.length; i++) {
            if (i == 0) {
                dirFile = new File(SDCardRoot + strs[i]);
            } else {
                dirFile = createDirInDir(dirFile, strs[i]);
            }
            if (!dirFile.exists()) {
                dirFile.mkdirs();
            }
        }
        return dirFile;
    }

    /**
     * 在某个目录下创建目录
     */
    public File createDirInDir(File dir, String dirName) {
        File dirFile = null;
        if (dirName.contains("/")) {
            String[] strs = dirName.split("/");
            for (int i = 0; i < strs.length; i++) {
                if (i == 0) {
                    dirFile = new File(dir.getAbsolutePath() + File.separator
                            + strs[i]);
                } else {
                    if (!dirFile.exists())
                        dirFile = createDirInDir(dirFile, strs[i]);
                }
                if (!dirFile.exists()) {
                    dirFile.mkdirs();
                }
            }
        } else {
            dirFile = new File(dir.getAbsolutePath() + File.separator + dirName);
            if (!dirFile.exists()) {
                dirFile.mkdirs();
            }
        }
        return dirFile;
    }

    /**
     * 在SD卡上创建文件
     */
    public File createFileInSDCard(String fileName) throws IOException {
        File file = null;
        if (fileName.contains("/")) {
            String[] strs = fileName.split("/");
            for (int i = 0; i < strs.length; i++) {
                if (i == 0) {
                    file = createDirInSDCard(strs[i]);
                } else if (i == strs.length - 1) {
                    file = new File(file.getAbsolutePath() + File.separator
                            + strs[i]);
                    file.createNewFile();
                } else {
                    file = createDirInDir(file, strs[i]);
                }
            }
        } else {
            file = new File(getStorageDirectory() + "/" + fileName);
            file.createNewFile();
        }
        return file;
    }

    /**
     * 在某个目录上创建文件
     */
    public File createFileInDir(File dir, String fileName) throws IOException {
        File file = dir;
        if (fileName.contains("/")) {
            String[] strs = fileName.split("/");
            for (int i = 0; i < strs.length; i++) {
                if (i == strs.length - 1) {
                    file = new File(file.getAbsolutePath() + File.separator
                            + strs[i]);
                    file.createNewFile();
                } else {
                    file = createDirInDir(file, strs[i]);
                }
            }
        } else {
            file = new File(dir.getAbsolutePath() + File.separator + fileName);
            file.createNewFile();
        }
        return file;
    }

    /**
     * 判断SD卡上某个文件（目录）是否存在
     */
    public boolean isFileExist(String fileName) {
        File file = new File(SDCardRoot + fileName);
        return file.exists();
    }

    /**
     * 判断SD卡上的某个目录某个文件（目录）是否存在
     */
    public boolean isFileExist(String dir, String fileName) {
        File file = new File(SDCardRoot + dir + File.separator + fileName);
        return file.exists();
    }

    /**
     * 判断某个目录上的某个文件（目录）是否存在
     */
    public boolean isFileExist(File dir, String fileName) {
        File file = new File(dir.getAbsolutePath() + File.separator + fileName);
        return file.exists();
    }

    /***
     * 获取SDCard的相对路径
     */
    public String getPathSDCard() {
        return SDCardRoot;
    }

    /***
     * 获取SDCard的相对路径
     */
    public String getAbsPathSDCard() {
        return "file://" + SDCardRoot;
    }

    /***
     * 获取文件的相对路径
     */
    public String getFilePathInSDCard(String fileName) {
        return SDCardRoot + fileName;
    }

    /***
     * 获取文件的绝对路径
     */
    public String getAbsFilePathInSDCard(String fileName) {
        return "file://" + SDCardRoot + fileName;
    }

    /***
     * 获取文件的相对路径
     */
    public String getFilePathInSDCard(String dir, String fileName) {
        return SDCardRoot + dir + File.separator + fileName;
    }

    /***
     * 获取文件的绝对路径
     */
    public String getAbsFilePathInSDCard(String dir, String fileName) {
        return "file://" + SDCardRoot + dir + File.separator + fileName;
    }

    /***
     * 获取某个目录下文件的相对路径
     */
    public String getFilePathInDir(File dir, String fileName) {
        return dir.getAbsolutePath() + File.separator + fileName;
    }

    /***
     * 获取某个目录下文件的绝对路径
     */
    public String getAbsFilePathInDir(File dir, String fileName) {
        return "file://" + dir.getAbsolutePath() + File.separator + fileName;
    }

    /***
     * 获取SD卡的剩余容量,单位是Byte
     *
     * @return
     */
    @SuppressWarnings("deprecation")
    public long getSDAvailableSize() {
        if (SDStateString.equals(Environment.MEDIA_MOUNTED)) {
            // 取得sdcard文件路径
            File pathFile = Environment
                    .getExternalStorageDirectory();
            android.os.StatFs statfs = new android.os.StatFs(pathFile.getPath());
            // 获取SDCard上每个block的SIZE
            long nBlocSize = statfs.getBlockSize();
            // 获取可供程序使用的Block的数量
            long nAvailaBlock = statfs.getAvailableBlocks();
            // 计算 SDCard 剩余大小Byte
            long nSDFreeSize = nAvailaBlock * nBlocSize;
            return nSDFreeSize;
        }
        return 0;
    }

    public File getFileInSDCard(String fileName) {
        return new File(SDCardRoot + fileName);
    }

    ;

    /***
     * 从sd卡中读取文件，并且以字节流返回
     */
    public byte[] readFromSD(String fileName) {
        File file = new File(SDCardRoot + fileName);
        if (!file.exists()) {
            return null;
        }
        InputStream inputStream = null;
        try {
            inputStream = new BufferedInputStream(new FileInputStream(file));
            byte[] data = new byte[inputStream.available()];
            inputStream.read(data);
            return data;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 从SD卡用读取字符
     *
     * @param fileName
     * @return
     */
    public String readSringFromSD(String fileName) {
        File file = new File(SDCardRoot + fileName);
        if (!file.exists()) {
            return null;
        }
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            StringBuffer sb = new StringBuffer();
            String s;
            while ((s = reader.readLine()) != null) {
                sb.append(s);
            }
            return sb.toString();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return null;
    }


    /***
     * 从sd卡中读取文件，并且以字节流返回
     */
    public byte[] readFromSD(String dir, String fileName) {
        File file = new File(SDCardRoot + dir + File.separator + fileName);
        if (!file.exists()) {
            return null;
        }
        InputStream inputStream = null;
        try {
            inputStream = new BufferedInputStream(new FileInputStream(file));
            byte[] data = new byte[inputStream.available()];
            inputStream.read(data);
            return data;
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return null;
    }

    /***
     * 从sd卡中读取文件，并且以字节流返回
     */
    public byte[] readFromDir(File dir, String fileName) {
        File file = new File(dir.getAbsolutePath() + File.separator + fileName);
        if (!file.exists()) {
            return null;
        }
        InputStream inputStream = null;
        try {
            inputStream = new BufferedInputStream(new FileInputStream(file));
            byte[] data = new byte[inputStream.available()];
            inputStream.read(data);
            return data;
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 将一个字节数组数据写入到SD卡中
     */
    public boolean write2SDFromByte(String fileName, byte[] bytes) {
        if (bytes == null) {
            return false;
        }
        OutputStream output = null;
        try {
            // 拥有可读可写权限，并且有足够的容量
            if (SDStateString.equals(Environment.MEDIA_MOUNTED)
                    && bytes.length < getSDAvailableSize()) {
                File file = createFileInSDCard(fileName);
                output = new BufferedOutputStream(new FileOutputStream(file));
                output.write(bytes);
                output.flush();
                return true;
            }
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } finally {
            try {
                if (output != null) {
                    output.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 将一个字节数组数据写入到SD卡中
     */
    public boolean write2SDFromByte(String dir, String fileName, byte[] bytes) {
        if (bytes == null) {
            return false;
        }
        OutputStream output = null;
        try {
            // 拥有可读可写权限，并且有足够的容量
            if (SDStateString.equals(Environment.MEDIA_MOUNTED)
                    && bytes.length < getSDAvailableSize()) {
                File myDir = createDirInSDCard(dir);
                File file = createFileInDir(myDir, fileName);
                output = new BufferedOutputStream(new FileOutputStream(file));
                output.write(bytes);
                output.flush();
                return true;
            }
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } finally {
            try {
                if (output != null) {
                    output.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 将一个字节数组数据写入到SD卡中
     */
    public boolean write2DirFromByte(File dir, String fileName, byte[] bytes) {
        if (bytes == null) {
            return false;
        }
        OutputStream output = null;
        try {
            // 拥有可读可写权限，并且有足够的容量
            if (SDStateString.equals(Environment.MEDIA_MOUNTED)
                    && bytes.length < getSDAvailableSize()) {
                File file = createFileInDir(dir, fileName);
                output = new BufferedOutputStream(new FileOutputStream(file));
                output.write(bytes);
                output.flush();
                return true;
            }
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } finally {
            try {
                if (output != null) {
                    output.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 将一个InputStream里面的数据写入到SDcard中
     */
    public File write2SDFromInput(String fileName, InputStream input) {
        File file = null;
        OutputStream output = null;
        try {
            int size = input.available();
            // 拥有可读可写权限，并且有足够的容量
            if (SDStateString.equals(Environment.MEDIA_MOUNTED)
                    && size < getSDAvailableSize()) {
                file = createFileInSDCard(fileName);
                output = new BufferedOutputStream(new FileOutputStream(file));
                byte buffer[] = new byte[4 * 1024];
                int temp;
                while ((temp = input.read(buffer)) != -1) {
                    output.write(buffer, 0, temp);
                }
                output.flush();
            }
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } finally {
            try {
                if (output != null) {
                    output.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (input != null) {
                    input.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return file;
    }


    public File createFileInSDCard1(String fileName) throws IOException {
        File filedrs = null;
        File file = null;
        if (fileName.contains("/")) {
            String[] strs = fileName.split("/");

            filedrs = new File(SDCardRoot + strs[0]);
            if (!filedrs.exists()) {
                filedrs.mkdir();
            }
            file = new File(filedrs, strs[1]);
            if (!file.exists()) {
                file.createNewFile();
            }
        }
        return file;
    }

    /**
     * 将一个字符串写入文本指定文本
     *
     * @param isAppend 取值1和0,0代表写入，1代表追加
     */
    public File write2SDFromString(String fileName, String content, int isAppend) {
        File file = null;
        BufferedWriter writer = null;
        try {
            if (SDStateString.equals(Environment.MEDIA_MOUNTED)) {
                //file = createFileInSDCard1(fileName);
                file = new File(SDCardRoot + fileName);

                writer = new BufferedWriter(new OutputStreamWriter(
                        new FileOutputStream(file, true)));
                if (isAppend == 0) {
                    writer.write(content);
                } else if (isAppend == 1) {

                    writer.append(content);
                }

                writer.flush();

            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return file;

    }

    /**
     * 将一个InputStream里面的数据写入到SD卡中
     */
    public File write2SDFromInput(String dir, String fileName, InputStream input) {
        File file = null;
        OutputStream output = null;
        try {
            int size = input.available();
            // 拥有可读可写权限，并且有足够的容量
            if (SDStateString.equals(Environment.MEDIA_MOUNTED)
                    && size < getSDAvailableSize()) {
                File fileDir = createDirInSDCard(dir);
                file = createFileInDir(fileDir, fileName);
                output = new BufferedOutputStream(new FileOutputStream(file));
                byte buffer[] = new byte[4 * 1024];
                int temp;
                while ((temp = input.read(buffer)) != -1) {
                    output.write(buffer, 0, temp);
                    if (AppConfig.DEBUG)
                        AppContext.log("DownloadService", "大小：" + temp);
                }
                output.flush();
            }
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } finally {
            try {
                if (output != null) {
                    output.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (input != null) {
                    input.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    /**
     * 将一个InputStream里面的数据写入到SD卡中
     */
    public File write2DirFromInput(File dir, String fileName, InputStream input) {
        File file = null;
        OutputStream output = null;
        try {
            int size = input.available();
            // 拥有可读可写权限，并且有足够的容量
            if (SDStateString.equals(Environment.MEDIA_MOUNTED)
                    && size < getSDAvailableSize()) {
                file = createFileInDir(dir, fileName);
                output = new BufferedOutputStream(new FileOutputStream(file));
                byte buffer[] = new byte[4 * 1024];
                int temp;
                while ((temp = input.read(buffer)) != -1) {
                    output.write(buffer, 0, temp);
                }
                output.flush();
            }
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } finally {
            try {
                if (output != null) {
                    output.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (input != null) {
                    input.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    /**
     * 删除某个文件目录
     */
    public boolean delete(File file) throws IOException {
        if (file.isFile()) {
            return file.delete();
        }
        if (file.isDirectory()) {
            File[] childFiles = file.listFiles();
            if (childFiles == null || childFiles.length == 0) {
                return file.delete();
            }
            for (int i = 0; i < childFiles.length; i++) {
                delete(childFiles[i]);
            }
            file.delete();
        }
        return false;
    }

    /**
     * 删除某个目录下的文件
     */
    public boolean delete(File dir, String name) throws IOException,
            FileNotFoundException {
        if (dir == null || !dir.isDirectory())
            throw new FileNotFoundException();
        File file = new File(dir.getAbsolutePath(), name);
        if (file.isFile()) {
            return file.delete();
        }
        if (file.isDirectory()) {
            File[] childFiles = file.listFiles();
            if (childFiles == null || childFiles.length == 0) {
                return file.delete();
            }

            for (int i = 0; i < childFiles.length; i++) {
                delete(childFiles[i]);
            }
            file.delete();
        }
        return false;
    }

    /**
     * 复制单个文件
     *
     * @param oldPath String 原文件路径 如：c:/fqf.txt
     * @param newPath String 复制后路径 如：f:/fqf.txt
     * @return boolean
     */
    public void copyFile(String oldPath, String newPath) {
        try {
            int bytesum = 0;
            int byteread = 0;
            File oldfile = new File(oldPath);
            if (oldfile.exists()) { // 文件存在时
                InputStream inStream = new FileInputStream(oldPath); // 读入原文件
                FileOutputStream fs = new FileOutputStream(newPath);
                byte[] buffer = new byte[1444];
                @SuppressWarnings("unused")
                int length;
                while ((byteread = inStream.read(buffer)) != -1) {
                    bytesum += byteread; // 字节数 文件大小
                    System.out.println(bytesum);
                    fs.write(buffer, 0, byteread);
                }
                inStream.close();
                fs.flush();
                fs.close();
            }
        } catch (Exception e) {
            System.out.println("复制单个文件操作出错");
            e.printStackTrace();
        }
    }

    /**
     * 复制整个文件夹内容
     *
     * @param oldPath String 原文件路径 如：c:/fqf
     * @param newPath String 复制后路径 如：f:/fqf/ff
     * @return boolean
     */
    public void copyFolder(String oldPath, String newPath) {

        try {
            (new File(newPath)).mkdirs(); // 如果文件夹不存在 则建立新文件夹
            File a = new File(oldPath);
            String[] file = a.list();
            File temp = null;
            for (int i = 0; i < file.length; i++) {
                if (oldPath.endsWith(File.separator)) {
                    temp = new File(oldPath + file[i]);
                } else {
                    temp = new File(oldPath + File.separator + file[i]);
                }

                if (temp.isFile()) {
                    FileInputStream input = new FileInputStream(temp);
                    FileOutputStream output = new FileOutputStream(newPath
                            + "/" + (temp.getName()).toString());
                    byte[] b = new byte[1024 * 5];
                    int len;
                    while ((len = input.read(b)) != -1) {
                        output.write(b, 0, len);
                    }
                    output.flush();
                    output.close();
                    input.close();
                }
                if (temp.isDirectory()) {// 如果是子文件夹
                    copyFolder(oldPath + "/" + file[i], newPath + "/" + file[i]);
                }
            }
        } catch (Exception e) {
            System.out.println("复制整个文件夹内容操作出错");
            e.printStackTrace();
        }
    }

    /**
     * 删除某个目录下的所有文件（不包括自己，除非自己是一个文件）
     *
     * @param file
     */
    public void deleteFile(File file) {
        if (file.isFile()) {
            file.delete();
            return;
        }

        if (file.isDirectory()) {
            File[] childFiles = file.listFiles();
            if (childFiles == null || childFiles.length == 0) {
                file.delete();
                return;
            }

            for (int i = 0; i < childFiles.length; i++) {
                deleteFile(childFiles[i]);
            }
            file.delete();
        }
    }

    public ArrayList<File> deepDir(File file) throws IOException {
        ArrayList<File> lists = new ArrayList<File>();
        if (file.isDirectory()) {
            File[] childFiles = file.listFiles();
            for (int i = 0; i < childFiles.length; i++) {
                if (childFiles[i].isFile()) {
                    lists.add(childFiles[i]);
                } else {// 是一个目录
                    lists.addAll(deepDir(childFiles[i]));
                }
            }
        }
        return lists;
    }

    /**
     * 设置为只读文件
     *
     * @param filePath
     */
    public void setReadOnlyFiles(String filePath) {
        File root = new File(filePath);
        File[] filedirs = root.listFiles();
        for (File file : filedirs) {
            if (file.isDirectory()) {
                setReadOnlyFiles(file.getAbsolutePath());
            } else {
                LinuxCommandUtil.runCommand("chmod 400 "
                        + file.getAbsolutePath());
            }
        }
    }

    /**
     * 四舍五入保留两位小数
     *
     * @param data 数值
     * @return 四舍五入返回的数值
     */
    public static String stringFormat(double data) {
        return stringFormat(data, 2);
    }

    /**
     * 四舍五入
     *
     * @param data   数值
     * @param number 几位小数
     * @return 四舍五入返回的数值
     */
    public static String stringFormat(double data, int number) {
        return String.format("%." + number + "f", data);
    }

    public static double getDirSize(File file) {
        //判断文件是否存在
        if (file.exists()) {
            //如果是目录则递归计算其内容的总大小
            if (file.isDirectory()) {
                File[] children = file.listFiles();
                double size = 0;
                for (File f : children)
                    size += getDirSize(f);
                return size;
            } else {//如果是文件则直接返回其大小,以“兆”为单位
                double size = (double) file.length() / 1024 / 1024;
                return size;
            }
        } else {
            System.out.println("文件或者文件夹不存在，请检查路径是否正确！");
            return 0.0;
        }
    }
}
