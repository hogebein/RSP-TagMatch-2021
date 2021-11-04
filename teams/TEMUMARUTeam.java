package teams;

import agents.MaruAgent;
import agents.TemAgent;
import core.Agent;
import core.Team;

import java.util.ArrayList;
import java.util.HashMap;

// ただ同じ手を出し続けるだけのチーム
public class TEMUMARUTeam implements Team {


    private String teamName = "TEMUMARUTeam";
    private ArrayList<Agent> agents = new ArrayList<>();
   private ArrayList<Integer> hand = new ArrayList<Integer>();



    public TEMUMARUTeam(){
        Agent Maru = new MaruAgent("MaruAgent");
        agents.add(Maru);
        Agent Temu = new TemAgent("TemAgent");
        agents.add(Temu);
    }

    @Override
    public void before(){

    //新しく入った相手の手を学習したい
    

    }

    @Override
    public void after(HashMap<String, Integer> actions, HashMap<String, Integer> wins){
        /*
        for(HashMap.Entry<String, Integer> s : wins.entrySet()) {
            System.out.println(s.getKey() + ":" + s.getValue());
        }
        */

    // 相手の手を2個持ってきたい
    // if name != "TEMUMARUTeam" 手を追加
        for (String key : actions.keySet()) {
            if (key != teamName) hand.add(actions.get(key));
        }

        int maru_r = MaruAgent.Action(MaruAgent.eGreedy(),hand.get(0),hand.get(1));
        MaruAgent.updateQ(maru_r,MaruAgent.eGreedy());
        MaruAgent.updateS();

        int tem_r = TemAgent.Action(TemAgent.eGreedy(),hand.get(0),hand.get(1));
        TemAgent.updateQ(tem_r,TemAgent.eGreedy());
        TemAgent.updateS();

    }

    @Override
    public ArrayList<Integer> getAgentActions(){
        ArrayList<Integer> actions = new ArrayList<>();
        for(int i = 0; i < agents.size(); i++){
            if (i < agents.size()-1 && agents.get(i).getAction() == agents.get(i+1).getAction()) {
                actions.add((agents.get(i).getAction()+1)%3);
            }else{
                actions.add(agents.get(i).getAction());
            }

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
