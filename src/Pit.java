import java.util.LinkedList;
import java.util.Queue;

public class Pit {

    public static double[][][] pit(double[][][] knowledge, int[][][] map, int x, int y, int breeze) {

        if (breeze != 1) {
            if (x > 0) {
                knowledge[x - 1][y][0] = 0;
            }
            if (x < 3) {
                knowledge[x + 1][y][0] = 0;
            }
            if (y > 0) {
                knowledge[x][y - 1][0] = 0;
            }
            if (y < 3) {
                knowledge[x][y + 1][0] = 0;
            }

        } else {


            Queue<Integer> q = new LinkedList<>();

            int count = 0;
            if (knowledge[x - 1][y][0] != 0 && (x > 0)) {
                q.add(x - 1);
                q.add(y);
                count++;
            }
            if (knowledge[x + 1][y][0] != 0 && (x < 3)) {
                q.add(x + 1);
                q.add(y);
                count++;
            }
            if (knowledge[x][y - 1][0] != 0 && (y > 0)) {
                q.add(x);
                q.add(y - 1);
                count++;
            }
            if (knowledge[x][y + 1][0] != 0 && (y < 3)) {
                q.add(x);
                q.add(y + 1);
                count++;
            }

            while (q.peek() != null) {
                int a = q.remove();
                int b = q.remove();
                knowledge[a][b][0] = (double) 1 / count;
            }
        }

        return knowledge;
    }
}