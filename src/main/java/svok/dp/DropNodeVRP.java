/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package svok.dp;

import com.google.ortools.Loader;
import com.google.ortools.constraintsolver.Assignment;
import com.google.ortools.constraintsolver.FirstSolutionStrategy;
import com.google.ortools.constraintsolver.LocalSearchMetaheuristic;
import com.google.ortools.constraintsolver.RoutingIndexManager;
import com.google.ortools.constraintsolver.RoutingModel;
import com.google.ortools.constraintsolver.RoutingSearchParameters;
import com.google.ortools.constraintsolver.main;
import com.google.protobuf.Duration;

/**
 *
 * @author svok
 */
public class DropNodeVRP {

  static class DataModel {
    public final long[][] distanceMatrix = {
        {0, 548, 776, 696, 582, 274, 502, 194, 308, 194, 536, 502, 388, 354, 468, 776, 662},
        {548, 0, 684, 308, 194, 502, 730, 354, 696, 742, 1084, 594, 480, 674, 1016, 868, 1210},
        {776, 684, 0, 992, 878, 502, 274, 810, 468, 742, 400, 1278, 1164, 1130, 788, 1552, 754},
        {696, 308, 992, 0, 114, 650, 878, 502, 844, 890, 1232, 514, 628, 822, 1164, 560, 1358},
        {582, 194, 878, 114, 0, 536, 764, 388, 730, 776, 1118, 400, 514, 708, 1050, 674, 1244},
        {274, 502, 502, 650, 536, 0, 228, 308, 194, 240, 582, 776, 662, 628, 514, 1050, 708},
        {502, 730, 274, 878, 764, 228, 0, 536, 194, 468, 354, 1004, 890, 856, 514, 1278, 480},
        {194, 354, 810, 502, 388, 308, 536, 0, 342, 388, 730, 468, 354, 320, 662, 742, 856},
        {308, 696, 468, 844, 730, 194, 194, 342, 0, 274, 388, 810, 696, 662, 320, 1084, 514},
        {194, 742, 742, 890, 776, 240, 468, 388, 274, 0, 342, 536, 422, 388, 274, 810, 468},
        {536, 1084, 400, 1232, 1118, 582, 354, 730, 388, 342, 0, 878, 764, 730, 388, 1152, 354},
        {502, 594, 1278, 514, 400, 776, 1004, 468, 810, 536, 878, 0, 114, 308, 650, 274, 844},
        {388, 480, 1164, 628, 514, 662, 890, 354, 696, 422, 764, 114, 0, 194, 536, 388, 730},
        {354, 674, 1130, 822, 708, 628, 856, 320, 662, 388, 730, 308, 194, 0, 342, 422, 536},
        {468, 1016, 788, 1164, 1050, 514, 514, 662, 320, 274, 388, 650, 536, 342, 0, 764, 194},
        {776, 868, 1552, 560, 674, 1050, 1278, 742, 1084, 810, 1152, 274, 388, 422, 764, 0, 798},
        {662, 1210, 754, 1358, 1244, 708, 480, 856, 514, 468, 354, 844, 730, 536, 194, 798, 0},
    };
    public final long[] demands = {0, 1, 1, 3, 6, 3, 6, 8, 8, 1, 2, 1, 2, 6, 6, 8, 8};
    public final long[] vehicleCapacities = {15, 15, 15, 15};
    public final int vehicleNumber = 4;
    public final int depot = 0;
  }

  /// @brief Print the solution.
  static void printSolution(
      DataModel data, RoutingModel routing, RoutingIndexManager manager, Assignment solution) {
    // Solution cost.
    Presenter.println(Message.objective + solution.objectiveValue());
    // Inspect solution.
    // Display dropped nodes.
    String droppedNodes = Message.droppedNodes;
    for (int node = 0; node < routing.size(); ++node) {
      if (routing.isStart(node) || routing.isEnd(node)) {
        continue;
      }
      if (solution.value(routing.nextVar(node)) == node) {
        droppedNodes += " " + manager.indexToNode(node);
      }
    }
    Presenter.println(Message.droppedNodes);
    // Display routes
    long totalDistance = 0;
    long totalLoad = 0;
    for (int i = 0; i < data.vehicleNumber; ++i) {
      long index = routing.start(i);
      Presenter.println(Message.routeForVehicle + i + ":");
      long routeDistance = 0;
      long routeLoad = 0;
      String route = "";
      while (!routing.isEnd(index)) {
        long nodeIndex = manager.indexToNode(index);
        routeLoad += data.demands[(int) nodeIndex];
        route += nodeIndex + " "+ Message.load + "(" + routeLoad + ") -> ";
        long previousIndex = index;
        index = solution.value(routing.nextVar(index));
        routeDistance += routing.getArcCostForVehicle(previousIndex, index, i);
      }
      route += manager.indexToNode(routing.end(i));
      Presenter.println(route);
      Presenter.println(Message.distanceOfRoute + routeDistance + "m");
      totalDistance += routeDistance;
      totalLoad += routeLoad;
    }
    Presenter.println(Message.totalDistanceOfRoutes + totalDistance + "m");
    Presenter.println(Message.totalLoadOfRoutes + totalLoad);
  }

  public void run() throws Exception {
    Loader.loadNativeLibraries();
    // Instantiate the data problem.
    final DataModel data = new DataModel();

    // Create Routing Index Manager
    RoutingIndexManager manager =
        new RoutingIndexManager(data.distanceMatrix.length, data.vehicleNumber, data.depot);

    // Create Routing Model.
    RoutingModel routing = new RoutingModel(manager);

    // Create and register a transit callback.
    final int transitCallbackIndex =
        routing.registerTransitCallback((long fromIndex, long toIndex) -> {
          // Convert from routing variable Index to user NodeIndex.
          int fromNode = manager.indexToNode(fromIndex);
          int toNode = manager.indexToNode(toIndex);
          return data.distanceMatrix[fromNode][toNode];
        });

    // Define cost of each arc.
    routing.setArcCostEvaluatorOfAllVehicles(transitCallbackIndex);

    // Add Capacity constraint.
    final int demandCallbackIndex = routing.registerUnaryTransitCallback((long fromIndex) -> {
      // Convert from routing variable Index to user NodeIndex.
      int fromNode = manager.indexToNode(fromIndex);
      return data.demands[fromNode];
    });
    routing.addDimensionWithVehicleCapacity(demandCallbackIndex, 0, // null capacity slack
        data.vehicleCapacities, // vehicle maximum capacities
        true, // start cumul to zero
        "Capacity");
    // Allow to drop nodes.
    long penalty = 1000;
    for (int i = 1; i < data.distanceMatrix.length; ++i) {
      routing.addDisjunction(new long[] {manager.nodeToIndex(i)}, penalty);
    }

    // Setting first solution heuristic.
    RoutingSearchParameters searchParameters =
        main.defaultRoutingSearchParameters()
            .toBuilder()
            .setFirstSolutionStrategy(FirstSolutionStrategy.Value.PATH_CHEAPEST_ARC)
            .setLocalSearchMetaheuristic(LocalSearchMetaheuristic.Value.GUIDED_LOCAL_SEARCH)
            .setTimeLimit(Duration.newBuilder().setSeconds(1).build())
            .build();

    // Solve the problem.
    Assignment solution = routing.solveWithParameters(searchParameters);

    // Print solution on console.
    printSolution(data, routing, manager, solution);
  }
}