package createfilesutility;

import createfilesutility.cla.CreateFileUtilityCmdArgsParser;
import createfilesutility.core.FileCreater;
import libs.parsers.jsonparsers.JsonConverter;
import libs.parsers.jsonparsers.exceptions.CannotConvertObjectException;
import org.apache.commons.cli.ParseException;

import java.io.IOException;

public class CreateFilesMain {
  static public final String DEFAULT_DIRECTORY = "./";

  static public void main(String[] args) {
    CreateFileUtilityCmdArgsParser cmdArgsParser = new CreateFileUtilityCmdArgsParser();
    try {
      cmdArgsParser.parse(args);
      if (cmdArgsParser.hasFlag(CreateFileUtilityCmdArgsParser.HELP_NAME)) {
        cmdArgsParser.printHelp("File creater");
        return;
      }
      String fileName = cmdArgsParser.getArgValue(CreateFileUtilityCmdArgsParser.FILE);
      String directory = cmdArgsParser.hasFlag(CreateFileUtilityCmdArgsParser.DIRECTORY) ?
              cmdArgsParser.getArgValue(CreateFileUtilityCmdArgsParser.DIRECTORY) : DEFAULT_DIRECTORY;
      boolean shipFile = cmdArgsParser.hasFlag(CreateFileUtilityCmdArgsParser.SHIP);
      boolean craneFile = cmdArgsParser.hasFlag(CreateFileUtilityCmdArgsParser.CRANE);
      boolean delCraneFile = cmdArgsParser.hasFlag(CreateFileUtilityCmdArgsParser.DEL_CRANE);
      if (!shipFile && !craneFile && !delCraneFile) {
        System.out.println("You have to select what file to create");
        cmdArgsParser.printHelp("File creater");
        return;
      }
      FileCreater fileCreater = new FileCreater(new JsonConverter());
      if (shipFile) {
        fileCreater.createShip(fileName, directory);
      }
      if (craneFile) {
        fileCreater.createCrane(fileName, directory);
      }
      if (delCraneFile) {
        fileCreater.createDeleteCraneCommand(fileName, directory);
      }
      System.out.println("File(s) created");
    }
    catch (ParseException e) {
      cmdArgsParser.printHelp("File creater");
    }
    catch (CannotConvertObjectException | IOException e) {
      System.out.println(e.getMessage());
    }
  }
}
