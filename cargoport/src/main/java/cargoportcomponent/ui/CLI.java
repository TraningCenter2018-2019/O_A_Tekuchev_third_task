package cargoportcomponent.ui;

import libs.simulationattributes.Crane;
import libs.simulationattributes.Ship;

import java.io.IOException;

public class CLI implements UserInterface {
  @Override
  public void addShip(Ship ship) {
    System.out.println("Add ship: " + ship.getName() + " " + ship.getType() + " " + ship.getCargoMass());
  }

  @Override
  public void deleteShip(Ship ship) {

  }

  @Override
  public void addCrane(Crane crane) {
    System.out.println("Add crane: " + crane.getNumber() + " " + crane.getType() + " " + crane.getSpeed());
  }

  @Override
  public void displayCraneWork(Crane crane, Ship ship, int initMass) {
    System.out.println("Unload: " + crane.getNumber() + " " + ship.getName() + " " + ship.getCargoMass());
  }

  @Override
  public void deleteCrane(Crane crane) {
    System.out.println("Delete crane: " + crane.getNumber() + " " + crane.getType() + " " + crane.getSpeed());
  }

  @Override
  public void showWarning(String message) {
    System.out.println("Warning: " + message);
  }

  @Override
  public void show() throws IOException {

  }
}
