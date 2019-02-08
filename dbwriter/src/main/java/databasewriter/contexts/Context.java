package databasewriter.contexts;

import databasewriter.core.databases.AbstractDb;
import databasewriter.core.databases.ApacheDerbyDb;
import databasewriter.core.tables.unloadingprocesstable.TableRow;
import databasewriter.core.tables.unloadingprocesstable.UnloadingProcessTable;
import databasewriter.logging.DataWriterLoggerManager;
import libs.socketconnection.bytesockets.ByteServerSocket;
import libs.socketconnection.contracts.ClientToServerClientToServerContractSerializationImpl;
import libs.socketconnection.contracts.ClientToServerContract;

import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Context {
  static private final Logger LOGGER = DataWriterLoggerManager.getInstance().getLoggerApp();
  static private final String DB_NAME = "mydb";
  static private final String TABLE_NAME = "unload_process";

  static public final int DEFAULT_PORT = 6667;

  private AbstractDb dataBase;
  private UnloadingProcessTable table;
  private ClientToServerContract clientToServerContractSerializationImpl;
  private int port;

  public Context(int port) {
    this.port = port;
  }

  private byte[] inputDataProcessor(byte[] data) {
    try {
      Object recievedObject = clientToServerContractSerializationImpl.bytesToObject(data);
      String className = recievedObject.getClass().getName();
      if (className.equals(TableRow.class.getName())) {
        table.insert((TableRow) recievedObject);
      }
      else {
        LOGGER.log(Level.WARNING, "Unexpected income data type: " + className);
      }
    }
    catch (SQLException e) {
      LOGGER.log(Level.WARNING, e.getMessage());
    }
    return new byte[1];
  }

  public void startApplication() {
    try {
      dataBase = new ApacheDerbyDb(DB_NAME);
      table = new UnloadingProcessTable(TABLE_NAME);
      dataBase.addTable(table);
      clientToServerContractSerializationImpl = new ClientToServerClientToServerContractSerializationImpl();
      ByteServerSocket serverSocket = new ByteServerSocket(port, this::inputDataProcessor);
      serverSocket.startListen();

      System.in.read();

      serverSocket.stopListen();
    }
    catch (SQLException | IOException e) {
      LOGGER.log(Level.SEVERE, e.getMessage());
    }
  }
}
