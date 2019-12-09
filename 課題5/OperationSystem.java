import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JFrame;

enum OperationType {
	Pickup("Pickup"), Putdown("Putdown"), Place("Place"), Remove("Remove");
	private String name;

	private OperationType(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public static OperationType getTagFromString(String name) {
		for (OperationType type : OperationType.values()) {
			if (type.getName().equals(name))
				return type;
		}
		return null;
	}
}

public class OperationSystem {
	private ArrayList<ObjectCharactor> usableObjects;
	private ArrayList<ObjectCharactor> usedObjects;
	private ObjectCharactor holdingObject;

	private ArrayList<ObjectCharactor> goalState;

	private void InitList() {
		usableObjects = new ArrayList<>();
		usedObjects = new ArrayList<>();
		goalState = new ArrayList<>();
	}

	public OperationSystem(ArrayList<ObjectCharactor> usableObjects, ArrayList<ObjectCharactor> usedObjects,
			ArrayList<ObjectCharactor> goalList) {
		InitList();
		this.usableObjects.addAll(usableObjects);
		this.usedObjects.addAll(usedObjects);
		this.goalState.addAll(goalList);
		holdingObject = null;
	}

	public ArrayList<ObjectCharactor> getUsableObjets() {
		return usableObjects;
	}

	public ArrayList<ObjectCharactor> getUsedObjects() {
		return usedObjects;
	}

	public ObjectCharactor getHoldingObject() {
		return holdingObject;
	}

	public ArrayList<ObjectCharactor> getGoalState() {
		return goalState;
	}

	public boolean pickupObjectFromTable(String targetObjSymbol) {
		if (holdingObject != null)
			return false;
		int i;
		for (i = 0; i < usableObjects.size(); i++) {
			ObjectCharactor tmpObj = usableObjects.get(i);
			if (tmpObj.getSymbol().equals(targetObjSymbol)) {
				holdingObject = tmpObj;
				break;
			}
		}
		if (i == usableObjects.size())
			return false;
		usableObjects.remove(i);
		return true;
	}

	public boolean removeObjectOnBox(String removeObj, String baseObj) {
		if (holdingObject != null || getUsedObjects().size() < 2)
			return false;
		ObjectCharactor topObj = getUsedObjects().get(getUsedObjects().size() - 1);
		ObjectCharactor secondObj = getUsedObjects().get(getUsedObjects().size() - 2);

		if (!topObj.getSymbol().equals(removeObj) || !secondObj.getSymbol().equals(baseObj))
			return false;

		holdingObject = getUsedObjects().get(getUsedObjects().size() - 1);
		usedObjects.remove(getUsedObjects().size() - 1);
		if (usedObjects.size() == 1) {
			usableObjects.add(usedObjects.get(0));
			usedObjects.remove(0);
		}
		return true;
	}

	public boolean placeObjectOnBox(String placeObj, String baseObj) {
		if (holdingObject == null)
			return false;
		if (!holdingObject.getSymbol().equals(placeObj))
			return false;
		if (getUsedObjects().isEmpty()) {
			int i;
			for (i = 0; i < usableObjects.size(); i++) {
				ObjectCharactor tmpObj = usableObjects.get(i);
				if (tmpObj.getSymbol().equals(baseObj)) {
					usedObjects.add(tmpObj);
					break;
				}
			}
			if (i == usableObjects.size())
				return false;
			usableObjects.remove(i);
		} else {
			ObjectCharactor topObj = getUsedObjects().get(getUsedObjects().size() - 1);
			if (!topObj.getSymbol().equals(baseObj)) {
				return false;
			}
		}
		usedObjects.add(holdingObject);
		holdingObject = null;
		return true;
	}

	public boolean putDownHoldingBox(String targetObj) {
		if (holdingObject == null) {
			return false;
		}
		if (!holdingObject.getSymbol().equals(targetObj))
			return false;
		usableObjects.add(holdingObject);
		holdingObject = null;
		return true;
	}

	public boolean deleteObject(String objectSymbol) {
		if (holdingObject != null) {
			if (holdingObject.getSymbol().equals(objectSymbol)) {
				holdingObject = null;
				ObjectCharactor.deleteSymbol(objectSymbol);
				return true;
			}
		}
		int i;
		for (i = 0; i < usableObjects.size(); i++) {
			ObjectCharactor tmpObj = usableObjects.get(i);
			if (tmpObj.getSymbol().equals(objectSymbol)) {
				break;
			}
		}
		if (i != usableObjects.size()) {
			usableObjects.remove(i);
			ObjectCharactor.deleteSymbol(objectSymbol);
			return true;
		}
		int j;
		for (j = 0; j < usedObjects.size(); j++) {
			ObjectCharactor tmpObj = usedObjects.get(j);
			if (tmpObj.getSymbol().equals(objectSymbol)) {
				break;
			}
		}
		if (j != usedObjects.size()) {
			usedObjects.remove(j);
			ObjectCharactor.deleteSymbol(objectSymbol);
			return true;
		}
		return false;
	}

	public boolean moveObjInitToGoal(String target) {
		return moveObjBetweenList(target, usableObjects, usedObjects);
	}

	public boolean moveObjGoalToInit(String target) {
		return moveObjBetweenList(target, usedObjects, usableObjects);
	}

	private boolean moveObjBetweenList(String target, List<ObjectCharactor> srcList, List<ObjectCharactor> dstList) {
		int i;
		for (i = 0; i < srcList.size(); i++) {
			ObjectCharactor tmpObj = srcList.get(i);
			if (tmpObj.getSymbol().equals(target)) {
				dstList.add(tmpObj);
				break;
			}
		}
		if (i == srcList.size())
			return false;
		srcList.remove(i);
		return true;
	}

	public ObjectCharactor findObjInSystem(String objectSymbol) {
		if (holdingObject != null) {
			if (holdingObject.getSymbol().equals(objectSymbol)) {
				return holdingObject;
			}
		}
		for (int i = 0; i < usableObjects.size(); i++) {
			ObjectCharactor tmpObj = usableObjects.get(i);
			if (tmpObj.getSymbol().equals(objectSymbol)) {
				return tmpObj;
			}
		}
		for (int j = 0; j < usedObjects.size(); j++) {
			ObjectCharactor tmpObj = usedObjects.get(j);
			if (tmpObj.getSymbol().equals(objectSymbol)) {
				return tmpObj;
			}
		}
		return null;
	}

	public boolean addDefinedObjToGoalState(String symbol) {
		boolean[] alreadyExist = {false};
		goalState.forEach(obj -> {
			if(obj.getSymbol().equals(symbol)){
				alreadyExist[0] = true;
			}
		});
		if(alreadyExist[0]){
			return false;
		}

		if (ObjectCharactor.existSameSymbol(symbol)) {
			ObjectCharactor obj = findObjInSystem(symbol);
			if (obj != null) {
				goalState.add(obj);
				return true;
			}
		}
		return false;
	}

	public boolean addNotDefinedObjToGoalState(ObjectCharactor obj){
		if(obj == null){
			return false;
		}
		goalState.add(obj);
		return true;
	}

	public void goalStateInit(){
		goalState.clear();
	}


	public LinkedList<JFrame> planningExecute(String[] plan) {
		ArrayList<ObjectCharactor> copyInitUsableList = new ArrayList<>(usableObjects);
		ArrayList<ObjectCharactor> copyInitUsedList = new ArrayList<>(usedObjects);
		LinkedList<JFrame> frames = new LinkedList<>();
		SituationMaker maker = new SituationMaker();
		int processNumber = 0;
		frames.add(maker.displaySituation(getUsableObjets(), getUsedObjects(), holdingObject,
				"Process: " + processNumber));
		for (String operation : plan) {
			System.out.println("operation: " + operation);
			processNumber++;
			String[] splitedOperation = operation.split("\\s+", 0);
			String opType = splitedOperation[0].toLowerCase();
			String target = null;
			String bottom = null;
			boolean success = false;
			switch (opType) {
			case "pick":
				target = splitedOperation[2];
				success = pickupObjectFromTable(target);
				break;
			case "put":
				target = splitedOperation[1];
				success = putDownHoldingBox(target);
				break;
			case "place":
				target = splitedOperation[1];
				bottom = splitedOperation[3];
				success = placeObjectOnBox(target, bottom);
				break;
			case "remove":
				target = splitedOperation[1];
				bottom = splitedOperation[5];
				success = removeObjectOnBox(target, bottom);
				break;
			default:
				System.out.println("operation identification missed");
				usableObjects = copyInitUsableList;
				usedObjects = copyInitUsedList;
				holdingObject = null;
				return null;
			}
			System.out.println("type =" + opType);
			System.out.println("target: " + target + " bottom: " + bottom);
			System.out.println("success: " + success);
			if (!success) {
				System.out.println("operation false");
				return null;
			}
			frames.add(maker.displaySituation(getUsableObjets(), getUsedObjects(), holdingObject,
					"Process: " + processNumber));
		}
		usableObjects = copyInitUsableList;
		usedObjects = copyInitUsedList;
		holdingObject = null;
		return frames;
	}
}