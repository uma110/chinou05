import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.Vector;

//変更点
//Plannner
//	リストblock追加
//	initGoalList
//		ブロック特定し、表示
//	identifyメソッド追加
//		ブロック特定してくれます
//Block追加
//	構造体的ななにか

public class Planner {
	Vector operators;
	Random rand;
	Vector plan;
	int roopCount;
	final int roopCountMax = 1000;
	boolean infinityRoopSwitch;
	static List<Block> block = new ArrayList<Block>(); // Block型のリストblock
														// 一番下にBlockクラスあります

	/*
	 * public static void main(String argv[]) { // ここはGUI等で何かつくるならつくってほしいです
	 * block.add(new Block("A", "Red", "Triangle")); // 変更 block.add(new
	 * Block("B", "Blue", "Square")); // 変更 block.add(new Block("C", "Yellow",
	 * "Circle")); // 変更
	 * 
	 * (new Planner()).start(); }
	 */

	Planner() {
		rand = new Random();
	}

	public String[] startMakePlan(List<ObjectCharactor> initUsableList, List<ObjectCharactor> initUsedList,
			List<ObjectCharactor> goalListOnGUI) {
		roopCount = 0;
		infinityRoopSwitch = false;
		block.clear();
		initUsableList.forEach(obj -> {
			block.add(new Block(obj.getSymbol(), obj.getColor().toString(), obj.getShape().toString()));
		});
		initUsedList.forEach(obj -> {
			block.add(new Block(obj.getSymbol(), obj.getColor().toString(), obj.getShape().toString()));
		});

		block.forEach(block -> {
			System.out.println(block.toString());
		});

		initOperators();

		Vector goalList = initGoalListFromGUI(goalListOnGUI);
		Vector initialState = initInitialState(initUsableList, initUsedList);

		goalList.forEach(System.out::println);
		initialState.forEach(System.out::println);

		Hashtable theBinding = new Hashtable();
		plan = new Vector();
		planning(goalList, initialState, theBinding);

		if (!infinityRoopSwitch)
			return null;

		String[] plans = new String[plan.size()];
		System.out.println("***** This is a plan! *****");
		for (int i = 0; i < plan.size(); i++) {
			Operator op = (Operator) plan.elementAt(i);
			plans[i] = (op.instantiate(theBinding)).name;
			System.out.println(plans[i]);
		}
		return plans;
	}

	private Vector initGoalListFromGUI(List<ObjectCharactor> list) {
		Vector goalList = new Vector();
		Vector newGoalList = new Vector();

		ObjectCharactor preIndexObj = list.get(0);
		ObjectCharactor currentIndexObj;
		for (int i = 1; i < list.size(); i++) {
			currentIndexObj = list.get(i);
			String upsideObj = getFeatureFromObjChara(currentIndexObj);
			String underObj = getFeatureFromObjChara(preIndexObj);
			goalList.addElement(upsideObj + " on " + underObj);
			// System.out.println(upsideObj+" on "+underObj);
			preIndexObj = currentIndexObj;
		}

		// GoalListからブロックの特定をする
		newGoalList = identify(goalList); // "A on blue"->"A on B"等
		System.out.println("*** GOALS ***" + goalList + "\n"); // 元の目標状態書く
		for (int i = 0; i < goalList.size(); i++) // "A on blue -> A on B"等書く
			System.out.println(goalList.elementAt(i) + " -> " + newGoalList.elementAt(i));

		System.out.println(); // 見やすくするため
		return newGoalList;
	}

	private String getFeatureFromObjChara(ObjectCharactor targetObj) {
		String feature = null;
		if (targetObj.getSymbol().equals("?")) {
			if (targetObj.getColor() == SupportedColor.None) {
				feature = targetObj.getShape().toString();
			} else {
				feature = targetObj.getColor().toString();
			}
		} else {
			feature = targetObj.getSymbol();
		}
		return feature;
	}

	private Vector initInitialState(List<ObjectCharactor> usableList, List<ObjectCharactor> usedList) {
		Vector initialState = new Vector();

		usableList.forEach(obj -> {
			String symbol = obj.getSymbol();
			initialState.addElement("clear " + symbol);
			initialState.addElement("ontable " + symbol);
		});

		if (usedList.size() >= 2) {
			ObjectCharactor preIndexObj = usedList.get(0);
			ObjectCharactor currentIndexObj;
			for (int i = 1; i < usedList.size(); i++) {
				currentIndexObj = usedList.get(i);
				String upsideObj = getFeatureFromObjChara(currentIndexObj);
				String underObj = getFeatureFromObjChara(preIndexObj);
				initialState.addElement(upsideObj + " on " + underObj);
				// System.out.println(upsideObj+" on "+underObj);
				preIndexObj = currentIndexObj;
			}
		}

		initialState.addElement("handEmpty");
		return initialState;
	}

	public void start() {
		initOperators();
		Vector goalList = initGoalList();
		Vector initialState = initInitialState();

		Hashtable theBinding = new Hashtable();
		plan = new Vector();
		planning(goalList, initialState, theBinding);

		System.out.println("***** This is a plan! *****");
		for (int i = 0; i < plan.size(); i++) {
			Operator op = (Operator) plan.elementAt(i);
			System.out.println((op.instantiate(theBinding)).name);
		}
	}

	private boolean planning(Vector theGoalList, Vector theCurrentState, Hashtable theBinding) {
		System.out.println("*** GOALS ***" + theGoalList);
		roopCount++;
		if (roopCount > roopCountMax) {
			System.out.println("infinity roop happen");
			infinityRoopSwitch = true;
			return false;
		}
		if (theGoalList.size() == 1) {
			String aGoal = (String) theGoalList.elementAt(0);
			if (planningAGoal(aGoal, theCurrentState, theBinding, 0) != -1) {
				return true;
			} else {
				return false;
			}
		} else {
			String aGoal = (String) theGoalList.elementAt(0);
			int cPoint = 0;
			while (cPoint < operators.size()) {
				// System.out.println("cPoint:"+cPoint);
				// Store original binding
				Hashtable orgBinding = new Hashtable();
				for (Enumeration e = theBinding.keys(); e.hasMoreElements();) {
					String key = (String) e.nextElement();
					String value = (String) theBinding.get(key);
					orgBinding.put(key, value);
				}
				Vector orgState = new Vector();
				for (int i = 0; i < theCurrentState.size(); i++) {
					orgState.addElement(theCurrentState.elementAt(i));
				}

				int tmpPoint = planningAGoal(aGoal, theCurrentState, theBinding, cPoint);
				// System.out.println("tmpPoint: "+tmpPoint);
				if (tmpPoint != -1) {
					theGoalList.removeElementAt(0);
					System.out.println(theCurrentState);
					if (planning(theGoalList, theCurrentState, theBinding)) {
						// System.out.println("Success !");
						return true;
					} else {
						cPoint = tmpPoint;
						// System.out.println("Fail::"+cPoint);
						theGoalList.insertElementAt(aGoal, 0);

						theBinding.clear();
						for (Enumeration e = orgBinding.keys(); e.hasMoreElements();) {
							String key = (String) e.nextElement();
							String value = (String) orgBinding.get(key);
							theBinding.put(key, value);
						}
						theCurrentState.removeAllElements();
						for (int i = 0; i < orgState.size(); i++) {
							theCurrentState.addElement(orgState.elementAt(i));
						}
					}
				} else {
					theBinding.clear();
					for (Enumeration e = orgBinding.keys(); e.hasMoreElements();) {
						String key = (String) e.nextElement();
						String value = (String) orgBinding.get(key);
						theBinding.put(key, value);
					}
					theCurrentState.removeAllElements();
					for (int i = 0; i < orgState.size(); i++) {
						theCurrentState.addElement(orgState.elementAt(i));
					}
					return false;
				}
			}
			return false;
		}
	}

	private int planningAGoal(String theGoal, Vector theCurrentState, Hashtable theBinding, int cPoint) {
		System.out.println("**" + theGoal);
		int size = theCurrentState.size();
		for (int i = 0; i < size; i++) {
			String aState = (String) theCurrentState.elementAt(i);
			if ((new Unifier()).unify(theGoal, aState, theBinding)) {
				return 0;
			}
		}

		// 変更箇所
		String[] goalElement = theGoal.split(" ");
		Operator remove;
		for (int i = 0; i < 4; i++) {
			Operator op = (Operator) operators.elementAt(i);
			if (op.name.contains("remove")) {
				remove = op;
			}
			Vector addList = op.getAddList();
			int flag = 0;
			// theGoalの状態がaddListにあるオペレーターを先頭に持ってくる。
			for (int j = 0; j < addList.size(); j++) {
				String add;
				// 各オペレーター特有の操作のみで判断
				// place x on y -> x on y
				// remove x from on y -> clear y
				// pickup x from the table -> holding x
				// put down x on the table -> ontable x
				if (op.name.contains("put") && j == 2) {
					add = (String) addList.elementAt(2);
				} else {
					add = (String) addList.elementAt(0);
				}

				// theGoalとaddの変数でない部分を比較
				String[] addElement = add.split(" ");
				if (goalElement.length == addElement.length) {
					for (int k = 0; k < goalElement.length; k++) {
						if (goalElement[k].equals(addElement[k])) {
							flag = 1;
						}
					}
				}
			}
			// theGoalを達成できるオペレーターを先頭に持ってくる
			if (flag == 1) {
				operators.removeElementAt(i);
				operators.add(cPoint, op);
			}
		}

		// ループ対策
		// 例えばA on BでAがテーブルの上にないときにholdingをpick up A from the tableで達成しようとすると
		// 無限ループになるのでそのような場合はremove A from on Bを先頭に持ってくる。
		if (theGoal.contains("holding")) {
			String[] s = theGoal.split(" ");
			String var = s[1];
			int flag = 0;
			for (int i = 0; i < theCurrentState.size(); i++) {
				String state = (String) theCurrentState.elementAt(i);
				if (state.contains(var + " on")) {
					flag = 1;
				}
			}
			if (flag == 1) {
				for (int j = 0; j < operators.size(); j++) {
					Operator op = (Operator) operators.elementAt(j);
					if (op.name.contains("remove")) {
						operators.removeElementAt(j);
						operators.add(cPoint, op);
					}
				}
			}
		}
		// 変更箇所終わり

		for (int i = cPoint; i < operators.size(); i++) {
			Operator anOperator = rename((Operator) operators.elementAt(i));
			// 現在のCurrent state, Binding, planをbackup
			Hashtable orgBinding = new Hashtable();
			for (Enumeration e = theBinding.keys(); e.hasMoreElements();) {
				String key = (String) e.nextElement();
				String value = (String) theBinding.get(key);
				orgBinding.put(key, value);
			}
			Vector orgState = new Vector();
			for (int j = 0; j < theCurrentState.size(); j++) {
				orgState.addElement(theCurrentState.elementAt(j));
			}
			Vector orgPlan = new Vector();
			for (int j = 0; j < plan.size(); j++) {
				orgPlan.addElement(plan.elementAt(j));
			}

			Vector addList = (Vector) anOperator.getAddList();
			for (int j = 0; j < addList.size(); j++) {
				if ((new Unifier()).unify(theGoal, (String) addList.elementAt(j), theBinding)) {
					Operator newOperator = anOperator.instantiate(theBinding);
					Vector newGoals = (Vector) newOperator.getIfList();
					System.out.println(newOperator.name);
					if (planning(newGoals, theCurrentState, theBinding)) {
						System.out.println(newOperator.name);
						plan.addElement(newOperator);
						theCurrentState = newOperator.applyState(theCurrentState);
						return i + 1;
					} else {
						// 失敗したら元に戻す．
						theBinding.clear();
						for (Enumeration e = orgBinding.keys(); e.hasMoreElements();) {
							String key = (String) e.nextElement();
							String value = (String) orgBinding.get(key);
							theBinding.put(key, value);
						}
						theCurrentState.removeAllElements();
						for (int k = 0; k < orgState.size(); k++) {
							theCurrentState.addElement(orgState.elementAt(k));
						}
						plan.removeAllElements();
						for (int k = 0; k < orgPlan.size(); k++) {
							plan.addElement(orgPlan.elementAt(k));
						}
					}
				}
			}
		}
		return -1;
	}

	int uniqueNum = 0;

	private Operator rename(Operator theOperator) {
		Operator newOperator = theOperator.getRenamedOperator(uniqueNum);
		uniqueNum = uniqueNum + 1;
		return newOperator;
	}

	// 変更
	private Vector initGoalList() {
		Vector goalList = new Vector();
		Vector newGoalList = new Vector(); // 追加

		goalList.addElement("Blue on C"); // 変更
		goalList.addElement("A on Blue"); // 変更
		// System.out.println(goalList); //確認用

		// GoalListからブロックの特定をする
		newGoalList = identify(goalList); // "A on blue"->"A on B"等
		System.out.println("*** GOALS ***" + goalList + "\n"); // 元の目標状態書く
		for (int i = 0; i < goalList.size(); i++) // "A on blue -> A on B"等書く
			System.out.println(goalList.elementAt(i) + " -> " + newGoalList.elementAt(i));

		System.out.println(); // 見やすくするため
		return newGoalList; // 変更
	}

	Vector identify(Vector theGoalList) {
		String aGoal; // goalListの一要素入れるもの "A on B"等
		StringTokenizer st; // aGoalを分割する用 "A" "on" "B"等
		Vector newGoalList = new Vector(); // 新しい目標状態 このメソッドで返すもの
		String[] token = new String[3]; // "?x on ?y"であると仮定
										// 別の目標状態を作るつもりならArrayListで作るべき

		int preBlockNum[] = new int[theGoalList.size()]; // ?xのブロックの番号(A=0,B=1,C=2)
		int postBlockNum[] = new int[theGoalList.size()]; // ?yのブロックの番号(A=0,B=1,C=2)
		int blockAttribute[][] = new int[theGoalList.size()][2]; // ?x,?yのブロックの属性(name=0,color=1,shape=2)
		int counter = 0; // token用カウンター

		// System.out.println(theGoalList); //確認用
		for (int i = 0; i < theGoalList.size(); i++) { // GoalListに対して
			aGoal = (String) theGoalList.elementAt(i); // aGoal =
														// goalListからi番目を取り除いたもの
			st = new StringTokenizer(aGoal); // "A" "on" "B"
			while (st.hasMoreTokens()) // st("A","on","B")に対して
				token[counter++] = st.nextToken(); // token[0] = "A", token[1] =
													// "on", token[2]= "B"

			counter = 0; // カウンターを0戻す

			for (int j = 0; j < block.size(); j++) { // どのブロックのどの属性か特定(?x)
				if (token[0].equals((block.get(j)).name)) { // 名前かな
					preBlockNum[i] = j;
					blockAttribute[i][0] = 0;
					break;
				}
				if (token[0].equals((block.get(j)).color)) { // 色かな
					preBlockNum[i] = j;
					blockAttribute[i][0] = 1;
					break;
				}
				if (token[0].equals((block.get(j)).shape)) { // 形かな
					preBlockNum[i] = j;
					blockAttribute[i][0] = 2;
					break;
				}
			}
			for (int j = 0; j < block.size(); j++) { // どのブロックのどの属性か特定(?y)
				if (token[2].equals((block.get(j)).name)) {
					postBlockNum[i] = j;
					blockAttribute[i][1] = 0;
					break;
				}
				if (token[2].equals((block.get(j)).color)) {
					postBlockNum[i] = j;
					blockAttribute[i][1] = 1;
					break;
				}
				if (token[2].equals((block.get(j)).shape)) {
					postBlockNum[i] = j;
					blockAttribute[i][1] = 2;
					break;
				}
			}
			newGoalList.addElement((block.get(preBlockNum[i])).name + " on " + (block.get(postBlockNum[i])).name); // 新しい目標状態に名前で追加
																													// "A
																													// on
																													// blue"->"A
																													// on
																													// B"等
			// System.out.println(newGoalList); //確認用
		}
		return newGoalList;
	}

	private Vector initInitialState() {
		Vector initialState = new Vector();
		initialState.addElement("clear A");
		initialState.addElement("clear B");
		initialState.addElement("clear C");

		initialState.addElement("ontable A");
		initialState.addElement("ontable B");
		initialState.addElement("ontable C");
		initialState.addElement("handEmpty");
		return initialState;
	}

	private void initOperators() {
		operators = new Vector();

		// OPERATOR 1
		/// NAME
		String name1 = new String("Place ?x on ?y");
		/// IF
		Vector ifList1 = new Vector();
		ifList1.addElement(new String("clear ?y"));
		ifList1.addElement(new String("holding ?x"));
		/// ADD-LIST
		Vector addList1 = new Vector();
		addList1.addElement(new String("?x on ?y"));
		addList1.addElement(new String("clear ?x"));
		addList1.addElement(new String("handEmpty"));
		/// DELETE-LIST
		Vector deleteList1 = new Vector();
		deleteList1.addElement(new String("clear ?y"));
		deleteList1.addElement(new String("holding ?x"));
		Operator operator1 = new Operator(name1, ifList1, addList1, deleteList1);
		operators.addElement(operator1);

		// OPERATOR 2
		/// NAME
		String name2 = new String("remove ?x from on top ?y");
		/// IF
		Vector ifList2 = new Vector();
		ifList2.addElement(new String("?x on ?y"));
		ifList2.addElement(new String("clear ?x"));
		ifList2.addElement(new String("handEmpty"));
		/// ADD-LIST
		Vector addList2 = new Vector();
		addList2.addElement(new String("clear ?y"));
		addList2.addElement(new String("holding ?x"));
		/// DELETE-LIST
		Vector deleteList2 = new Vector();
		deleteList2.addElement(new String("?x on ?y"));
		deleteList2.addElement(new String("clear ?x"));
		deleteList2.addElement(new String("handEmpty"));
		Operator operator2 = new Operator(name2, ifList2, addList2, deleteList2);
		operators.addElement(operator2);

		// OPERATOR 3
		/// NAME
		String name3 = new String("pick up ?x from the table");
		/// IF
		Vector ifList3 = new Vector();
		ifList3.addElement(new String("ontable ?x"));
		ifList3.addElement(new String("clear ?x"));
		ifList3.addElement(new String("handEmpty"));
		/// ADD-LIST
		Vector addList3 = new Vector();
		addList3.addElement(new String("holding ?x"));
		/// DELETE-LIST
		Vector deleteList3 = new Vector();
		deleteList3.addElement(new String("ontable ?x"));
		deleteList3.addElement(new String("clear ?x"));
		deleteList3.addElement(new String("handEmpty"));
		Operator operator3 = new Operator(name3, ifList3, addList3, deleteList3);
		operators.addElement(operator3);

		// OPERATOR 4
		/// NAME
		String name4 = new String("put ?x down on the table");
		/// IF
		Vector ifList4 = new Vector();
		ifList4.addElement(new String("holding ?x"));
		/// ADD-LIST
		Vector addList4 = new Vector();
		addList4.addElement(new String("ontable ?x"));
		addList4.addElement(new String("clear ?x"));
		addList4.addElement(new String("handEmpty"));
		/// DELETE-LIST
		Vector deleteList4 = new Vector();
		deleteList4.addElement(new String("holding ?x"));
		Operator operator4 = new Operator(name4, ifList4, addList4, deleteList4);
		operators.addElement(operator4);
	}
}

class Operator {
	String name;
	Vector ifList;
	Vector addList;
	Vector deleteList;

	Operator(String theName, Vector theIfList, Vector theAddList, Vector theDeleteList) {
		name = theName;
		ifList = theIfList;
		addList = theAddList;
		deleteList = theDeleteList;
	}

	public Vector getAddList() {
		return addList;
	}

	public Vector getDeleteList() {
		return deleteList;
	}

	public Vector getIfList() {
		return ifList;
	}

	public String toString() {
		String result = "NAME: " + name + "\n" + "IF :" + ifList + "\n" + "ADD:" + addList + "\n" + "DELETE:"
				+ deleteList;
		return result;
	}

	public Vector applyState(Vector theState) {
		for (int i = 0; i < addList.size(); i++) {
			theState.addElement(addList.elementAt(i));
		}
		for (int i = 0; i < deleteList.size(); i++) {
			theState.removeElement(deleteList.elementAt(i));
		}
		return theState;
	}

	public Operator getRenamedOperator(int uniqueNum) {
		Vector vars = new Vector();
		// IfListの変数を集める
		for (int i = 0; i < ifList.size(); i++) {
			String anIf = (String) ifList.elementAt(i);
			vars = getVars(anIf, vars);
		}
		// addListの変数を集める
		for (int i = 0; i < addList.size(); i++) {
			String anAdd = (String) addList.elementAt(i);
			vars = getVars(anAdd, vars);
		}
		// deleteListの変数を集める
		for (int i = 0; i < deleteList.size(); i++) {
			String aDelete = (String) deleteList.elementAt(i);
			vars = getVars(aDelete, vars);
		}
		Hashtable renamedVarsTable = makeRenamedVarsTable(vars, uniqueNum);

		// 新しいIfListを作る
		Vector newIfList = new Vector();
		for (int i = 0; i < ifList.size(); i++) {
			String newAnIf = renameVars((String) ifList.elementAt(i), renamedVarsTable);
			newIfList.addElement(newAnIf);
		}
		// 新しいaddListを作る
		Vector newAddList = new Vector();
		for (int i = 0; i < addList.size(); i++) {
			String newAnAdd = renameVars((String) addList.elementAt(i), renamedVarsTable);
			newAddList.addElement(newAnAdd);
		}
		// 新しいdeleteListを作る
		Vector newDeleteList = new Vector();
		for (int i = 0; i < deleteList.size(); i++) {
			String newADelete = renameVars((String) deleteList.elementAt(i), renamedVarsTable);
			newDeleteList.addElement(newADelete);
		}
		// 新しいnameを作る
		String newName = renameVars(name, renamedVarsTable);

		return new Operator(newName, newIfList, newAddList, newDeleteList);
	}

	private Vector getVars(String thePattern, Vector vars) {
		StringTokenizer st = new StringTokenizer(thePattern);
		for (int i = 0; i < st.countTokens();) {
			String tmp = st.nextToken();
			if (var(tmp)) {
				vars.addElement(tmp);
			}
		}
		return vars;
	}

	private Hashtable makeRenamedVarsTable(Vector vars, int uniqueNum) {
		Hashtable result = new Hashtable();
		for (int i = 0; i < vars.size(); i++) {
			String newVar = (String) vars.elementAt(i) + uniqueNum;
			result.put((String) vars.elementAt(i), newVar);
		}
		return result;
	}

	private String renameVars(String thePattern, Hashtable renamedVarsTable) {
		String result = new String();
		StringTokenizer st = new StringTokenizer(thePattern);
		for (int i = 0; i < st.countTokens();) {
			String tmp = st.nextToken();
			if (var(tmp)) {
				result = result + " " + (String) renamedVarsTable.get(tmp);
			} else {
				result = result + " " + tmp;
			}
		}
		return result.trim();
	}

	public Operator instantiate(Hashtable theBinding) {
		// name を具体化
		String newName = instantiateString(name, theBinding);
		// ifList を具体化
		Vector newIfList = new Vector();
		for (int i = 0; i < ifList.size(); i++) {
			String newIf = instantiateString((String) ifList.elementAt(i), theBinding);
			newIfList.addElement(newIf);
		}
		// addList を具体化
		Vector newAddList = new Vector();
		for (int i = 0; i < addList.size(); i++) {
			String newAdd = instantiateString((String) addList.elementAt(i), theBinding);
			newAddList.addElement(newAdd);
		}
		// deleteListを具体化
		Vector newDeleteList = new Vector();
		for (int i = 0; i < deleteList.size(); i++) {
			String newDelete = instantiateString((String) deleteList.elementAt(i), theBinding);
			newDeleteList.addElement(newDelete);
		}
		return new Operator(newName, newIfList, newAddList, newDeleteList);
	}

	private String instantiateString(String thePattern, Hashtable theBinding) {
		String result = new String();
		StringTokenizer st = new StringTokenizer(thePattern);
		for (int i = 0; i < st.countTokens();) {
			String tmp = st.nextToken();
			if (var(tmp)) {
				String newString = (String) theBinding.get(tmp);
				if (newString == null) {
					result = result + " " + tmp;
				} else {
					result = result + " " + newString;
				}
			} else {
				result = result + " " + tmp;
			}
		}
		return result.trim();
	}

	private boolean var(String str1) {
		// 先頭が ? なら変数
		return str1.startsWith("?");
	}
}

class Unifier {
	StringTokenizer st1;
	String buffer1[];
	StringTokenizer st2;
	String buffer2[];
	Hashtable vars;

	Unifier() {
		// vars = new Hashtable();
	}

	public boolean unify(String string1, String string2, Hashtable theBindings) {
		Hashtable orgBindings = new Hashtable();
		for (Enumeration e = theBindings.keys(); e.hasMoreElements();) {
			String key = (String) e.nextElement();
			String value = (String) theBindings.get(key);
			orgBindings.put(key, value);
		}
		this.vars = theBindings;
		if (unify(string1, string2)) {
			return true;
		} else {
			// 失敗したら元に戻す．
			theBindings.clear();
			for (Enumeration e = orgBindings.keys(); e.hasMoreElements();) {
				String key = (String) e.nextElement();
				String value = (String) orgBindings.get(key);
				theBindings.put(key, value);
			}
			return false;
		}
	}

	public boolean unify(String string1, String string2) {
		// 同じなら成功
		if (string1.equals(string2))
			return true;

		// 各々トークンに分ける
		st1 = new StringTokenizer(string1);
		st2 = new StringTokenizer(string2);

		// 数が異なったら失敗
		if (st1.countTokens() != st2.countTokens())
			return false;

		// 定数同士
		int length = st1.countTokens();
		buffer1 = new String[length];
		buffer2 = new String[length];
		for (int i = 0; i < length; i++) {
			buffer1[i] = st1.nextToken();
			buffer2[i] = st2.nextToken();
		}

		// 初期値としてバインディングが与えられていたら
		if (this.vars.size() != 0) {
			for (Enumeration keys = vars.keys(); keys.hasMoreElements();) {
				String key = (String) keys.nextElement();
				String value = (String) vars.get(key);
				replaceBuffer(key, value);
			}
		}

		for (int i = 0; i < length; i++) {
			if (!tokenMatching(buffer1[i], buffer2[i])) {
				return false;
			}
		}

		return true;
	}

	boolean tokenMatching(String token1, String token2) {
		if (token1.equals(token2))
			return true;
		if (var(token1) && !var(token2))
			return varMatching(token1, token2);
		if (!var(token1) && var(token2))
			return varMatching(token2, token1);
		if (var(token1) && var(token2))
			return varMatching(token1, token2);
		return false;
	}

	boolean varMatching(String vartoken, String token) {
		if (vars.containsKey(vartoken)) {
			if (token.equals(vars.get(vartoken))) {
				return true;
			} else {
				return false;
			}
		} else {
			replaceBuffer(vartoken, token);
			if (vars.contains(vartoken)) {
				replaceBindings(vartoken, token);
			}
			vars.put(vartoken, token);
		}
		return true;
	}

	void replaceBuffer(String preString, String postString) {
		for (int i = 0; i < buffer1.length; i++) {
			if (preString.equals(buffer1[i])) {
				buffer1[i] = postString;
			}
			if (preString.equals(buffer2[i])) {
				buffer2[i] = postString;
			}
		}
	}

	void replaceBindings(String preString, String postString) {
		Enumeration keys;
		for (keys = vars.keys(); keys.hasMoreElements();) {
			String key = (String) keys.nextElement();
			if (preString.equals(vars.get(key))) {
				vars.put(key, postString);
			}
		}
	}

	boolean var(String str1) {
		// 先頭が ? なら変数
		return str1.startsWith("?");
	}

}

// 追加
// ブロックの構造体(名前、色、形)
class Block {
	String name, color, shape;

	Block(String name, String color, String shape) {
		this.name = name;
		this.color = color;
		this.shape = shape;
	}

	public String toString() {
		String result = "SymbolName: " + name + "\n" + "Shape :" + shape.toString() + "\n" + "Color :"
				+ color.toString();
		return result;
	}
}