package net.convnet.server.protocol;

import net.convnet.server.session.Session;

public abstract class AbstractFilter implements Filter {
   @Override
   public boolean accept(Session session, Request request) {
      return true;
   }

   @Override
   public void init() {
   }

   @Override
   public void destroy() {
   }
}
