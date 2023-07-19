package io.inodream.wallet.util.encrypt;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;

/**
 * <pre>
 *     author : zhen
 *     time   : 2023/07/19
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class EncryptUtils {

    public static PublicKey get(String filename) throws Exception {
        byte[] keyBytes = Files.readAllBytes(Paths.get(filename));
        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePublic(spec);
    }

    public static String encrypt() {
        return "";

//        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(byteArray);
//
//        try {
//            PublicKey pubkey = getPublicKeyFromX509(ALGORITHM, publicKey);
//            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
//            cipher.init(Cipher.ENCRYPT_MODE, pubkey);
//            byte plaintext[] = content.getBytes(CHARSET);
//            byte[] output = cipher.doFinal(plaintext);
//            String s = new String(Base64.encode(output, Base64.NO_WRAP), CHARSET);
//            return s;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }
}
