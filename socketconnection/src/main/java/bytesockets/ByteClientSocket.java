package bytesockets;

import bytesockets.requestresponseprocessors.ClientResponseProcessor;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ByteClientSocket implements ByteSocket {
  private Socket clientSocket;
  private OutputStream outStream;
  private InputStream inStream;
  private ClientResponseProcessor processor;

  public ByteClientSocket(String ip, int port, ClientResponseProcessor processor) throws IOException {
    clientSocket = new Socket(ip, port);
    outStream = clientSocket.getOutputStream();
    inStream = clientSocket.getInputStream();
    this.processor = processor;
  }

  public ByteClientSocket(String ip, int port) throws IOException {
    this(ip, port, null);
  }

  public void sendMessage(byte[] data) throws IOException {
    outStream.write(getByteSize(data.length));
    outStream.write(data);
    int contentLength = readContentLength(inStream);
    byte[] response = new byte[contentLength];
    if (processor != null) {
      processor.process(response);
    }
  }

  public boolean isConnected() {
    return clientSocket.isConnected();
  }

  public void stopConnection() throws IOException {
    inStream.close();
    outStream.close();
    clientSocket.close();
  }
}
