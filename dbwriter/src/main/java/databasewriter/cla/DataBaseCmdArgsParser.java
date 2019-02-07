package databasewriter.cla;

import libs.cmdargsparser.AbstractCommandLineArgsParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

public class DataBaseCmdArgsParser extends AbstractCommandLineArgsParser {
  static public final String PORT_NAME = "port";

  @Override
  protected Options createOptions() {
    Options options = new Options();
    Option input = new Option("p", "port", true, "the port to listen");
    input.setRequired(false);
    options.addOption(input);
    return options;
  }
}
