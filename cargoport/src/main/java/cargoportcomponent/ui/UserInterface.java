package cargoportcomponent.ui;

import libs.simulationattributes.Crane;
import libs.simulationattributes.Ship;

public interface UserInterface {
  void displayShipOnRoadstead(Ship ship);

  void displayCrane(Crane crane);

  void displayCraneWork(Crane crane, Ship ship);
}
