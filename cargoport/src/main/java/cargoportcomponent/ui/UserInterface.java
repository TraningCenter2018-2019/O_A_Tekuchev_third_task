package cargoportcomponent.ui;

import libs.simulationattributes.Crane;
import libs.simulationattributes.Ship;

import java.io.IOException;

/**
 * The user interface to display work
 */
public interface UserInterface {
  void addShip(Ship ship);

  void deleteShip(Ship ship);

  void addCrane(Crane crane);

  void displayCraneWork(Crane crane, Ship ship, int initMass);

  void deleteCrane(Crane crane);

  void showWarning(String message);

  void show() throws IOException;
}
