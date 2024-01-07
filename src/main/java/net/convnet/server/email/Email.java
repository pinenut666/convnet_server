package net.convnet.server.email;

import lombok.Data;
import lombok.ToString;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.activation.DataSource;
import java.util.Date;
import java.util.List;
@Data
@ToString
public class Email {
   private String from;
   private String fromLabel;
   private String[] to;
   private String[] cc;
   private String[] bcc;
   private String replyTo;
   private Date sentDate;
   private int priority;
   private String subject;
   private String body;
   private List<DataSource> attachment;
   private List<DataSource> inline;
   private Callback callback;
   private String feedback;

   public interface Callback {
      void error(Email var1);

      void complete(Email var1);
   }
}
