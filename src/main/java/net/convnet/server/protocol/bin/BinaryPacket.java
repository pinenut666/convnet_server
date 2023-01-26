package net.convnet.server.protocol.bin;

import net.convnet.server.protocol.Cmd;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class BinaryPacket {
   private final Cmd cmd;
   private final List<Object> parts;
   private boolean needstar = true;

   public void NeedEndStar(boolean isneedstar) {
      this.needstar = isneedstar;
   }

   public boolean IsNeedStar() {
      return this.needstar;
   }

   public BinaryPacket(Cmd cmd, Object[] parts) {
      this.cmd = cmd;
      this.parts = Arrays.asList(parts);
   }

   public BinaryPacket() {
      this.cmd = null;
      this.parts = new ArrayList<>();
   }

   public Cmd getCmd() {
      return this.cmd;
   }

   public List<Object> getParts() {
      return this.parts;
   }

   public Object get(int index) {
      return this.parts.get(index);
   }

   public BinaryPacket add(Object part) {
      this.parts.add(part);
      return this;
   }

   public BinaryPacket end(Object part) {
      this.parts.add('*');
      this.parts.add(part);
      return this;
   }

   public BinaryPacket end() {
      this.parts.add('*');
      return this;
   }
}
