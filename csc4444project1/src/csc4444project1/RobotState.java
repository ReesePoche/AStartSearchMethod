
package csc4444project1;

/**
 * 
 * @author Reese
 */
public class RobotState implements Comparable<RobotState>{
    //a boolean array that where true in a position shows that a square is dirty
    //the follwing is how the positions in the array represent the 3x3 vacume grid
    //number of trues is equal to the number of trues
    //  ____________
    // | 0 | 1 | 2 |
    // | 3 | 4 | 5 |
    // | 6 | 7 | 8 |
    // -------------
    public boolean[] isdirty;
    //the position that the robot is in the current state. 
    //if it is ever -1 then the state is considered "invalid"
    public int pos;
    //the number of dirty squares. 
    //should be equal to the number of true positions in the boolean array
    public int numOfDS;
    
    //the node that it was exspanded from
    public RobotState parent;
    
    //the actual cost to get to this node on the path
    public int cost;
    
    //the cost given by heuristic function
    //in this case there is h1 and h2
    //both definded below in the methods h1 and h2
    public int hCost;
    
    //this is the action from the parent that creates this state
    public char action;
    
    
    ////////////////////////////////////////////////////////////////////////////
    //////          Construction methods                                ////////
    ////////////////////////////////////////////////////////////////////////////
    
    /**
     * 
     * @param position the position number that the robot is in
     * @param dirtySquares an array of ints that say the position of the dirty sqeares
     */
    public RobotState(int position, int[] dirtySquares) {
        pos = position;
        numOfDS = dirtySquares.length;
        isdirty = new boolean[9];
        for(int i = 0; i < 9; i++)
            isdirty[i] = false;
        for(int i =0; i < dirtySquares.length; i++) {
            isdirty[dirtySquares[i]] = true;
        }
    }
    
    /**
     * This method is used to make a clone of another in the M methods
     * @param position
     * @param dirtySquares
     * @param numOfDS 
     */
    public RobotState(int position, boolean[] dirtySquares, int numOfDS) {
        pos = position;
        this.numOfDS = numOfDS;
        isdirty = dirtySquares;
    }
    
    public RobotState(){
        pos = -1;
        numOfDS = 0;
        isdirty = new boolean[9];
    }
    
    ////////////////////////////////////////////////////////////////////////////
    //////                     Boolean methods                    //////////////
    //////                The following determine if the          //////////////
    //////                  Action can be done or if useless      //////////////
    ////////////////////////////////////////////////////////////////////////////
    
    
    public boolean canUp(){
        if(pos < 3)
            return false;
        return true;
    }
    
    public boolean canDown(){
        if(pos > 5)
            return false;
        return true;
    }
    
    public boolean canRight(){
        if(pos == 2 || pos == 5 || pos == 8)
            return false;
        return true;
    }
    
    public boolean canLeft(){
        if(pos==0 || pos == 3 || pos == 6)
            return false;
        return true;
    }
    public boolean canSuck(){
        if( pos != -1 && isdirty[pos])
            return true;
        return false;
    }
    
    ////////////////////////////////////////////////////////////////////////////
    //////              Doing methods                                    //////
    //////      Minipulates the the current state parameters             //////
    //////      If the action is invalid on the state, the state becomes //////
    //////      Invalid so the position is set to -1 and nothing changed //////
    ///////////////////////////////////////////////////////////////////////////
    
    public void doUp(){
        if(this.canUp())
            pos = pos - 3;
        else
            pos = -1;
    }
    
    public void doDown(){
        if(this.canDown())
            pos = pos +3;
        else
            pos = -1;
    }
    
    public void doRight(){
        if(this.canRight())
            pos++;
        else
            pos = -1;
    }
    
    public void doLeft(){
        if(this.canLeft()) 
            pos--;
        else
            pos = -1;
    }
    
    public void doSuck(){
        if(this.canSuck()) {
        isdirty[pos] = false;
        numOfDS--;
        }
        else
            pos = -1;
    }
    
    
    ////////////////////////////////////////////////////////////////////////////
    //////                     Making new child state methods             //////
    //////     Follwing return a new state that is a child of this state  //////
    //////     after doing the correspoinding action                      //////
    //////      All return a invalid state(pos = -1) if action cant' be done ////
    ////////////////////////////////////////////////////////////////////////////
    
    
    //method for just making a copy of this state
    private RobotState copy(){
        RobotState copy = new RobotState();
        copy.pos = this.pos;
        copy.numOfDS = this.numOfDS;
        for(int i = 0; i < 9; i++)
            copy.isdirty[i] = this.isdirty[i];
        return copy;
    }
    
    public RobotState MUp(){
            RobotState child = this.copy();
            child.doUp();
            return child;
    }
    
    public RobotState MDown(){
        RobotState child = this.copy();
            child.doDown();
            return child;
    }
    
    public RobotState MRight(){
        RobotState child = this.copy();
        child.doRight();
        return child;
    }
    
    public RobotState MLeft(){
        RobotState child = this.copy();
        child.doLeft();
        return child;
    }
    
   public RobotState MSuck(){
        RobotState child = this.copy();
        child.doSuck();
        return child;
    }
   
   
   ///calculates the cost of any action from this node
   ///is a step cost not total cost
   // Just a step cost added to all childs
   //if this is invalid steps costs are max int
   public int cost(boolean didSuck) {
       if(pos != -1){
            if(didSuck){
                return (numOfDS-1)*2 +1;
            }
            else {
                return numOfDS*2 +1;
            }
       }
       else
           return Integer.MAX_VALUE;
   }
   
   
   //returns the distance the robot pos is from the nearest dirty square
   private int distFromDS(){
       if(pos == -1)
           return Integer.MAX_VALUE;
       if(isdirty[pos])
           return 0;
       if(pos == 0) {
          if(isdirty[1] || isdirty[3])
              return 1;
          else if(isdirty[2] || isdirty[4] || isdirty[6])
              return 2;
          else if(isdirty[5] || isdirty[7])
              return 3;
          else if(isdirty[8])
              return 4;
          else
              return 0;
       }
        if(pos == 2) {
          if(isdirty[1] || isdirty[5])
              return 1;
          else if(isdirty[0] || isdirty[4] || isdirty[8])
              return 2;
          else if(isdirty[3] || isdirty[7])
              return 3;
          else if(isdirty[6])
              return 4;
          else
              return 0;
       }
        if(pos == 6) {
          if(isdirty[3] || isdirty[7])
              return 1;
          else if(isdirty[0] || isdirty[4] || isdirty[8])
              return 2;
          else if(isdirty[5] || isdirty[1])
              return 3;
          else if(isdirty[2])
              return 4;
          else
              return 0;
       }
         if(pos == 8) {
          if(isdirty[5] || isdirty[7])
              return 1;
          else if(isdirty[2] || isdirty[4] || isdirty[6])
              return 2;
          else if(isdirty[3] || isdirty[1])
              return 3;
          else if(isdirty[0])
              return 4;
          else
              return 0;
       }
       if(pos == 1) {
          if(isdirty[0] || isdirty[2] || isdirty[4])
              return 1;
          else if(isdirty[3] || isdirty[5] || isdirty[7])
              return 2;
          else if(isdirty[6] || isdirty[8])
              return 3;
          else
              return 0;
       }
       if(pos == 7) {
          if(isdirty[6] || isdirty[8] || isdirty[4])
              return 1;
          else if(isdirty[3] || isdirty[5] || isdirty[1])
              return 2;
          else if(isdirty[0] || isdirty[2])
              return 3;
          else
              return 0;
       }
       if(pos == 3) {
          if(isdirty[0] || isdirty[6] || isdirty[4])
              return 1;
          else if(isdirty[1] || isdirty[5] || isdirty[7])
              return 2;
          else if(isdirty[2] || isdirty[8])
              return 3;
          else
              return 0;
       }
       if(pos == 5) {
          if(isdirty[8] || isdirty[2] || isdirty[4])
              return 1;
          else if(isdirty[1] || isdirty[3] || isdirty[7])
              return 2;
          else if(isdirty[6] || isdirty[0])
              return 3;
          else
              return 0;
       }
       if(pos == 4) {
          if(isdirty[1] || isdirty[3] || isdirty[5] || isdirty[7])
              return 1;
          else if(isdirty[0] || isdirty[2] || isdirty[6] || isdirty[8])
              return 2;
          else
              return 0;
       }
       return -1;
   }
           
           
           
  //////////////////////////////////////////////////////////////////////////////
  ///////               Heuristic function of the nodes                  ///////
  //////    Notes that h2 is always half of h1                          ////////
  //////////////////////////////////////////////////////////////////////////////
   public int h1() {
       return this.distFromDS()*(this.numOfDS*2 + 1);
   }
   
   public int h2() {
       return this.h1()/2;
   }
   
   /////////////////////////////////////////////////////////////////////////////
   /////        State checkers                                           ///////
   /////     Following check if the state is the goal state or valid     ///////
   ///// goal state means isdirty all false and invalid is pos = -1      ///////
   /////////////////////////////////////////////////////////////////////////////
   public boolean goalReached(){
       for(int i= 0; i < 9; i++)
           if(isdirty[i])
               return false;
       return true;
   }
   
   
   public boolean isValid(){
          return(this.pos != -1);
       }
   
   
   /////////////////////////////////////////////////////////////////////////////
   ////// Comparer method how states are compared in the priority queue   //////
   /////////////////////////////////////////////////////////////////////////////
   public int compareTo(RobotState that) {
            if((this.cost + this.hCost) > (that.cost + that.hCost))
               return 1;
           else if((this.cost + this.hCost) < (that.cost + that.hCost))
               return -1;
           else
               return 0;
        }    
   }
   
   
   
    

