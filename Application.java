import java.util.Arrays;

// 実行クラス
public class Application {
    public static void main(String[] args){
        boolean debug = Arrays.asList(args).contains("--debug");
        RSPArena bm = new RSPArena(debug);
        bm.run();
    }
}