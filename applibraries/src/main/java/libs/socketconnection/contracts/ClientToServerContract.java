package libs.socketconnection.contracts;

import java.io.*;

public class ClientToServerContract implements Contract {

  @Override
  public byte[] objectToBytes(Object object) {
    try (ByteArrayOutputStream out = new ByteArrayOutputStream();
         ObjectOutputStream os = new ObjectOutputStream(out)) {
      os.writeObject(object);
      return out.toByteArray();
    }
    catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }

  @Override
  public Object bytesToObject(byte[] bytes) {
    try (ByteArrayInputStream in = new ByteArrayInputStream(bytes);
         ObjectInputStream is = new ObjectInputStream(in)) {
      return is.readObject();
    }
    catch (IOException e) {
      e.printStackTrace();
      return null;
    }
    catch (ClassNotFoundException e) {
      e.printStackTrace();
      return null;
    }
  }
}
