package net.convnet.server.protocol;

import net.convnet.server.session.Session;

public abstract class AbstractFilter implements Filter {
   public boolean accept(Session session, Request request) {
      return true;
   }

   public void init() {
   }

   public void destroy() {
   }
}
