package libs.socketconnection.contracts;

public interface ServerToClientContract {
  byte OK_CODE = 1;
  byte UNEXPECTED_SENT_DATA_CODE = 2;
  byte INTERNAL_SERVER_ERROR_CODE = 3;

  byte[] sendOk();

  byte[] sendError(byte code, String message);

  String getMessage(byte[] data);
}
