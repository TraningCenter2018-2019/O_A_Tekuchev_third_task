package libs.socketconnection.bytesockets;

import libs.socketconnection.bytesockets.requestresponseprocessors.ServerRequestProcessor;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ByteServerSocket extends Thread implements ByteSocket {
  static private final int threadSleep = 200;

  private boolean stopListen;
  private int port;
  private ServerRequestProcessor processor;
  private Thread thread;
  private ServerSocket serverSocket;
  private Socket clientSocket;
  private OutputStream outStream;
  private InputStream inStream;

  public ByteServerSocket(int port, ServerRequestProcessor processor) {
    this.port = port;
    this.processor = processor;
  }

  private synchronized boolean isStopListen() {
    return stopListen;
  }

  private synchronized void setStopListen(boolean stopListen) {
    this.stopListen = stopListen;
  }

  private void listen() throws IOException, InterruptedException {
    try {
      setStopListen(false);
      serverSocket = new ServerSocket(port);
      clientSocket = serverSocket.accept();
      outStream = clientSocket.getOutputStream();
      inStream = clientSocket.getInputStream();
      while (!isStopListen()) {
        if (inStream.available() > 0) {
          int contentLength = readContentLength(inStream);
          byte[] request = new byte[contentLength];
          inStream.read(request);
          byte[] response = processor.process(request);
          outStream.write(getByteSize(response.length));
          outStream.write(response);
        }
        Thread.sleep(threadSleep);
      }
    }
    finally {
      serverSocket.close();
      clientSocket.close();
      inStream.close();
      outStream.close();
    }
  }

  @Override
  public void run() {
    try {
      listen();
    }
    catch (IOException e) {
      e.printStackTrace();
    }
    catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  public void startListen() {
    thread = new Thread(this);
    thread.start();
  }

  public boolean isListen() {
    return !isStopListen();
  }

  public void stopListen() {
    setStopListen(true);
  }
}
