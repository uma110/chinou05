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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;

public class Match {
	public static final String datafileName = "database.txt";
	public static void Serch(String arg[]) {
		if (arg.length == 1 || arg.length == 2) {
			try { // ファイル読み込みに失敗した時の例外処理のためのtry-catch構文
				String fileName = datafileName; // ファイル名指定

				// 文字コードUTF-8を指定してBufferedReaderオブジェクトを作る
				BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), "UTF-8"));

				ArrayList<HashMap<String, String>> hashMaps = new ArrayList<>();
				// 変数lineに1行ずつ読み込むfor文
				for (String line = in.readLine(); line != null; line = in.readLine()) {
					System.out.println((new Unifier()).unify(arg[0], line, hashMaps));
				}
				in.close(); // ファイルを閉じる
				if (arg.length == 2) {

					ArrayList<HashMap<String, String>> ansHashMaps = new ArrayList<>();

					// arg一行目で変数束縛をしない場合、arraylistに空のmapを入れる
					if (hashMaps.isEmpty())
						hashMaps.add(new HashMap<>());
					// 引数2つ目の処理を書く
					for (Iterator<HashMap<String, String>> iterator = hashMaps.iterator(); iterator.hasNext();) {
						HashMap<String, String> vars = (HashMap<String, String>) iterator.next();
						in = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), "UTF-8"));
						// 変数lineに1行ずつ読み込むfor文
						for (String line = in.readLine(); line != null; line = in.readLine()) {
							System.out.println((new Unifier()).unify(arg[1], line, vars, ansHashMaps));
						}
						in.close();
					}
					System.out.println(ansHashMaps.toString());
				} else {
					System.out.println(hashMaps.toString());
				}

			} catch (IOException e) {
				e.printStackTrace(); // 例外が発生した所までのスタックトレースを表示
			}
		} else {
			System.out.println("***error*** arg typing miss");
		}
	}

	public static void Add(){
		Scanner scanner = new Scanner(System.in);
		try{
			String filename = datafileName;
			PrintWriter out = new PrintWriter(
					new OutputStreamWriter(new FileOutputStream(filename,true),"UTF-8"));
			out.println();
			while(true){
				System.out.println("Write data|If you write 'end',Stop add data");

				String data = scanner.nextLine();

				if("end".equals(data))break;

				out.println(data);
			}
			out.close();
		}catch(IOException e){
			e.printStackTrace();
		}
	}

	public static void Delete(){
		Scanner scanner = new Scanner(System.in);
		System.out.println("Write Delete Data");
		String deleteData = scanner.nextLine();
		String filename = datafileName;
		String tmpFilename = filename+"tmp";
		try {
			PrintWriter out = new PrintWriter(new OutputStreamWriter(new FileOutputStream(tmpFilename), "UTF-8"));
			BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(filename), "UTF-8"));
			for (String line = in.readLine(); line != null; line = in.readLine()) {
				if(!line.equals(deleteData)) {
					System.out.println("**********");
					out.println(line);
				}
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

	public static void main(String arg[]) {
		Scanner stdin = new Scanner(System.in);
		while (true) {
			System.out.println("Choose your command");
			System.out.println("1->Add 2->Serch 3->Delete 4->Quit");
			int command = stdin.nextInt();
			switch (command) {
			case 1:
				Add();
				break;
			case 2:
				Serch(arg);
				break;
			case 3:
				Delete();
				break;
			case 4:
				stdin.close();
				return;
			default:
				System.out.println("INVALID COMMAND");
			}
		}
	}
}