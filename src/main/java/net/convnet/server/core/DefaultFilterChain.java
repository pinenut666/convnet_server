package net.convnet.server.core;

import net.convnet.server.protocol.Filter;
import net.convnet.server.protocol.FilterChain;
import net.convnet.server.protocol.Request;
import net.convnet.server.protocol.Response;
import net.convnet.server.session.Session;

import java.util.Iterator;
import java.util.List;

final class DefaultFilterChain implements FilterChain {
   private final List<Filter> filters;

   public DefaultFilterChain(List<Filter> filters) {
      this.filters = filters;
   }

   @Override
   public void doFilter(Session session, Request request, Response response) {
      (new Chain(this.filters.iterator())).doFilter(session, request, response);
   }

   private final class Chain implements FilterChain {
      private Iterator<Filter> filtersIterator;

      public Chain(Iterator<Filter> filtersIterator) {
         this.filtersIterator = filtersIterator;
      }

      @Override
      public void doFilter(Session session, Request request, Response response) {
         if (this.filtersIterator.hasNext()) {
            Filter filter = this.filtersIterator.next();
            if (filter.accept(session, request)) {
               filter.doFilter(session, request, response, this);
            } else {
               this.doFilter(session, request, response);
            }
         }

      }
   }
}
