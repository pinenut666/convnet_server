package net.convnet.server.protocol;

import net.convnet.server.session.Session;

public interface FilterChain {
   void doFilter(Session var1, Request var2, Response var3);
}
