package createfilesutility.cla;

import libs.cmdargsparser.AbstractCommandLineArgsParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

public class CreateFileUtilityCmdArgsParser extends AbstractCommandLineArgsParser {
  static public final String SHIP_NAME = "ship";
  static public final String CRANE_NAME = "crane";
  static public final String DEL_CRANE_NAME = "deletecrane";
  static public final String FILE_NAME = "filename";
  static public final String DIRECTORY = "directory";

  @Override
  protected Options createOptions() {
    Options options = new Options();

    Option fileName = new Option("f", FILE_NAME, true, "the file name");
    fileName.setRequired(true);
    options.addOption(fileName);

    Option ship = new Option(SHIP_NAME, false, "create ship file");
    ship.setRequired(false);
    options.addOption(ship);

    Option crane = new Option(CRANE_NAME, false, "create crane file");
    crane.setRequired(false);
    options.addOption(crane);

    Option delCrane = new Option(DEL_CRANE_NAME, false, "create delete crane command file");
    delCrane.setRequired(false);
    options.addOption(delCrane);

    Option dir = new Option("d", DIRECTORY, true, "the directory where to place file");
    dir.setRequired(false);
    options.addOption(dir);

    return options;
  }
}
