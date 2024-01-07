package net.convnet.server.protocol;

import net.convnet.server.session.Session;

/**
 * 这个FilterChain似乎是javax.servlet.FilterChain的仿品。
 *
 * @author Administrator
 * @date 2024/01/07
 */
public interface FilterChain {
   void doFilter(Session var1, Request var2, Response var3);
}
