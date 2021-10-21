package agents;

import core.Agent;

import java.util.Random;

public class RandomAgent implements Agent {
    private String name;
    private Random random;


    public RandomAgent(String name){
        this.name= name;
        this.random = new Random();
    }

    @Override
    public String  getName(){
        return this.name;
    }

    @Override
    public int getAction(){
        return this.random.nextInt(3);
    }
}
