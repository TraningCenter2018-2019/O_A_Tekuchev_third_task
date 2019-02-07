package libs.socketconnection.bytesockets.requestresponseprocessors;

@FunctionalInterface
public interface ServerRequestProcessor {
  byte[] process(byte[] data);
}
