package net.convnet.server.support.encrypt;

import javax.crypto.Cipher;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.SecureRandom;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class RSAEncryptServiceImpl extends AbstractEncryptService {
   @Override
   protected Cipher initEncryptCipher(byte[] key) throws GeneralSecurityException {
      SecureRandom sr = new SecureRandom();
      Cipher cipher = Cipher.getInstance(this.getMethod());
      cipher.init(1, KeyFactory.getInstance(this.getMethod()).generatePrivate(new PKCS8EncodedKeySpec(key)), sr);
      return cipher;
   }

   @Override
   protected Cipher initDecryptCipher(byte[] key) throws GeneralSecurityException {
      SecureRandom sr = new SecureRandom();
      Cipher cipher = Cipher.getInstance(this.getMethod());
      cipher.init(2, KeyFactory.getInstance(this.getMethod()).generatePublic(new X509EncodedKeySpec(key)), sr);
      return cipher;
   }

   @Override
   public String getMethod() {
      return "RSA";
   }
}
