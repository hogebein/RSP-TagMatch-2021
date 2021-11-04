import java.io.*;
import java.util.*;

import core.Team;
import teams.*;


public class RSPArena {

    public static final int ROCK = 0;
    public static final int SCISSORS = 1;
    public static final int PAPER = 2;

    public static final int LOSE = 0;
    public static final int DRAW = 1;
    public static final int WIN = 2;


    private boolean debug;

    public RSPArena(boolean debug) {
        this.debug = debug;
        init();
    }

    // Results内を初期化
    private void init(){
        File results = new File("./Results");
        if(!results.exists()) results.mkdir();
        File[] files = results.listFiles();
        for(int i=0; i<files.length; i++) {
            if(files[i].exists() == false) {
                continue;
            } else if(files[i].isFile()) {
                files[i].delete();
            }
        }
    }

    // 1対1のじゃんけん　現状用なし
    public int RSP(int a, int b) {
        boolean win = ((a == ROCK) && (b == SCISSORS)) || ((a == SCISSORS) && (b == PAPER)) || ((a == PAPER) && (b == ROCK));
        boolean draw = (a == b);

        if(win) return WIN;
        else if(draw) return DRAW;
        else return LOSE;
    }

    // 2対2のじゃんけん
    public HashMap<String, Integer> RSP2v2(Team A, Team B, HashMap<String, Integer> actions) {

        HashMap<String, Integer> wins = new HashMap<>();
        for (HashMap.Entry<String, Integer> s : actions.entrySet()) wins.put(s.getKey(), 0);

        int[] actionDist = new int[3];
        if(actions.containsValue(ROCK)) actionDist[ROCK]++;
        if(actions.containsValue(SCISSORS)) actionDist[SCISSORS]++;
        if(actions.containsValue(PAPER)) actionDist[PAPER]++;

        // あいこ判定
        boolean threeActions = actionDist[ROCK]!=0 && actionDist[SCISSORS]!=0 && actionDist[PAPER]!=0;
        boolean allSame = actionDist[ROCK]==actions.size() || actionDist[SCISSORS]==actions.size() || actionDist[PAPER]==actions.size();
        if(threeActions || allSame) return wins;

        int winner = -1;
        if(actionDist[ROCK]==0) winner = SCISSORS;
        if(actionDist[SCISSORS]==0) winner = PAPER;
        if(actionDist[PAPER]==0) winner = ROCK;

        for (HashMap.Entry<String, Integer> s : actions.entrySet()) {
            if(s.getValue()==winner) wins.replace(s.getKey(), 1);
        }

        return wins;
    }

    // 各エージェントの行動を取得
    private HashMap<String, Integer> getActions(Team A, Team B){

        HashMap<String, Integer> actions = new HashMap<>();

        List<String> nameA = A.getAgentNames();
        List<Integer> actionA = A.getAgentActions();
        for(int i = 0; i < nameA.size(); i++) actions.put(nameA.get(i), actionA.get(i));

        List<String> nameB = B.getAgentNames();
        List<Integer> actionB = B.getAgentActions();
        for(int i = 0; i < nameB.size(); i++) actions.put(nameB.get(i), actionB.get(i));

        return actions;
    }

    // 結果のリストをcsvに（課題特化ハードコード）
    private void resultToCSV(Team A, Team B, List<HashMap<String, Integer>> scores, int set){
        try{
            String a1 =  A.getAgentNames().get(0), a2 =  A.getAgentNames().get(1);
            String b1 =  B.getAgentNames().get(0), b2 =  B.getAgentNames().get(1);

            File file = new File("./Results/" + A.getTeamName() + " vs " + B.getTeamName() + " - " + set + ".csv");
            PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file,true)));

            pw.printf("round,%s,%s,%s,%s,%s,%s\n", a1, a2, b1, b2, A.getTeamName(), B.getTeamName());
            if(this.debug) System.out.println("---" + A.getTeamName() + " vs " + B.getTeamName() + "---");
            int round = 1;
            for(HashMap<String, Integer> s : scores){
                pw.printf("%d,%d,%d,%d,%d,%d,%d\n", round, s.get(a1), s.get(a2), s.get(b1), s.get(b2), s.get(a1) + s.get(a2), s.get(b1) + s.get(b2));
                if(this.debug) System.out.printf("第%d戦　%s:%d %s:%d %s:%d %s:%d チームスコア：%s:%d %s:%d\n", round,
                        a1, s.get(a1), a2, s.get(a2), b1, s.get(b1), b2, s.get(b2),
                        A.getTeamName(), s.get(a1) + s.get(a2), B.getTeamName(), s.get(b1) + s.get(b2));
                round++;
            }
            pw.close();

        }catch (IOException e){
            e.printStackTrace();
        }
    }

    // 対戦し，結果を更新
    private void match(Team A, Team B, List<HashMap<String, Integer>> scores) {

        // 事前処理
        A.before();
        B.before();

        // じゃんけん
        HashMap<String, Integer> actions = getActions(A, B);
        HashMap<String, Integer> wins = RSP2v2(A, B, actions);

        // 事後処理
        A.after(actions, wins);
        B.after(actions, wins);

        // 記録
        if (scores.isEmpty()) {
            scores.add(wins);
        }else{
            HashMap<String, Integer> total = new HashMap<>();
            for (HashMap.Entry<String, Integer> s : scores.get(scores.size() - 1).entrySet()) {
                total.put(s.getKey(), s.getValue() + wins.get(s.getKey()));
            }
            scores.add(total);
        }
    }

    // 総当り戦の開催
    public void run() {

        // ここにチームインスタンスを置く(注：名前は重複しないように！！！)
        Team[] teams = {new TEMUMARUTeam(), new TeamOM(), new TeamNH()};
        int rounds = 10000, sets = 5;

        for (int i = 0; i < teams.length; i++) {
            for (int j = i + 1; j < teams.length; j++) {
                for(int k = 0; k < sets; k++){
                    Team A = teams[i];
                    Team B = teams[j];
                    List<HashMap<String, Integer>> scores = new ArrayList<>();
                    for (int l = 0; l < rounds; l++) {
                        match(A, B, scores);
                    }
                    resultToCSV(A, B, scores, k+1);
                }
            }
        }

    }

}
