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
import java.util.Scanner;

public class QuestionSystem {
	SemanticNet sn;
	ArrayList<Link> query;
	public static String datafileName = "information.txt";
	public QuestionSystem() {
		// TODO 自動生成されたコンストラクター・スタブ
		sn = new SemanticNet();
		query = new ArrayList<Link>();
	}
	public void addData(String data,boolean isSaveText){
		String[] splitData = strSplit3line(data);
		if(splitData == null){
			System.out.println("data is invalid");
			return;
		}
		else{
			makeLink(splitData[0], splitData[1], splitData[2]);
			if(isSaveText){
				try{
					PrintWriter out = new PrintWriter(
							new OutputStreamWriter(new FileOutputStream(datafileName,true),StandardCharsets.UTF_8));
					out.println();

					String output = splitData[0]+" "+splitData[1]+" "+splitData[2];
					System.out.println(output);
					out.println(output);
					out.close();
				}catch(IOException e){
					e.printStackTrace();
				}
			}
		}
	}

	public void addData(String[] data,boolean isSaveText){
		try {
			PrintWriter out = new PrintWriter(
					new OutputStreamWriter(new FileOutputStream(datafileName,true),StandardCharsets.UTF_8));
			System.out.println(data.length);
			for(String str:data){
				String[] splitData = strSplit3line(str);
				System.out.print(str);
				if(splitData == null){
					//System.out.println("data is invalid");
					continue;
				}else{
					makeLink(splitData[0], splitData[1], splitData[2]);
					if(isSaveText){
						String output = splitData[0]+" "+splitData[1]+" "+splitData[2];
						System.out.println(output);
						out.println(output);
					}
				}
			}
			System.out.println();
			out.close();
		} catch (FileNotFoundException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
	}

	public void makeLink(String p,String s,String o){
		sn.addLink(new Link(p, s, o,sn));
	}

	public static String[] strSplit3line(String str){
		String[] value = str.trim().split("\\s+",0);
		if(value.length != 3) return null;
		return value;
	}

	public void DeleteData(String deleteData){

		String filename = datafileName;
		String tmpFilename = filename+"tmp";
		try {
			PrintWriter out = new PrintWriter(new OutputStreamWriter(new FileOutputStream(tmpFilename), StandardCharsets.UTF_8));
			BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(filename), StandardCharsets.UTF_8));
			for (String line = in.readLine(); line != null; line = in.readLine()) {
				line = line.trim();
				if(!line.equals(deleteData)) out.println(line);
				System.out.println("***"+line+"***");
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

	public void addQuery(Link link){
		query.add(link);
	}

	public void addQuery(String str){
		String[] splitStr = strSplit3line(str);
		if(splitStr == null){
			System.out.println(str+" is invalid");
			return;
		}
		query.add(new Link(splitStr[0], splitStr[1], splitStr[2]));
	}

	public void ResetQuery(){
		query.clear();
	}

	public String Search(){
		for(int i = 0 ; i < query.size() ; i++){
		    System.out.println(((Link)query.get(i)).toString());
		}
		System.out.println((sn.doQuery(query)).toString());
		return sn.doQuery(query).toString();
	}

	public static void main(String[] args) {
		// TODO 自動生成されたメソッド・スタブ
		Scanner std = new Scanner(System.in);

		while(true){
			String string = std.nextLine();
			System.out.println(string.trim());
			String[] str = strSplit3line(string);
			for(String moji:str){
				System.out.println(moji);
			}
			if(string.equals("0")){
				break;
			}
		}
	}
}
