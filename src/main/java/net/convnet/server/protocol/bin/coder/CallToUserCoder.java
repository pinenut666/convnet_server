package net.convnet.server.protocol.bin.coder;

import net.convnet.server.protocol.Cmd;
import net.convnet.server.protocol.P2PCallType;
import net.convnet.server.protocol.RequestBuilder;
import net.convnet.server.protocol.ResponseReader;
import net.convnet.server.protocol.bin.AbstractPacketCoder;
import net.convnet.server.protocol.bin.BinaryPacket;
import org.springframework.stereotype.Service;

@Service
public class CallToUserCoder extends AbstractPacketCoder {
   public Cmd getCmd() {
      return Cmd.CALL_TO_USER;
   }

   public Cmd getRespCmd() {
      return Cmd.CALL_TO_USER_RESP;
   }

   public void decode(RequestBuilder builder, BinaryPacket packet) {
      builder.set("id", packet.get(0));
      builder.set("count", packet.get(1));
      builder.set("password", packet.get(2));
   }

   public void encode(ResponseReader reader, BinaryPacket packet) {
      P2PCallType callType = (P2PCallType)reader.getAttr("callType");
      packet.add(callType.ordinal());
      switch(callType) {
      case ALL_DATA:
      case UDP_S2SResp:
      case UDP_C2SResp:
      case UDP_C2CResp:
      case UDP_P2PResp:
      case TCP_C2SResp:
      case ALL_NOTARRIVE:
      case NOTCONNECT:
      case DISCONNECT:
      default:
         break;
      case UDP_S2S:
         packet.add(reader.getAttr("callerid"));
         packet.add(reader.getAttr("callerip"));
         packet.add(reader.getAttr("callerudpport"));
         packet.add(reader.getAttr("callermac"));
         break;
      case UDP_C2S:
         packet.add(reader.getAttr("callerid"));
         packet.add(reader.getAttr("callerip"));
         packet.add(reader.getAttr("callerudpport"));
         packet.add(reader.getAttr("callermac"));
         break;
      case UDP_C2C:
         packet.add(reader.getAttr("callerid"));
         packet.add(reader.getAttr("callerip"));
         packet.add(reader.getAttr("callerudpport"));
         packet.add(reader.getAttr("callermac"));
         break;
      case UDP_GETPORT:
         packet.add(reader.getAttr("callerid"));
         packet.add(reader.getAttr("callermac"));
         break;
      case TCP_C2S:
         packet.add(reader.getAttr("callerid"));
         packet.add(reader.getAttr("callerip"));
         packet.add(reader.getAttr("callertcpport"));
         packet.add(reader.getAttr("callermac"));
         break;
      case TCP_SvrTrans:
         packet.add(reader.getAttr("id"));
         packet.add(reader.getAttr("mac"));
         break;
      case SAMEIP_CALL:
         packet.add(reader.getAttr("id"));
      }

   }
}
