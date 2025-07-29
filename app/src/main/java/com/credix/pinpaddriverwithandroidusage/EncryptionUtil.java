package com.credix.pinpaddriverwithandroidusage;
import android.util.Base64;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class EncryptionUtil {

  private static final String ALGORITHM = "AES";
  private static final String TRANSFORMATION = "AES";

  // יצירת מפתח משרשור של מילת מפתח עם SHA-256
          private static SecretKeySpec generateKey(String password) throws Exception {
    final MessageDigest digest = MessageDigest.getInstance("SHA-256");
    byte[] bytes = password.getBytes("UTF-8");
    digest.update(bytes, 0, bytes.length);
    byte[] key = digest.digest();
    return new SecretKeySpec(key, ALGORITHM);
  }

  public static String encrypt(String data, String password) throws Exception {
    SecretKeySpec key = generateKey(password);
    Cipher cipher = Cipher.getInstance(TRANSFORMATION);
    cipher.init(Cipher.ENCRYPT_MODE, key);
    byte[] encVal = cipher.doFinal(data.getBytes());
    return Base64.encodeToString(encVal, Base64.DEFAULT);
  }

  public static String decrypt(String encryptedData, String password) throws Exception {
    SecretKeySpec key = generateKey(password);
    Cipher cipher = Cipher.getInstance(TRANSFORMATION);
    cipher.init(Cipher.DECRYPT_MODE, key);
    byte[] decodedValue = Base64.decode(encryptedData, Base64.DEFAULT);
    byte[] decValue = cipher.doFinal(decodedValue);
    return new String(decValue);
  }
}
