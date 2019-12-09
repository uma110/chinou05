import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

enum DisplayType{
	InitialState,GoalState,Process,
}

enum SupportedShape {
	Triangle("Triangle"), Circle("Circle"), Square("Square"),None("None");
	private String name;

	private SupportedShape(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public static SupportedShape getTagFromString(String name) {
		for (SupportedShape shape : SupportedShape.values()) {
			if (shape.getName().toLowerCase().equals(name.toLowerCase()))
				return shape;
		}
		return null;
	}
};

enum SupportedColor {
	Glay(Color.LIGHT_GRAY), Red(Color.RED), Blue(Color.BLUE), Yellow(Color.YELLOW),None(Color.WHITE);
	private Color color;

	private SupportedColor(Color color) {
		this.color = color;
	}

	public Color getColor() {
		return color;
	}

	public static Color getColorFromString(String name) {
		for (SupportedColor color : SupportedColor.values()) {
			if (color.toString().equals(name)) {
				return color.getColor();
			}
		}
		return null;
	}

	public static SupportedColor getTagFromString(String name) {
		for (SupportedColor color : SupportedColor.values()) {
			if (color.toString().toLowerCase().equals(name.toLowerCase()))
				return color;
		}
		return null;
	}
};

public class SituationMaker {
	private final int posFitValue = 5;
	private int objectHeight = 70;

	public JFrame displaySituation(List<ObjectCharactor> usableObjects, List<ObjectCharactor> usedObjects,
			ObjectCharactor holdingObj, String title) {
		ArrayList<ObjectCharactor> copyUsableList = new ArrayList<>(usableObjects);
		ArrayList<ObjectCharactor> copyUsedList = new ArrayList<>(usedObjects);

		JFrame displayFrame = new JFrame() {
			@Override
			public void paint(Graphics g) {
				super.paint(g);
				Graphics2D g2 = (Graphics2D) g;

				int usedObjectPosX = 510;
				int holdingHandPosX = 370;
				int holdingHandPosY = 380;
				int usableObjectPosX[] = new int[] { 60, 150, 240};
				int objectStartPosY = 700;
				int usableObjectDifY = (objectHeight + 20);

				if (holdingObj != null)
					setObject(g2, holdingHandPosX, holdingHandPosY, holdingObj);

				for (int i = 0; i < copyUsableList.size(); i++) {
					int xIndex = i % 3;
					int yIndex = i / 3;
					setObject(g2, usableObjectPosX[xIndex], objectStartPosY - usableObjectDifY * yIndex,
							copyUsableList.get(i));
				}
				for (int i = 0; i < copyUsedList.size(); i++) {
					setObject(g2, usedObjectPosX, objectStartPosY - objectHeight * i, copyUsedList.get(i));
				}
			}
		};

		/* 目標状態の作成はここでやらない仕様変更で廃止
		int[] setBoundsX = new int[]{
				570,1140,570
		};

		int displayX = setBoundsX[type.ordinal()];
		*/

		int displayX = 570;
		displayFrame.setBounds(displayX, 100, 580, 800);
		displayFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		displayFrame.getContentPane().setLayout(null);

		JLabel lblResult = new JLabel(title);
		lblResult.setFont(new Font("MS UI Gothic", Font.BOLD, 25));
		lblResult.setBounds(5, 0, 150, 60);
		lblResult.setHorizontalAlignment(SwingConstants.CENTER);
		displayFrame.getContentPane().add(lblResult);

		JLabel lblHoldingBox = new JLabel("HOLDING BOX");
		lblHoldingBox.setFont(new Font("MS UI Gothic", Font.BOLD, 22));
		lblHoldingBox.setBounds(290, 300, 150, 45);
		displayFrame.getContentPane().add(lblHoldingBox);

		return displayFrame;
	}
	
	public JFrame goalSituation(List<ObjectCharactor> goalList,String title){
		JFrame displayFrame = new JFrame() {
			@Override
			public void paint(Graphics g) {
				super.paint(g);
				Graphics2D g2 = (Graphics2D) g;

				int usedObjectPosX = 80;
				int objectStartPosY = 700;

				for (int i = 0; i < goalList.size(); i++) {
					setObject(g2, usedObjectPosX, objectStartPosY - objectHeight * i, goalList.get(i));
				}
			}
		};
		
		int displayX = 1140;
		displayFrame.setBounds(displayX, 100, 170, 800);
		displayFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		displayFrame.getContentPane().setLayout(null);

		JLabel lblResult = new JLabel(title);
		lblResult.setFont(new Font("MS UI Gothic", Font.BOLD, 25));
		lblResult.setBounds(0, 0, 150, 60);
		lblResult.setHorizontalAlignment(SwingConstants.CENTER);
		displayFrame.getContentPane().add(lblResult);
		return displayFrame;
	}



	private void setObject(Graphics2D g, int x, int y, ObjectCharactor object) {
		String symbol = object.getSymbol();
		SupportedShape shape = object.getShape();
		SupportedColor color = object.getColor();
		switch (shape) {
		case Triangle:
			setTriangle(g, x, y, color.getColor(), symbol);
			break;
		case Circle:
			setCircle(g, x, y, color.getColor(), symbol);
			break;
		case Square:
			setSquare(g, x, y, color.getColor(), symbol);
			break;
		case None:
			setNotDefinedObject(g, x, y, color.getColor(), symbol);
			break;
		}
	}

	private void setNotDefinedObject(Graphics2D g,int x,int y,Color c,String symbol){
		drawNotDefinedShape(g, x, y, c);
		g.setFont(new Font("MS UI Gothic", Font.PLAIN, 20));
		g.setColor(Color.BLACK);
		drawStringCenter(g, symbol, x, (int) (y + (objectHeight / 2.0) + posFitValue));
	}

	private void drawNotDefinedShape(Graphics2D g,int x,int y,Color c){
		int recX, recY;
		recX = (int) (x - objectHeight / 2);
		recY = y;
		Rectangle2D rectangle2d = new Rectangle2D.Double(recX, recY, objectHeight, objectHeight);
		g.setColor(c);
		g.fill(rectangle2d);
		g.setStroke(
				new BasicStroke(2.0f, BasicStroke.JOIN_ROUND,
			        BasicStroke.CAP_BUTT,
			        1.0f,
			        new float[]{10},
			        0.0f));
		g.setColor(Color.BLACK);
		g.draw(rectangle2d);
	}

	private void setTriangle(Graphics2D g, int x, int y, Color c, String symbol) {
		drawTriangle(g, x, y, c);
		g.setFont(new Font("MS UI Gothic", Font.PLAIN, 20));
		g.setColor(Color.BLACK);
		drawStringCenter(g, symbol, x, (int) (y + (objectHeight / 2.0) + posFitValue));
	}

	/**
	 * 三角形の描写 頂点の指定から
	 *
	 * @param g
	 * @param x
	 *            頂点のｘ
	 * @param y
	 *            頂点のｙ
	 * @param h
	 *            高さ
	 * @param c
	 *            カラー
	 */
	private void drawTriangle(Graphics2D g, int x, int y, Color c) {
		int n = 3;
		int triX[] = new int[n];
		int triY[] = new int[n];

		double dif = objectHeight / Math.sqrt(3);
		triX[0] = x;
		triY[0] = y;
		triX[1] = (int) (x - dif);
		triY[1] = y + objectHeight;
		triX[2] = (int) (x + dif);
		triY[2] = y + objectHeight;

		Polygon polygon = new Polygon(triX, triY, n);

		g.setColor(c);
		g.fillPolygon(polygon);

		g.setColor(Color.BLACK);
		g.setStroke(new BasicStroke());
		g.drawLine(triX[0], triY[0], triX[1], triY[1]);
		g.drawLine(triX[1], triY[1], triX[2], triY[2]);
		g.drawLine(triX[2], triY[2], triX[0], triY[0]);

	}

	private void setCircle(Graphics2D g, int x, int y, Color c, String symbol) {
		drawCircle(g, x, y, c);
		g.setFont(new Font("MS UI Gothic", Font.PLAIN, 20));
		g.setColor(Color.BLACK);
		drawStringCenter(g, symbol, x, (int) (y + (objectHeight / 2.0)));
	}

	/**
	 * 円の描写 円の頂上から下に描写
	 *
	 * @param g
	 * @param x
	 *            頂上のｘ
	 * @param y
	 *            頂上のｙ
	 * @param r
	 *            円の半径
	 * @param c
	 *            カラー
	 */
	private void drawCircle(Graphics2D g, int x, int y, Color c) {
		int circleX, circleY;
		circleX = (int) (x - objectHeight / 2.0);
		circleY = y;
		Ellipse2D ellipse2d = new Ellipse2D.Double(circleX, circleY, objectHeight, objectHeight);
		g.setColor(c);
		g.fill(ellipse2d);
		g.setColor(Color.BLACK);
		g.setStroke(new BasicStroke());
		g.draw(ellipse2d);
	}

	private void setSquare(Graphics2D g, int x, int y, Color c, String symbol) {
		drawSquare(g, x, y, c);
		g.setFont(new Font("MS UI Gothic", Font.PLAIN, 20));
		g.setColor(Color.BLACK);
		drawStringCenter(g, symbol, x, (int) (y + (objectHeight / 2.0)));
	}

	/**
	 * 正方形の描写 中央上部から下に描写
	 *
	 * @param g
	 * @param x
	 *            中央上部ｘ
	 * @param y
	 *            中央上部ｙ
	 * @param a
	 *            正方形の辺の長さ
	 * @param c
	 *            カラー
	 */
	private void drawSquare(Graphics2D g, int x, int y, Color c) {
		int recX, recY;
		recX = (int) (x - objectHeight / 2);
		recY = y;
		Rectangle2D rectangle2d = new Rectangle2D.Double(recX, recY, objectHeight, objectHeight);
		g.setColor(c);
		g.fill(rectangle2d);
		g.setColor(Color.BLACK);
		g.setStroke(new BasicStroke());
		g.draw(rectangle2d);
	}

	private void drawStringCenter(Graphics2D g, String text, int x, int y) {
		FontMetrics fm = g.getFontMetrics();
		Rectangle rectText = fm.getStringBounds(text, g).getBounds();
		x = x - rectText.width / 2;
		y = y - rectText.height / 2 + fm.getMaxAscent();

		g.drawString(text, x, y);
	}
}