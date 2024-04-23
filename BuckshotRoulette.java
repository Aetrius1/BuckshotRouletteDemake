//Created by Jacob Shelton. 
//VERSION 1.0 completed on 4/23/2024. 

import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;


public class BuckshotRoulette {
    public Scanner keyboard = new Scanner(System.in);
    List<String> playerInv = new ArrayList<>();
    List<String> dealerInv = new ArrayList<>();
    public String input = null;
    public int playerHealth;
    public int dealerHealth;
    public String[] items = {"Saw", "Cigarettes", "Beer", "Magnifying Glass", "Handcuffs"};
    public ArrayList chamber = new ArrayList();
    public int phase;
    public int round;
    
    public String getInput(){
        input = keyboard.nextLine().toLowerCase();
        return input;
    }
    
    
    public void beginPhase(){
        phase+=1;
        round = 0;
        playerHealth = 0;
        dealerHealth = 0;                       
        for (int i = 0; i < phase; i++){            //Sets health for the phase.
            playerHealth += 2;
            dealerHealth += 2;
        }
        
        System.out.println("PHASE " + phase + ":");
        System.out.println("You and the dealer both begin with " + playerHealth + " health.\n");

        beginRound();
    }
    
    public void beginRound(){
        int live = 0, blank = 0;
        int itemCount = 0;
        round+=1;
        
        
        if (phase == 1){
            itemCount = 0;
            
            switch (round){
                case 1: 
                    live = 1; blank = 2;
                    break;
                case 2:
                    live = 3; blank = 2;
                    break;
                default:
                    live = 4; blank = 2;
                    break;
            }    
        }
        else if (phase == 2){
            itemCount = 2;
            
            switch (round){
                case 1:
                    live = 1; blank = 1;
                    break;
                case 2:
                    live = 2; blank = 2;
                    break;
                case 3:
                    live = 3; blank = 2;
                    break;
                case 4:
                    live = 3; blank = 3;
                    break;
                default:
                    live = 5; blank = 3;
                    break;
            }    
        }
        else if (phase == 3){
            itemCount = 4;
            
            switch (round){
                case 1:
                    live = 1; blank = 2;
                    break;
                case 2:
                    live = 4; blank = 4;
                    break;
                case 3:
                    live = 3; blank = 2;
                    break;
                case 4:
                    live = 4; blank = 2;
                    break;
                default:
                    live = 3; blank = 1;
                    break;
            }    
        }
        System.out.print("ROUND " + round + ":\n");
        
        String s1 = "";             //All this just checks if "shell" needs to be plural.
        String s2 = "";
        if (live > 1){
            s1 = "S";
        }
        if (blank > 1){
            s2 = "S";
        }
        System.out.println(live + " LIVE SHELL" + s1 + ". " + blank + " BLANK" + s2 + ".");
        
        insertShells(live, blank);
        addToInventories(itemCount);
        playerTurn();
        //FINISH    
    }
    
    public void insertShells(int li, int bl){    //live and blank counts.
        int shellAmt = li + bl;
        chamber.clear();                        //Makes sure chamber is empty.
        for (int i = 0; i < shellAmt; i++){
   
            chamber.add((int) (Math.random() * 2));
        
        }
        
        while (chamber.size() > 8){
            chamber.remove(8);
        }
        while (Collections.frequency(chamber, 1) > li || Collections.frequency(chamber, 0) > bl){   //activates if chamber has the incorrect amount of lives/blanks.
            chamber.set((int) (Math.random() * shellAmt), (int) (Math.random() * 2));   //This might be an incredibly inefficient fix, it might be fine. I dunno, man.
        }        

        System.out.println(chamber);          //Print for dev purposes. Remove in final.
    }
    
    
    
    
    public void addToInventories(int amt){
        for (int i = 0; i < amt; i++){
            playerInv.add(items[(int) (Math.random() * items.length)]);
            dealerInv.add(items[(int) (Math.random() * items.length)]);
        }
    }
    
    
    public void playerTurn(){
        
        for (int i = 0; i < playerInv.size(); i++) {
//          System.out.println((i+1) + ". " + playerInv.get(i));
        }
        int health = playerHealth;                              //Tracks whether or not health has been lost.

        while (health == playerHealth){
            System.out.println("Your turn. What do you do? Press ENTER without "
                + "typing anything to ready the shotgun.");
            getInput();                
            
            switch (input){
                case (""):
                    System.out.println("You pick up the shotgun. Where do you aim "
                            + "it?\n1. DEALER\t\t\t2.YOURSELF");
                    getInput();
                    if (input.equals("1")){                                     //Shooting the Dealer
                        System.out.println("You squeeze the trigger...");
                        if (chamber.get(0).equals(1)){
                            dealerHealth -= 1;
                            System.out.println("BANG!");
                            System.out.println("DEALER: " + dealerHealth + " HEALTH.\t\tYOU: " + playerHealth + " HEALTH."); 

                            chamber.remove(0);
                            health += 1;                //Artificially makes health and playerHealth different so it moves on to dealerTurn without losing health.

                        }
                        else {
                            System.out.println("*click!*");
                            chamber.remove(0);
                            health += 1;                    
                        }
                    }
                    else if (input.equals("2")){                                //Shooting yourself
                        System.out.println("You squeeze the trigger...");
                        if (chamber.get(0).equals(1)){
                            playerHealth -= 1;
                            System.out.println("BA...!");
                            if(playerHealth != 0){
                                System.out.println("\n\nYou wake up, gasping. You've been brought back. The game continues.");
                                System.out.println("DEALER: " + dealerHealth + " HEALTH.\t\tYOU: " + playerHealth + " HEALTH."); 
                            }
                            chamber.remove(0);
                        }
                        else {
                            System.out.print("*click!*\nYou place the shotgun back down. ");
                            if (!chamber.isEmpty()){
                                System.out.println("You get another turn.\n");
                            }
                            chamber.remove(0);

                        }
                    }
                    else {
                        System.out.println("Invalid input. Please try again.");
                    
                    }
                    break;       
            }
            if (playerHealth > 0 && dealerHealth > 0 && chamber.isEmpty()){
                System.out.println("\nThe shotgun is empty. The dealer picks it up and begins to reload.");
                beginRound();
            }
        }    
        
        if (playerHealth <= 0){
            System.out.println("The last shot rings out. You have lost.\n\nGAME OVER. Restart the program to try again.");
            System.exit(0);
        }
        if (dealerHealth <= 0){
            System.out.println("The dealer pulls himself up from the floor, still wearing that crooked smile. A new phase begins.");
            beginPhase();
        }
        dealerTurn();
        
        
    }
    
    
    public void dealerTurn(){
        int health = dealerHealth;                              //Tracks whether or not health has been lost.
        System.out.println("DEALER'S TURN.");
        
        while (health == dealerHealth){
            int chooseWho = 0;
        
                /////////////////////                                  //IF DEALER HAS ITEMS
        
            if (dealerInv.isEmpty() && chamber.size() != 1){
                chooseWho = (int) (Math.random() * 2);
            }
            if (chamber.size() == 1){               //If there's only one left in the chamber, the Dealer knows what it is.
                chooseWho = (int) chamber.get(0);
            }
        //////////////////////////////                                  //FUNCTIONALITY FOR ITEM USAGE GOES HERE PROBABLY
            System.out.print("The dealer picks up the shotgun and ");
                
            if (chooseWho == 0){
                System.out.println("points it at himself...");
                if (chamber.get(0).equals(1)){
                    dealerHealth -= 1;
                    System.out.println("BANG!");
                    System.out.println("DEALER: " + dealerHealth + " HEALTH.\t\tYOU: " + playerHealth + " HEALTH."); 
                    chamber.remove(0);

                }
                else {
                    System.out.println("*click!*\nThe dealer grins. It's his turn again.");
                    chamber.remove(0);
                }

            }
            if (chooseWho == 1){
                System.out.println("aims it at you...");
                if (chamber.get(0).equals(1)){
                    playerHealth -= 1;
                    System.out.println("BA...!");
                    if(playerHealth != 0){
                        System.out.println("\n\nYou wake up, gasping. You've been brought back. The game continues.");
                        System.out.println("DEALER: " + dealerHealth + " HEALTH.\t\tYOU: " + playerHealth + " HEALTH."); 
                    }
                    chamber.remove(0);
                    health += 1;
                }
                else {
                    System.out.println("*click!*\nThe dealer places the shotgun back down.\n");
                    chamber.remove(0);
                    health += 1;
                }
            
            }
        }
            
            if (playerHealth > 0 && dealerHealth > 0 && chamber.isEmpty()){
                System.out.println("\nThe shotgun is empty. The dealer picks it up and begins to reload.");
                beginRound();
            }
            if (playerHealth <= 0){
                System.out.println("The last shot rings out. You have lost.\n\nGAME OVER. Restart the program to try again.");
                System.exit(0);
            }
            if (dealerHealth <= 0){
                System.out.println("The dealer pulls himself up from the floor, still wearing that crooked smile. A new phase begins.");
                beginPhase();
            }
            playerTurn();
            
        
    }
    
/*  public void takeDamage(boolean sawUsed){
        int damage = 1;
        if (sawUsed){
            damage = 2;
        }
        
    }   */
    
    public static void main(String[] args) {

        BuckshotRoulette davidHirsch = new BuckshotRoulette();        
                
        System.out.println("You awaken.\n");  
        
        davidHirsch.beginPhase();
    }
}
