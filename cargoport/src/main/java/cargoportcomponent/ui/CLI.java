package cargoportcomponent.ui;

import libs.simulationattributes.Crane;
import libs.simulationattributes.Ship;

public class CLI implements UserInterface {
  @Override
  public void displayShipOnRoadstead(Ship ship) {
    System.out.println("Add ship: " + ship.getName() + " " + ship.getType() + " " + ship.getCargoMass());
  }

  @Override
  public void displayCrane(Crane crane) {
    System.out.println("Add crane: " + crane.getNumber() + " " + crane.getType() + " " + crane.getSpeed());
  }

  @Override
  public void displayCraneWork(Crane crane, Ship ship) {
    System.out.println("Unload: " + crane.getNumber() + " " + ship.getName() + " " + ship.getCargoMass());
  }

  @Override
  public void deleteCrane(Crane crane) {
    System.out.println("Delete crane: " + crane.getNumber() + " " + crane.getType() + " " + crane.getSpeed());
  }
}
