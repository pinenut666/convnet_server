package net.convnet.server.email;

import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.activation.DataSource;
import java.util.Date;
import java.util.List;

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

   public String getFrom() {
      return this.from;
   }

   public void setFrom(String from) {
      this.from = from;
   }

   public String[] getTo() {
      return this.to;
   }

   public void setTo(String[] to) {
      this.to = to;
   }

   public String[] getCc() {
      return this.cc;
   }

   public void setCc(String[] cc) {
      this.cc = cc;
   }

   public String[] getBcc() {
      return this.bcc;
   }

   public void setBcc(String[] bcc) {
      this.bcc = bcc;
   }

   public String getReplyTo() {
      return this.replyTo;
   }

   public void setReplyTo(String replyTo) {
      this.replyTo = replyTo;
   }

   public Date getSentDate() {
      return this.sentDate;
   }

   public void setSentDate(Date sentDate) {
      this.sentDate = sentDate;
   }

   public int getPriority() {
      return this.priority;
   }

   public void setPriority(int priority) {
      this.priority = priority;
   }

   public String getSubject() {
      return this.subject;
   }

   public void setSubject(String subject) {
      this.subject = subject;
   }

   public String getBody() {
      return this.body;
   }

   public void setBody(String body) {
      this.body = body;
   }

   public List<DataSource> getAttachment() {
      return this.attachment;
   }

   public void setAttachment(List<DataSource> attachment) {
      this.attachment = attachment;
   }

   public List<DataSource> getInline() {
      return this.inline;
   }

   public void setInline(List<DataSource> inline) {
      this.inline = inline;
   }

   public Callback getCallback() {
      return this.callback;
   }

   public void setCallback(Callback callback) {
      this.callback = callback;
   }

   public String getFeedback() {
      return this.feedback;
   }

   public void setFeedback(String feedback) {
      this.feedback = feedback;
   }

   public String getFromLabel() {
      return this.fromLabel;
   }

   public void setFromLabel(String fromLabel) {
      this.fromLabel = fromLabel;
   }

   public String toString() {
      return ToStringBuilder.reflectionToString(this);
   }

   public interface Callback {
      void error(Email var1);

      void complete(Email var1);
   }
}
