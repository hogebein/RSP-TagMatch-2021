package agents;

import java.util.Random;
import core.Agent;

public class AgentOM1 implements Agent {
    private final double WIN2_REWARD = 1.0;
    private final double WIN1_REWARD = 0.5;
    private final double DRAW_REWARD = 0.0;
    private final double LOSE1_REWARD = -0.5;
    private final double LOSE2_REWARD = -1.0;

    private String name;

    private final double ALPH_E = 0.01; //学習率の最終値
    private final double T_E = 0.1;     //温度パラメータの最終地

    private double[][] Q;               //Q関数

    private int s, s_;                  //状態s
    private int a;                      //行動a
    private double r;                   //報酬r

    private double T;                   //ボルツマン選択の温度パラメータ

    private double alph;                //学習率
    private double gamm;                //割引率

    private double alphd;               //学習率の減衰率
    private double Td;                  //温度パラメータの減衰率

    private Random rand;
    
    public AgentOM1(String name){
        this.name = name;
    }

    public void init(int step){
        rand = new Random();

        this.Q = new double[6][3];

        this.s = -1;
        this.T = 1.0;
        this.alph = 0.5;
        this.gamm = 0.2;
        this.alphd = Math.pow(ALPH_E/alph, 1.0/step);
        this.Td = Math.pow(T_E/T, 1.0/step);
        this.Td = Math.pow(T_E/T, 1.0/step);
    }

    @Override
    public String  getName(){ return this.name; }

    @Override
    public int getAction() {
        if(this.s == -1){
            this.a = this.rand.nextInt(3);
            return this.a;
        }

        double div=0;
        double[] pi = new double[3];
        for(int i=0; i<3; i++){
            pi[i] = Math.exp(Q[this.s][i]/T);
            div += pi[i];
        }

        double n = this.rand.nextDouble();
        double sum = 0.0;
        for(int i=0; i<3; i++){
            sum += pi[i]/div;
            if(n < sum){
                this.a = i;
                return this.a;
            }
        }

        this.a = this.rand.nextInt(3);
        return this.a;
    }

    public void Observation(int ...s_){
        this.s_ = (1<<s_[0] | 1<<s_[1]) - 1;
        this.r  = calcReward(this.a, s_[0], s_[1]);
        UpdateQ();
        this.s = this.s_;
    }

    public void UpdateQ(){
        if(this.s == -1) return;

        double maxQ = Q[this.s_][0];
        for(int i=1; i<3; i++){
            double tmp =  Q[this.s_][i];
            if(maxQ < tmp) maxQ = tmp;
        }
        this.Q[this.s][this.a] = (1 - this.alph)*this.Q[this.s][this.a] + this.alph*(this.r + this.gamm*maxQ);

        this.alph *= this.alphd;
        this.T *= this.Td;
    }

    // 報酬の計算を行う関数　（自身だけのエージェントにおける相手二人に対する勝敗の判定）
    private double calcReward(int myself, int opponent1, int opponent2){
        int[] hand = {myself, opponent1, opponent2};

        //引き分けの判定
        int draw = 0;
        for(int i=0; i<3; i++){
            for(int j=0; j<3; j++){
                if(hand[j]==i){
                    draw++;
                    break;
                }
            }
        }
        if(draw==3 || draw==1) return DRAW_REWARD;

        //勝ちの判定
        if( (hand[0]+1)%3 == hand[1] && hand[1] != hand[2] ) return WIN1_REWARD;
        if( (hand[0]+1)%3 == hand[2] && hand[1] != hand[2] ) return WIN1_REWARD;
        if( (hand[0]+1)%3 == hand[1] && hand[1] == hand[2] ) return WIN2_REWARD;

        //負けの判定
        if( hand[0] == (hand[1]+1)%3 && hand[1] != hand[2] ) return LOSE1_REWARD;
        if( hand[0] == (hand[2]+1)%3 && hand[1] != hand[2] ) return LOSE1_REWARD;
        return LOSE2_REWARD;
    }

    public double[] getPi(){
        if(this.s == -1){
            double third = 1/3;
            return new double[]{third, third, 1-2*third};
        }

        double div=0;
        double[] pi = new double[3];
        for(int i=0; i<3; i++){
            pi[i] = Math.exp(Q[this.s][i]/T);
            div += pi[i];
        }

        for(int i=0; i<3; i++) pi[i] /= div;

        return pi;
    }
}