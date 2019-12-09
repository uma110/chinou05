import java.util.HashSet;

class ObjectCharactor {
	private String symbol;
	private SupportedShape shape;
	private SupportedColor color;
	private static HashSet<String> symbols = new HashSet<>();

	public static boolean existSameSymbol(String symbol) {
		return symbols.contains(symbol);
	}

	public ObjectCharactor(String symbol, SupportedShape shape, SupportedColor color) {
		this.setSymbol(symbol);
		this.setShape(shape);
		this.setColor(color);
		if (!symbol.equals("?"))
			symbols.add(symbol);
	}
	
	public ObjectCharactor(String symbol,SupportedShape shape,SupportedColor color,boolean isRegister){
		this.setSymbol(symbol);
		this.setShape(shape);
		this.setColor(color);
		if(isRegister){
			if(!symbol.equals("?"))
				symbols.add(symbol);
		}
	}

	public static boolean deleteSymbol(String symbol) {
		return symbols.remove(symbol);
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public SupportedShape getShape() {
		return shape;
	}

	public void setShape(SupportedShape shape) {
		this.shape = shape;
	}

	public SupportedColor getColor() {
		return color;
	}

	public void setColor(SupportedColor color) {
		this.color = color;
	}

	public String toString() {
		String result = "SymbolName: " + symbol + "\n" + "Shape :" + shape.toString() + "\n" + "Color :"
				+ color.toString();
		return result;
	}
}
