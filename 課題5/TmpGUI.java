import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class TmpGUI {

	private JFrame baseFrame_1;
	private JFrame baseFrame_1_1;
	private final Action action = new SwingAction();
	private JTextField textField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TmpGUI window = new TmpGUI();
					window.baseFrame_1_1.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public TmpGUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		baseFrame_1_1 = new JFrame(){
			@Override
			public void paint(Graphics g){
				super.paint(g);
				Graphics2D g2 = (Graphics2D) g;
				int x = 100,y = 100;
				int objectHeight = 100;
				int recX, recY;
				recX = (int) (x - objectHeight / 2);
				recY = y;
				Rectangle2D rectangle2d = new Rectangle2D.Double(recX, recY, objectHeight, objectHeight);
				g2.setColor(Color.white);
				g2.fill(rectangle2d);
				   g2.setStroke(new BasicStroke(
					        2.0f,
					        BasicStroke.JOIN_ROUND,
					        BasicStroke.CAP_BUTT,
					        1.0f,
					        new float[]{10},
					        0.0f));
				g2.setColor(Color.BLACK);
				g2.draw(rectangle2d);
			}
		};
		baseFrame_1_1.setBounds(100, 100, 1000, 1000);
		baseFrame_1_1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		baseFrame_1_1.setTitle("描画");
		baseFrame_1_1.getContentPane().setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(12, 13, 458, 77);
		baseFrame_1_1.getContentPane().add(scrollPane);
		
		JTextArea textArea = new JTextArea();
		scrollPane.setViewportView(textArea);
		textArea.setLineWrap(true);
		textArea.setEditable(false);
	}

	
	
	public void changePaint(){
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
	private void drawTriangle(Graphics2D g, int x, int y, int h, Color c) {
		int n = 3;
		int triX[] = new int[n];
		int triY[] = new int[n];

		double dif = h / Math.sqrt(3);
		triX[0] = x;
		triY[0] = y;
		triX[1] = (int) (x - dif);
		triY[1] = y + h;
		triX[2] = (int) (x + dif);
		triY[2] = y + h;

		Polygon polygon = new Polygon(triX, triY, n);

		g.setColor(c);
		g.fillPolygon(polygon);

		g.setColor(Color.BLACK);
		g.drawLine(triX[0], triY[0], triX[1], triY[1]);
		g.drawLine(triX[1], triY[1], triX[2], triY[2]);
		g.drawLine(triX[2], triY[2], triX[0], triY[0]);

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
	private void drawEllipse(Graphics2D g, int x, int y, int r, Color c) {
		int circleX, circleY;
		circleX = (int) (x - r / 2.0);
		circleY = y;
		Ellipse2D ellipse2d = new Ellipse2D.Double(circleX, circleY, r, r);
		g.setColor(c);
		g.fill(ellipse2d);
		g.setColor(Color.BLACK);
		g.draw(ellipse2d);
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
	private void drawRectangle(Graphics2D g, int x, int y, int a, Color c) {
		int recX, recY;
		recX = (int) (x - a / 2);
		recY = y;
		Rectangle2D rectangle2d = new Rectangle2D.Double(recX, recY, a, a);
		g.setColor(c);
		g.fill(rectangle2d);
		g.setColor(Color.BLACK);
		g.draw(rectangle2d);
	}

	private class SwingAction extends AbstractAction {
		public SwingAction() {
			putValue(NAME, "SWITCH");
			putValue(SHORT_DESCRIPTION, "Some short description");
		}

		public void actionPerformed(ActionEvent e) {
			System.out.println("1");
		}
	}

	private static void addPopup(Component component, final JPopupMenu popup) {
		component.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}

			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}

			private void showMenu(MouseEvent e) {
				popup.show(e.getComponent(), e.getX(), e.getY());
			}
		});
	}
}