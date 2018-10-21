package wumpusworld;

/**
 * Contains starting code for creating your own Wumpus World agent.
 * Currently the agent only make a random decision each turn.
 * 
 * @author Johan Hagelbäck
 */
public class MyAgent implements Agent
{
    private World w;
    int rnd;
    
    /**
     * Creates a new instance of your solver agent.
     * 
     * @param world Current world state 
     */
    public MyAgent(World world)
    {
        w = world;   
    }
   
            
    /**
     * Asks your solver agent to execute an action.
     */

    public void doAction()
    {
        //Location of the player
        int cX = w.getPlayerX();
        int cY = w.getPlayerY();
        
        
        //Basic action:
        //Grab Gold if we can.
        if (w.hasGlitter(cX, cY))
        {
            w.doAction(World.A_GRAB);
            return;
        }
        
        //Basic action:
        //We are in a pit. Climb up.
        if (w.isInPit())
        {
            w.doAction(World.A_CLIMB);
            return;
        }
        int [][] myWorld= new int[5][5];
        for(int i=1; i<5; i++)
        {
            for(int k=1; i<5; i++)
            {
                if(w.isValidPosition(i, k))
                {
                    if(w.isVisited(i, k))
                    {
                        if(w.hasStench(i, k) && w.hasBreeze(i, k))
                        {
                            myWorld[i][k]=5;
                        }
                        else if(w.hasBreeze(i, k))
                        {
                            myWorld[i][k]=1;
                        }
                        else if(w.hasStench(i, k))
                        {
                            myWorld[i][k]=2;
                        }
                        else if(w.hasPit(i, k))
                        {
                            myWorld[i][k]=3;
                        }
                        else if(w.hasWumpus(i, k))
                        {
                            myWorld[i][k]=4;
                        }
                        else
                        {
                            myWorld[i][k]=0; //Visited but safe surroundings
                        }
                    }
                    else
                    {
                        myWorld[i][k]=-1;
                    }
                }
            }
        }
        //Probability
//        double PWumpus=1/16;
//        double PPits=3/16;
//        double PGitter= 1/16;
        
        if(myWorld[cX][cY]==0)
        {
            w.doAction(World.A_MOVE);
            
        }
        
        else if(w.hasBreeze(cX, cY))
        {
            if(w.getDirection()==0) //dir_up
            {
                moveFromBreeze(cX,cY,myWorld,0,1);
            }
            if(w.getDirection()==1) //dir_right
            {
                moveFromBreeze(cX,cY,myWorld,1,0);
            }
            if(w.getDirection()==2) // dir_down
            {
                moveFromBreeze(cX,cY,myWorld,0,-1);
            }
            if(w.getDirection()==3) //dir_left
            {
                moveFromBreeze(cX,cY,myWorld,-1,0);
            }
            
            
        }
     
    }    
    
     /**
     * Genertes a random instruction for the Agent.
     */
    public int decideRandomMove()
    {
      return (int)(Math.random() * 4);
    }
    
    public void moveFromBreeze(int cX,int cY, int [][] myWorld,int xDir,int yDir)
    {
        if(w.isValidPosition(cX+xDir, cY+yDir))
            {
                if(w.isVisited(cX+xDir, cY+yDir))
                {
                    if(myWorld[cX+1][cY]!=3);
                    {
                        w.doAction(World.A_MOVE);
                        
                    }
                    
                }
                else
                {
                    int nrOfBreezes=0;
                    
                    for(int i=-1; i<2;i++)
                    {
                        for(int k=-1; k<2; k++)
                        {
                            if(w.isValidPosition(cX+i, cY+k))
                            {
                                if(w.hasBreeze(cX+i,cY+k))
                                {
                                    nrOfBreezes++;
                                }
                            }
                        }
                    }
                    
                    if(nrOfBreezes>1)
                    {
                        if((w.isValidPosition(cX+1, cY+1) && w.hasBreeze(cX+1, cY+1))||(w.isValidPosition(cX+1, cY-1) && w.hasBreeze(cX+1, cY-1))||(w.isValidPosition(cX-1, cY+1) && w.hasBreeze(cX-1, cY+1))||(w.isValidPosition(cX-1, cY-1) && w.hasBreeze(cX-1, cY-1)))
                        {
                            if(w.isValidPosition(cX-1, cY))
                            {
                                w.doAction(World.A_TURN_LEFT);
                                w.doAction(World.A_TURN_LEFT);
                                w.doAction(World.A_MOVE);
                                 
                            }
                            else if(w.isValidPosition(cX, cY-1))
                            {
                                w.doAction(World.A_TURN_RIGHT);
                                w.doAction(World.A_MOVE);
                                 
                            }
                            else if(w.isValidPosition(cX, cY+1))
                            {
                                w.doAction(World.A_TURN_LEFT);
                                w.doAction(World.A_MOVE);
                                 
                            }
                            
                        }
                    }
                    else
                    {
                        w.doAction(World.A_MOVE);
                        
                    }
                    
                }
                
            }
        else
        {
            w.doAction(World.A_TURN_LEFT);
            
            if(w.getDirection()==0) //dir_up
            {
                moveFromBreeze(cX,cY,myWorld,0,1);
            }
            if(w.getDirection()==1) //dir_right
            {
                moveFromBreeze(cX,cY,myWorld,1,0);
            }
            if(w.getDirection()==2) // dir_down
            {
                moveFromBreeze(cX,cY,myWorld,0,-1);
            }
            if(w.getDirection()==3) //dir_left
            {
                moveFromBreeze(cX,cY,myWorld,-1,0);
            }
            
        }
    }
    
}

