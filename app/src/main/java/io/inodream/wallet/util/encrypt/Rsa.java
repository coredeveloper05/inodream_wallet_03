package io.inodream.wallet.util.encrypt;

import android.content.Context;
import android.content.res.AssetManager;

import com.blankj.utilcode.util.EncodeUtils;
import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.PathUtils;
import com.blankj.utilcode.util.ResourceUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

/**
 * RSA加解密算法<br>
 * 1.客户端采用公钥进行数据加密，服务端采用私钥进行解密<br>
 * 2.服务端采用私钥进行数据签名，客户端采用公钥进行签名验证<br>
 *
 * @author houjinyun
 */
public class Rsa {

    /**
     * 加密算法
     */
    private static final String ALGORITHM = "RSA";

    /**
     * 签名算法
     */
    private static final String SIGN_ALGORITHMS = "SHA1WithRSA";

    /**
     * 签名算法
     */
    private static final String SIGN_ALGORITHMS_256 = "SHA256WithRSA";

    /**
     * 编码
     */
    private static final String CHARSET = "UTF-8";

    /**
     * @param algorithm 算法
     * @param bysKey
     * @return
     * @throws InvalidKeySpecException
     * @throws NoSuchAlgorithmException
     */
    private static PublicKey getPublicKeyFromX509(String algorithm, String bysKey) throws InvalidKeySpecException, NoSuchAlgorithmException {
        byte[] decodedKey = Base64.decode(bysKey, Base64.NO_WRAP);
        X509EncodedKeySpec x509 = new X509EncodedKeySpec(decodedKey);
        KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
        return keyFactory.generatePublic(x509);
    }

    /**
     * 采用公钥进行加密
     *
     * @param content   需要加密的字符串
     * @param publicKey 公钥，base64编码
     * @return base64编码后的加密字符串
     */
    public static String encryptByPublicKey(String content, String publicKey) {
        try {
            PublicKey pubkey = getPublicKeyFromX509(ALGORITHM, publicKey);
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, pubkey);
            byte plaintext[] = content.getBytes(CHARSET);
            byte[] output = cipher.doFinal(plaintext);
            String s = new String(Base64.encode(output, Base64.NO_WRAP), CHARSET);
            return s;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 采用公钥进行加密
     *
     * @param content   需要加密的字符串
     * @return base64编码后的加密字符串
     */
    public static String encryptByPublicKey(String content) {
        try {
            String file = PathUtils.getInternalAppDataPath() + "/key";
            boolean isCreate = FileUtils.createOrExistsDir(file);
            String keyFile = file + "/public.der";
            boolean result = ResourceUtils.copyFileFromAssets("public.der", keyFile);
            byte[] keyBytes = Files.readAllBytes(Paths.get(keyFile));
            X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            PublicKey pubkey = kf.generatePublic(spec);
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, pubkey);
            byte plaintext[] = content.getBytes(CHARSET);
            byte[] output = cipher.doFinal(plaintext);
            String s = new String(Base64.encode(output, Base64.NO_WRAP), CHARSET);
            return s;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private enum Mode {
        ENCRYPT(Cipher.ENCRYPT_MODE),
        DECRYPT(Cipher.DECRYPT_MODE);
        private final int value;

        Mode(int value) {
            this.value = value;
        }
    }

    public static String encryptByPublicKey1(String data) throws Exception {
        String file = PathUtils.getInternalAppDataPath() + "/key";
        boolean isCreate = FileUtils.createOrExistsDir(file);
        String keyFile = file + "/public.der";
        boolean result = ResourceUtils.copyFileFromAssets("public.der", keyFile);
        byte[] keyBytes = Files.readAllBytes(Paths.get(keyFile));
        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        PublicKey pubKey = kf.generatePublic(spec);


        try {
            return EncodeUtils.base64Encode2String(doFinal(pubKey, Mode.ENCRYPT, data.getBytes(CHARSET)));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void test() {
        String src = "第一次握手：Client将标志位SYN置为1，随机产生一个值seq=J，并将该数据包发送给Server，Client进入SYN_SENT状态，等待Server确认。";
        System.out.println("原文字：\t" + src);
        String encodedData = encryptByPublicKey(src);
        System.out.println("加密后：\t" + encodedData);
    }

    private static final int MAX_ENCRYPT_BLOCK = 117;
    private static final int MAX_DECRYPT_BLOCK = 128;

    private static byte[] doFinal(Key key, Mode mode, byte[] data) throws Exception {
        final int MAX = (mode == Mode.ENCRYPT) ? MAX_ENCRYPT_BLOCK : MAX_DECRYPT_BLOCK;
        final int LEN = data.length;
        byte[] cache;
        int i = 0, off = 0;

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1PADDING");
        cipher.init(mode.value, key);
        while (off < LEN) {
            cache = cipher.doFinal(data, off, Math.min(LEN - off, MAX));
            out.write(cache, 0, cache.length);
            i++;
            off = i * MAX;
        }
        byte[] result = out.toByteArray();
        out.close();
        return result;
    }

    public static byte[] readTextFromAssetFile(Context context, String assetFileName) {
        AssetManager manager = context.getAssets();
        try {
            InputStream inputStream = manager.open(assetFileName);
            int length = inputStream.available();
            byte[] buffer = new byte[length];
            inputStream.read(buffer);
            inputStream.close();
            return buffer;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 采用私钥进行解密
     *
     * @param encryptData 加密后的数据，base64编码
     * @param privateKey  私钥，base64编码
     * @return 解密后的字符串
     */
    public static String decryptByPrivateKey(String encryptData, String privateKey) {
        try {
            byte[] keyBytes = Base64.decode(privateKey, Base64.NO_WRAP);
            PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
            Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);

            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.DECRYPT_MODE, privateK);

            byte[] plaintext = Base64.decode(encryptData.getBytes(CHARSET), Base64.NO_WRAP);
            byte[] result = cipher.doFinal(plaintext);
            return new String(result, CHARSET);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 采用私钥进行签名
     *
     * @param content    需要签名的内容
     * @param privateKey 私钥，base64编码
     * @return base64编码后的签名字符串
     */
    public static String signByPrivateKey(String content, String privateKey) {
        try {
            PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(Base64.decode(privateKey, Base64.NO_WRAP));
            KeyFactory keyf = KeyFactory.getInstance(ALGORITHM);
            PrivateKey priKey = keyf.generatePrivate(priPKCS8);
            java.security.Signature signature = java.security.Signature.getInstance(SIGN_ALGORITHMS);
            signature.initSign(priKey);
            signature.update(content.getBytes(CHARSET));
            byte[] signed = signature.sign();
            return new String(Base64.encode(signed, Base64.NO_WRAP), CHARSET);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 验证签名
     *
     * @param content   需要验证的字符串
     * @param sign      签名字符串
     * @param publicKey 公钥, base64编码
     * @return true表示签名一致
     */
    public static boolean doCheck(String content, String sign, String publicKey) {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
            byte[] encodedKey = Base64.decode(publicKey, Base64.NO_WRAP);
            PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));
            java.security.Signature signature = java.security.Signature.getInstance(SIGN_ALGORITHMS);
            signature.initVerify(pubKey);
            signature.update(content.getBytes(CHARSET));
            boolean bverify = signature.verify(Base64.decode(sign, Base64.NO_WRAP));
            return bverify;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 公钥解密
     *
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    public static String decryptByPublicKey(String data, String key) {
        try {
            byte[] decode = Base64.decode(data.getBytes(), Base64.NO_WRAP);
            byte[] byPublicKey = decryptByPublicKey(decode, key.getBytes("utf-8"));
            return new String(byPublicKey, CHARSET);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 公钥解密
     *
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    private static byte[] decryptByPublicKey(byte[] data, byte[] key) throws Exception {
        byte[] keyBytes = Base64.decode(key, Base64.NO_WRAP);

        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
        PublicKey publicKey = keyFactory.generatePublic(keySpec);

//        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        //java 与 android平台不一致, android需采用以下方法
        Cipher cipher = Cipher.getInstance("RSA/None/PKCS1Padding");

        cipher.init(Cipher.DECRYPT_MODE, publicKey);
        return cipher.doFinal(data);
    }

    /**
     * 采用私钥进行签名
     *
     * @param content    需要签名的内容
     * @param privateKey 私钥，base64编码
     * @return base64编码后的签名字符串
     */
    public static String signByPrivateKey256(String content, String privateKey) {
        try {
            PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(Base64.decode(privateKey, Base64.NO_WRAP));
            KeyFactory keyf = KeyFactory.getInstance(ALGORITHM);
            PrivateKey priKey = keyf.generatePrivate(priPKCS8);

            java.security.Signature signature = java.security.Signature.getInstance(SIGN_ALGORITHMS_256);
            signature.initSign(priKey);
            signature.update(content.getBytes(CHARSET));
            byte[] signed = signature.sign();

            return new String(Base64.encode(signed, Base64.NO_WRAP), CHARSET);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 验证签名
     *
     * @param content   需要验证的内容
     * @param sign      签名字符串
     * @param publicKey 公钥, base64编码
     * @return true表示签名一致
     */
    public static boolean doCheck256(String content, String sign, String publicKey) {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
            byte[] encodedKey = Base64.decode(publicKey, Base64.NO_WRAP);
            PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));
            java.security.Signature signature = java.security.Signature.getInstance(SIGN_ALGORITHMS_256);
            signature.initVerify(pubKey);
            signature.update(content.getBytes(CHARSET));
            boolean bverify = signature.verify(Base64.decode(sign, Base64.NO_WRAP));
            return bverify;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
