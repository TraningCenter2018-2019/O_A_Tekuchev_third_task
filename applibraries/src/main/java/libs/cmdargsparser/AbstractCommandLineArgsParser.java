package libs.cmdargsparser;

import org.apache.commons.cli.*;

public abstract class AbstractCommandLineArgsParser {
  static public final String HELP_NAME = "help";

  private CommandLine commandLine;
  private CommandLineParser parser;
  private Options options;

  public AbstractCommandLineArgsParser() {
    parser = createCommandLineParser();
    options = createOptions();
    Option help =  new Option(HELP_NAME, false, "the help");
    help.setRequired(false);
    options.addOption(help);
  }

  protected Options getOptions() {
    return options;
  }

  protected CommandLine getCommandLine() {
    return commandLine;
  }

  protected abstract Options createOptions();

  protected CommandLineParser createCommandLineParser() {
    return new DefaultParser();
  }

  public final void parse(String[] args) throws ParseException {
    commandLine = parser.parse(options, args);
  }

  public final String getArgValue(String argName) {
    return commandLine.getOptionValue(argName);
  }

  public final boolean hasFlag(String flagName) {
    return commandLine.hasOption(flagName);
  }

  public void printHelp(String header) {
    HelpFormatter formatter = new HelpFormatter();
    formatter.printHelp(header, options);
  }
}
