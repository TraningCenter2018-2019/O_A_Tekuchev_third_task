package libs.simulationattributes;

import java.io.Serializable;

public class DeleteCraneCommand implements Serializable {
  private int craneNumber;

  public DeleteCraneCommand(int aCraneNumber) {
    craneNumber = aCraneNumber;
  }

  public int getCraneNumber() {
    return craneNumber;
  }
}
