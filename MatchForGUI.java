import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class MatchForGUI {
	public static final String datafileName = "data.txt";

	public static String Search(String searchData[]) {
		if (searchData.length == 1 || searchData.length == 2) {
			try { // ファイル読み込みに失敗した時の例外処理のためのtry-catch構文
				String fileName = datafileName; // ファイル名指定

				// 文字コードUTF-8を指定してBufferedReaderオブジェクトを作る
				BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), StandardCharsets.UTF_8));

				ArrayList<HashMap<String, String>> hashMaps = new ArrayList<>();
				// 変数lineに1行ずつ読み込むfor文
				for (String line = in.readLine(); line != null; line = in.readLine()) {
					System.out.println((new Unifier()).unify(searchData[0], line, hashMaps));
				}
				in.close(); // ファイルを閉じる
				if (searchData.length == 2) {

					ArrayList<HashMap<String, String>> ansHashMaps = new ArrayList<>();

					// arg一行目で変数束縛をしない場合、arraylistに空のmapを入れる
					if (hashMaps.isEmpty())
						hashMaps.add(new HashMap<>());
					// 引数2つ目の処理を書く
					for (Iterator<HashMap<String, String>> iterator = hashMaps.iterator(); iterator.hasNext();) {
						HashMap<String, String> vars = (HashMap<String, String>) iterator.next();
						in = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), StandardCharsets.UTF_8));
						// 変数lineに1行ずつ読み込むfor文
						for (String line = in.readLine(); line != null; line = in.readLine()) {
							System.out.println((new Unifier()).unify(searchData[1], line, vars, ansHashMaps));
						}
						in.close();
					}
					System.out.println(ansHashMaps.toString());
					return ansHashMaps.toString();
				} else {
					System.out.println(hashMaps.toString());
					return hashMaps.toString();
				}
			} catch (IOException e) {
				e.printStackTrace(); // 例外が発生した所までのスタックトレースを表示
			}
		}
		System.out.println("***error*** arg typing miss");
		return null;
	}

	public static void Add(String[] addData){
		try{
			String filename = datafileName;
			PrintWriter out = new PrintWriter(
					new OutputStreamWriter(new FileOutputStream(filename,true),StandardCharsets.UTF_8));
			out.println();
			for(String str:addData){
				out.println(str);
			}
			out.close();
		}catch(IOException e){
			e.printStackTrace();
		}
	}

	public static void Delete(String deleteData){
		String filename = datafileName;
		String tmpFilename = filename+"tmp";
		try {
			PrintWriter out = new PrintWriter(new OutputStreamWriter(new FileOutputStream(tmpFilename), StandardCharsets.UTF_8));
			BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(filename), StandardCharsets.UTF_8));
			int blankCount = 0;
			for (String line = in.readLine(); line != null; line = in.readLine()) {
				/*
				if(line.isEmpty()){
					blankCount++;
					if(blankCount < 2 && !line.equals(deleteData)) {
						out.println(line);
						blankCount = 0;
					}
				}else{
					if(!line.equals(deleteData)){
						out.println(line);
					}
					blankCount = 0;
				}*/

				if(!line.equals(deleteData)) out.println(line);
			}
			out.close();
			in.close();
			File copyFile = new File(tmpFilename);
			File srcFile = new File(filename);
			if(srcFile.exists()){
				srcFile.delete();
			}
			System.out.println(copyFile.renameTo(srcFile));
		} catch (UnsupportedEncodingException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
	}
}
