package teams;

import agents.RandomAgent;
import core.Agent;
import core.Team;


import java.util.ArrayList;
import java.util.HashMap;


// 乱数選択チーム
public class RandomTeam implements Team {
    private String teamName = "Random";
    private ArrayList<Agent> agents = new ArrayList<>();


    public RandomTeam(){
        Agent agentA = new RandomAgent("randA");
        agents.add(agentA);
        Agent agentB = new RandomAgent("randB");
        agents.add(agentB);
    }

    public void before(){

    };

    @Override
    public void after(HashMap<String, Integer> actions, HashMap<String, Integer> wins){
        /*
        for(HashMap.Entry<String, Integer> s : wins.entrySet()) {
            System.out.println(s.getKey() + ":" + s.getValue());
        }
        */
    };

    @Override
    public ArrayList<Integer> getAgentActions(){
        ArrayList<Integer> actions = new ArrayList<>();
        for(int i = 0; i < agents.size(); i++){
            actions.add(agents.get(i).getAction());
        }
        return actions;
    };


    public String getTeamName(){ return teamName;}

    public ArrayList<String> getAgentNames(){
        ArrayList<String> names = new ArrayList<>();
        for(int i = 0; i < agents.size(); i++){
            names.add(agents.get(i).getName());
        }
        return names;
    }
}
