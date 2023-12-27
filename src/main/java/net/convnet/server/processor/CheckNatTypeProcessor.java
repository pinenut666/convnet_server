package net.convnet.server.processor;

import net.convnet.server.ex.ConvnetException;
import net.convnet.server.protocol.AbstractProcessor;
import net.convnet.server.protocol.Cmd;
import net.convnet.server.protocol.Request;
import net.convnet.server.protocol.Response;
import net.convnet.server.session.Session;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.*;

@Service
public class CheckNatTypeProcessor extends AbstractProcessor {
   @Override
   public Cmd accept() {
      return Cmd.CHECK_NAT_TYPE;
   }

   @Override
   public void process(Session session, Request request, Response response) throws ConvnetException {
      int udpport = request.getIntParam("udpport");
      int tcpport = request.getIntParam("tcpport");
      Socket socket = new Socket();

      try {
         socket.connect(new InetSocketAddress(session.getIp(), tcpport), 5000);
         Writer writer = new PrintWriter(socket.getOutputStream());
         writer.write("Hello Convnet\n");
         writer.flush();
      } catch (IOException ignored) {
      }

      try {
         DatagramSocket clientSocket = new DatagramSocket();
         String cmd = Cmd.IS_CLIENT_UDP.ordinal() + ",ConVnet";
         InetAddress ineta = InetAddress.getByName(session.getIp());
         DatagramPacket sendPacket = new DatagramPacket(cmd.getBytes(), cmd.getBytes().length, ineta, udpport);
         clientSocket.send(sendPacket);
      } catch (IOException ignored) {
      }

   }
}
