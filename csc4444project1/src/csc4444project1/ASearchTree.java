/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csc4444project1;

import java.util.PriorityQueue;


/**
 *
 * @author Reese
 */
public class ASearchTree {
    
    //contains the queue of nodes sorted by cost when goal found is false
    //contains the queue of the optimal goal path if goal is found
    //if goalFound true and tree empty then no solution can be found
    public PriorityQueue<treeNode> tree;
    public treeNode InitalState;
    public treeNode finalNode;
    public boolean searchComplete;
    
//////////////////////////////////////////////////////////////
////////        Tree Node                               //////
////////    Use for the Priority Queue                  //////
////////    each node comparable by the cost            //////
//////////////////////////////////////////////////////////////
    
    private class treeNode implements Comparable<treeNode> {
       //cost to get to this node. 
       public int cost;
       //what the heuristic says the distance from goal this state is
       public int hCost;
       //how far down the tree this node is
       public int depth;
       //state of the node
       public RobotState state;
       //the parent of this node
       public treeNode daddy;
       //the action the parent did to spawn this node
       public char action;
       
       
       public treeNode(RobotState state, int cost, int depth, treeNode daddy, char action) {
           this.depth = depth;
           this.cost = cost;
           this.hCost = 0;
           this.state = state;
           this.daddy = daddy;
           this.action = action;
       }
       
       public boolean isValid(){
          return(this.pos() != -1);
       }
       
       public int pos() {
           return state.pos;
       }
       
       
       public int h1(){
           return state.h1();
       }
       
       public int h2(){
           return state.h2();
       }

        public int compareTo(treeNode that) {
            if((this.cost + this.hCost) > (that.cost + that.hCost))
               return 1;
           else if((this.cost + this.hCost) < (that.cost + that.hCost))
               return -1;
           else
               return 0;
        }
        
        
        public boolean isGoalState(){
            return state.goalReached();
        }
        
        
        public String toString(){
            String out = "";
            out += "Node:\npos: " + this.pos() + "\ncost : " + this.cost + "\nh cost: " + this.h1() + "\naction: " + this.action + "\nnumDS: "+ this.state.numOfDS ;
            if(this.action != 'i')
                out+= "\ndaddy: " + this.daddy.action;
            return out;
        }
    }
///////////////////////////////////////////////////////////////////////////////
///////////         Constructors                                    ///////////
//////////          Just need a start State                         ///////////
///////////////////////////////////////////////////////////////////////////////
    public ASearchTree(RobotState startState){
       tree = new PriorityQueue();
       treeNode startNode = new treeNode(startState, 0, 0, null, 'i');
       startNode.hCost = startNode.h1();
       this.InitalState = startNode;
       this.searchComplete = false;
       this.finalNode = null;
       tree.add(startNode);  
    }
    
    
    ///////////////////////////////////////////////////////////////////////////
    //////              exspand method                                   //////
    //////          Where all the magic happens. apply to top of queue. ///////
    ////// make the kids then add the kids to the queue.                ///////
    ///////////////////////////////////////////////////////////////////////////
    
    private void exspandNode(){
        treeNode eNode = tree.poll();
        if(eNode != null){ //note if eNode is null that means that the tree is empty so out of ways to solve
            if(eNode.isGoalState()){ //means you found the best way to solve puzzle
                this.searchComplete = true;
                tree.clear();
                this.finalNode = eNode;
            }
            else {
                if(eNode.pos() != -1) {
                    //we know the node isn't invalid now make the childeren
                    treeNode up = new treeNode(eNode.state.MUp(), eNode.cost, (eNode.depth + 1), eNode, 'u');
                    if(up.pos() != -1) { //check to see if parent can have this child
                        up.cost+= eNode.state.cost(false);
                        up.hCost = up.h1();
                        tree.add(up);
                    }
                    treeNode down = new treeNode(eNode.state.MDown(), eNode.cost, (eNode.depth + 1), eNode, 'd');
                    if(down.pos() != -1){
                        down.cost+=eNode.state.cost(false);
                        down.hCost = down.h1();
                        tree.add(down);
                    }
                    treeNode left = new treeNode(eNode.state.MLeft(), eNode.cost, (eNode.depth + 1), eNode, 'l');
                    if(left.pos() != -1){
                        left.cost+=eNode.state.cost(false);
                        left.hCost = left.h1();
                        tree.add(left);
                    }
                    treeNode right = new treeNode(eNode.state.MRight(), eNode.cost, (eNode.depth + 1), eNode, 'r');
                    if(right.pos() != -1){
                        right.cost+=eNode.state.cost(false);
                        right.hCost = right.h1();
                        tree.add(right);
                    }
                    treeNode suck = new treeNode(eNode.state.MSuck(), eNode.cost, (eNode.depth + 1), eNode, 's');
                    if(suck.pos() != -1){
                        suck.cost+=eNode.state.cost(true);
                        suck.hCost = suck.h1();
                        tree.add(suck);
                    }   
                }
                else
                    System.out.print("YOU PULLED AN INVALID STATE");
            }
        }
        else
            this.searchComplete = true;
    }
    
    
    
    ///////////////////////////////////////same as one above but calls h2 where h1 is called 
    private void exspandNodeH2(){
        treeNode eNode = tree.poll();
        if(eNode != null){ //note if eNode is null that means that the tree is empty so out of ways to solve
            if(eNode.isGoalState()){ //means you found the best way to solve puzzle
                this.searchComplete = true;
                tree.clear();
                this.finalNode = eNode;
            }
            else {
                if(eNode.pos() != -1) {
                    //we know the node isn't invalid now make the childeren
                    treeNode up = new treeNode(eNode.state.MUp(), eNode.cost, (eNode.depth + 1), eNode, 'u');
                    if(up.pos() != -1) { //check to see if parent can have this child
                        up.cost+= eNode.state.cost(false);
                        up.hCost = up.h2();
                        tree.add(up);
                    }
                    treeNode down = new treeNode(eNode.state.MDown(), eNode.cost, (eNode.depth + 1), eNode, 'd');
                    if(down.pos() != -1){
                        down.cost+=eNode.state.cost(false);
                        down.hCost = down.h2();
                        tree.add(down);
                    }
                    treeNode left = new treeNode(eNode.state.MLeft(), eNode.cost, (eNode.depth + 1), eNode, 'l');
                    if(left.pos() != -1){
                        left.cost+=eNode.state.cost(false);
                        left.hCost = left.h2();
                        tree.add(left);
                    }
                    treeNode right = new treeNode(eNode.state.MRight(), eNode.cost, (eNode.depth + 1), eNode, 'r');
                    if(right.pos() != -1){
                        right.cost+=eNode.state.cost(false);
                        right.hCost = right.h2();
                        tree.add(right);
                    }
                    treeNode suck = new treeNode(eNode.state.MSuck(), eNode.cost, (eNode.depth + 1), eNode, 's');
                    if(suck.pos() != -1){
                        suck.cost+=eNode.state.cost(true);
                        suck.hCost = suck.h2();
                        tree.add(suck);
                    }   
                }
                else
                    System.out.print("YOU PULLED AN INVALID STATE");
            }
        }
        else
            this.searchComplete = true;
    }
   

 
////////////////////////////////////////////////////////////////////////////////
//////                  User exspand methods                            ////////
//////          supports expands n number of times or until finished    ////////
//////              both return number of nodes exspanded               ////////
//////              see next section to read results                    ////////
////////////////////////////////////////////////////////////////////////////////
   public int Exspand(int n) {
       int i = 0;
       while(!this.searchComplete && i < n) {
           this.exspandNode();
           i++;
       }
       return i;
   }
   
   //returns a count of node exspansions
   public int solve(){
       int count = 0;
       while(!this.searchComplete) {
           this.exspandNode();
           count++;
       }
       return count;
   }
   
   public int solveH2(){
       int count = 0;
       while(!this.searchComplete) {
           this.exspandNodeH2();
           count++;
       }
       return count;
   }
   
   
   
   /////////////////////////////////////////////////////////////////////////////
   //////           Interpret results methods                             //////
   /////////////////////////////////////////////////////////////////////////////
   
   
   //a string array each with the list of instructions
   //returns a size 1 string with "not complete yet" or 
   // "no path to solution 
   public String[] getPath(){
       if(this.searchComplete){
           if(this.finalNode != null) {
                int len = this.finalNode.depth+1;
                String[] path = new String[len];
                treeNode temp = this.finalNode;
                for(int i = len-1; i > 0; i--){
                   path[i] = this.moveString(temp.action);
                   temp = temp.daddy;
                }
                path[0] = this.moveString(this.InitalState.action);
                return path;
            }
           String[] out = new String[1];
           out[0] = "Search Finished and no solution found";
           return out;
        }
       String[] out = new String[1];
       out[0] = "Search not complete yet";
       return out;
   }
   
   //returns an array of size one with value -1 if cant be done or -100 if unsolvable 
   //not final value is total value
   //have to increment to get step costs. 
   public int[] getCosts(){
       if(this.searchComplete){
           if(this.finalNode != null){
               int len = this.finalNode.depth + 1;
               int[] out = new int[len];
               treeNode temp = this.finalNode;
               for(int i = len -1; i > 0; i--){
                   out[i] = temp.cost;
                   temp = temp.daddy;
               }
               out[0] = 0;
               return out;
               
           }
           else{
               int[] out = new int[1];
               out[0] = -100;
               return out;
           }
           
       }
       else{
           int[] out = new int[1];
           out[0] = -1;
           return out;
       }
   }
   
   
   private String moveString(char c){
       if(c == 'u')
           return "Up";
       if(c=='d')
           return "Down";
       if(c=='l')
           return "Left";
       if(c=='r')
           return "Right";
       if(c == 's')
           return "Suck";
       if(c == 'i')
           return "Start";
       return "invalid";
               
   }
   
}
