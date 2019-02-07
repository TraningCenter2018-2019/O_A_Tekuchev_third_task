import cargoportcomponent.core.CargoPort;
import org.junit.Before;
import org.junit.Test;
import libs.simulationattributes.Crane;
import libs.simulationattributes.Ship;
import libs.simulationattributes.CargoTypes;
import cargoportcomponent.ui.UserInterface;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Random;

public class TestShipDisplay {
  private Random random;
  private CargoPort cargoPort;
  private Ship transferedShip;

  private String generateString(Random random, int length) {
    final String SOURCES =
            "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890";
    char[] text = new char[length];
    for (int i = 0; i < length; ++i) {
      text[i] = SOURCES.charAt(random.nextInt(SOURCES.length()));
    }
    return new String(text);
  }

  private Ship getRandomShip() {
    int mass = random.nextInt(500);
    CargoTypes type = CargoTypes.values()[random.nextInt(3)];
    return new Ship(type, mass, generateString(random, 6));
  }

  @Before
  public void initPort() {
    random = new Random();
    cargoPort = new CargoPort(3, new UserInterface() {
      @Override
      public void displayShipOnRoadstead(Ship ship) {
        assertTrue(ship.equalShip(transferedShip));
        cargoPort.stopPort();
      }

      @Override
      public void displayCrane(Crane crane) {

      }

      @Override
      public void displayCraneWork(Crane crane, Ship ship) {

      }
    });
  }

  @Test
  public void testDisplay() throws InterruptedException {
    cargoPort.startPort();
    transferedShip = getRandomShip();
    cargoPort.putShipToRoadstead(transferedShip);
    cargoPort.join();
  }
}
