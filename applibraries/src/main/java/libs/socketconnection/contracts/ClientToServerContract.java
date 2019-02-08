package libs.socketconnection.contracts;

public interface ClientToServerContract {

  byte[] objectToBytes(Object object);

  Object bytesToObject(byte[] bytes);

}
