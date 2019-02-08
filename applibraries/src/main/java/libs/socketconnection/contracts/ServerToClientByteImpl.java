package libs.socketconnection.contracts;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class ServerToClientByteImpl implements ServerToClientContract {
  @Override
  public byte[] sendOk() {
    return new byte[] {ServerToClientContract.OK_CODE};
  }

  @Override
  public byte[] sendError(byte code, String message) {
    byte[] strBytes = message.getBytes();
    byte[] resultBytes = new byte[strBytes.length + 1];
    resultBytes[0] = code;
    for (int i = 0; i < strBytes.length; ++i) {
      resultBytes[i + 1] = strBytes[i];
    }
    return resultBytes;
  }

  @Override
  public String getMessage(byte[] data) {
    return new String(Arrays.copyOfRange(data, 1, data.length));
  }
}
