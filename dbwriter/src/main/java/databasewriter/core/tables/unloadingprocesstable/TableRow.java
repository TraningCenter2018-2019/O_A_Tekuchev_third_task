package databasewriter.core.tables.unloadingprocesstable;

import java.io.Serializable;

/**
 * Represent a table row
 */
public class TableRow implements Serializable {
  private int id;
  private int craneNumber;
  private String shipName;
  private int initialMass;
  private int currentMass;

  public TableRow() { }

  /**
   * A constructor
   *
   * @param anId the row id
   * @param aCraneNum the crane number
   * @param aShipName the ship name
   * @param anInitMass the initial cargo mass
   * @param aCurrMass the current cargo mass
   */
  public TableRow(int anId, int aCraneNum, String aShipName, int anInitMass, int aCurrMass) {
    id = anId;
    craneNumber = aCraneNum;
    shipName = aShipName;
    initialMass = anInitMass;
    currentMass = aCurrMass;
  }

  public TableRow(int aCraneNum, String aShipName, int anInitMass, int aCurrMass) {
    this(0, aCraneNum, aShipName, anInitMass, aCurrMass);
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public int getCraneNumber() {
    return craneNumber;
  }

  public void setCraneNumber(int craneNumber) {
    this.craneNumber = craneNumber;
  }

  public String getShipName() {
    return shipName;
  }

  public void setShipName(String shipName) {
    this.shipName = shipName;
  }

  public int getInitialMass() {
    return initialMass;
  }

  public void setInitialMass(int initialMass) {
    this.initialMass = initialMass;
  }

  public int getCurrentMass() {
    return currentMass;
  }

  public void setCurrentMass(int currentMass) {
    this.currentMass = currentMass;
  }

  /**
   * Equals rows by its content
   *
   * @param row the row to equal
   * @return true if they are equal
   */
  public boolean equalRows(TableRow row) {
    return row != null && id == row.getId() &&
            craneNumber == row.getCraneNumber() &&
            shipName.equals(row.getShipName()) &&
            initialMass == row.getInitialMass() &&
            currentMass == row.getCurrentMass();
  }

  /**
   * Equals rows by its content without id
   *
   * @param row the row to equal
   * @return true if they are equal
   */
  public boolean equalRowsWithoutId(TableRow row) {
    return row != null && craneNumber == row.getCraneNumber() &&
            shipName.equals(row.getShipName()) &&
            initialMass == row.getInitialMass() &&
            currentMass == row.getCurrentMass();
  }
}
