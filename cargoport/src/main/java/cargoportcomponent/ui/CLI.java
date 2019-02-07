package cargoportcomponent.ui;

import libs.simulationattributes.Crane;
import libs.simulationattributes.Ship;

public class CLI implements UserInterface {
  @Override
  public void displayShipOnRoadstead(Ship ship) {
    System.out.println("Ship: " + ship.getName() + " " + ship.getType() + " " + ship.getCargoMass());
  }

  @Override
  public void displayCrane(Crane crane) {
    System.out.println("Crane: " + crane.getNumber() + " " + crane.getType() + " " + crane.getSpeed());
  }

  @Override
  public void displayCraneWork(Crane crane, Ship ship) {
    System.out.println("Work: " + crane.getNumber() + " " + ship.getName() + " " + ship.getCargoMass());
  }
}
