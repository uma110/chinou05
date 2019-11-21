import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.LinkedList;

import opennlp.tools.chunker.ChunkerME;
import opennlp.tools.chunker.ChunkerModel;
import opennlp.tools.lemmatizer.DictionaryLemmatizer;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;

enum VerbCategory{
	VD,VBP,VBD,VBN,VBZ,VBG;
};

public class NaturalLanguageProcessing {
	private static final String wordDicName = "dictionary.txt";
public static String wString = "aa";
	protected enum TopCategory {
		What("What"), Who("Who"), Why("Why"), Where("Where"), When("When"), How("How"), Query("Query"), Other("Other");

		private String name;

		private TopCategory(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}

		public static TopCategory getCategory(String str) {
			for (TopCategory v : values()) {
				if (v.getName().equals(str)) {
					return v;
				}
			}
			return TopCategory.Other;
		}
	};

	public static String trimBlankSeries(String str) {
		return str.trim().replaceAll("\\s+", " ");
	}

	private String sentenceFormat(String question) {
		question = question.trim();
		if (question.matches("^[a-z].*")) {
			question = Character.toTitleCase(question.charAt(0)) + question.substring(1);
		}
		return question;
	}
	/*
	 * public static String convertToVerbS(String defaultVerb) { int convertType
	 * = 0; String[] targetStrs = new String[] { "s", "o", "x", "sh", "ch" };
	 * for (String targetStr : targetStrs) { if (defaultVerb.matches(".*" +
	 * targetStr + "$")) convertType = 1; } if
	 * (defaultVerb.matches(".*[^aiueo]y$")) convertType = 2; if
	 * (defaultVerb.equals("have")) convertType = 3; String returnValue = null;
	 * switch (convertType) { case 0: System.out.println("convert type is 0");
	 * returnValue = defaultVerb + "s"; break; case 1:
	 * System.out.println("convert type is 1"); returnValue = defaultVerb +
	 * "es"; break; case 2: System.out.println("convert type is 2"); returnValue
	 * = defaultVerb.replaceAll("y$", ""); returnValue += "ies"; break; case 3:
	 * System.out.println("convert type is 3"); returnValue = "has"; break;
	 * default: System.out.println("not type"); break; } return returnValue; }
	 */
	/*
	 * public static String convertToPastVerb(String defaultVerb) { int
	 * convertType = 0; if (defaultVerb.matches(".+e$")) { //
	 * System.out.println("1"); convertType = 1; } if
	 * (defaultVerb.matches(".*[^aiueo]y$")) { // System.out.println("2");
	 * convertType = 2; } if (defaultVerb.matches(".*[aiueo][^aiueo]$")) { //
	 * System.out.println("3"); convertType = 3; } if
	 * (defaultVerb.matches(".*[aiueo]y$")) { // System.out.println("4");
	 * convertType = 0; } if (defaultVerb.matches(".*[aiueo][aiueo][^aiueo]$"))
	 * { // System.out.println("5"); convertType = 0; } if
	 * (defaultVerb.matches(".+c$")) { // System.out.println("6"); convertType =
	 * 4; }
	 *
	 * String returnValue; switch (convertType) { case 0: //
	 * System.out.println("ed"); returnValue = defaultVerb + "ed"; break; case
	 * 1: // System.out.println("d"); returnValue = defaultVerb + "d"; break;
	 * case 2: // System.out.println("ied"); returnValue =
	 * defaultVerb.replaceFirst("y$", "ied"); break; case 3: //
	 * System.out.println("+ed"); returnValue =
	 * defaultVerb.replaceFirst("([a-z])$", "$1$1ed"); break; case 4: //
	 * System.out.println("cked"); returnValue = defaultVerb + "ked"; break;
	 * default: returnValue = null; break; } return returnValue; }
	 */

	/**
	 * 動詞の原形からタグの動詞をワードファイルから参照する
	 *
	 * @param lemmaWord
	 *            動詞の原形
	 * @param pattern
	 *            VB:原形 VBP:非三人称単数 VBD:過去形 VBN:過去分詞 VBZ:三人称単数 VBG:動名詞
	 * @return 引数が正しくない、辞書から見つからない場合null,それ以外は変換されたStringを返す
	 */
	public static String openWordDic(String lemmaWord, VerbCategory pattern) {
		String returnValue = null;
		try {
			BufferedReader in = new BufferedReader(
					new InputStreamReader(new FileInputStream(wordDicName), StandardCharsets.UTF_8));
			int count = 0;
			for (String line = in.readLine(); line != null; line = in.readLine(),count++) {
				String[] words = line.split("\\s+", 0);
				if (words.length != 3) {
					System.out.println(count);
					System.out.println("One WordDicData is INVALID");
					System.out.println(Arrays.asList(words));
					continue;
				}
				if (lemmaWord.equals(words[2]) && pattern.toString().equals(words[1])) {
					returnValue = words[0];
					break;
				}
			}
			in.close();
		} catch (UnsupportedEncodingException e) {
			// TODO: handle exception
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		return returnValue;
	}

	private String[] MessageToSentences(String message) {
		// 1文ずつに区切る
		/*
		 * String[] cutSentence = message.split("\\.", 0); for (int i = 0; i <
		 * cutSentence.length; i++) { cutSentence[i] =
		 * messageFormat(cutSentence[i]) + "."; }
		 *
		 * for (String str : cutSentence) { System.out.println(str); } return
		 * cutSentence;
		 */
		InputStream inputStream = null;
		SentenceModel model = null;
		try {
			inputStream = new FileInputStream("en-sent.bin");
			model = new SentenceModel(inputStream);
		} catch (FileNotFoundException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		SentenceDetectorME detectorME = new SentenceDetectorME(model);
		String[] cutSentence;
		cutSentence = detectorME.sentDetect(message);
		for (String str : cutSentence) {
			System.out.println(str);
		}
		return cutSentence;
	}

	private String[] morphemes;
	private String[] tags;
	private String[] lemmas;
	private String[] chunkerTags;

	private void messageAnalyze(String message) {
		// 単語で区切る
		InputStream tokenModelIn;
		TokenizerModel tokenModel = null;
		try {
			tokenModelIn = new FileInputStream("en-token.bin");
			tokenModel = new TokenizerModel(tokenModelIn);
		} catch (FileNotFoundException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		Tokenizer tokenizer = new TokenizerME(tokenModel);

		morphemes = tokenizer.tokenize(message);

		System.out.println(Arrays.asList(morphemes));

		// 単語の品詞可
		InputStream posModelIn;
		POSModel posModel = null;
		try {
			posModelIn = new FileInputStream("en-pos-maxent.bin");
			posModel = new POSModel(posModelIn);
		} catch (FileNotFoundException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		POSTaggerME posTagger = new POSTaggerME(posModel);

		tags = posTagger.tag(morphemes);
		System.out.println(Arrays.asList(tags));

		// 単語の原型化
		InputStream dictLemmatizer;
		DictionaryLemmatizer lemmatizer = null;
		try {
			dictLemmatizer = new FileInputStream("en-Lemmatizer.txt");
			lemmatizer = new DictionaryLemmatizer(dictLemmatizer);
		} catch (FileNotFoundException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}

		lemmas = lemmatizer.lemmatize(morphemes, tags);
		System.out.println(Arrays.asList(lemmas));

		// 浅い構文木の作成
		try (InputStream chunkModelIn = new FileInputStream("en-chunker.bin")) {
			ChunkerModel chunkModel = new ChunkerModel(chunkModelIn);
			ChunkerME chunker = new ChunkerME(chunkModel);
			chunkerTags = chunker.chunk(morphemes, tags);
			System.out.println(Arrays.asList(chunkerTags));
		} catch (FileNotFoundException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
	}

	// １センテンスごとに一つの?xという前提で、hatenaは?xのこと
	private void makeQueryFromMessage(String sentence, String hatena, LinkedList<String> query) {
		if (sentence == null || sentence.matches("\\s*")) {
			System.out.println("INVALID MESSAGE");
			return;
		}
		if (sentence.indexOf(".") == -1 && sentence.indexOf("?") == -1) {
			System.out.println("sentence fixed. add \".\"");
			sentence += ".";
		}
		messageAnalyze(sentence);
		String topMorpheme = morphemes[0];
		String category = topMorpheme;
		for (String morpheme : morphemes) {
			if (morpheme.matches("^\\?.+")) {
				category = "Query";
			}
		}
		TopCategory myCategory = TopCategory.getCategory(category);
		// 5W1Hの判定
		switch (myCategory) {
		case What:
			String secondTag = tags[1];
			String secondLemma = lemmas[1];
			int sentenceLength = tags.length;
			if (secondTag.indexOf("VB") != -1) {
				if (secondLemma.equals("be")) {
					// what is what are
					System.out.println("what is noun group");
					String baseStr = hatena + " " + morphemes[1];
					String question = baseStr;
					for (int i = 2; i < sentenceLength; i++) {
						if (!tags[i].equals(",") && !tags[i].equals("CC") && !tags[i].equals(".")) {
							if (!tags[i].equals("POS")) {
								question += " ";
							}
							question += morphemes[i];
						} else {
							question += ".";
							query.add(sentenceFormat(question));
							question = baseStr;
						}
					}
				} else if (secondLemma.equals("do")) {
					// what do you what does she
					System.out.println("what do ---");
					int verbPattern;
					if (morphemes[1].equals("did")) {
						verbPattern = 2;
					} else if (morphemes[1].equals("does")) {
						verbPattern = 1;
					} else {
						// do pattern
						verbPattern = 0;
					}
					String question = "";
					for (int i = 2; i < sentenceLength; i++) {
						if (tags[i].equals(".")) {
							question += " " + hatena + ".";
							query.add(sentenceFormat(question));
							break;
						}
						if (tags[i].equals("VB") || tags[i].equals("VBP")) {
							switch (verbPattern) {
							case 1:
								question += " " + openWordDic(lemmas[i], VerbCategory.VBZ);
								break;
							case 2:
								question += " " + openWordDic(lemmas[i], VerbCategory.VBD);
								break;
							default:
								question += " " + morphemes[i];
								break;
							}
						} else {
							question += " " + morphemes[i];
						}
					}
				} else {
					System.out.println("not support");
				}
			} else if (secondTag.equals("POS")) {
				// what is
				System.out.println("what's ");
				String baseStr = hatena + " is";
				String question = baseStr;
				for (int i = 2; i < sentenceLength; i++) {
					if (!tags[i].equals(",") && !tags[i].equals("CC") && !tags[i].equals(".")) {
						if (!tags[i].equals("POS")) {
							question += " ";
						}
						question += morphemes[i];
					} else {
						question += ".";
						query.add(sentenceFormat(question));
						question = baseStr;
					}
				}
			} else if (secondTag.equals("MD")) {
				// what can what may what will
				String baseStr = "";
				String question = baseStr;
				System.out.println("what MD ---");
				boolean isModalUsed = false;
				for (int i = 2; i < sentenceLength; i++) {
					if (tags[i].equals(".")) {
						question += " " + hatena + ".";
						query.add(sentenceFormat(question));
						break;
					}
					if(tags[i].equals("VB") || tags[i].equals("VBP")){
						if(!isModalUsed){
							question += " " + morphemes[1];
							isModalUsed = true;
						}
						question += " " + morphemes[i];
					}else{
						question += " " + morphemes[i];
					}
				}
				if (!isModalUsed)
					System.out.println("we can not make query");
			} else
				System.out.println("unknown input data");
			break;
		case Who:
			System.out.println("Who ---");
			boolean isModal = false;
			if (tags[1].equals("MD")) {
				isModal = true;
			}
			boolean isQuestionPattern;
			if (!lemmas[1].equals("do")) {
				isQuestionPattern = false;
				if (isModal) {
					int indexCursor = 2;
					if (morphemes[2].equals("not")) {
						indexCursor++;
					}
					if (!tags[indexCursor].equals("VB") && !tags[indexCursor].equals("VBP")) {
						isQuestionPattern = true;
					}
				}
			} else {
				if (morphemes[1].equals("do")) {
					isQuestionPattern = true;
				} else {
					// Who does or Who did
					isQuestionPattern = false;
					for (int i = 2; i < tags.length; i++) {
						if (tags[i].equals("VB") || tags[i].equals("VBP")) {
							isQuestionPattern = true;
							break;
						}
					}
				}
			}
			if (isQuestionPattern) {
				System.out.println("Who QUESTION");

				// 0-> modal or 原形 1->単数形 2->過去形
				int verbPattern = 0;
				if (morphemes[1].equals("does")) {
					verbPattern = 1;
				} else if (morphemes[1].equals("did")) {
					verbPattern = 2;
				}

				String question = "";
				boolean isModalUsed = false;
				for (int i = 2; i < morphemes.length; i++) {
					if (tags[i].equals(".")) {
						question += " " + hatena + ".";
						query.add(sentenceFormat(question));
						break;
					}
					if (tags[i].equals("VB") || tags[i].equals("VBP")) {
						if (isModal) {
							if (!isModalUsed) {
								question += " " + morphemes[1];
								isModalUsed = true;
							}
						}
						switch(verbPattern){
						case 0:
							question += " " + morphemes[i];
							break;
						case 1:
							question += " " + openWordDic(lemmas[i], VerbCategory.VBZ);
							break;
						case 2:
							question += " " + openWordDic(lemmas[i], VerbCategory.VBD);
							break;
						}
					} else {
						question += " " + morphemes[i];
					}
				}
			} else {
				System.out.println("?x DO -----");
				String baseStr = hatena;
				int startChange = 0;
				if (isModal) {
					baseStr += " " + morphemes[1];
					startChange++;
				}
				String question = baseStr;
				for (int i = 1 + startChange; i < morphemes.length; i++) {
					if (!tags[i].equals(",") && !tags[i].equals("CC") && !tags[i].equals(".")) {
						if (!tags[i].equals("POS")) {
							question += " ";
						}
						question += morphemes[i];
					} else {
						question += ".";
						query.add(sentenceFormat(question));
						question = baseStr;
					}
				}
			}
			break;
		case Why:
			System.out.println("not support WHY");
			break;
		case Where:
			System.out.println("not support Where");
			break;
		case When:
			System.out.println("not support When");
			break;
		case How:
			System.out.println("not support How");
			break;
		case Query:
			System.out.println("is query");
			query.add(sentence);
			break;
		case Other:
			System.out.println("not support question");
			break;
		default:
			throw new IllegalAccessError("SYSTEM ERROR");
		}
	}

	public LinkedList<String> makeQuestionFromGUIText(String inputMessage) {
		System.out.println("===================");
		String[] cutSentence = MessageToSentences(inputMessage);

		LinkedList<String> query = new LinkedList<>();
		for (String sentence : cutSentence) {
			System.out.println("----------------");
			makeQueryFromMessage(sentenceFormat(sentence), getQueryStr(), query);
		}
		System.out.println(query);
		return query;
	}

	private static int asciiNumber = 97;

	private String getQueryStr() {
		if (asciiNumber > 122) {
			try {
				throw new Exception("We can't make more queryVar");
			} catch (Exception e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}
		}
		return "?" + (char) asciiNumber++;
	}

	public String debug(String message) {
		LinkedList<String> query = new LinkedList<>();
		makeQueryFromMessage(message, "?x", query);
		return query.toString();
	}

	public void debug1(String message) {
		String[] messages = MessageToSentences(message);
		System.out.println("=============");
		System.out.println("length " + messages.length);
		for (String str : messages) {
			System.out.println(str);
		}
	}
}
