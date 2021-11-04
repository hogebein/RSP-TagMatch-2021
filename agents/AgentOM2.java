package agents;

import java.util.Random;
import core.Agent;

public class AgentOM2 implements Agent {
    private final double WIN1_REWARD = 0.2;
    private final double DRAW_REWARD = 0.8;
    private final double LOSE1_REWARD = -1.0;
    
    private String name = new String();

    private final double DRAW_JUDGE = 0.45; //引き分け判定にする基準

    private double[][][] Q;             //Q関数
    private double[] pi;

    private int s, s_;        //状態s
    private int allyA;      //味方の行動a
    private int a;
    private double r;

    private final double T_E = 0.1;     //温度パラメータの最終値
    private final double ALPH_E = 0.01; //学習率の最終値

    private double alph;                //学習率
    private double alphd;               //学習率の減衰率

    private double gamm;                //割引率

    private double T;                   //ボルツマン選択の温度パラメータ
    private double Td;                  //温度パラメータの減衰率

    private Random rand;

    public AgentOM2(String name){
        this.name = name;
    }

    public void init(int step){
        rand = new Random();

        this.Q = new double[6][3][3];

        this.s = -1;
        this.s_ = -1;

        this.alph = 0.5;
        this.gamm = 0.0;
        this.alphd = Math.pow(ALPH_E/alph, 1.0/step);
        this.T = 1.0;
        this.Td = Math.pow(T_E/T, 1.0/step);
    }


    @Override
    public String  getName(){ return this.name; }

    @Override
    public int getAction() {
        if(this.s == -1){
            this.a = this.allyA;
            return this.a;
        }

        if(this.pi[this.allyA] > DRAW_JUDGE){
            this.a = this.allyA;
            return this.a;
        }

        double div=0;
        double[] myPi = new double[3];
        for(int i=0; i<3; i++){
            myPi[i] = Math.exp(Q[this.s][this.allyA][i]/this.T);
            div += myPi[i];
        }

        double n = this.rand.nextDouble();
        double sum = 0.0;
        for(int i=0; i<3; i++){
            sum += myPi[i]/div;
            if(n < sum){
                this.a = i;
                return this.a;
            }
        }

        this.a = this.rand.nextInt(3);
        return this.a;
    }

    public void Observation(int ...S_){
        this.s_ = (1<<S_[0] | 1<<S_[1]) - 1;
        this.r = calcReward(S_[2], S_[3]);

        UpdateQ();

        this.s = this.s_;
    }

    // 報酬の計算を行う関数　（自身だけのエージェントにおける相手二人に対する勝敗の判定）
    private double calcReward(int draw, int win){
        if(draw == 1) return DRAW_REWARD;
        if(win == 1) return WIN1_REWARD;
        else return LOSE1_REWARD;
    }

    //味方の手を観測する
    public void ObservateAlly(int allyA){ this.allyA = allyA; }

    public void UpdateQ(){
        if(this.s  == -1) return;

        double maxQ = Q[this.s_][this.allyA][0];
        for(int i=1; i<3; i++){
            double tmp =  Q[this.s_][this.allyA][i];
            if(maxQ < tmp) maxQ = tmp;
        }
        this.Q[this.s][allyA][this.a] = (1 - this.alph)*this.Q[this.s][this.allyA][this.a] + this.alph*(this.r + this.gamm*maxQ);

        this.alph *= this.alphd;
        this.T *= this.Td;
    }

    //味方のQテーブルを取得する
    public void setPi(double[] pi){ this.pi = pi; }
}
