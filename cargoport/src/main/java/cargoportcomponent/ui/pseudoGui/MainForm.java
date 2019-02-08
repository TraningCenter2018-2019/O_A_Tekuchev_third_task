package cargoportcomponent.ui.pseudoGui;

import cargoportcomponent.ui.UserInterface;
import cargoportcomponent.ui.pseudoGui.windows.MainWindow;
import com.googlecode.lanterna.gui2.MultiWindowTextGUI;
import com.googlecode.lanterna.gui2.dialogs.MessageDialogBuilder;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.swing.TerminalEmulatorAutoCloseTrigger;
import libs.simulationattributes.Crane;
import libs.simulationattributes.Ship;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class MainForm implements UserInterface {

  private final int COUNT_CRANES;
  private DefaultTerminalFactory terminalFactory;
  private Screen screen;
  private MainWindow mainWindow;
  private MultiWindowTextGUI textGui;

  private List<Ship> ships = new LinkedList<>();

  public MainForm(int countCranes) {
    COUNT_CRANES = countCranes;
  }

  @Override
  public void show() throws IOException {
    terminalFactory = new DefaultTerminalFactory();
    terminalFactory.setTerminalEmulatorFrameAutoCloseTrigger(TerminalEmulatorAutoCloseTrigger.CloseOnExitPrivateMode);
    screen = terminalFactory.createScreen();
    mainWindow = new MainWindow(COUNT_CRANES);
    screen.startScreen();
    screen.doResizeIfNecessary();
    textGui = new MultiWindowTextGUI(screen);
    textGui.addWindowAndWait(mainWindow);
  }

  @Override
  public void addShip(Ship ship) {
    ships.add(ship);
    mainWindow.setShips(ships);
  }

  @Override
  public void deleteShip(Ship ship) {
    ships.remove(ship);
    mainWindow.setShips(ships);
  }

  @Override
  public void addCrane(Crane crane) {
    mainWindow.setCrane(crane);
  }

  @Override
  public void displayCraneWork(Crane crane, Ship ship, int initMass) {
    mainWindow.addUnloadingStatistic(crane, ship, initMass);
  }

  @Override
  public void deleteCrane(Crane crane) {
    mainWindow.deleteCrane(crane);
  }

  @Override
  public void showWarning(String message) {
    new MessageDialogBuilder()
            .setTitle("Warning")
            .setText(message)
            .build()
            .showDialog(textGui);
  }
}
