
import java.util.*;

public class Main {
    public static void main(String[] args) {
        Random r = new Random();

        int[][][] mapMatrix = new int[4][4][5]; //create a 4X4 grid, where each point on the grid has 5 pieces of data
        /*
        The Wumpus, pits, and gold will be randomly placed.
        Note that in about 21% of the possible environments, it is impossible for the agent to reach the gold

        The 5 pieces of data for each coordinate represent pecpects the agent will percieve
        The 5 percepts are: Stench, Breeze, Glitter, being killed by a Wumpus, and falling in a pit
        */

        int agentX = 0, agentY = 3; //agent starts in bottom left corner of mapMatrix

        //randomly place Wumpus
        int wumpusX = r.nextInt(4);
        int wumpusY = r.nextInt(4);
        while (wumpusX == 0 && wumpusY == 3) { //Wumpus cannot be in agent's starting position
            wumpusX = r.nextInt(4);
            wumpusY = r.nextInt(4);
        }
        mapMatrix[wumpusX][wumpusY][3] = 1;

        //randomly place gold
        int goldX = r.nextInt(4);
        int goldY = r.nextInt(4);
        while ((goldX == 0 && goldY == 3) | (goldX == wumpusX && goldY == wumpusY)) {
            goldX = r.nextInt(4);
            goldY = r.nextInt(4);
        }
        //Gold cannot be in agent's starting position, or where the Wumpus is

        mapMatrix[goldX][goldY][2] = 1;

        /*
        Randomly add pits. Each location has a 0.2 chance of having a pit, except for location of
        Wumpus, location of gold, and agent's starting location
        */
        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 4; y++) {
                if ((!(x == agentX && y == agentY)) && (!(x == wumpusX && y == wumpusY)) && (!(x == goldX && y == goldY))) {
                    int pit = r.nextInt(10);
                    if (pit < 2) {
                        mapMatrix[x][y][4] = 1;
                    }
                }
            }
        }

        //add a stench to squares directly adjacent to Wumpus
        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 4; y++) {
                if (x == wumpusX && y == wumpusY) {
                    if (wumpusX > 0) {
                        mapMatrix[x - 1][y][0] = 1;
                    }
                    if (wumpusX < 3) {
                        mapMatrix[x + 1][y][0] = 1;
                    }
                    if (wumpusY > 0) {
                        mapMatrix[x][y - 1][0] = 1;
                    }
                    if (wumpusY < 3) {
                        mapMatrix[x][y + 1][0] = 1;
                    }
                }

            }
        }
        //add a breeze to squares directly adjacent to pits
        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 4; y++) {
                if (mapMatrix[x][y][4] == 1) {
                    if (x > 0) {
                        mapMatrix[x - 1][y][1] = 1;
                    }
                    if (x < 3) {
                        mapMatrix[x + 1][y][1] = 1;
                    }
                    if (y > 0) {
                        mapMatrix[x][y - 1][1] = 1;
                    }
                    if (y < 3) {
                        mapMatrix[x][y + 1][1] = 1;
                    }
                }

            }
        }

        //print the starting Wumpus Environment
        System.out.print("    0   1   2   3");
        for (int y = 0; y < 4; y++) {
            System.out.print("\n" + y + " |");
            for (int x = 0; x < 4; x++) {
                if (x == agentX && y == agentY) {
                    System.out.print(" A ");
                } else if (x == wumpusX && y == wumpusY) {
                    System.out.print(" W ");
                } else if (x == goldX && y == goldY) {
                    System.out.print(" G ");
                } else if (mapMatrix[x][y][4] == 1) {
                    System.out.print(" P ");
                } else {
                    System.out.print("   ");
                }
                System.out.print("|");
            }
        }
        System.out.println("\n");

        /*store all knowledgeBase the agent has acquired. The knowledgeBase for each square includes:
        chance the square has a pit, the chance it has a Wumpus, and whether it has been visited by the agent
        */
        double[][][] knowledgeBase = new double[4][4][3];

        //the agent starts by assuming each square has a 0.2 chance of having a pit, and a 1/15 chance of having the Wumpus
        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 4; y++) {
                if (!(x == 0 && y == 3)) {
                    knowledgeBase[x][y][0] = 0.2;
                    knowledgeBase[x][y][1] = 1d / 15d;
                }
            }
        }

        boolean isGameFinished = false; //true once the gold has been brought back to the starting point
        boolean isGoldGrabbed = false;
        while (!isGameFinished) {
            knowledgeBase[agentX][agentY][2] += 1; //mark the current nextLocation as visited
            System.out.println("\nThe Agent's Location: (" + agentX + ", " + agentY + ")");

            if (mapMatrix[agentX][agentY][3] == 1) {
                System.out.println("Wumpus has eaten the Agent!!!");
                break;
            } else if (mapMatrix[agentX][agentY][4] == 1) {
                System.out.println("The agent has fallen in a pit.");
                break;
            }

            //update knowledgeBase base based on whether or not the agent smells a stench
            knowledgeBase = Wumpus.wumpus(knowledgeBase, mapMatrix, agentX, agentY, mapMatrix[agentX][agentY][0]);

            //update knowledgeBase base based on whether or not the agent feels a breeze
            knowledgeBase = Pit.pit(knowledgeBase, mapMatrix, agentX, agentY, mapMatrix[agentX][agentY][1]);
            if (mapMatrix[agentX][agentY][2] == 1) {
                System.out.println("The Agent has grabbed the gold.");
                isGoldGrabbed = true;
            }

            //display information about the current nextLocation
            Information.information(agentX, agentY, knowledgeBase, mapMatrix);

            if (agentX == 0 && agentY == 3 && isGoldGrabbed) {
                isGameFinished = true;
                System.out.println("\nGold has been brought back to the start point!!!The Agent won!!!");
            }

            //decide whether or not to attempt to shoot the wumpus
            if (!Problem3_3d.Shooting.arrowShot && Problem3_3d.Shooting.isWumpusAlive) {
                mapMatrix = Problem3_3d.Shooting.shoot(agentX, agentY, knowledgeBase, mapMatrix);
                if (!Problem3_3d.Shooting.isWumpusAlive) {
                    for (int x = 0; x < 4; x++) {
                        for (int y = 0; y < 4; y++) {
                            if (!(x == 0 && y == 3)) {
                                knowledgeBase[x][y][1] = 0;
                            }
                        }
                    }
                }

            }

            //decide where to move. Agent will move to adjacent square with lowest chance of having a pit/wumpus
            int[] nextLocation = Move.move(agentX, agentY, knowledgeBase, isGoldGrabbed);
            agentX = nextLocation[0];
            agentY = nextLocation[1];
        }
    }
}