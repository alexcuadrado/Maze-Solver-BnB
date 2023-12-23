import edu.salle.url.maze.business.MazeSolver;
import edu.salle.url.maze.business.enums.Cell;
import edu.salle.url.maze.business.enums.Direction;
import edu.salle.url.maze.presentation.MazeRenderer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;

public class BranchAndBound implements MazeSolver {
    @Override
    public List<Direction> solve(Cell[][] cells, MazeRenderer mazeRenderer) {
        long startTime = System.currentTimeMillis();

        final int columns = cells[0].length;
        final int rows = cells.length;
        int[] start = new int[2];
        int[] exit = new int[2];

        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {
                switch (cells[row][column]) {
                    case Cell.START:
                        start[0] = row;
                        start[1] = column;
                        break;
                    case Cell.EXIT:
                        exit[0] = row;
                        exit[1] = column;
                        break;
                }
            }
        }

        HashMap<List<Integer>, Integer> visitedCells = new HashMap<>();
        int best = Integer.MAX_VALUE;
        List<Direction> bestSuccessorDirections = new ArrayList<>();

        PriorityQueue<MazeConfig> queue = new PriorityQueue<>();

        MazeConfig first = new MazeConfig(cells, start, exit);
        queue.offer(first);

        while (!queue.isEmpty()) {
            MazeConfig config = queue.poll();

            List<MazeConfig> successors = config.expand(mazeRenderer, visitedCells);

            for (MazeConfig successor: successors) {
                if (!successor.foundExit()) {
                    if (successor.estimation() < best) {
                        queue.offer(successor);
                    }
                    continue;
                }
                if (successor.estimation() < best) {
                    best = successor.cost();
//                    System.out.println("BEST: " + best);
                    bestSuccessorDirections = successor.getDirections();
                }
            }
        }

        mazeRenderer.render(cells, bestSuccessorDirections);

        long endTime = System.currentTimeMillis();
        long duration = (endTime - startTime);
        System.out.println("Duration: " + duration + "ms - Best: " + best);

        return bestSuccessorDirections;
    }
}
