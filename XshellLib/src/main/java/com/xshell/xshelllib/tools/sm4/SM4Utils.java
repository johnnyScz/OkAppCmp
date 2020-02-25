//package com.xshell.xshelllib.tools.sm4;
//
//import com.xshell.xshelllib.utils.Util;
//
//import java.io.IOException;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//
//
//
//
//public class SM4Utils {
//    public String secretKey = "";
//    public String iv = "";
//    public boolean hexString = false;
//
//    public SM4Utils() {
//    }
//
//    public String encryptData_ECB(String plainText) {
//        try {
//            SM4_Context ctx = new SM4_Context();
//            ctx.isPadding = true;
//            ctx.mode = SM4.SM4_ENCRYPT;
//
//            byte[] keyBytes;
//            if (hexString) {
//                keyBytes = Util.hexStringToBytes(secretKey);
//            } else {
//                keyBytes = secretKey.getBytes();
//            }
//
//            SM4 sm4 = new SM4();
//            sm4.sm4_setkey_enc(ctx, keyBytes);
//            byte[] encrypted = sm4.sm4_crypt_ecb(ctx, plainText.getBytes("UTF-8"));
//            Util.printHexString(encrypted);
////            String cipherText = new BASE64Encoder().encode(encrypted);
////
////            if (cipherText != null && cipherText.trim().length() > 0) {
////                Pattern p = Pattern.compile("\\s*|\t|\r|\n");
////                Matcher m = p.matcher(cipherText);
////                cipherText = m.replaceAll("");
////            }
//            return "cipherText";
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//
//    public String decryptData_ECB(String cipherText) {
//        try {
//            SM4_Context ctx = new SM4_Context();
//            ctx.isPadding = true;
//            ctx.mode = SM4.SM4_DECRYPT;
//
//            byte[] keyBytes;
//            if (hexString) {
//                keyBytes = Util.hexStringToBytes(secretKey);
//            } else {
//                keyBytes = secretKey.getBytes();
//            }
//
//            SM4 sm4 = new SM4();
//            sm4.sm4_setkey_dec(ctx, keyBytes);
//            byte[] decrypted = sm4.sm4_crypt_ecb(ctx, new BASE64Decoder().decodeBuffer(cipherText));
//            return new String(decrypted, "UTF-8");
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//
//    public String encryptData_CBC(String plainText) {
//        try {
//            SM4_Context ctx = new SM4_Context();
//            ctx.isPadding = true;
//            ctx.mode = SM4.SM4_ENCRYPT;
//
//            byte[] keyBytes;
//            byte[] ivBytes;
//            if (hexString) {
//                keyBytes = Util.hexStringToBytes(secretKey);
//                ivBytes = Util.hexStringToBytes(iv);
//            } else {
//                keyBytes = secretKey.getBytes();
//                ivBytes = iv.getBytes();
//            }
//
//            SM4 sm4 = new SM4();
//            sm4.sm4_setkey_enc(ctx, keyBytes);
//            byte[] encrypted = sm4.sm4_crypt_cbc(ctx, ivBytes, plainText.getBytes("UTF-8"));
//            String cipherText = new BASE64Encoder().encode(encrypted);
//            if (cipherText != null && cipherText.trim().length() > 0) {
//                Pattern p = Pattern.compile("\\s*|\t|\r|\n");
//                Matcher m = p.matcher(cipherText);
//                cipherText = m.replaceAll("");
//            }
//            return cipherText;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//
//    public String decryptData_CBC(String cipherText) {
//        try {
//            SM4_Context ctx = new SM4_Context();
//            ctx.isPadding = true;
//            ctx.mode = SM4.SM4_DECRYPT;
//
//            byte[] keyBytes;
//            byte[] ivBytes;
//            if (hexString) {
//                keyBytes = Util.hexStringToBytes(secretKey);
//                ivBytes = Util.hexStringToBytes(iv);
//            } else {
//                keyBytes = secretKey.getBytes();
//                ivBytes = iv.getBytes();
//            }
//
//            SM4 sm4 = new SM4();
//            sm4.sm4_setkey_dec(ctx, keyBytes);
//            byte[] decrypted = sm4.sm4_crypt_cbc(ctx, ivBytes, new BASE64Decoder().decodeBuffer(cipherText));
//            return new String(decrypted, "UTF-8");
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//
//    public static void main(String[] args) throws IOException {
//        String plainText = "曾芝云风格的ksgjr939####";
//        System.out.println(plainText);
//
//        String newStr = new String(plainText.getBytes(), "UTF-8");
//        System.out.println(newStr);
//
//        SM4Utils sm4 = new SM4Utils();
//        sm4.secretKey = "1111111111111111";
//        sm4.hexString = false;
//
//        System.out.println("ECB model");
//        String cipherText = sm4.encryptData_ECB(newStr);
//        System.out.println("miwen: " + cipherText);
//        System.out.println("");
//
//
//        plainText = sm4.decryptData_ECB(cipherText);
//        System.out.println("ming wen: " + plainText);
//        System.out.println("");
//
////        System.out.println("CBC模式");
////        sm4.iv = "UISwD9fW6cFh9SNS";
////        cipherText = sm4.encryptData_CBC(plainText);
////        System.out.println("密文: " + cipherText);
////        System.out.println("");
////
////        plainText = sm4.decryptData_CBC(cipherText);
////        System.out.println("明文: " + plainText);
//    }
//
//
//}
//
//
//
//
