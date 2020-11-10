public class Output {
    public static void output(int pointX, int pointY, double[][][] knowledge, int[][][] map) {
        System.out.println("------------------------------------------------------------------------");
        System.out.println("Current feelings in :" + "(" + pointX + ";" + pointY + ")");

        if (map[pointX][pointY][0] == 0 && map[pointX][pointY][1] == 0 && map[pointX][pointY][2] == 0) {
            System.out.println("Adjacent rooms are completely safe.");
        }
        if (map[pointX][pointY][1] == 1) {
            System.out.println("Agent sensor: breeze");
        }
        if (map[pointX][pointY][0] == 1) {
            System.out.println("Agent sensor: stench");
        }
        if (map[pointX][pointY][2] == 1) {
            System.out.println("Agent sensor: glitter - GOLD!");
        }
        System.out.println();
        System.out.println("---Predictions to fall in pit---");
        if (pointY > 0) {
            System.out.println("Up -> " + knowledge[pointX][pointY - 1][0]);
        }
        if (pointY < 3) {
            System.out.println("Down -> " + knowledge[pointX][pointY + 1][0]);
        }
        if (pointX > 0) {
            System.out.println("Left -> " + knowledge[pointX - 1][pointY][0]);
        }
        if (pointX < 3) {
            System.out.println("Right ->" + knowledge[pointX + 1][pointY][0]);
        }

        System.out.println();

        System.out.println("---Predictions of finding Wumpus---");
        if (pointY > 0) {
            System.out.println("Up -> " + knowledge[pointX][pointY - 1][1]);
        }
        if (pointY < 3) {
            System.out.println("Down -> " + knowledge[pointX][pointY + 1][1]);
        }
        if (pointX > 0) {
            System.out.println("Left ->" + knowledge[pointX - 1][pointY][1]);
        }
        if (pointX < 3) {
            System.out.println("Right ->" + knowledge[pointX + 1][pointY][1]);
        }


    }
}