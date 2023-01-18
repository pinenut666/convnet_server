package net.convnet.server.protocol;

import net.convnet.server.ex.ConvnetException;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public final class DefaultProtocolFactory implements ProtocolFactory {
   private final Map<Integer, Protocol> protocols = new HashMap();
   private int defaultVersion = 1;

   public void setDefaultVersion(int defaultVersion) {
      this.defaultVersion = defaultVersion;
   }

   public void setProtocols(List<Protocol> protocols) {
      Iterator i$ = protocols.iterator();

      while(i$.hasNext()) {
         Protocol protocol = (Protocol)i$.next();
         this.protocols.put(protocol.getVersion(), protocol);
      }

   }

   public Protocol getProtocol(int version) {
      Protocol protocol = (Protocol)this.protocols.get(version);
      if (protocol == null) {
         throw new ConvnetException("Protocol impl for version " + version + " not found");
      } else {
         return protocol;
      }
   }

   public Protocol getDefaultProtocol() {
      return this.getProtocol(this.defaultVersion);
   }
}
