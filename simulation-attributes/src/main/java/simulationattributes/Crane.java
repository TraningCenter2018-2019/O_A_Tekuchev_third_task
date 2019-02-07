package simulationattributes;

import java.io.Serializable;

public class Crane implements Serializable {
  private int number;
  private CargoTypes type;
  private int speed;

  public Crane(int aNum, CargoTypes aType, int aSpeed) {
    number = aNum;
    type = aType;
    speed = aSpeed;
  }

  public int getNumber() {
    return number;
  }

  public CargoTypes getType() {
    return type;
  }

  public int getSpeed() {
    return speed;
  }
}
