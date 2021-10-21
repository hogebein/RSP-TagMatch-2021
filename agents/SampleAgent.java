package agents;

import core.Agent;

// ただ同じ手を出し続けるだけのエージェント
public class SampleAgent implements Agent{
    private int ret;
    private String name;
    
    public SampleAgent(int ret, String name){
        this.ret = ret;
        this.name= name;
    }

    @Override
    public String  getName(){
        return name;
    }

    @Override
    public int getAction(){
        return ret;
    }


}