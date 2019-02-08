package cargoportcomponent.ui.pseudoGui.windows.components;

import libs.simulationattributes.Ship;

@FunctionalInterface
public interface DisplayShip {
  String display(Ship ship);
}
