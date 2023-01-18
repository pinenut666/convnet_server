package net.convnet.server.ex;

public interface ErrorCode {
   int CUSTOM = 100;
   int SUCCEED = 0;
   int SERVER_ERROR = 1;
   int ILLEGAL_PARAM = 2;
   int MISS_PARAM = 3;
   int ILLEGAL_STATE = 4;
   int UN_SUPPORTED = 5;
   int NO_PERMISSON = 6;
   int ENTITY_ERROR = 50;
   int ENTITY_NOT_FOUND = 51;
   int ENTITY_EXISTS = 52;
   int CODEC_ERROR = 101;
   int PASSWORD_INCORRECT = 102;
}
