package databasewriter.cla;

import databasewriter.contexts.DataWriterAppContext;
import libs.cmdargsparser.AbstractCommandLineArgsParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

/**
 * Command line arguments for database writer app
 */
public class DataWriterCmdArgsParser extends AbstractCommandLineArgsParser {
  static public final String HELP_DESCRIPTION = "This application is the server that gets a data about" +
          "ship unloading and puts it into the database";

  static public final String PORT_NAME = "port";

  @Override
  protected Options createOptions() {
    Options options = new Options();
    Option input = new Option("p", "port", true, "the port to listen " +
            "(optional, the default value is " + DataWriterAppContext.DEFAULT_PORT + ")");
    input.setRequired(false);
    options.addOption(input);
    return options;
  }
}
