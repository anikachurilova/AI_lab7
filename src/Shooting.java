
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class Shooting {
    public static boolean isWumpusAlive = true;
    public static boolean arrowShot = false;

    public static int[][][] shoot(int AgentX, int AgentY, double[][][] knowledge, int[][][] map) {

        Queue<Integer> shoot = new LinkedList<>(); //used to check if the agent should try to shoot the wumpus

        int shootingX = 0; //where the arrow is being aimed
        int shootingY = 0;
        int count = 0;
        if (AgentX > 0) {
            shoot.add(AgentX - 1);
            shoot.add(AgentY);
            count++;
        }
        if (AgentX < 3) {
            shoot.add(AgentX + 1);
            shoot.add(AgentY);
            count++;
        }
        if (AgentY > 0) {
            shoot.add(AgentX);
            shoot.add(AgentY - 1);
            count++;
        }
        if (AgentY < 3) {
            shoot.add(AgentX);
            shoot.add(AgentY + 1);
            count++;
        }

        //Agent will shoot arrow if it knows where the Wumpus is, or if it has only 2 possible locations
        int allPossiblePlacesToShootAmount = 0;
        for (int a = 0; a < count; a++) {
            int x = shoot.remove();
            int y = shoot.remove();
            if (knowledge[x][y][1] == 1) {
                shootingX = x;
                shootingY = y;
                allPossiblePlacesToShootAmount++;
            } else if (knowledge[x][y][1] == 0.5) {
                shoot.add(x);
                shoot.add(y);
                allPossiblePlacesToShootAmount++;
            }
        }
        
        /*
        if there are 2 locations that each have a 0.5 chance of having a wumpus, pick one at random.
        The agent will now know where the wumpus is, regardless of whether or not it hits the wumpus.
        */
        if (allPossiblePlacesToShootAmount == 2) {

            Random r = new Random();
            int num = r.nextInt(2);
            if (num == 1) {
                shoot.remove();
                shoot.remove();
            }

            shootingX = shoot.remove();
            shootingY = shoot.remove();
        }

        if (allPossiblePlacesToShootAmount > 0) {
            arrowShot = true;

            System.out.println("The Agent is shooting to location " + shootingX);

            if (map[shootingX][shootingY][3] == 1) {
                System.out.println("SCREAM!!! The Wumpus has been killed!");
                isWumpusAlive = false;
            }

            if (map[shootingX][shootingY][3] == 0) { //if the agent does not hit the wumpus, it knows the Wumpus is in the second location
                System.out.println("OOPS!!!The agent hasn't heard a scream. It was a miss-shoot. Although, the Agent knows correctly where the Wumpus is");
            }

            for (int x = 0; x < 4; x++) {
                for (int y = 0; y < 4; y++) {
                    map[x][y][3] = 0;
                }
            }
        }
        return map;
    }
}