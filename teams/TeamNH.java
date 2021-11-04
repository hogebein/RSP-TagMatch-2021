//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package teams;

import agents.HayashiAgent;
import agents.NakamuraAgent;
import core.Team;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TeamNH implements Team {
    private String teamName = "Nakamura-Hayashi";
    private NakamuraAgent nakamuraAgent = new NakamuraAgent("Nakamura");
    private HayashiAgent hayashiAgent = new HayashiAgent("Hayashi");
    String opponentNameA;
    String opponentNameB;
    private ArrayList<Integer> actionEpisodeA = new ArrayList();
    private ArrayList<Integer> actionEpisodeB = new ArrayList();

    private static int episodeSize = 2;

    public TeamNH() {
    }

    public void before() {
        this.hayashiAgent.before(this.nakamuraAgent.getAction());
    }

    public void after(HashMap<String, Integer> actions, HashMap<String, Integer> wins) {

        HashMap<String, Integer> ourActions = new HashMap<>(), opponentActions = new HashMap<>();

        for(Map.Entry<String, Integer> s: actions.entrySet()){
            if(opponentNameA == null || opponentNameB == null){
                if(s.getKey()!=nakamuraAgent.getName() && s.getKey()!=hayashiAgent.getName()){
                    if(opponentNameA == null) opponentNameA = s.getKey();
                    if(s.getKey()!=opponentNameA) opponentNameB = s.getKey();
                }
            }
            if(s.getKey()==nakamuraAgent.getName() || s.getKey()==hayashiAgent.getName()) ourActions.put(s.getKey(), s.getValue());
            else opponentActions.put(s.getKey(), s.getValue());
        }

        nakamuraAgent.after(ourActions, opponentActions, wins);

        for(Map.Entry<String, Integer> s: actions.entrySet()){
            if (s.getKey() == this.opponentNameA) {
                this.actionEpisodeA.add(s.getValue());
                if (this.actionEpisodeA.size() > episodeSize) {
                    this.actionEpisodeA.remove(0);
                }
            }
            if (s.getKey() == this.opponentNameB) {
                this.actionEpisodeB.add(s.getValue());
                if (this.actionEpisodeB.size() > episodeSize) {
                    this.actionEpisodeB.remove(0);
                }
            }

        }
        this.hayashiAgent.after(this.actionEpisodeA, this.actionEpisodeB);
    }

    public ArrayList<Integer> getAgentActions() {
        ArrayList<Integer> actions = new ArrayList();
        actions.add(this.nakamuraAgent.getAction());
        actions.add(this.hayashiAgent.getAction());
        return actions;
    }

    public String getTeamName() {
        return this.teamName;
    }

    public ArrayList<String> getAgentNames() {
        ArrayList<String> names = new ArrayList();
        names.add(this.nakamuraAgent.getName());
        names.add(this.hayashiAgent.getName());
        return names;
    }
}
