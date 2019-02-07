package libs.simulationattributes;

import java.io.Serializable;

public class Ship implements Serializable {
  private CargoTypes type;
  private int cargoMass;
  private String name;

  public Ship(CargoTypes aType, int aCargoMass, String aName) {
    type = aType;
    cargoMass = aCargoMass;
    name = aName;
  }

  public CargoTypes getType() {
    return type;
  }

  public int getCargoMass() {
    return cargoMass;
  }

  public void setCargoMass(int value) {
    cargoMass = value;
  }

  public String getName() {
    return name;
  }

  public boolean equalShip(Ship ship) {
    return type == ship.getType() &&
            cargoMass == ship.getCargoMass() &&
            name.equals(ship.getName());
  }
}
