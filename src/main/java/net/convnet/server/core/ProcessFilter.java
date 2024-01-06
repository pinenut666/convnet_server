package net.convnet.server.core;

import net.convnet.server.ex.ConvnetException;
import net.convnet.server.protocol.*;
import net.convnet.server.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

final class ProcessFilter extends AbstractFilter {
   private static final Logger LOG = LoggerFactory.getLogger(ProcessFilter.class);
   private final Map<Cmd, Processor> processors = new ConcurrentHashMap<>();

   ProcessFilter(Collection<Processor> processors) {

      for (Processor processor : processors) {
         this.processors.put(processor.accept(), processor);
      }

   }

   @Override
   public void doFilter(Session session, Request request, Response response, FilterChain chain) throws ConvnetException {
      Processor processor = this.processors.get(request.getCmd());
      if (processor != null) {
         processor.process(session, request, response);
      } else {
         LOG.debug("Processor for cmd [" + request.getCmd() + "] not found");
      }

      chain.doFilter(session, request, response);
   }
}
