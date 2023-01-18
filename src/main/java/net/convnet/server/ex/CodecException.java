package net.convnet.server.ex;

public class CodecException extends ConvnetException {
   private static final long serialVersionUID = -1974937277693378901L;

   public CodecException(Object obj) {
      super((String)obj.toString(), 101);
   }
}
