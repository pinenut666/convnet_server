package net.convnet.server.support.encrypt;

import net.convnet.server.util.Codecs;
import org.springframework.beans.factory.InitializingBean;

import javax.crypto.Cipher;
import java.security.GeneralSecurityException;

public abstract class AbstractEncryptService implements EncryptService, InitializingBean {
   private Cipher encryptCipher;
   private Cipher decryptCipher;
   private String key;
   private String encryptKey;
   private String decryptKey;

   public String getKey() {
      return this.key;
   }

   public void setKey(String key) {
      this.key = key;
   }

   public String getEncryptKey() {
      return this.encryptKey;
   }

   public void setEncryptKey(String encryptKey) {
      this.encryptKey = encryptKey;
   }

   public String getDecryptKey() {
      return this.decryptKey;
   }

   public void setDecryptKey(String decryptKey) {
      this.decryptKey = decryptKey;
   }

   @Override
   public String encrypt(String str) throws GeneralSecurityException {
      return Codecs.encode(this.encrypt(Codecs.getBytes(str)));
   }

   @Override
   public String decrypt(String str) throws GeneralSecurityException {
      return Codecs.toString(this.decrypt(Codecs.decode(str)));
   }

   @Override
   public byte[] encrypt(byte[] bytes) throws GeneralSecurityException {
      if (this.encryptCipher == null) {
         throw new IllegalStateException("EncryptCipher not init");
      } else {
         return this.encryptCipher.doFinal(bytes);
      }
   }

   @Override
   public byte[] decrypt(byte[] bytes) throws GeneralSecurityException {
      if (this.decryptCipher == null) {
         throw new IllegalStateException("DecryptCipher not init");
      } else {
         return this.decryptCipher.doFinal(bytes);
      }
   }

   @Override
   public void afterPropertiesSet() throws Exception {
      if (this.encryptKey == null) {
         this.encryptKey = this.key;
      }

      if (this.decryptKey == null) {
         this.decryptKey = this.key;
      }

      if (this.encryptKey != null) {
         this.encryptCipher = this.initEncryptCipher(Codecs.decode(this.encryptKey));
      }

      if (this.decryptCipher != null) {
         this.decryptCipher = this.initDecryptCipher(Codecs.decode(this.decryptKey));
      }

   }

   protected abstract Cipher initEncryptCipher(byte[] var1) throws GeneralSecurityException;

   protected abstract Cipher initDecryptCipher(byte[] var1) throws GeneralSecurityException;
}
