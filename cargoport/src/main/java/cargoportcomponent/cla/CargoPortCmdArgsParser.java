package cargoportcomponent.cla;

import libs.cmdargsparser.AbstractCommandLineArgsParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

/**
 * Command line arguments parser for cargo port app
 */
public class CargoPortCmdArgsParser extends AbstractCommandLineArgsParser {
  static public final String LISTEN_PORT_NAME = "listenport";
  static public final String SEND_DATA_IP_NAME = "sendip";
  static public final String SEND_DATA_PORT_NAME = "sendport";
  static public final String NOT_SEND_NAME = "notsend";

  @Override
  protected Options createOptions() {
    Options options = new Options();

    Option listenPort = new Option(LISTEN_PORT_NAME, true, "the port to listen");
    listenPort.setRequired(false);
    options.addOption(listenPort);

    Option sendDataIp = new Option(SEND_DATA_IP_NAME, true, "the ip of the server to send data");
    sendDataIp.setRequired(false);
    options.addOption(sendDataIp);

    Option sendDataPort = new Option(SEND_DATA_PORT_NAME, true, "the ip of the server to send data");
    sendDataPort.setRequired(false);
    options.addOption(sendDataPort);

    Option notSend = new Option(NOT_SEND_NAME, false,
            "flag indicates that unload statistics will not be sent to db server");
    notSend.setRequired(false);
    options.addOption(notSend);

    return options;
  }
}
