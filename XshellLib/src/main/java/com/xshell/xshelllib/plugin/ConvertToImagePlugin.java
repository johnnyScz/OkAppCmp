//package com.xshell.xshelllib.plugin;
//
//import android.util.Base64;
//
//import com.xshell.xshelllib.utils.FileUtil;
//
//import org.apache.cordova.CallbackContext;
//import org.apache.cordova.CordovaArgs;
//import org.apache.cordova.CordovaPlugin;
//import org.json.JSONException;
//
//import java.io.File;
//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
//import java.io.IOException;
//
//import Decoder.BASE64Decoder;
//
///**
// * base64保存
// */
//public class ConvertToImagePlugin extends CordovaPlugin {
//
//    @Override
//    public boolean execute(String action, CordovaArgs args, CallbackContext callbackContext) throws JSONException {
//        String result = args.getString(0);
//        String filePath = args.getString(1);
//        String fileName = args.getString(2);
//        if (result == null) {
//            callbackContext.error("result is null!");
//            return false;
//        }
//        try {
//            //convertBase64DataToImage(result, Environment.getExternalStorageDirectory().getAbsolutePath()+"/b.pdf");
//            String url = filePath + "/" + fileName;
//            saveStringToPdf(result, url);
//            callbackContext.success();
//            return true;
//        } catch (Exception e) {
//            // TODO: handle exception
//            callbackContext.error("base64 error");
//            return false;
//        }
//    }
//
//    /**
//     * 把base64图片数据转为本地图片
//     *
//     * @param base64ImgData
//     * @param filePath
//     * @throws IOException
//     */
//    public static void convertBase64DataToImage(String base64ImgData, String filePath) throws IOException {
//        BASE64Decoder d = new BASE64Decoder();
//        byte[] bs = d.decodeBuffer(base64ImgData);
//        FileOutputStream os = new FileOutputStream(filePath);
//        os.write(bs);
//        os.close();
//    }
//
//
//    private void saveStringToPdf(final String txt, final String fileName) {
//
//        try {
////            Date date = new Date();
////            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
////            String path = "ebsi_open_paper/" + simpleDateFormat.format(date)+"/"+fileName;
//            File filePath = FileUtil.getInstance().createFileInSDCard(fileName);
//            //final File dwldsPath = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + fileName);
//            byte[] pdfAsBytes = Base64.decode(txt, 0);
//            FileOutputStream os;
//            os = new FileOutputStream(filePath, false);
//            os.write(pdfAsBytes);
//            os.flush();
//            os.close();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//}
