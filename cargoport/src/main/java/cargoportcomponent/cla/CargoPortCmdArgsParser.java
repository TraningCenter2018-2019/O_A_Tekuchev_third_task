package cargoportcomponent.cla;

import cargoportcomponent.contexts.CargoPortAppContext;
import libs.cmdargsparser.AbstractCommandLineArgsParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

/**
 * Command line arguments parser for cargo port app
 */
public class CargoPortCmdArgsParser extends AbstractCommandLineArgsParser {
  static public final String HELP_DESCRIPTION = "This application gets data about new ships and cranes" +
          "and displays it to user or send to database application";

  static public final String LISTEN_PORT = "listenport";
  static public final String SERVER_IP = "sendip";
  static public final String SERVER_PORT = "sendport";
  static public final String NOT_SEND = "notsend";

  @Override
  protected Options createOptions() {
    Options options = new Options();

    Option listenPort = new Option(LISTEN_PORT, true, "the port to listen " +
            "(optional, the default value is" + CargoPortAppContext.DEFAULT_LISTEN_PORT + ")");
    listenPort.setRequired(false);
    options.addOption(listenPort);

    Option sendDataIp = new Option(SERVER_IP, true, "the server ip to send data, " +
            "must be applied with \"" + SERVER_PORT + "\"" +
            "(optional, the default value is " + CargoPortAppContext.DEFAULT_SERVER_IP + ")");
    sendDataIp.setRequired(false);
    options.addOption(sendDataIp);

    Option sendDataPort = new Option(SERVER_PORT, true, "the server port " +
            "must be applied with \"" + SERVER_IP + "\"" +
            "(optional, the default value is " + CargoPortAppContext.DEFAULT_SERVER_PORT + ")");
    sendDataPort.setRequired(false);
    options.addOption(sendDataPort);

    Option notSend = new Option(NOT_SEND, false,
            "flag indicates that unload statistics will not be sent to db server");
    notSend.setRequired(false);
    options.addOption(notSend);

    return options;
  }
}
