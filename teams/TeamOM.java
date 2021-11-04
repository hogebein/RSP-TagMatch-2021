package teams;

import java.util.ArrayList;
import java.util.HashMap;

import agents.AgentOM1;
import agents.AgentOM2;
import core.Team;

public class TeamOM implements Team{
    public static final int ROCK = 0;
    public static final int SCISSORS = 1;
    public static final int PAPER = 2;

    public static final int LOSE = 0;
    public static final int DRAW = 1;
    public static final int WIN = 2;

    int m;

    private String teamName = "OM";

    AgentOM1 agent1;
    AgentOM2 agent2;
    
    public TeamOM(){
        agent1 = new AgentOM1("OM1");
        agent2 = new AgentOM2("OM2");
        init();
    }

    public void init() {
        agent1.init(20000);
        agent2.init(30000);
        m=0;
    }

    @Override
    public void before() {
        agent2.setPi(agent1.getPi());;
    }

    @Override
    public ArrayList<Integer> getAgentActions() {
        int action1 = agent1.getAction();
        agent2.ObservateAlly(action1);
        int action2 = agent2.getAction();

        ArrayList<Integer> actions = new ArrayList<>();
        actions.add(action1);
        actions.add(action2);
        return actions;
    }

    @Override
    public void after(HashMap<String, Integer> actions, HashMap<String, Integer> wins) {

        String[] keys;
        HashMap<String, Integer> a = new HashMap<>(actions);

        int draw = wins.containsValue(1) ? 0:1;
        int win2 = wins.get(agent2.getName()); 

        a.remove(agent1.getName());
        a.remove(agent2.getName());
        keys = a.keySet().toArray(new String[2]);

        agent1.Observation(a.get(keys[0]), a.get(keys[1]));
        agent2.Observation(a.get(keys[0]), a.get(keys[1]), draw, win2);
    }

    @Override
    public String getTeamName() {
        return teamName;
    }

    @Override
    public ArrayList<String> getAgentNames() {
        ArrayList<String> names = new ArrayList<>();
        names.add(agent1.getName());
        names.add(agent2.getName());
        return names;
    }
}
