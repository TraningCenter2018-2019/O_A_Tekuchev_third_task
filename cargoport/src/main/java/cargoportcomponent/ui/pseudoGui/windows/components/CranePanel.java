package cargoportcomponent.ui.pseudoGui.windows.components;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.Borders;
import com.googlecode.lanterna.gui2.EmptySpace;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.TextBox;
import libs.simulationattributes.Crane;
import libs.simulationattributes.Ship;

public class CranePanel {
  private Panel panel;
  private Crane crane;
  private Ship spip;
  private TextBox craneTxtBx;
  private TextBox shipTxtBx;
  private DisplayShip displayShip;
  private DisplayCrane displayCrane;

  public CranePanel(int width, DisplayShip ds, DisplayCrane dc) {
    displayCrane = dc;
    displayShip = ds;
    panel = new Panel();
    TerminalSize ts = new TerminalSize(width,1);
    craneTxtBx = new TextBox(ts);
    craneTxtBx.setReadOnly(true);
    shipTxtBx = new TextBox(ts);
    shipTxtBx.setReadOnly(true);
    panel.addComponent(craneTxtBx.withBorder(Borders.singleLine("Crane")));
    panel.addComponent(shipTxtBx.withBorder(Borders.singleLine("Ship")));
    panel.addComponent(new EmptySpace(ts));
  }

  public Panel getPanel() {
    return panel;
  }

  public Crane getCrane() {
    return crane;
  }

  public void setCrane(Crane crane) {
    this.crane = crane;
    if (crane == null) {
      craneTxtBx.setText("");
    }
    else {
      craneTxtBx.setText(displayCrane.display(crane));
    }
  }

  public Ship getShip() {
    return spip;
  }

  public void setShip(Ship spip) {
    this.spip = spip;
    if (spip == null) {
      shipTxtBx.setText("");
    }
    else {
      shipTxtBx.setText(displayShip.display(spip));
    }
  }
}