package cargoportcomponent.ui.pseudoGui.windows;

import cargoportcomponent.ui.pseudoGui.windows.components.CranePanel;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.*;
import libs.simulationattributes.Crane;
import libs.simulationattributes.Ship;

import java.util.Arrays;
import java.util.List;

public class MainWindow extends BasicWindow {
  static private final int CRANE_PANEL_WIDTH = 30;
  static private final int SHIPS_PANEL_HEIGHT = 18;
  static private final int STAT_PANEL_HEIGHT = 15;
  static private final int STAT_PANEL_WIDTH = 50;

  private TextBox shipTextBox;
  private TextBox statTextBox;
  private CranePanel[] cranePanels;
  private final int COUNT_CRANES;

  public MainWindow(int countCranes) {
    COUNT_CRANES = countCranes;
    setHints(Arrays.asList(Hint.FULL_SCREEN));

    Panel mainPanel = new Panel();
    mainPanel.setLayoutManager(new LinearLayout(Direction.HORIZONTAL));

    Panel unlodingPanel = new Panel();
    Panel cranePanel = new Panel(new LinearLayout(Direction.HORIZONTAL));
    cranePanels = createCranePanels(COUNT_CRANES);
    for (CranePanel cp : cranePanels) {
      cranePanel.addComponent(cp.getPanel());
    }
    Panel shipPanel = new Panel();
    shipTextBox = new TextBox(new TerminalSize(CRANE_PANEL_WIDTH  * COUNT_CRANES, SHIPS_PANEL_HEIGHT));
    shipTextBox.setReadOnly(true);
    shipPanel.addComponent(shipTextBox.withBorder(Borders.singleLine("Ships on roadstead")));

    unlodingPanel.addComponent(cranePanel);
    unlodingPanel.addComponent(shipPanel);


    Panel statPanel = new Panel();
    statTextBox = new TextBox(new TerminalSize(STAT_PANEL_WIDTH, STAT_PANEL_HEIGHT));
    statTextBox.setReadOnly(true);
    statPanel.addComponent(statTextBox.withBorder(Borders.singleLine("Statistic")));
    Button clearBtn = new Button("clear");
    clearBtn.addListener(btn -> statTextBox.setText(""));
    statPanel.addComponent(clearBtn);

    mainPanel.addComponent(unlodingPanel);
    mainPanel.addComponent(statPanel);

    setComponent(mainPanel);
  }

  private CranePanel[] createCranePanels(int count) {
    CranePanel[] panels = new CranePanel[count];
    for (int i = 0; i < count; ++i) {
      panels[i] = new CranePanel(CRANE_PANEL_WIDTH, this::displayShip, this::displayCrane);
    }
    return panels;
  }

  private CranePanel getSpareCranePlace() {
    for (CranePanel cp : cranePanels) {
      if (cp.getCrane() == null) {
        return cp;
      }
    }
    return null;
  }

  private CranePanel findByCrane(Crane crane) {
    for (CranePanel cp : cranePanels) {
      if (cp.getCrane() == crane) {
        return cp;
      }
    }
    return null;
  }

  private String displayShip(Ship ship) {
    return "<(\"" + ship.getName() + "\" " + ship.getType() + " " + ship.getCargoMass() + " t)";
  }

  private String displayCrane(Crane crane) {
    return "/#" + crane.getNumber() + " " + crane.getType() + " " + crane.getSpeed() + " t/h \\";
  }

  private String displayWork(Crane crane, Ship ship, int initMass) {
    return "Unload: Crane #" + crane.getNumber() + " -> Ship: \"" + ship.getName() + "\" " +
            ship.getCargoMass() + "/" + initMass;
  }

  public void setShips(List<Ship> ships) {
    shipTextBox.setText("");
    int i = 0;
    StringBuilder builder = new StringBuilder();
    for (Ship ship : ships) {
      if (i++ < COUNT_CRANES) {
        builder.append(displayShip(ship) + "  ");
      }
      else {
        shipTextBox.addLine(builder.toString());
        i = 0;
        builder = new StringBuilder();
      }
    }
    shipTextBox.addLine(builder.toString());
  }

  public void setCrane(Crane crane) {
    CranePanel sparePlace = getSpareCranePlace();
    sparePlace.setCrane(crane);
  }

  public void deleteCrane(Crane crane) {
    CranePanel cp = findByCrane(crane);
    cp.setCrane(null);
    cp.setShip(null);
  }

  public void addUnloadingStatistic(Crane crane, Ship ship, int initMass) {
    CranePanel cp = findByCrane(crane);
    statTextBox.addLine(displayWork(crane, ship, initMass));
    if (ship.getCargoMass() == 0) {
      cp.setShip(null);
      statTextBox.addLine("Unloading is ended");
    }
    else {
      cp.setShip(ship);
    }
  }
}
