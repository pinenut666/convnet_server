package net.convnet.server.protocol;

public interface ProtocolFactory {
   Protocol getProtocol(int var1);

   Protocol getDefaultProtocol();
}
