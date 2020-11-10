import java.util.LinkedList;
import java.util.Queue;

public class Wumpus {
    public static double[][][] wumpus(double[][][] knowledge, int[][][] map, int x, int y, int stench) {

        if (stench == 1) { //in case agent smells a stench
            Queue<Integer> w = new LinkedList<>();

            int counter = 0;
            if(x > 0 && knowledge[x-1][y][1] != 0) {
                w.add(x - 1);
                w.add(y);
                counter++;
            }
            if(x < 3 && knowledge[x+1][y][1] != 0) {
                w.add(x + 1);
                w.add(y);
                counter++;
            }
            if(y > 0  && knowledge[x][y-1][1] != 0) {
                w.add(x);
                w.add(y - 1);
                counter++;
            }
            if(y < 3  && knowledge[x][y+1][1] != 0) {
                w.add(x);
                w.add(y + 1);
                counter++;
            }


            while (w.peek() != null) {
                int a = w.remove();
                int b = w.remove();
                knowledge[a][b][1] = (double) 1 / counter;
            }


            for (x = 0; x < 4; x++) {
                for (y = 0; y < 4; y++) {
                    if (knowledge[x][y][1] < (double) 1 / counter) {
                        knowledge[x][y][1] = 0;
                    }
                }
            }
        } else {

            if (x > 0) {
                knowledge[x - 1][y][1] = 0;
            }
            if (x < 3) {
                knowledge[x + 1][y][1] = 0;
            }
            if (y > 0) {
                knowledge[x][y - 1][1] = 0;
            }
            if (y < 3) {
                knowledge[x][y + 1][1] = 0;
            }
        }

        return knowledge;
    }
}