package net.convnet.server.protocol;

import net.convnet.server.ex.ConvnetException;
import net.convnet.server.session.Session;

public interface Filter {
   boolean accept(Session var1, Request var2);

   void doFilter(Session var1, Request var2, Response var3, FilterChain var4) throws ConvnetException;

   void init();

   void destroy();
}
