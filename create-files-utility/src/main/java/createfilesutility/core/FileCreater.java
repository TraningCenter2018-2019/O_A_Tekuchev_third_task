package createfilesutility.core;

import libs.parsers.jsonparsers.JsonConverter;
import libs.parsers.jsonparsers.exceptions.CannotConvertObjectException;
import libs.simulationattributes.CargoTypes;
import libs.simulationattributes.Crane;
import libs.simulationattributes.DeleteCraneCommand;
import libs.simulationattributes.Ship;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class FileCreater {

  private JsonConverter converter;

  public FileCreater(JsonConverter aConverter) {
    converter = aConverter;
  }

  private String checkOnSlach(String directory) {
    if (directory.charAt(directory.length() - 1) != '/') {
      directory += "/";
    }
    return directory;
  }

  private CargoTypes chooseType(Scanner scanner) {
    int ans;
    int count = CargoTypes.values().length;
    do {
      System.out.println("Choose type:");
      for (int i = 0; i < count; ++i) {
        System.out.println(i + " - " + CargoTypes.values()[i]);
      }
      ans = scanner.nextInt();
    } while (ans < 0 || ans > count - 1);
    CargoTypes shipType =  CargoTypes.values()[ans];
    return shipType;
  }

  private void writeToFile(String fullFileName, String content) throws IOException {
    try (FileWriter fileWriter = new FileWriter(fullFileName)) {
      fileWriter.write(content);
    }
  }

  public void createShip(String fileName, String directory) throws IOException, CannotConvertObjectException {
    directory = checkOnSlach(directory);
    Scanner scanner = new Scanner(System.in);
    System.out.println("Input ship name:");
    String shipName = scanner.next();
    CargoTypes shipType = chooseType(scanner);
    int cargoMass;
    do {
      System.out.println("Input cargo mass (tons)");
      cargoMass = scanner.nextInt();
    } while (cargoMass < 0);
    Ship ship = new Ship(shipType, cargoMass, shipName);
    String content = converter.objectToJson(ship);
    writeToFile(directory + fileName, content);
  }

  public void createCrane(String fileName, String directory) throws IOException, CannotConvertObjectException {
    directory = checkOnSlach(directory);
    Scanner scanner = new Scanner(System.in);
    int craneNum;
    do {
      System.out.println("Input crane number:");
      craneNum = scanner.nextInt();
    } while (craneNum < 0);
    CargoTypes craneType = chooseType(scanner);
    int craneSpeed;
    do {
      System.out.println("Input unloading speed:");
      craneSpeed = scanner.nextInt();
    } while (craneSpeed < 0);
    Crane crane = new Crane(craneNum, craneType, craneSpeed);
    String content = converter.objectToJson(crane);
    writeToFile(directory + fileName, content);
  }

  public void createDeleteCraneCommand(String fileName, String directory) throws CannotConvertObjectException, IOException {
    directory = checkOnSlach(directory);
    Scanner scanner = new Scanner(System.in);
    int craneNum;
    do {
      System.out.println("Input crane number:");
      craneNum = scanner.nextInt();
    } while (craneNum < 0);
    DeleteCraneCommand deleteCraneCommand = new DeleteCraneCommand(craneNum);
    String content = converter.objectToJson(deleteCraneCommand);
    writeToFile(directory + fileName, content);
  }
}
