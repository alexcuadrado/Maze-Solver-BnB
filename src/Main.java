import edu.salle.url.maze.Maze;
import edu.salle.url.maze.MazeBuilder;

public class Main {
    public static void main(String[] args) {
        Maze maze = new MazeBuilder()
                .setMazeColumns(100)
                .setMazeRows(100)
//                .setSeed(22)
                .setMazeSolver(new BranchAndBound())
                .buildDungeonMaze();
//                .buildCaveMaze();

        maze.run();
    }
}