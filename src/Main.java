
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

        int agentX = 0, agentY = 3;

        //randomly place Wumpus
        int wumpusX = r.nextInt(4);
        int wumpusY = r.nextInt(4);
        while (wumpusX == 0 && wumpusY == 3) {
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


        mapMatrix[goldX][goldY][2] = 1;

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

        //adding stenches
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
        //adding breezes
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

        double[][][] knowledgeBase = new double[4][4][3]; //0 - has a pit, 1 - has a wumpus, 2 - visited

        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 4; y++) {
                if (!(x == 0 && y == 3)) {
                    knowledgeBase[x][y][0] = 0.2; //chance for a pit
                    knowledgeBase[x][y][1] = 1d / 15d; // chance for a wumpus
                }
            }
        }

        boolean isGameFinished = false;
        boolean isGoldGrabbed = false;
        while (!isGameFinished) {
            knowledgeBase[agentX][agentY][2] += 1; //current point is visited
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

            Output.output(agentX, agentY, knowledgeBase, mapMatrix);

            if (agentX == 0 && agentY == 3 && isGoldGrabbed) {
                isGameFinished = true;
                System.out.println("\nGold has been brought back to the start point!!!The Agent won!!!");
            }

            if (!Shooting.arrowShot && Shooting.isWumpusAlive) {
                mapMatrix = Shooting.shoot(agentX, agentY, knowledgeBase, mapMatrix);
                if (!Shooting.isWumpusAlive) {
                    for (int x = 0; x < 4; x++) {
                        for (int y = 0; y < 4; y++) {
                            if (!(x == 0 && y == 3)) {
                                knowledgeBase[x][y][1] = 0;
                            }
                        }
                    }
                }

            }

            int[] nextLocation = Action.action(agentX, agentY, knowledgeBase, isGoldGrabbed);
            agentX = nextLocation[0];
            agentY = nextLocation[1];
        }
    }
}