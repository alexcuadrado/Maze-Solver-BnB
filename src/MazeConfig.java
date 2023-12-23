import edu.salle.url.maze.business.enums.Cell;
import edu.salle.url.maze.business.enums.Direction;
import edu.salle.url.maze.presentation.MazeRenderer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public final class MazeConfig implements Comparable<MazeConfig> {
    private final Cell[][] cells;
    private final int currentRow;
    private final int currentColumn;
    private final int[] start;
    private final int[] exit;
    private final List<Direction> directions;

    public MazeConfig(Cell[][] cells, int[] start, int[] exit) {
        this.cells = cells;
        currentRow = start[0];
        currentColumn = start[1];
        this.start = start;
        this.exit = exit;

        directions = new ArrayList<>();
    }

    private MazeConfig(MazeConfig that, int nextRow, int nextColumn, Direction nextDirection) {
        this.cells = that.cells;
        this.currentRow = nextRow;
        this.currentColumn = nextColumn;
        this.start = that.start;
        this.exit = that.exit;

        this.directions = new ArrayList<>();
        this.directions.addAll(that.directions);

        // Add the new visited direction
        this.directions.add(nextDirection);


    }

    public List<MazeConfig> expand(MazeRenderer mazeRenderer, HashMap<List<Integer>, Integer> visitedCells) {
        List<MazeConfig> successors = new ArrayList<>();
        Direction[] directions = { Direction.LEFT, Direction.UP, Direction.RIGHT, Direction.DOWN };

        for (int i = 0; i < 4; i++) {
            Cell nextCell;
            int nextRow = currentRow;
            int nextColumn = currentColumn;
            switch (i) {
                case 0:
                    // Left
                    nextColumn = currentColumn - 1;
                    break;
                case 1:
                    // Up
                    nextRow = currentRow - 1;
                    break;
                case 2:
                    // Right
                    nextColumn = currentColumn + 1;
                    break;
                case 3:
                    // Down
                    nextRow = currentRow + 1;
                    break;
            }
            nextCell = cells[nextRow][nextColumn];

            if (nextCell == Cell.WALL || visitedCells.containsKey(List.of(nextRow, nextColumn)) && visitedCells.get(List.of(nextRow, nextColumn)) <= cost() + 1) {
                continue;
            }

            MazeConfig successor = new MazeConfig(this, nextRow, nextColumn, directions[i]);

//            mazeRenderer.render(cells, successor.getDirections(), 70);

            successors.add(successor);

            visitedCells.put(List.of(successor.currentRow, successor.currentColumn), successor.cost());
        }
        return successors;
    }

    public boolean foundExit() {
//        return currentRow == exit[0] && currentColumn == exit[1];
        if (currentRow == exit[0] && currentColumn == exit[1]) {
            System.out.println("FOUND EXIT " + cost());
            return true;
        }
        return false;
    }

    public int cost() {
        return directions.size();
    }

    public int estimation() {
        return Math.abs(currentRow - exit[0]) + Math.abs(currentColumn - exit[1]) + cost();
    }

    @Override
    public int compareTo(MazeConfig that) {
        return this.estimation() - that.estimation();
    }

    public String toString() {
        return "Length " + cost() + " - CurrentRow " + currentRow + " - CurrentColumn " + currentColumn + " - directions " + directions;
    }

    public List<Direction> getDirections() {
        return directions;
    }
}
