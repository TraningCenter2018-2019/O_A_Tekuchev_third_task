package cargoportcomponent.ui;

import libs.simulationattributes.Crane;
import libs.simulationattributes.Ship;

import java.io.IOException;

/**
 * The user interface to display work
 */
public interface UserInterface {
  /**
   * Displays new ship
   *
   * @param ship the ship
   */
  void addShip(Ship ship);

  /**
   * Deletes ship
   *
   * @param ship the ship
   */
  void deleteShip(Ship ship);

  /**
   * Displays new crane
   *
   * @param crane the crane
   */
  void addCrane(Crane crane);

  /**
   * Displays the unloading process
   *
   * @param crane the crane that unloads
   * @param ship the unloaded ship
   * @param initMass the initial cargo mass
   */
  void displayCraneWork(Crane crane, Ship ship, int initMass);

  /**
   * Deletes a crane
   *
   * @param crane the crane
   */
  void deleteCrane(Crane crane);

  /**
   * Shows warning
   *
   * @param message the text
   */
  void showWarning(String message);

  /**
   * Starts to display UI, must stop the program execution
   *
   * @throws IOException
   */
  void startUi() throws IOException;
}
