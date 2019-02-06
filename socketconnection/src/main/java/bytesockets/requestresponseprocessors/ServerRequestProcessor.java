package bytesockets.requestresponseprocessors;

@FunctionalInterface
public interface ServerRequestProcessor {
  byte[] process(byte[] data);
}
