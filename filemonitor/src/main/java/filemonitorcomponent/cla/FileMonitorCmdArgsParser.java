package filemonitorcomponent.cla;

import libs.cmdargsparser.AbstractCommandLineArgsParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

public class FileMonitorCmdArgsParser extends AbstractCommandLineArgsParser {
  static public final String IP_NAME = "ip";
  static public final String PORT_NAME = "port";
  static public final String DIR_NAME = "dir";

  @Override
  protected Options createOptions() {
    Options options = new Options();

    Option ip = new Option(IP_NAME, true, "the ip to send data");
    ip.setRequired(false);
    options.addOption(ip);

    Option port = new Option(PORT_NAME, true, "the port");
    port.setRequired(false);
    options.addOption(port);

    Option dir = new Option(DIR_NAME, true, "the port");
    dir.setRequired(false);
    options.addOption(dir);

    return options;
  }
}
