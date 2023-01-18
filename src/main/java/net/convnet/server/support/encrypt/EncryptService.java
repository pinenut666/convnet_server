package net.convnet.server.support.encrypt;

import java.security.GeneralSecurityException;

public interface EncryptService {
   String encrypt(String var1) throws GeneralSecurityException;

   String decrypt(String var1) throws GeneralSecurityException;

   byte[] encrypt(byte[] var1) throws GeneralSecurityException;

   byte[] decrypt(byte[] var1) throws GeneralSecurityException;

   String getMethod();
}
