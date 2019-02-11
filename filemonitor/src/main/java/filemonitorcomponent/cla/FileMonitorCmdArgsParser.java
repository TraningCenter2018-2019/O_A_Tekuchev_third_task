package filemonitorcomponent.cla;

import filemonitorcomponent.contexts.FileMonitorAppContext;
import libs.cmdargsparser.AbstractCommandLineArgsParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

/**
 * Command line arguments for file monitor app
 */
public class FileMonitorCmdArgsParser extends AbstractCommandLineArgsParser {
  static public final String HELP_DESCRIPTION = "This application monitores a directory for files," +
          "reads them and send data to the server";

  static public final String IP_NAME = "ip";
  static public final String PORT_NAME = "port";
  static public final String DIR_NAME = "dir";

  @Override
  protected Options createOptions() {
    Options options = new Options();

    Option ip = new Option(IP_NAME, true, "the server ip (optional, the default value is " +
            FileMonitorAppContext.DEFAULT_SERVER_IP + ")");
    ip.setRequired(false);
    options.addOption(ip);

    Option port = new Option(PORT_NAME, true, "the server port (optional, the default value is " +
            FileMonitorAppContext.DEFAULT_PORT);
    port.setRequired(false);
    options.addOption(port);

    Option dir = new Option(DIR_NAME, true, "the directory where files ara read " +
            "(optional, the default value is \"" + FileMonitorAppContext.DEFAULT_MONITORED_DIRECTORY + "\"");
    dir.setRequired(false);
    options.addOption(dir);

    return options;
  }
}
