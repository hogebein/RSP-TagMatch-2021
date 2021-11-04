package core;

import java.util.ArrayList;
import java.util.HashMap;

public interface Team {

    // 前処理
    public void before();

    // 後処理 action：各エージェントの手，wins：各エージェントの勝数
    public void after(HashMap<String, Integer> actions, HashMap<String, Integer> wins);

    // 次の手を取得
    public ArrayList<Integer> getAgentActions();

    public String getTeamName();

    public ArrayList<String> getAgentNames();


}
