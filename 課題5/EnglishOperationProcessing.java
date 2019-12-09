import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import opennlp.tools.lemmatizer.DictionaryLemmatizer;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;

enum VerbCategory {
	VD, VBP, VBD, VBN, VBZ, VBG;
};

public class EnglishOperationProcessing {
	private static final String wordDicName = "dictionary.txt";

	protected enum SystemCommand {
		Add, Delete, Move, MakeGoal, Other
	}

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
			for (String line = in.readLine(); line != null; line = in.readLine(), count++) {
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
	}

	private ObjectCharactor extractAddObjectFeature(String sentence) {
		if (sentence == null || sentence.matches("\\s*")) {
			System.out.println("INVALID MESSAGE");
			return null;
		}
		if (sentence.indexOf(".") == -1 && sentence.indexOf("?") == -1) {
			System.out.println("sentence fixed. add \".\"");
			sentence += ".";
		}
		messageAnalyze(sentence);
		String symbol = null;
		SupportedShape shape = null;
		SupportedColor color = null;

		symbol = extractExplicitSymbol(sentence);
		if (symbol == null) {
			for (int i = 0; i < tags.length; i++) {
				if (tags[i].equals("NNP")) {
					symbol = morphemes[i];
					break;
				}
			}
		}
		if (symbol == null)
			return null;
		if (symbol.equals("?") || symbol.matches("\\s*")) {
			return null;
		}
		if (ObjectCharactor.existSameSymbol(symbol)) {
			return null;
		}

		System.out.println("extract success!! ->" + symbol);

		String[] shapeStrs = new String[SupportedShape.values().length - 1];
		SupportedShape[] shapes = SupportedShape.values();
		for (int i = 0; i < shapeStrs.length; i++) {
			shapeStrs[i] = shapes[i].getName().toLowerCase();
		}

		// Arrays.stream(shapeStrs).forEach(System.out::println);

		for (String morpheme : morphemes) {
			for (String str : shapeStrs) {
				if (morpheme.equals(str)) {
					shape = SupportedShape.getTagFromString(morpheme);
					break;
				}
			}
			if (shape != null) {
				break;
			}
		}

		if (shape == null) {
			return null;
		}

		System.out.println("extract success!! ->" + shape);

		String[] colorStrs = new String[SupportedColor.values().length - 1];
		SupportedColor[] colors = SupportedColor.values();
		for (int i = 0; i < colorStrs.length; i++) {
			colorStrs[i] = colors[i].toString().toLowerCase();
		}

		// Arrays.stream(colorStrs).forEach(System.out::println);

		for (String morpheme : morphemes) {
			for (String str : colorStrs) {
				if (morpheme.equals(str)) {
					color = SupportedColor.getTagFromString(morpheme);
					break;
				}
			}
			if (color != null) {
				break;
			}
		}
		if (color == null) {
			return null;
		}

		System.out.println("extract success!! ->" + color);

		return new ObjectCharactor(symbol, shape, color);
	}

	private String extractDeleteSymbol(String sentence) {
		if (sentence == null || sentence.matches("\\s*")) {
			System.out.println("INVALID MESSAGE");
			return null;
		}
		if (sentence.indexOf(".") == -1 && sentence.indexOf("?") == -1) {
			System.out.println("sentence fixed. add \".\"");
			sentence += ".";
		}
		messageAnalyze(sentence);
		String symbol = null;

		symbol = extractExplicitSymbol(sentence);

		if (symbol == null) {
			for (int i = 0; i < tags.length; i++) {
				if (tags[i].equals("NNP")) {
					symbol = morphemes[i];
					break;
				}
			}
		}
		if (symbol == null)
			return null;
		if (symbol.equals("?") || symbol.matches("\\s*")) {
			return null;
		}
		if (!ObjectCharactor.existSameSymbol(symbol))
			return null;

		System.out.println("extract success!! ->" + symbol);
		return symbol;
	}

	private String extractExplicitSymbol(String sentence) {
		Pattern pattern;
		Matcher matcher;

		String regax = "\"(.*)\"";

		pattern = Pattern.compile(regax);
		matcher = pattern.matcher(sentence);
		if (matcher.find()) {
			return matcher.group(1).trim();
		}
		regax = "'(.*)'";
		pattern = Pattern.compile(regax);
		matcher = pattern.matcher(sentence);
		if (matcher.find()) {
			return matcher.group(1).trim();
		}
		return null;
	}

	private void makeGoalStateForPlanner(String sentence, List<String> initGoalList) {
		if (sentence == null || sentence.matches("\\s*")) {
			System.out.println("INVALID MESSAGE");
			return;
		}
		if (sentence.indexOf(".") == -1 && sentence.indexOf("?") == -1) {
			System.out.println("sentence fixed. add \".\"");
			sentence += ".";
		}
		messageAnalyze(sentence);

		int startIndexCutSent;
		int endIndexCutSent;

		ArrayList<Integer> separatoIndexs = new ArrayList<>();
		for (int i = 0; i < tags.length; i++) {
			if (tags[i].equals(",") || tags[i].equals("CC") || tags[i].equals(".")) {
				separatoIndexs.add(i);
			}
		}

		startIndexCutSent = 0;
		for (int separatoIndex : separatoIndexs) {
			endIndexCutSent = separatoIndex - 1;

			int prepositionIndex = -1;
			boolean PPisOn = true;
			for (int i = startIndexCutSent; i <= endIndexCutSent; i++) {
				if (morphemes[i].equals("on")) {
					PPisOn = true;
					prepositionIndex = i;
					break;
				} else if (morphemes[i].equals("under")) {
					PPisOn = false;
					prepositionIndex = i;
					break;
				}
			}
			if (prepositionIndex == -1) {
				System.out.println("preposition is not found");
				continue;
			}

			System.out.println(
					"start morpheme->" + morphemes[startIndexCutSent] + " middle ->" + morphemes[prepositionIndex - 1]);
			System.out.println(
					"middle morpheme ->" + morphemes[prepositionIndex + 1] + " end->" + morphemes[endIndexCutSent]);

			String feature1 = extractFeatureFromCutSent(startIndexCutSent, prepositionIndex - 1);
			String feature2 = extractFeatureFromCutSent(prepositionIndex + 1, endIndexCutSent);
			System.out.println("feature1 ->" + feature1);
			System.out.println("feature2 ->" + feature2);
			if (feature1 != null && feature2 != null) {
				System.out.println("extract success -> make goal state ↓");
				String element;
				if (PPisOn) {
					element = feature1 + " on " + feature2;
					System.out.println(element);
					initGoalList.add(element);
				} else {
					element = feature2 + " on " + feature1;
					System.out.println(element);
					initGoalList.add(element);
				}
			} else {
				System.out.println("extract failed");
			}

			startIndexCutSent = separatoIndex + 1;
		}
	}

	private String extractFeatureFromCutSent(int startIndex, int endIndex) {
		String symbol = null;
		SupportedShape shape = null;
		SupportedColor color = null;

		String sentence = "";
		for (int i = startIndex; i <= endIndex; i++) {
			sentence += morphemes[i] + " ";
		}
		System.out.println("remake ->" + sentence);
		symbol = extractExplicitSymbol(sentence);
		if (symbol == null) {
			for (int i = startIndex; i <= endIndex; i++) {
				if (tags[i].equals("NNP")) {
					symbol = morphemes[i];
					break;
				}
			}
		}

		if (symbol != null) {
			if (!symbol.equals("?") && !symbol.matches("\\s*")) {
				System.out.println("symbol extract ->" + symbol);
				return symbol;
			}
		}

		String[] shapeStrs = new String[SupportedShape.values().length - 1];
		SupportedShape[] shapes = SupportedShape.values();
		for (int i = 0; i < shapeStrs.length; i++) {
			shapeStrs[i] = shapes[i].getName().toLowerCase();
		}

		String[] colorStrs = new String[SupportedColor.values().length - 1];
		SupportedColor[] colors = SupportedColor.values();
		for (int i = 0; i < colorStrs.length; i++) {
			colorStrs[i] = colors[i].toString().toLowerCase();
		}

		for (int i = startIndex; i <= endIndex; i++) {
			for (String str : shapeStrs) {
				if (morphemes[i].toLowerCase().equals(str)) {
					shape = SupportedShape.getTagFromString(morphemes[i]);
					break;
				}
			}
			for (String str : colorStrs) {
				if (morphemes[i].toLowerCase().equals(str)) {
					color = SupportedColor.getTagFromString(morphemes[i]);
					break;
				}
			}
			if (shape != null || color != null) {
				break;
			}
		}
		if (color != null) {
			System.out.println("color extract ->" + color.toString());
			return color.toString();
		}
		if (shape != null) {
			System.out.println("shape extract ->" + shape.toString());
			return shape.toString();
		}
		System.out.println("extract failed");
		return null;
	}

	private SystemCommand classifyCategory(String sentence) {
		if (sentence == null || sentence.matches("\\s*")) {
			System.out.println("INVALID MESSAGE");
			return SystemCommand.Other;
		}
		if (sentence.indexOf(".") == -1 && sentence.indexOf("?") == -1) {
			System.out.println("sentence fixed. add \".\"");
			sentence += ".";
		}
		messageAnalyze(sentence);
		SystemCommand command = SystemCommand.Other;
		for (String lemma : lemmas) {
			switch (lemma) {
			case "add":
				command = SystemCommand.Add;
				break;
			case "delete":
				command = SystemCommand.Delete;
				break;
			case "move":
				command = SystemCommand.Move;
				break;
			case "make":
				command = SystemCommand.MakeGoal;
				break;
			default:
				break;
			}
			if (command != SystemCommand.Other) {
				break;
			}
		}
		if (command != SystemCommand.Other) {
			return command;
		}
		for (String morpheme : morphemes) {
			switch (morpheme.toLowerCase()) {
			case "add":
				command = SystemCommand.Add;
				break;
			case "delete":
				command = SystemCommand.Delete;
				break;
			case "move":
				command = SystemCommand.Move;
				break;
			case "make":
				command = SystemCommand.MakeGoal;
				break;
			default:
				break;
			}
			if (command != SystemCommand.Other) {
				break;
			}
		}
		if (command != SystemCommand.Other)
			return command;
		return SystemCommand.Other;
	}

	private LinkedList<ObjectCharactor> makeGoalStateForGUI(List<String> elementList, OperationSystem system,
			List<ObjectCharactor> goalState) {
		LinkedList<String> escapeElement = new LinkedList<>();
		for (String element : elementList) {
			String[] splits = element.split("\\son\\s");
			if (splits.length != 2) {
				System.out.println("messagge split failed");
				return null;
			}
			if (splits[0] == null || splits[1] == null) {
				System.out.println("Name is invalid");
				return null;
			}
			String upperName = splits[0];
			String underName = splits[1];

			ObjectCharactor upperObj = identifyObjFromSystem(upperName, system);
			ObjectCharactor underObj = identifyObjFromSystem(underName, system);
			if (upperObj == null || underObj == null) {
				System.out.println("object can not be identified");
				return null;
			}

			System.out.println("gogogogo");
			int underObjIndex = findIndexFromGoalList(underName, goalState);
			if (underObjIndex != -1) {
				System.out.println("upperObj add");
				goalState.add(underObjIndex + 1, upperObj);
			} else {
				int upperObjIndex = findIndexFromGoalList(upperName, goalState);
				if (upperObjIndex != -1) {
					System.out.println("underObj add");
					goalState.add(upperObjIndex, underObj);
				} else {
					System.out.println("upper and under object is not found in goal list.");
					if (goalState.isEmpty()) {
						System.out.println("goal list is empty,so two obj add");
						goalState.add(underObj);
						goalState.add(upperObj);
					} else {
						System.out.println("goal list is not empty,so element escape");
						escapeElement.add(element);
					}
				}
			}
		}
		if (escapeElement.size() != 0) {
			if (escapeElement.size() == elementList.size()) {
				System.out.println("infinity roop");
				return null;
			}
			makeGoalStateForGUI(escapeElement, system, goalState);
		}
		return new LinkedList<>(goalState);
	}

	private int findIndexFromGoalList(String feature, List<ObjectCharactor> list) {
		if (list.isEmpty()) {
			System.out.println("list is empty");
			return -1;
		}
		int i;
		for (i = list.size() - 1; i >= 0; i--) {
			ObjectCharactor searchObj = list.get(i);
			System.out.println(searchObj);
			if (searchObj.getSymbol().equals("?")) {
				System.out.println("feature ->"+feature);
				System.out.println("shape ->"+searchObj.getShape().getName());
				System.out.println("color ->"+searchObj.getColor().toString());
				if (feature.equals(searchObj.getShape().getName()) || feature.equals(searchObj.getColor().toString())) {
					break;
				}
			} else {
				System.out.println("ponpon");
				if (feature.equals(searchObj.getSymbol())) {
					break;
				}
			}
		}
		return i;
	}

	private ObjectCharactor identifyObjFromSystem(String feature, OperationSystem system) {
		ObjectCharactor obj = null;
		if (ObjectCharactor.existSameSymbol(feature)) {
			System.out.println("11");
			obj = system.findObjInSystem(feature);
			System.out.println(obj);
		}

		if (obj == null) {
			System.out.println("22");
			SupportedColor color = SupportedColor.getTagFromString(feature);
			SupportedShape shape = SupportedShape.getTagFromString(feature);
			if (color != null) {
				obj = new ObjectCharactor("?", SupportedShape.None, color, false);
			} else if (shape != null) {
				obj = new ObjectCharactor("?", shape, SupportedColor.None, false);
			} else {
				System.out.println("shape or color is invalid");
				obj = null;
			}
		}

		System.out.print("identifyObj ->");
		if (obj != null) {
			System.out.println(obj.toString());
		} else {
			System.out.println("null");
		}
		return obj;
	}

	public boolean executeWordOperation(String inputMessage, OperationSystem targetStateSystem,
			LinkedList<String> logList) {
		System.out.println("===================");
		String[] cutSentence = MessageToSentences(inputMessage);
		if (cutSentence.length <= 1) {
			System.out.println("sentence cut is failed.");
			return false;
		}
		boolean operationSuccess = false;
		SystemCommand command = classifyCategory(cutSentence[0]);
		System.out.println("Command -> \n" + command);
		logList.add("Command -> " + command);
		switch (command) {
		case Add:
			ObjectCharactor addObj = extractAddObjectFeature(cutSentence[1]);
			if (addObj == null) {
				operationSuccess = false;
				logList.add("operation success -> " + operationSuccess);
			} else {
				targetStateSystem.getUsableObjets().add(addObj);
				operationSuccess = true;
				logList.add("operation success -> " + operationSuccess);
				logList.add("add object -> " + addObj.toString());
			}
			break;
		case Delete:
			String deleteSymbol = extractDeleteSymbol(cutSentence[1]);
			if (deleteSymbol == null) {
				operationSuccess = false;
				logList.add("operation success -> " + operationSuccess);

			} else {
				targetStateSystem.deleteObject(deleteSymbol);
				operationSuccess = true;
				logList.add("operation success -> " + operationSuccess);
				logList.add("delete object symbol -> " + deleteSymbol);
			}
			break;
		case Move:
			System.out.println("not support move operation now");
			logList.add("not support move operation now");
			operationSuccess = false;
			break;
		case MakeGoal:
			LinkedList<String> goalElementList = new LinkedList<>();
			for (int i = 1; i < cutSentence.length; i++) {
				makeGoalStateForPlanner(cutSentence[i], goalElementList);
			}
			logList.add("↓made goal state");
			goalElementList.forEach(str -> {
				logList.add(str);
			});
			LinkedList<ObjectCharactor> goalState = makeGoalStateForGUI(goalElementList, targetStateSystem,
					new LinkedList<ObjectCharactor>());
			if (goalState == null) {
				System.out.println("can not make goal state");
				operationSuccess = false;
				logList.add("operation success -> " + operationSuccess);
			} else {
				operationSuccess = true;
				logList.add("operation success -> " + operationSuccess);
				targetStateSystem.goalStateInit();
				targetStateSystem.getGoalState().addAll(goalState);
			}
			break;
		case Other:
			System.out.println("can not classify operation category");
			logList.add("can not classify operation category");
			operationSuccess = false;
			break;
		}
		return operationSuccess;
	}
}
