package bytesockets;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.nio.ByteBuffer;

public interface ByteSocket {
  default int readContentLength(InputStream inputStream) throws IOException {
    final int INT_SIZE = 4;
    byte[] len = new byte[INT_SIZE];
    inputStream.read(len);
    int contentLength = len[3] & 0xFF |
            (len[2] & 0xFF) << 8 |
            (len[1] & 0xFF) << 16 |
            (len[0] & 0xFF) << 24;
    return contentLength;
  }

  default byte[] getByteSize(int len) {
    return ByteBuffer.allocate(4).putInt(len).array();
  }
}
