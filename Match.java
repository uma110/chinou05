import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class Match {
	//rrrrr
    public static void main(String arg[]){
		if (arg.length == 1 || arg.length == 2) {
			try { // ファイル読み込みに失敗した時の例外処理のためのtry-catch構文
				String fileName = "database.txt"; // ファイル名指定

				// 文字コードUTF-8を指定してBufferedReaderオブジェクトを作る
				BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), "UTF-8"));

				ArrayList<HashMap<String, String>> hashMaps = new ArrayList<>();
				// 変数lineに1行ずつ読み込むfor文
				for (String line = in.readLine(); line != null; line = in.readLine()) {
					System.out.println((new Unifier()).unify(arg[0], line,hashMaps));
				}
				in.close(); // ファイルを閉じる
				if(arg.length == 2){

					ArrayList<HashMap<String, String>> ansHashMaps = new ArrayList<>();

					//arg一行目で変数束縛をしない場合、arraylistに空のmapを入れる
					if(hashMaps.isEmpty()) hashMaps.add(new HashMap<>());
					//引数2つ目の処理を書く
					for(Iterator<HashMap<String, String>> iterator=hashMaps.iterator();iterator.hasNext();){
							HashMap<String, String> vars = (HashMap<String, String>)iterator.next();
							in = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), "UTF-8"));
							// 変数lineに1行ずつ読み込むfor文
							for (String line = in.readLine(); line != null; line = in.readLine()) {
								System.out.println((new Unifier()).unify(arg[1], line,vars,ansHashMaps));
							}
							in.close();
					}
					System.out.println(ansHashMaps.toString());
				}else{
					System.out.println(hashMaps.toString());
				}

			} catch (IOException e) {
				e.printStackTrace(); // 例外が発生した所までのスタックトレースを表示
			}
		}
		else{
			System.out.println("***error*** arg typing miss");
		}
    }
}