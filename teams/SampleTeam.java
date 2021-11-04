package teams;

import agents.SampleAgent;
import core.Agent;
import core.Team;

import java.util.ArrayList;
import java.util.HashMap;

// ただ同じ手を出し続けるだけのチーム
public class SampleTeam implements Team {

    private String teamName = "Sample";
    private ArrayList<Agent> agents = new ArrayList<>();


    public SampleTeam(){
        Agent agentA = new SampleAgent(0, "sampleA");
        agents.add(agentA);
        Agent agentB = new SampleAgent(1, "sampleB");
        agents.add(agentB);
    }

    @Override
    public void before(){

    }

    @Override
    public void after(HashMap<String, Integer> actions, HashMap<String, Integer> wins){
        /*
        for(HashMap.Entry<String, Integer> s : wins.entrySet()) {
            System.out.println(s.getKey() + ":" + s.getValue());
        }
        */
    }

    @Override
    public ArrayList<Integer> getAgentActions(){
        ArrayList<Integer> actions = new ArrayList<>();
        for(int i = 0; i < agents.size(); i++){
            actions.add(agents.get(i).getAction());
        }
        return actions;
    }


    public String getTeamName(){ return teamName;}

    public ArrayList<String> getAgentNames(){
        ArrayList<String> names = new ArrayList<>();
        for(int i = 0; i < agents.size(); i++){
            names.add(agents.get(i).getName());
        }
        return names;
    }



}
