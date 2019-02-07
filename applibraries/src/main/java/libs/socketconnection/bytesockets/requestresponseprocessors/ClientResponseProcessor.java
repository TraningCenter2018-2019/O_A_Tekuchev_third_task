package libs.socketconnection.bytesockets.requestresponseprocessors;

@FunctionalInterface
public interface ClientResponseProcessor {
  void process(byte[] data);
}
