
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class DebugClass {
	public static void dataProcess() {
		String filename = "en-lemmatizer.txt";
		String dstFilename = "dictionary.txt";
		try {
			BufferedReader in = new BufferedReader(
					new InputStreamReader(new FileInputStream(filename), StandardCharsets.UTF_8));
			PrintWriter out = new PrintWriter(
					new OutputStreamWriter(new FileOutputStream(dstFilename, true), StandardCharsets.UTF_8));
			String outputLine = "";
			boolean verbFound = false;
			String lemma = null;
			for (String line = in.readLine(); line != null; line = in.readLine()) {
				String[] cutWords = line.split("\\s+", 0);
				if (cutWords.length != 3) {
					System.out.println("error");
					break;
				}
				if (cutWords[1].indexOf("VB") != -1) {
					out.println(line);
				}
			}
			in.close();
			out.close();
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

	public static void main(String[] args) {
		String[] str = new String[] {
				// "you say that what's is are can may maybe do does did will
				// "What is a side car, a grafitti and a element",
				// "what's a side car and grafitti.",
				// "what can you play.?",
				// "what will it be today?.",
				// "what is the man wearing."
				/*
				 * "What can Ken play very well",
				 * "What should she do every day",
				 * "What should he do every day",
				 * "What should you do every day",
				 * "What should a human do every day",
				 * "What should an Ener do every day",
				 * "What should that profgyessor swim every day",
				 * "What can that professor running over there do",
				 * "What should his friend perform", "What should his car have",
				 */
				/*
				 * "?x should Ken do every day ?d . His Name is Ken",
				 * "Ken plays soccer", "You ss?xk play soccer",
				 * //"?x has a tale",
				 */
				// "What is your hobby. What is your car."
				/*
				 * "What does she play?", "What does Ken perform?",
				 * "What does Mika do?", "What do cars work?",
				 * "What do you play?", "What does car work?",
				 * "What do you perform.", "What did she play?",
				 * "What did you perform?",
				 */
				// "What does Ken play"
				// "What is today's soup"
				"Who makes this soup?", "Who has a tale?", "Who is talking about this over there?",
				"who played soccer in this park?", "Who does your mother live with?", "who do you respect for?",
				"Who can you play soccer with?", "Who does Ken do soccer with?", "Who may you play soccer with?",
				"Who did you play soccer with", };
		NaturalLanguageProcessing system = new NaturalLanguageProcessing();
		for (String message : str) {
			system.makeQuestionFromGUIText(messageFilter(message));
		}
		String string;
		Scanner stdIn = new Scanner(System.in);
		while (true) {
			string = stdIn.nextLine();
			if (string.equals("end")) {
				break;
			}

//			System.out.println(NaturalLanguageProcessing.openWordDic(string, VerbCategory.VBD));
			System.out.println(string = messageFilter(string));
			system.makeQuestionFromGUIText(string);
		}
		stdIn.close();
	}

	// GUIテキストから受け取った質問に対してのフィルタリング
	private static String messageFilter(String string) {
		string = string.replaceAll("\\.([^" + " " + "])", ". $1");
		string = string.replaceAll("\\,([^" + " " + "])", ", $1");
		string = string.replaceAll("\\?([A-Z])", "? $1");
		string = string.replaceAll("([a-zA-Z]\\?)([a-zA-Z])", "$1 $2");
		return string;
	}
}
