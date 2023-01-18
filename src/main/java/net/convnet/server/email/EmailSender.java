package net.convnet.server.email;

import java.util.concurrent.Future;

public interface EmailSender {
   Future<Email> send(Email var1);
}
