package cargoportcomponent.cla;

import libs.cmdargsparser.AbstractCommandLineArgsParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

public class CargoPortCmdArgsParser extends AbstractCommandLineArgsParser {
  static public final String LISTEN_PORT_NAME = "listenport";
  static public final String SEND_DATA_IP_NAME = "sendip";
  static public final String SEND_DATA_PORT_NAME = "sendport";
  static public final String HELP_NAME = "help";

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

    Option help =  new Option(HELP_NAME, false, "the help");
    help.setRequired(false);
    options.addOption(help);

    return options;
  }
}
