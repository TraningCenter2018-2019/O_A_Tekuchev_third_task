package createfilesutility.cla;

import createfilesutility.CreateFilesMain;
import libs.cmdargsparser.AbstractCommandLineArgsParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

public class CreateFileUtilityCmdArgsParser extends AbstractCommandLineArgsParser {
  static public final String HELP_DESCRIPTION = "This application helps you to create ship, crane etc. files " +
          "in appropriate format that can be parsed and sent to cargo port app";

  static public final String SHIP = "ship";
  static public final String CRANE = "crane";
  static public final String DEL_CRANE = "deletecrane";
  static public final String FILE = "filename";
  static public final String DIRECTORY = "directory";

  @Override
  protected Options createOptions() {
    Options options = new Options();

    Option fileName = new Option("f", FILE, true, "the file name");
    fileName.setRequired(true);
    options.addOption(fileName);

    Option ship = new Option(SHIP, false, "create ship file");
    ship.setRequired(false);
    options.addOption(ship);

    Option crane = new Option(CRANE, false, "create crane file");
    crane.setRequired(false);
    options.addOption(crane);

    Option delCrane = new Option(DEL_CRANE, false, "create delete crane command file");
    delCrane.setRequired(false);
    options.addOption(delCrane);

    Option dir = new Option("d", DIRECTORY, true, "the directory where to place file " +
            "(optional, the default value is " + CreateFilesMain.DEFAULT_DIRECTORY + ")");
    dir.setRequired(false);
    options.addOption(dir);

    return options;
  }
}
