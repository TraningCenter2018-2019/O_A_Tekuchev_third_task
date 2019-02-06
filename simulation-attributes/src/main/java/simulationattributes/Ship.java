package simulationattributes;

import java.io.Serializable;

public class Ship implements Serializable {
  private ShipTypes type;
  private int cargoMass;

  public Ship(ShipTypes aType, int aCargoMass) {
    type = aType;
    cargoMass = aCargoMass;
  }

  public ShipTypes getType() {
    return type;
  }

  public int getCargoMass() {
    return cargoMass;
  }
}
