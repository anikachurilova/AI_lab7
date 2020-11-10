
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class Action {

    public static int[] action(int pointX, int pointY, double[][][] knowledge, boolean hasGold) {

        Random random = new Random();

        Queue<Integer> canMove = new LinkedList<>();
        int counter = 0;
        if (pointY > 0) {

            canMove.add(pointX);
            canMove.add(pointY - 1);
            counter++;
        }
        if (pointY < 3) {

            canMove.add(pointX);
            canMove.add(pointY + 1);
            counter++;
        }
        if (pointX > 0) {

            canMove.add(pointX - 1);
            canMove.add(pointY);
            counter++;
        }
        if (pointX < 3) {

            canMove.add(pointX + 1);
            canMove.add(pointY);
            counter++;
        }

        double smallestOdds = 2;
        for (int z = 0; z < counter; z++) {
            int a = canMove.remove();
            int b = canMove.remove();

            if ((knowledge[a][b][0] + knowledge[a][b][1]) < smallestOdds) {
                smallestOdds = knowledge[a][b][0] + knowledge[a][b][1];
            }
            canMove.add(a);
            canMove.add(b);
        }

        int counterMultiple = 0;

        for (int z = 0; z < counter; z++) {
            int a = canMove.remove();
            int b = canMove.remove();

            if ((knowledge[a][b][0] + knowledge[a][b][1]) == smallestOdds) {
                canMove.add(a);
                canMove.add(b);
                counterMultiple++;
            }
        }

        if (counterMultiple != 1) {
            int z = (random.nextInt(counterMultiple)) * 2;
            for (; z > 0; z--) {
                canMove.remove();
            }
            pointX = canMove.remove();
            pointY = canMove.remove();

        } else {
            pointX = canMove.remove();
            pointY = canMove.remove();
        }

        int[] result = new int[2];
        result[0] = pointX;
        result[1] = pointY;


        return result;
    }
}