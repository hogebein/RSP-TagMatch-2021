# 特別実験課題

強化学習エージェント同士で2vs2のじゃんけん大会を行います。
（勝・勝→勝利、勝・敗、引引→引き分け、敗・敗→敗北）

実行環境  
  Ubuntuで動作確認済み
　Java8以降なら動くはず  

コンパイル（ビルド）  
$ javac Application.java
$ java Application

実行オプション  
`--debug` ：　コンソール上にも結果を表示します。

概要  
　2チーム，エージェント4体による2vs2のじゃんけんを行います。
　登録チームの組合せ✕10,000回✕5セット分の対戦を行います。
　実行後は，各ラウンドにおける各エージェントの累計勝利数がcsvとして出力されます。

チーム・エージェントの登録  
　チーム・エージェントは"core"内にある"Team"・"Agent"インターフェースに準拠してください。
　（デフォルトで用意してある"SampleTeam"，"SampleAgent"などをご参考に）
　作成したチームは"teams"，エージェントは"agents"に入れてください。
　実行時には，参加させるチームを"RSPArena"にimportし，"run()"にある"Team[] teams"の宣言に書き加えてください。
