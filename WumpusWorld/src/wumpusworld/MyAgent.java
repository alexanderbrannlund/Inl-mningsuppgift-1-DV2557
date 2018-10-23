package wumpusworld;

/**
 * Contains starting code for creating your own Wumpus World agent. Currently
 * the agent only make a random decision each turn.
 *
 * @author Johan Hagelbäck
 */
public class MyAgent implements Agent {

    private World w;
    int rnd;

    /**
     * Creates a new instance of your solver agent.
     *
     * @param world Current world state
     */
    public MyAgent(World world) {
        w = world;
    }

    /**
     * Asks your solver agent to execute an action.
     */
    public void doAction() {
        //Location of the player
        int cX = w.getPlayerX();
        int cY = w.getPlayerY();

        int[][] myWorld = new int[37][37];
        int countBreeze = 0;
        int countStench = 0;
        int countPit = 0;
        int countKnownSqr = 0;

        for (int i = 1; i < 5; i++) {
            for (int k = 1; k < 5; k++) {
                if (w.isValidPosition(i, k)) {
                    if (w.isVisited(i, k)) {
                        if (w.hasStench(i, k) && w.hasBreeze(i, k)) {
                            myWorld[i][k] = 5;
                            countStench++;
                            countBreeze++;
                            countKnownSqr++;
                        } else if (w.hasBreeze(i, k)) {
                            myWorld[i][k] = 1;
                            countBreeze++;
                            countKnownSqr++;
                        } else if (w.hasStench(i, k)) {
                            myWorld[i][k] = 2;
                            countStench++;
                            countKnownSqr++;
                        } else if (w.hasPit(i, k)) {
                            myWorld[i][k] = 3;
                            countPit++;
                            countKnownSqr++;
                        } else if (w.hasWumpus(i, k)) {
                            myWorld[i][k] = 4;
                            countKnownSqr++;
                        } else {
                            myWorld[i][k] = 0; //Visited but safe surroundings
                            countKnownSqr++;
                        }
                    } else {
                        myWorld[i][k] = -1;
                    }
                }
            }
        }

        if (countKnownSqr == 1 && w.hasStench(cX, cY) && w.hasBreeze(cX, cY)) {
            w.doAction(World.A_MOVE);
        } //Basic action:
        //Grab Gold if we can.
        else if (w.hasGlitter(cX, cY)) {
            w.doAction(World.A_GRAB);
            return;
        } //Basic action:
        //We are in a pit. Climb up.
        else if (w.isInPit()) {
            w.doAction(World.A_CLIMB);
            if (!w.hasStench(cX, cY)) {

                moveToUnknown(cX, cY);
            } else {
                if (countStench > 1) {
                    if (w.getDirection() == 0) //dir_up
                    {
                        moveFromStench(cX, cY, myWorld, 0, 1);
                    } else if (w.getDirection() == 1) //dir_right
                    {
                        moveFromStench(cX, cY, myWorld, 1, 0);
                    } else if (w.getDirection() == 2) // dir_down
                    {
                        moveFromStench(cX, cY, myWorld, 0, -1);
                    } else if (w.getDirection() == 3) //dir_left
                    {
                        moveFromStench(cX, cY, myWorld, -1, 0);
                    }
                } else {
                    w.doAction(World.A_TURN_LEFT);
                    w.doAction(World.A_TURN_LEFT);
                    w.doAction(World.A_MOVE);
                }
            }
            return;
        } else if (myWorld[cX][cY] == 0) {

            moveToUnknown(cX, cY);

        } else if (w.hasStench(cX, cY)) {
            if(countKnownSqr==15)
            {
                if((w.getDirection()==3 && w.isUnknown(cX, cY-1))||(w.getDirection()==0 && w.isUnknown(cX-1, cY))||(w.getDirection()==1 && w.isUnknown(cX, cY+1))||(w.getDirection()==2 && w.isUnknown(cX+1, cY)))
                {
                    w.doAction(World.A_TURN_LEFT);
                    w.doAction(World.A_SHOOT);
                    w.doAction(World.A_MOVE);
                }
                else if((w.getDirection()==0 && w.isUnknown(cX+1, cY))||(w.getDirection()==1 && w.isUnknown(cX, cY-1))||(w.getDirection()==2 && w.isUnknown(cX-1, cY))||(w.getDirection()==3 && w.isUnknown(cX, cY+1)))
                {
                    w.doAction(World.A_TURN_RIGHT);
                    w.doAction(World.A_SHOOT);
                    w.doAction(World.A_MOVE);
                }
                else
                {
                  w.doAction(World.A_SHOOT);
                  w.doAction(World.A_MOVE);  
                }
            }
            else if (countStench > 1) {
                if (w.getDirection() == 0) //dir_up
                {
                    moveFromStench(cX, cY, myWorld, 0, 1);
                } else if (w.getDirection() == 1) //dir_right
                {
                    moveFromStench(cX, cY, myWorld, 1, 0);
                } else if (w.getDirection() == 2) // dir_down
                {
                    moveFromStench(cX, cY, myWorld, 0, -1);
                } else if (w.getDirection() == 3) //dir_left
                {
                    moveFromStench(cX, cY, myWorld, -1, 0);
                }
            } else {
                w.doAction(World.A_TURN_LEFT);
                w.doAction(World.A_TURN_LEFT);
                w.doAction(World.A_MOVE);
            }
        } else if (w.hasBreeze(cX, cY)) {
            if(countKnownSqr==1)
            {
                w.doAction(World.A_MOVE);
            }
            else if(countBreeze==1 && countStench==2)
            {
                w.doAction(World.A_MOVE);
            }
            else if(countBreeze==1 && countStench==1 && countKnownSqr<3)
            {
                w.doAction(World.A_TURN_LEFT);
                w.doAction(World.A_TURN_LEFT);
                w.doAction(World.A_MOVE);
            }
            else if(countBreeze==1 && countStench==1)
            {
                w.doAction(World.A_MOVE);
            }
            else if (countBreeze > 1) {
                if (w.getDirection() == 0) //dir_up
                {
                    moveFromBreeze(cX, cY, myWorld, 0, 1);
                } else if (w.getDirection() == 1) //dir_right
                {
                    moveFromBreeze(cX, cY, myWorld, 1, 0);
                } else if (w.getDirection() == 2) // dir_down
                {
                    moveFromBreeze(cX, cY, myWorld, 0, -1);
                } else if (w.getDirection() == 3) //dir_left
                {
                    moveFromBreeze(cX, cY, myWorld, -1, 0);
                }
            } else if(countBreeze==1 && countStench==1)
            {
                moveToUnknown(cX,cY);
            }
            else {
                w.doAction(World.A_TURN_LEFT);
                w.doAction(World.A_TURN_LEFT);
                w.doAction(World.A_MOVE);
            }

        }

    }

    /**
     * Genertes a random instruction for the Agent.
     */
    public int decideRandomMove() {
        return (int) (Math.random() * 4);
    }

    public void moveFromBreeze(int cX, int cY, int[][] myWorld, int xDir, int yDir) {
        if (w.isValidPosition(cX + xDir, cY + yDir) && (!w.hasPit(cX + xDir, cY + yDir))) {
            if (w.isVisited(cX + xDir, cY + yDir)) {
                if (myWorld[cX + 1][cY] != 3);
                {
                    w.doAction(World.A_MOVE);

                }

            } else {
                int nrOfBreezes = 0;

                for (int i = -1; i < 2; i++) {
                    for (int k = -1; k < 2; k++) {
                        if (w.isValidPosition(cX + i, cY + k)) {
                            if (w.hasBreeze(cX + i, cY + k)) {
                                nrOfBreezes++;
                            }
                        }
                    }
                }

                if (nrOfBreezes > 1) {

                    if ((w.isValidPosition(cX + 1, cY + 1) && w.hasBreeze(cX + 1, cY + 1)) || (w.isValidPosition(cX + 1, cY - 1) && w.hasBreeze(cX + 1, cY - 1)) || (w.isValidPosition(cX - 1, cY + 1) && w.hasBreeze(cX - 1, cY + 1)) || (w.isValidPosition(cX - 1, cY - 1) && w.hasBreeze(cX - 1, cY - 1))) {
                        if (w.hasPit(cX + xDir, cY + yDir)) {
                            if (w.isValidPosition(cX + xDir, cY + yDir) && (!w.hasPit(cX + xDir, cY + yDir))) {
                                w.doAction(World.A_TURN_LEFT);
                                w.doAction(World.A_MOVE);
                            } else if (w.isValidPosition(cX - yDir, cY - xDir) && (!w.hasPit(cX - yDir, cY - xDir))) {
                                w.doAction(World.A_TURN_RIGHT);
                                w.doAction(World.A_MOVE);

                            } else if (w.isValidPosition(cX - xDir, cY - yDir) && (!w.hasPit(cX - xDir, cY - yDir))) {
                                w.doAction(World.A_TURN_LEFT);
                                w.doAction(World.A_TURN_LEFT);
                                w.doAction(World.A_MOVE);

                            }

                        } else if (w.hasPit(cX - xDir, cY - yDir) || w.isUnknown(cX + xDir, cY + yDir)) {

                            w.doAction(World.A_MOVE);

                        } else {
                            w.doAction(World.A_TURN_LEFT);
                            w.doAction(World.A_TURN_LEFT);
                            w.doAction(World.A_MOVE);
                        }

                    } else {
                        w.doAction(World.A_MOVE);
                    }
                } else {
                    w.doAction(World.A_MOVE);

                }

            }

        } else {
            if (xDir == 1 || xDir == -1) {
                if (w.isValidPosition(cX, cY - xDir)) {
                    w.doAction(World.A_TURN_RIGHT);
                    w.doAction(World.A_MOVE);
                } else if (w.isValidPosition(cX, cY + xDir)) {
                    w.doAction(World.A_TURN_LEFT);
                    w.doAction(World.A_MOVE);
                }

            } else if (yDir == 1 || yDir == -1) {
                if (w.isValidPosition(cX + yDir, cY)) {
                    w.doAction(World.A_TURN_RIGHT);
                    w.doAction(World.A_MOVE);
                } else if (w.isValidPosition(cX - yDir, cY)) {
                    w.doAction(World.A_TURN_LEFT);
                    w.doAction(World.A_MOVE);
                }

            }

        }
    }

    public void moveFromStench(int cX, int cY, int[][] myWorld, int xDir, int yDir) {
        int nrOfStenches = 0;

        for (int i = -2; i < 3; i++) {
            for (int k = -2; k < 3; k++) {
                if (w.isValidPosition(cX + i, cY + k)) {
                    if (w.hasStench(cX + i, cY + k)) {
                        nrOfStenches++;
                    }
                }
            }
        }

        if (nrOfStenches > 1) {
            if (w.isValidPosition(cX - 1, cY + 1) && w.hasStench(cX - 1, cY + 1)) {
                if (w.isVisited(cX - 1, cY)) {
                    if (w.isValidPosition(cX + 1, cY)) {
                        if (yDir == 1) {
                            w.doAction(World.A_TURN_RIGHT);
                            w.doAction(World.A_MOVE);

                        } else if (xDir == 1 || xDir == -1) {
                            w.doAction(World.A_MOVE);
                        }

                    } else {
                        if (w.isValidPosition(cX, cY - 1) && w.isUnknown(cX, cY - 1)) {
                            if (xDir == 1) {
                                w.doAction(World.A_TURN_RIGHT);
                                w.doAction(World.A_MOVE);

                            } else if (yDir == 1) {
                                w.doAction(World.A_TURN_RIGHT);
                                w.doAction(World.A_TURN_RIGHT);
                                w.doAction(World.A_MOVE);
                            } else if (xDir == -1) {
                                w.doAction(World.A_TURN_LEFT);
                                w.doAction(World.A_MOVE);
                            }

                        }
                    }

                } else {
                    w.doAction(World.A_TURN_RIGHT);
                    w.doAction(World.A_TURN_RIGHT);
                    w.doAction(World.A_MOVE);
                }
            }

            else if (w.isValidPosition(cX + 1, cY + 1) && w.hasStench(cX + 1, cY + 1)) {
                if (w.isVisited(cX + 1, cY)) {
                    if (w.isValidPosition(cX - 1, cY)) {
                        if (yDir == 1) {
                            w.doAction(World.A_TURN_LEFT);
                            w.doAction(World.A_MOVE);

                        } else if (xDir == 1 || xDir == -1) {
                            w.doAction(World.A_MOVE);
                        }

                    } else {
                        if (w.isValidPosition(cX, cY - 1) && w.isUnknown(cX, cY - 1)) {
                            if (xDir == 1) {
                                w.doAction(World.A_TURN_RIGHT);
                                w.doAction(World.A_MOVE);

                            } else if (yDir == 1) {
                                w.doAction(World.A_TURN_RIGHT);
                                w.doAction(World.A_TURN_RIGHT);
                                w.doAction(World.A_MOVE);
                            } else if (xDir == -1) {
                                w.doAction(World.A_TURN_LEFT);
                                w.doAction(World.A_MOVE);
                            }

                        }
                    }

                } else {
                    w.doAction(World.A_TURN_RIGHT);
                    w.doAction(World.A_TURN_RIGHT);
                    w.doAction(World.A_MOVE);
                }
            }

            else if (w.isValidPosition(cX + 1, cY - 1) && w.hasStench(cX + 1, cY - 1)) {
                if (w.isVisited(cX, cY - 1)) {
                    if (w.isValidPosition(cX, cY + 1)) {
                        if (yDir == 1) {

                            w.doAction(World.A_MOVE);

                        } else if (xDir == 1) {
                            w.doAction(World.A_TURN_LEFT);
                            w.doAction(World.A_MOVE);
                        } else if (yDir == -1) {
                            w.doAction(World.A_TURN_LEFT);
                            w.doAction(World.A_TURN_LEFT);
                            w.doAction(World.A_MOVE);
                        }

                    } else {
                        if (w.isValidPosition(cX - 1, cY) && w.isUnknown(cX - 1, cY)) {
                            if (yDir == 1) {
                                w.doAction(World.A_TURN_LEFT);
                                w.doAction(World.A_MOVE);

                            } else if (xDir == 1) {
                                w.doAction(World.A_TURN_RIGHT);
                                w.doAction(World.A_TURN_RIGHT);
                                w.doAction(World.A_MOVE);
                            } else if (yDir == -1) {
                                w.doAction(World.A_TURN_RIGHT);
                                w.doAction(World.A_MOVE);
                            }

                        }
                    }

                } else {
                    w.doAction(World.A_TURN_RIGHT);
                    w.doAction(World.A_TURN_RIGHT);
                    w.doAction(World.A_MOVE);
                }
            }

          else if (w.isValidPosition(cX - 1, cY - 1) && w.hasStench(cX - 1, cY - 1)) {
                if (w.isVisited(cX - 1, cY)) {
                    if (w.isValidPosition(cX + 1, cY)) {
                        if (yDir == 1) {
                            w.doAction(World.A_TURN_RIGHT);
                            w.doAction(World.A_MOVE);

                        } else if (xDir == 1 || xDir == -1) {
                            w.doAction(World.A_MOVE);
                        }

                    } else {
                        if (w.isValidPosition(cX, cY + 1) && w.isUnknown(cX, cY + 1)) {
                            if (xDir == 1) {
                                w.doAction(World.A_TURN_LEFT);
                                w.doAction(World.A_MOVE);

                            } else if (yDir == -1) {
                                w.doAction(World.A_TURN_RIGHT);
                                w.doAction(World.A_TURN_RIGHT);
                                w.doAction(World.A_MOVE);
                            } else if (xDir == -1) {
                                w.doAction(World.A_TURN_RIGHT);
                                w.doAction(World.A_MOVE);
                            }

                        }
                    }

                } else {
                    w.doAction(World.A_TURN_RIGHT);
                    w.doAction(World.A_TURN_RIGHT);
                    w.doAction(World.A_MOVE);
                }
            }

          else if ((w.isValidPosition(cX + 1, cY + 1) && w.hasStench(cX + 1, cY + 1)) && (w.isValidPosition(cX - 1, cY + 1) && w.hasStench(cX - 1, cY + 1))) {
                if (yDir != 1) {
                    if (w.isUnknown(cX + xDir, cY + yDir)) {
                        w.doAction(World.A_MOVE);
                    } else if (xDir == 1) {
                        if (w.isUnknown(cX, cY - 1)) {
                            w.doAction(World.A_TURN_RIGHT);
                            w.doAction(World.A_MOVE);
                            xDir = 0;
                            yDir = -1;

                        }
                    } else if (xDir == -1) {
                        if (w.isUnknown(cX, cY - 1)) {
                            w.doAction(World.A_TURN_LEFT);
                            w.doAction(World.A_MOVE);
                            xDir = 0;
                            yDir = -1;

                        }
                    }

                } else {
                    if (w.isValidPosition(cX + 1, cY) && w.isUnknown(cX + 1, cY)) {
                        w.doAction(World.A_TURN_RIGHT);
                        w.doAction(World.A_MOVE);
                        xDir = 1;
                        yDir = 0;
                    } else if (w.isValidPosition(cX - 1, cY) && w.isUnknown(cX - 1, cY)) {
                        w.doAction(World.A_TURN_LEFT);
                        w.doAction(World.A_MOVE);
                        xDir = -1;
                        yDir = 0;
                    }

                }

            }
          else if ((w.isValidPosition(cX + 1, cY - 1) && w.hasStench(cX + 1, cY - 1)) && (w.isValidPosition(cX - 1, cY - 1) && w.hasStench(cX - 1, cY - 1))) {
                if (yDir != -1) {
                    if (w.isUnknown(cX + xDir, cY + yDir)) {
                        w.doAction(World.A_MOVE);
                    } else if (xDir == 1) {
                        if (w.isUnknown(cX, cY + 1)) {
                            w.doAction(World.A_TURN_LEFT);
                            w.doAction(World.A_MOVE);
                            xDir = 0;
                            yDir = -1;

                        }
                    } else if (xDir == -1) {
                        if (w.isUnknown(cX, cY + 1)) {
                            w.doAction(World.A_TURN_RIGHT);
                            w.doAction(World.A_MOVE);
                            xDir = 0;
                            yDir = -1;

                        }
                    }

                } else {
                    if (w.isValidPosition(cX - 1, cY) && w.isUnknown(cX - 1, cY)) {
                        w.doAction(World.A_TURN_RIGHT);
                        w.doAction(World.A_MOVE);
                        xDir = 1;
                        yDir = 0;
                    } else if (w.isValidPosition(cX + 1, cY) && w.isUnknown(cX + 1, cY)) {
                        w.doAction(World.A_TURN_LEFT);
                        w.doAction(World.A_MOVE);
                        xDir = -1;
                        yDir = 0;
                    }

                }

            }
           else if(w.hasStench(cX+2, cY)||w.hasStench(cX-2, cY)||w.hasStench(cX, cY+2)||w.hasStench(cX, cY-2))
           {
               w.doAction(World.A_TURN_RIGHT);
               w.doAction(World.A_TURN_RIGHT);
               w.doAction(World.A_MOVE);
           }

        } else {

            if (xDir == 1 || xDir == -1) {
                if (w.isValidPosition(cX, cY - xDir)) {
                    w.doAction(World.A_TURN_RIGHT);
                    w.doAction(World.A_MOVE);
                } else if (w.isValidPosition(cX, cY + xDir)) {
                    w.doAction(World.A_TURN_LEFT);
                    w.doAction(World.A_MOVE);
                }

            } else if (yDir == 1 || yDir == -1) {
                if (w.isValidPosition(cX + yDir, cY)) {
                    w.doAction(World.A_TURN_RIGHT);
                    w.doAction(World.A_MOVE);
                } else if (w.isValidPosition(cX - yDir, cY)) {
                    w.doAction(World.A_TURN_LEFT);
                    w.doAction(World.A_MOVE);
                }

            }

        }

    }

    public void moveToUnknown(int cX, int cY) {
        if (w.getDirection() == 0) //dir_up
        {
            if (w.isValidPosition(cX, cY + 1) && w.isUnknown(cX, cY + 1)) {
                w.doAction(World.A_MOVE);
            } else if (w.isValidPosition(cX - 1, cY) && w.isUnknown(cX - 1, cY)) {
                w.doAction(World.A_TURN_LEFT);
                w.doAction(World.A_MOVE);
            } else if (w.isValidPosition(cX + 1, cY) && w.isUnknown(cX + 1, cY)) {
                w.doAction(World.A_TURN_RIGHT);
                w.doAction(World.A_MOVE);
            } else if (w.isValidPosition(cX, cY - 1) && w.isUnknown(cX, cY - 1)) {
                w.doAction(World.A_TURN_RIGHT);
                w.doAction(World.A_TURN_RIGHT);
                w.doAction(World.A_MOVE);
            } else {
                if (w.isValidPosition(cX, cY + 1)) {
                    w.doAction(World.A_MOVE);
                } else if (w.isValidPosition(cX - 1, cY)) {
                    w.doAction(World.A_TURN_LEFT);
                    w.doAction(World.A_MOVE);
                } else if (w.isValidPosition(cX + 1, cY)) {
                    w.doAction(World.A_TURN_RIGHT);
                    w.doAction(World.A_MOVE);
                } else if (w.isValidPosition(cX, cY - 1)) {
                    w.doAction(World.A_TURN_RIGHT);
                    w.doAction(World.A_TURN_RIGHT);
                    w.doAction(World.A_MOVE);
                }
            }
        } else if (w.getDirection() == 1) //dir_right
        {
            if (w.isValidPosition(cX + 1, cY) && w.isUnknown(cX + 1, cY)) {
                w.doAction(World.A_MOVE);
            } else if (w.isValidPosition(cX, cY + 1) && w.isUnknown(cX, cY + 1)) {
                w.doAction(World.A_TURN_LEFT);
                w.doAction(World.A_MOVE);
            } else if (w.isValidPosition(cX, cY - 1) && w.isUnknown(cX, cY - 1)) {
                w.doAction(World.A_TURN_RIGHT);
                w.doAction(World.A_MOVE);
            } else if (w.isValidPosition(cX - 1, cY) && w.isUnknown(cX - 1, cY)) {
                w.doAction(World.A_TURN_RIGHT);
                w.doAction(World.A_TURN_RIGHT);
                w.doAction(World.A_MOVE);
            } else {
                if (w.isValidPosition(cX + 1, cY)) {
                    w.doAction(World.A_MOVE);
                } else if (w.isValidPosition(cX, cY + 1)) {
                    w.doAction(World.A_TURN_LEFT);
                    w.doAction(World.A_MOVE);
                } else if (w.isValidPosition(cX, cY - 1)) {
                    w.doAction(World.A_TURN_RIGHT);
                    w.doAction(World.A_MOVE);
                } else if (w.isValidPosition(cX - 1, cY)) {
                    w.doAction(World.A_TURN_RIGHT);
                    w.doAction(World.A_TURN_RIGHT);
                    w.doAction(World.A_MOVE);
                }
            }
        } else if (w.getDirection() == 2) // dir_down
        {
            if (w.isValidPosition(cX, cY - 1) && w.isUnknown(cX, cY - 1)) {
                w.doAction(World.A_MOVE);
            } else if (w.isValidPosition(cX + 1, cY) && w.isUnknown(cX + 1, cY)) {
                w.doAction(World.A_TURN_LEFT);
                w.doAction(World.A_MOVE);
            } else if (w.isValidPosition(cX - 1, cY) && w.isUnknown(cX - 1, cY)) {
                w.doAction(World.A_TURN_RIGHT);
                w.doAction(World.A_MOVE);
            } else if (w.isValidPosition(cX, cY + 1) && w.isUnknown(cX, cY + 1)) {
                w.doAction(World.A_TURN_RIGHT);
                w.doAction(World.A_TURN_RIGHT);
                w.doAction(World.A_MOVE);
            } else {
                if (w.isValidPosition(cX, cY - 1)) {
                    w.doAction(World.A_MOVE);
                } else if (w.isValidPosition(cX + 1, cY)) {
                    w.doAction(World.A_TURN_LEFT);
                    w.doAction(World.A_MOVE);
                } else if (w.isValidPosition(cX - 1, cY)) {
                    w.doAction(World.A_TURN_RIGHT);
                    w.doAction(World.A_MOVE);
                } else if (w.isValidPosition(cX, cY + 1)) {
                    w.doAction(World.A_TURN_RIGHT);
                    w.doAction(World.A_TURN_RIGHT);
                    w.doAction(World.A_MOVE);
                }
            }
        } else if (w.getDirection() == 3) //dir_left
        {
            if (w.isValidPosition(cX - 1, cY) && w.isUnknown(cX - 1, cY)) {
                w.doAction(World.A_MOVE);
            } else if (w.isValidPosition(cX, cY - 1) && w.isUnknown(cX, cY - 1)) {
                w.doAction(World.A_TURN_LEFT);
                w.doAction(World.A_MOVE);
            } else if (w.isValidPosition(cX, cY + 1) && w.isUnknown(cX, cY + 1)) {
                w.doAction(World.A_TURN_RIGHT);
                w.doAction(World.A_MOVE);
            } else if (w.isValidPosition(cX + 1, cY) && w.isUnknown(cX + 1, cY)) {
                w.doAction(World.A_TURN_RIGHT);
                w.doAction(World.A_TURN_RIGHT);
                w.doAction(World.A_MOVE);
            } else {
                if (w.isValidPosition(cX - 1, cY)) {
                    w.doAction(World.A_MOVE);
                } else if (w.isValidPosition(cX, cY - 1)) {
                    w.doAction(World.A_TURN_LEFT);
                    w.doAction(World.A_MOVE);
                } else if (w.isValidPosition(cX, cY + 1)) {
                    w.doAction(World.A_TURN_RIGHT);
                    w.doAction(World.A_MOVE);
                } else if (w.isValidPosition(cX + 1, cY)) {
                    w.doAction(World.A_TURN_RIGHT);
                    w.doAction(World.A_TURN_RIGHT);
                    w.doAction(World.A_MOVE);
                }
            }
        }
    }
}
