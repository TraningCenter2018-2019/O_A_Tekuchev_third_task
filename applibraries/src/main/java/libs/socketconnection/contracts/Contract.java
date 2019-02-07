package libs.socketconnection.contracts;

public interface Contract {

  byte[] objectToBytes(Object object);

  Object bytesToObject(byte[] bytes);

}
