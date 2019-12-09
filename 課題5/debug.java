import java.util.ArrayList;
import java.util.Arrays;

public class debug {

	static ArrayList<ObjectCharactor> objects;
	static ObjectCharactor objectCharactor;

	static void debug(ObjectCharactor target) {
	}

	static void print(ArrayList<ObjectCharactor> list) {
		for (ObjectCharactor obj : list) {
			System.out.println(obj.toString());
		}
	}

	static void debug() {
		ObjectCharactor tmp = objects.get(objects.size() - 1);
		objects.remove(objects.size() - 1);
		objectCharactor = tmp;
	}

	public static void main(String[] args) {
		String message = "A on Glay";
		String[] splits = message.split("\\son\\s");
		Arrays.stream(splits).forEach(System.out::println);
	}
}
