import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.LinkedList;

import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class Display {
	private boolean isPlanningExecute;

	private JFrame baseFrame;
	private JFrame initialStateDisplay;
	private JFrame goalStateDisplay;

	private JFrame messenger;
	private JTextArea messengerText;

	private JButton btnNewButton;
	private final Action action = new SwingAction();

	private OperationSystem stateSystem;
	private JFrame[] planDisplays;
	private int currentPlanDispIndex;
	private SituationMaker situationMaker;
	private JTextField addObjName;
	private JRadioButton[] colorButtons;
	private JRadioButton rdbtnGlay;
	private JRadioButton rdbtnBlue;
	private JRadioButton rdbtnRed;
	private JRadioButton rdbtnYellow;

	private JRadioButton[] shapeButtons;
	private JRadioButton rdbtnSquare;
	private JRadioButton rdbtnTriangle;
	private JRadioButton rdbtnCircle;

	private JPanel panel_1;
	private JPanel panel_2;
	private JLabel lblColor;
	private JLabel lblNewLabel;
	private JLabel lblSymbl;
	private JButton btnAdd;
	private final Action addAction = new SwingAction_1();
	private ButtonGroup colorButtonGroup;
	private ButtonGroup shapeButtonGroup;
	private JLabel lblSymbol;
	private JTextField deleteObjName;
	private JButton btnNewButton_1;
	private final Action deleteAction = new SwingAction_2();
	private JPanel panel_3;
	private JPanel panel_4;
	private JRadioButton moveInitToGoal;
	private JRadioButton moveGoalToInit;
	private JPanel panel_5;
	private JLabel lblObjectoperation;
	private JTextField opTarget;
	private JLabel lblTarget;
	private JButton btnNewButton_2;
	private final Action operationExecute = new SwingAction_3();
	private ButtonGroup operationButtonGroup;
	private JTextField OperationText;
	private JLabel lblNaturalLanguageOperation;
	private JButton btnNewButton_3;
	private JButton btnNewButton_4;
	private JButton btnNewButton_5;
	private final Action showStartDisplay = new SwingAction_4();
	private final Action showPreDisplay = new SwingAction_5();
	private final Action showNextDisplay = new SwingAction_6();
	private JButton btnNewButton_6;
	private final Action wordOperation = new SwingAction_7();
	private JPanel panel_6;
	private JLabel label;
	private JLabel lblPlann;
	private JLabel label_1;
	private JButton btnEnd;
	private final Action displayClose = new SwingAction_8();
	private ButtonGroup opStateButton;

	private EnglishOperationProcessing nlpSystem;
	private JPanel panel_7;
	private JLabel lblInitialStateOperation;
	private JPanel panel_8;
	private JPanel panel_9;

	private JRadioButton[] colorButtonsForGoal;
	private JRadioButton rdbGlayForGoal;
	private JRadioButton rdbBlueForGoal;
	private JRadioButton rdbRedForGoal;
	private JRadioButton rdbYellowForGoal;

	private ButtonGroup colorButtonGroupForGoal;
	private ButtonGroup shapeButtonGroupForGoal;

	private JLabel label_2;
	private JRadioButton rdbColorNoneForGoal;
	private JPanel panel_10;

	private JRadioButton[] shapeButtonsForGoal;
	private JRadioButton rdbSquareForGoal;
	private JRadioButton rdbTriangleForGoal;
	private JRadioButton rdbCircleForGoal;

	private JLabel label_3;
	private JRadioButton rdbShapeNoneForGoal;
	private JPanel panel_11;
	private JLabel label_4;
	private JTextField addObjNameToGoal;
	private JButton button_1;
	private JPanel panel_12;
	private JLabel lblGoalStateOperation;
	private JPanel panel_13;
	private JLabel lblNewLabel_1;
	private JButton btnNewButton_7;
	private final Action addActionForGoal = new SwingAction_9();
	private final Action goalStateReset = new SwingAction_10();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Display window = new Display();
					window.baseFrame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Display() {
		initialize();
		messengerInit();

		isPlanningExecute = false;
		situationMaker = new SituationMaker();
		nlpSystem = new EnglishOperationProcessing();

		ArrayList<ObjectCharactor> initUsableList = new ArrayList<>();

		initUsableList.add(new ObjectCharactor("A", SupportedShape.Triangle, SupportedColor.Glay));
		initUsableList.add(new ObjectCharactor("B", SupportedShape.Square, SupportedColor.Blue));
		initUsableList.add(new ObjectCharactor("C", SupportedShape.Circle, SupportedColor.Red));

		initUsableList.add(new ObjectCharactor("D", SupportedShape.Triangle, SupportedColor.Glay));
		initUsableList.add(new ObjectCharactor("E", SupportedShape.Square, SupportedColor.Blue));
		initUsableList.add(new ObjectCharactor("F", SupportedShape.Circle, SupportedColor.Red));
		initUsableList.add(new ObjectCharactor("G", SupportedShape.Triangle, SupportedColor.Glay));
		initUsableList.add(new ObjectCharactor("H", SupportedShape.Square, SupportedColor.Blue));
		initUsableList.add(new ObjectCharactor("I", SupportedShape.Circle, SupportedColor.Red));

		stateSystem = new OperationSystem(initUsableList, new ArrayList<>(), new ArrayList<>());

		displayUpdate();
	}

	private void setMessengerText(String message, boolean isOverwrite) {
		if (isOverwrite) {
			messengerText.setText(message);
		} else {
			String preMessage = messengerText.getText();
			String currentMessage = preMessage;
			if (preMessage.matches("\\s*"))
				currentMessage += message;
			else
				currentMessage += "\n" + message;
			messengerText.setText(currentMessage);
		}
	}

	private void messengerInit() {
		messenger = new JFrame();
		messenger.setBounds(380, 415, 500, 150);
		messenger.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		messenger.setTitle("SYSTEM MESSAGE");

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(12, 13, 458, 77);
		messenger.getContentPane().add(scrollPane);

		messengerText = new JTextArea();
		scrollPane.setViewportView(messengerText);
		messengerText.setLineWrap(true);
		messengerText.setEditable(false);

		messenger.setVisible(false);
	}

	/*
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		baseFrame = new JFrame();
		baseFrame.getContentPane().setFont(new Font("MS UI Gothic", Font.BOLD, 21));
		baseFrame.setBounds(30, 30, 550, 1000);
		baseFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		baseFrame.getContentPane().setLayout(null);

		colorButtons = new JRadioButton[SupportedColor.values().length - 1];

		colorButtonGroup = new ButtonGroup();

		shapeButtonGroup = new ButtonGroup();

		shapeButtons = new JRadioButton[SupportedShape.values().length - 1];

		colorButtonGroupForGoal = new ButtonGroup();

		shapeButtonGroupForGoal = new ButtonGroup();

		colorButtonsForGoal = new JRadioButton[SupportedColor.values().length];

		shapeButtonsForGoal = new JRadioButton[SupportedShape.values().length];

		operationButtonGroup = new ButtonGroup();

		opStateButton = new ButtonGroup();

		panel_6 = new JPanel();
		panel_6.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		panel_6.setBounds(12, 832, 513, 108);
		baseFrame.getContentPane().add(panel_6);
		panel_6.setLayout(null);

		btnNewButton = new JButton("New button");
		btnNewButton.setFont(new Font("MS UI Gothic", Font.BOLD, 15));
		btnNewButton.setBounds(12, 45, 65, 50);
		panel_6.add(btnNewButton);
		btnNewButton.setAction(action);

		btnNewButton_3 = new JButton("New button");
		btnNewButton_3.setFont(new Font("MS UI Gothic", Font.BOLD, 15));
		btnNewButton_3.setBounds(123, 45, 77, 50);
		panel_6.add(btnNewButton_3);
		btnNewButton_3.setAction(showStartDisplay);

		btnNewButton_4 = new JButton("New button");
		btnNewButton_4.setFont(new Font("MS UI Gothic", Font.BOLD, 15));
		btnNewButton_4.setBounds(212, 45, 77, 50);
		panel_6.add(btnNewButton_4);
		btnNewButton_4.setAction(showPreDisplay);

		btnNewButton_5 = new JButton("New button");
		btnNewButton_5.setFont(new Font("MS UI Gothic", Font.BOLD, 15));
		btnNewButton_5.setBounds(301, 45, 77, 50);
		panel_6.add(btnNewButton_5);
		btnNewButton_5.setAction(showNextDisplay);

		label = new JLabel("-->");
		label.setFont(new Font("Malgun Gothic", Font.BOLD, 21));
		label.setBounds(83, 45, 33, 50);
		panel_6.add(label);

		lblPlann = new JLabel("Planning Display Operation\r\n");
		lblPlann.setFont(new Font("MS UI Gothic", Font.BOLD, 21));
		lblPlann.setBounds(12, 13, 248, 26);
		panel_6.add(lblPlann);

		label_1 = new JLabel("-->");
		label_1.setFont(new Font("Malgun Gothic", Font.BOLD, 21));
		label_1.setBounds(382, 45, 33, 50);
		panel_6.add(label_1);

		btnEnd = new JButton("end");
		btnEnd.setFont(new Font("MS UI Gothic", Font.BOLD, 15));
		btnEnd.setAction(displayClose);
		btnEnd.setBounds(419, 45, 75, 50);
		panel_6.add(btnEnd);

		panel_7 = new JPanel();
		panel_7.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		panel_7.setBounds(12, 13, 513, 407);
		baseFrame.getContentPane().add(panel_7);
		panel_7.setLayout(null);

		panel_3 = new JPanel();
		panel_3.setBounds(12, 50, 359, 202);
		panel_7.add(panel_3);
		panel_3.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel_3.setLayout(null);

		panel_1 = new JPanel();
		panel_1.setBounds(145, 13, 85, 176);
		panel_3.add(panel_1);
		panel_1.setLayout(null);

		rdbtnGlay = new JRadioButton(SupportedColor.Glay.toString());
		rdbtnGlay.setBounds(5, 30, 55, 25);
		panel_1.add(rdbtnGlay);

		rdbtnBlue = new JRadioButton(SupportedColor.Blue.toString());
		rdbtnBlue.setBounds(5, 90, 55, 25);
		panel_1.add(rdbtnBlue);

		rdbtnRed = new JRadioButton(SupportedColor.Red.toString());
		rdbtnRed.setBounds(5, 60, 51, 25);
		panel_1.add(rdbtnRed);

		rdbtnYellow = new JRadioButton(SupportedColor.Yellow.toString());
		rdbtnYellow.setBounds(5, 120, 65, 25);
		panel_1.add(rdbtnYellow);
		colorButtons[0] = rdbtnGlay;
		colorButtons[1] = rdbtnRed;
		colorButtons[2] = rdbtnBlue;
		colorButtons[3] = rdbtnYellow;

		colorButtonGroup.add(rdbtnGlay);
		colorButtonGroup.add(rdbtnBlue);
		colorButtonGroup.add(rdbtnRed);
		colorButtonGroup.add(rdbtnYellow);
		rdbtnGlay.setSelected(true);

		lblColor = new JLabel("Color");
		lblColor.setFont(new Font("MS UI Gothic", Font.ITALIC, 21));
		lblColor.setBounds(4, 0, 65, 25);
		panel_1.add(lblColor);

		panel_2 = new JPanel();
		panel_2.setBounds(242, 13, 100, 160);
		panel_3.add(panel_2);
		panel_2.setLayout(null);

		rdbtnSquare = new JRadioButton(SupportedShape.Square.getName());
		rdbtnSquare.setBounds(5, 30, 69, 25);
		panel_2.add(rdbtnSquare);

		rdbtnTriangle = new JRadioButton(SupportedShape.Triangle.getName());
		rdbtnTriangle.setBounds(5, 60, 75, 25);
		panel_2.add(rdbtnTriangle);

		rdbtnCircle = new JRadioButton(SupportedShape.Circle.getName());
		rdbtnCircle.setBounds(5, 90, 63, 25);
		panel_2.add(rdbtnCircle);

		lblNewLabel = new JLabel("Shape");
		lblNewLabel.setFont(new Font("MS UI Gothic", Font.ITALIC, 21));
		lblNewLabel.setBounds(4, 0, 65, 25);
		panel_2.add(lblNewLabel);
		shapeButtonGroup.add(rdbtnSquare);
		shapeButtonGroup.add(rdbtnTriangle);
		shapeButtonGroup.add(rdbtnCircle);
		rdbtnSquare.setSelected(true);
		shapeButtons[0] = rdbtnSquare;
		shapeButtons[1] = rdbtnTriangle;
		shapeButtons[2] = rdbtnCircle;

		panel_4 = new JPanel();
		panel_4.setBorder(new EmptyBorder(0, 0, 0, 0));
		panel_4.setBounds(12, 11, 121, 164);
		panel_3.add(panel_4);
		panel_4.setLayout(null);

		lblSymbl = new JLabel("Symbol");
		lblSymbl.setBounds(22, 0, 65, 25);
		panel_4.add(lblSymbl);
		lblSymbl.setFont(new Font("MS UI Gothic", Font.ITALIC, 21));

		addObjName = new JTextField();
		addObjName.setBounds(12, 50, 95, 37);
		panel_4.add(addObjName);
		addObjName.setFont(new Font("MS UI Gothic", Font.BOLD, 18));
		addObjName.setColumns(10);
		addObjName.setAction(addAction);

		btnAdd = new JButton("Add");
		btnAdd.setBounds(12, 116, 95, 37);
		panel_4.add(btnAdd);
		btnAdd.setFont(new Font("MS UI Gothic", Font.BOLD, 18));
		btnAdd.setAction(addAction);

		JPanel panel = new JPanel();
		panel.setBounds(383, 50, 120, 202);
		panel_7.add(panel);
		panel.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel.setLayout(null);

		lblSymbol = new JLabel("Symbol");
		lblSymbol.setFont(new Font("MS UI Gothic", Font.ITALIC, 21));
		lblSymbol.setBounds(22, 10, 71, 32);
		panel.add(lblSymbol);

		deleteObjName = new JTextField();
		deleteObjName.setFont(new Font("MS UI Gothic", Font.BOLD, 18));
		deleteObjName.setBounds(12, 60, 95, 37);
		panel.add(deleteObjName);
		deleteObjName.setColumns(10);
		deleteObjName.setAction(deleteAction);

		btnNewButton_1 = new JButton("Delete");
		btnNewButton_1.setFont(new Font("MS UI Gothic", Font.ITALIC, 18));
		btnNewButton_1.setAction(deleteAction);
		btnNewButton_1.setBounds(12, 127, 95, 37);
		panel.add(btnNewButton_1);

		lblInitialStateOperation = new JLabel("Initial State Operation");
		lblInitialStateOperation.setFont(new Font("MS UI Gothic", Font.BOLD, 21));
		lblInitialStateOperation.setBounds(12, 13, 221, 24);
		panel_7.add(lblInitialStateOperation);

		panel_12 = new JPanel();
		panel_12.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel_12.setBounds(12, 265, 490, 116);
		panel_7.add(panel_12);
		panel_12.setLayout(null);

		lblObjectoperation = new JLabel("ObjectOperation");
		lblObjectoperation.setBounds(12, 13, 160, 20);
		panel_12.add(lblObjectoperation);
		lblObjectoperation.setFont(new Font("MS UI Gothic", Font.BOLD, 19));

		moveInitToGoal = new JRadioButton("Table->Pickup");
		moveInitToGoal.setBounds(31, 45, 141, 25);
		panel_12.add(moveInitToGoal);
		moveInitToGoal.setFont(new Font("MS UI Gothic", Font.ITALIC, 17));
		operationButtonGroup.add(moveInitToGoal);
		moveInitToGoal.setSelected(true);

		moveGoalToInit = new JRadioButton("Pickup->Table");
		moveGoalToInit.setBounds(31, 82, 141, 25);
		panel_12.add(moveGoalToInit);
		moveGoalToInit.setFont(new Font("MS UI Gothic", Font.ITALIC, 17));
		operationButtonGroup.add(moveGoalToInit);

		opTarget = new JTextField();
		opTarget.setBounds(190, 63, 110, 40);
		panel_12.add(opTarget);
		opTarget.setColumns(10);
		opTarget.setAction(operationExecute);

		lblTarget = new JLabel("Target");
		lblTarget.setBounds(80, 51, 57, 16);
		panel_12.add(lblTarget);
		lblTarget.setFont(new Font("MS UI Gothic", Font.ITALIC, 17));

		btnNewButton_2 = new JButton("New button");
		btnNewButton_2.setBounds(345, 63, 99, 40);
		panel_12.add(btnNewButton_2);
		btnNewButton_2.setFont(new Font("MS UI Gothic", Font.BOLD, 12));
		btnNewButton_2.setAction(operationExecute);

		panel_5 = new JPanel();
		panel_5.setBounds(12, 433, 513, 262);
		baseFrame.getContentPane().add(panel_5);
		panel_5.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		panel_5.setLayout(null);

		panel_8 = new JPanel();
		panel_8.setLayout(null);
		panel_8.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel_8.setBounds(12, 51, 359, 202);
		panel_5.add(panel_8);

		panel_9 = new JPanel();
		panel_9.setLayout(null);
		panel_9.setBounds(145, 13, 85, 176);
		panel_8.add(panel_9);

		rdbGlayForGoal = new JRadioButton("Glay");
		rdbGlayForGoal.setSelected(true);
		rdbGlayForGoal.setBounds(5, 30, 55, 25);
		panel_9.add(rdbGlayForGoal);

		rdbBlueForGoal = new JRadioButton("Blue");
		rdbBlueForGoal.setBounds(5, 90, 55, 25);
		panel_9.add(rdbBlueForGoal);

		rdbRedForGoal = new JRadioButton("Red");
		rdbRedForGoal.setBounds(5, 60, 51, 25);
		panel_9.add(rdbRedForGoal);

		rdbYellowForGoal = new JRadioButton("Yellow");
		rdbYellowForGoal.setBounds(5, 120, 65, 25);
		panel_9.add(rdbYellowForGoal);

		label_2 = new JLabel("Color");
		label_2.setFont(new Font("MS UI Gothic", Font.ITALIC, 21));
		label_2.setBounds(4, 0, 65, 25);
		panel_9.add(label_2);

		rdbColorNoneForGoal = new JRadioButton("None");
		rdbColorNoneForGoal.setBounds(5, 151, 72, 25);
		panel_9.add(rdbColorNoneForGoal);

		colorButtonGroupForGoal.add(rdbGlayForGoal);
		colorButtonGroupForGoal.add(rdbRedForGoal);
		colorButtonGroupForGoal.add(rdbBlueForGoal);
		colorButtonGroupForGoal.add(rdbYellowForGoal);
		colorButtonGroupForGoal.add(rdbColorNoneForGoal);
		rdbGlayForGoal.setSelected(true);
		colorButtonsForGoal[0] = rdbGlayForGoal;
		colorButtonsForGoal[1] = rdbRedForGoal;
		colorButtonsForGoal[2] = rdbBlueForGoal;
		colorButtonsForGoal[3] = rdbYellowForGoal;
		colorButtonsForGoal[4] = rdbColorNoneForGoal;

		panel_10 = new JPanel();
		panel_10.setLayout(null);
		panel_10.setBounds(242, 13, 100, 160);
		panel_8.add(panel_10);

		rdbSquareForGoal = new JRadioButton("Square");
		rdbSquareForGoal.setSelected(true);
		rdbSquareForGoal.setBounds(5, 30, 69, 25);
		panel_10.add(rdbSquareForGoal);

		rdbTriangleForGoal = new JRadioButton("Triangle");
		rdbTriangleForGoal.setBounds(5, 60, 75, 25);
		panel_10.add(rdbTriangleForGoal);

		rdbCircleForGoal = new JRadioButton("Circle");
		rdbCircleForGoal.setBounds(5, 90, 63, 25);
		panel_10.add(rdbCircleForGoal);

		label_3 = new JLabel("Shape");
		label_3.setFont(new Font("MS UI Gothic", Font.ITALIC, 21));
		label_3.setBounds(4, 0, 65, 25);
		panel_10.add(label_3);

		rdbShapeNoneForGoal = new JRadioButton("None");
		rdbShapeNoneForGoal.setBounds(8, 115, 69, 25);
		panel_10.add(rdbShapeNoneForGoal);

		shapeButtonGroupForGoal.add(rdbSquareForGoal);
		shapeButtonGroupForGoal.add(rdbTriangleForGoal);
		shapeButtonGroupForGoal.add(rdbCircleForGoal);
		shapeButtonGroupForGoal.add(rdbShapeNoneForGoal);
		rdbSquareForGoal.setSelected(true);
		shapeButtonsForGoal[0] = rdbSquareForGoal;
		shapeButtonsForGoal[1] = rdbTriangleForGoal;
		shapeButtonsForGoal[2] = rdbCircleForGoal;
		shapeButtonsForGoal[3] = rdbShapeNoneForGoal;

		panel_11 = new JPanel();
		panel_11.setLayout(null);
		panel_11.setBorder(new EmptyBorder(0, 0, 0, 0));
		panel_11.setBounds(12, 11, 121, 164);
		panel_8.add(panel_11);

		label_4 = new JLabel("Symbol");
		label_4.setFont(new Font("MS UI Gothic", Font.ITALIC, 21));
		label_4.setBounds(22, 0, 65, 25);
		panel_11.add(label_4);

		addObjNameToGoal = new JTextField();
		addObjNameToGoal.setFont(new Font("MS UI Gothic", Font.BOLD, 18));
		addObjNameToGoal.setColumns(10);
		addObjNameToGoal.setBounds(12, 50, 95, 37);
		addObjNameToGoal.setAction(addActionForGoal);
		panel_11.add(addObjNameToGoal);

		button_1 = new JButton("Add");
		button_1.setAction(addActionForGoal);
		button_1.setFont(new Font("MS UI Gothic", Font.BOLD, 18));
		button_1.setBounds(12, 116, 95, 37);
		panel_11.add(button_1);

		lblGoalStateOperation = new JLabel("Goal State Operation");
		lblGoalStateOperation.setFont(new Font("MS UI Gothic", Font.BOLD, 21));
		lblGoalStateOperation.setBounds(12, 13, 214, 25);
		panel_5.add(lblGoalStateOperation);

		lblNewLabel_1 = new JLabel("State Reset\r\n");
		lblNewLabel_1.setFont(new Font("MS UI Gothic", Font.ITALIC, 19));
		lblNewLabel_1.setBounds(383, 78, 113, 37);
		panel_5.add(lblNewLabel_1);

		btnNewButton_7 = new JButton("New button");
		btnNewButton_7.setFont(new Font("MS UI Gothic", Font.BOLD, 20));
		btnNewButton_7.setAction(goalStateReset);
		btnNewButton_7.setBounds(383, 128, 101, 43);
		panel_5.add(btnNewButton_7);

				panel_13 = new JPanel();
				panel_13.setBounds(12, 708, 513, 116);
				baseFrame.getContentPane().add(panel_13);
				panel_13.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
				panel_13.setLayout(null);

						OperationText = new JTextField();
						OperationText.setBounds(12, 41, 489, 22);
						panel_13.add(OperationText);
						OperationText.setColumns(10);
						OperationText.setAction(wordOperation);

								lblNaturalLanguageOperation = new JLabel("Natural Language Operation");
								lblNaturalLanguageOperation.setBounds(12, 13, 267, 19);
								panel_13.add(lblNaturalLanguageOperation);
								lblNaturalLanguageOperation.setFont(new Font("MS UI Gothic", Font.BOLD, 20));

										btnNewButton_6 = new JButton("New button");
										btnNewButton_6.setBounds(12, 76, 313, 27);
										panel_13.add(btnNewButton_6);
										btnNewButton_6.setFont(new Font("MS UI Gothic", Font.BOLD, 17));
										btnNewButton_6.setAction(wordOperation);

	}

	private void displayUpdate() {
		if (initialStateDisplay != null)
			initialStateDisplay.dispose();
		initialStateDisplay = situationMaker.displaySituation(stateSystem.getUsableObjets(),
				stateSystem.getUsedObjects(), stateSystem.getHoldingObject(), "InitialState");
		initialStateDisplay.setVisible(true);

		if (goalStateDisplay != null)
			goalStateDisplay.dispose();
		goalStateDisplay = situationMaker.goalSituation(stateSystem.getGoalState(), "GoalState");
		goalStateDisplay.setVisible(true);
	}

	private String getSelectedButtonText(ButtonGroup buttonGroup) {
		for (Enumeration<AbstractButton> buttons = buttonGroup.getElements(); buttons.hasMoreElements();) {
			AbstractButton button = buttons.nextElement();

			if (button.isSelected()) {
				return button.getText();
			}
		}
		return null;
	}

	private class SwingAction extends AbstractAction {
		public SwingAction() {
			putValue(NAME, "Plan");
			putValue(SHORT_DESCRIPTION, "Some short description");
		}

		public void actionPerformed(ActionEvent e) {
			if (isPlanningExecute)
				return;
			isPlanningExecute = true;

			if (stateSystem.getGoalState().size() < 2) {
				isPlanningExecute = false;
				return;
			}

			Planner planner = new Planner();
			String[] plans = planner.startMakePlan(stateSystem.getUsableObjets(), stateSystem.getUsedObjects(),
					stateSystem.getGoalState());
			if(plans == null){
				System.out.println("Plannning false");
				setMessengerText("Planning false", false);
				isPlanningExecute = false;
				return;
			}

			LinkedList<JFrame> tmpList = stateSystem.planningExecute(plans);
			planDisplays = tmpList.toArray(new JFrame[tmpList.size()]);
			if (planDisplays.length > 0) {
				initialStateDisplay.setVisible(false);
				currentPlanDispIndex = 0;
				planDisplays[currentPlanDispIndex].setVisible(true);
			}

		}
	}

	private class SwingAction_1 extends AbstractAction {
		public SwingAction_1() {
			putValue(NAME, "Add");
			putValue(SHORT_DESCRIPTION, "Some short description");
		}

		public void actionPerformed(ActionEvent e) {
			if (isPlanningExecute)
				return;
			System.out.println("Add");
			String symbol = addObjName.getText();
			if (symbol == null || ObjectCharactor.existSameSymbol(symbol)) {
				System.out.println("Same symbol can not be used or symbol is null");
				return;
			}
			if (symbol.equals("?") || symbol.matches("\\s*")) {
				System.out.println("Invalid symbol");
				return;
			}
			String colorName = getSelectedButtonText(colorButtonGroup);
			String shapeName = getSelectedButtonText(shapeButtonGroup);

			if (colorName == null || shapeName == null)
				return;

			SupportedColor colorTag = SupportedColor.getTagFromString(colorName);
			SupportedShape shapeTag = SupportedShape.getTagFromString(shapeName);

			if (colorTag == null || shapeTag == null)
				return;

			ObjectCharactor addObj = new ObjectCharactor(symbol, shapeTag, colorTag);
			stateSystem.getUsableObjets().add(addObj);
			displayUpdate();
		}
	}

	private class SwingAction_2 extends AbstractAction {
		public SwingAction_2() {
			putValue(NAME, "Delete");
			putValue(SHORT_DESCRIPTION, "Some short description");
		}

		public void actionPerformed(ActionEvent e) {
			if (isPlanningExecute)
				return;
			stateSystem.deleteObject(deleteObjName.getText());
			displayUpdate();
		}
	}

	private class SwingAction_3 extends AbstractAction {
		public SwingAction_3() {
			putValue(NAME, "Execute");
			putValue(SHORT_DESCRIPTION, "Some short description");
		}

		public void actionPerformed(ActionEvent e) {
			if (isPlanningExecute)
				return;
			String target = opTarget.getText();
			boolean operationSuccess = false;
			if (moveInitToGoal.isSelected()) {
				operationSuccess = stateSystem.moveObjInitToGoal(target);
			} else if (moveGoalToInit.isSelected()) {
				operationSuccess = stateSystem.moveObjGoalToInit(target);
			} else {
				operationSuccess = false;
			}
			System.out.println("operation =>" + operationSuccess);
			displayUpdate();
		}
	}

	private class SwingAction_4 extends AbstractAction {
		public SwingAction_4() {
			putValue(NAME, "Start");
			putValue(SHORT_DESCRIPTION, "Some short description");
		}

		public void actionPerformed(ActionEvent e) {
			if (planDisplays == null) {
				return;
			}
			planDisplays[currentPlanDispIndex].setVisible(false);
			currentPlanDispIndex = 0;
			planDisplays[currentPlanDispIndex].setVisible(true);
		}
	}

	private class SwingAction_5 extends AbstractAction {
		public SwingAction_5() {
			putValue(NAME, "Back");
			putValue(SHORT_DESCRIPTION, "Some short description");
		}

		public void actionPerformed(ActionEvent e) {
			if (planDisplays == null)
				return;

			if (currentPlanDispIndex < 1 || planDisplays.length < 1) {
				return;
			}
			planDisplays[currentPlanDispIndex].setVisible(false);
			planDisplays[--currentPlanDispIndex].setVisible(true);
		}
	}

	private class SwingAction_6 extends AbstractAction {
		public SwingAction_6() {
			putValue(NAME, "Next");
			putValue(SHORT_DESCRIPTION, "Some short description");
		}

		public void actionPerformed(ActionEvent e) {
			if (planDisplays == null)
				return;
			if (planDisplays.length <= 0 || currentPlanDispIndex >= planDisplays.length - 1)
				return;
			planDisplays[currentPlanDispIndex].setVisible(false);
			planDisplays[++currentPlanDispIndex].setVisible(true);
		}
	}

	// GUIテキストから受け取った質問に対してのフィルタリング
	private static String messageFilter(String string) {
		string = string.replaceAll("\\.([^" + " " + "])", ". $1");
		string = string.replaceAll("\\,([^" + " " + "])", ", $1");
		string = string.replaceAll("\\?([a-zA-Z])", "? $1");
		return string;
	}

	// word operation
	private class SwingAction_7 extends AbstractAction {
		public SwingAction_7() {
			putValue(NAME, "Natural Language Command Execute");
			putValue(SHORT_DESCRIPTION, "1行目:命令の種類->" + "(初期状態へのオブジェクトの追加 Add, " + "初期状態の操作 Move. "
					+ "目標状態の作成 MakeGoalState) " + "2行目以降:命令内容");
		}

		public void actionPerformed(ActionEvent e) {
			if (isPlanningExecute)
				return;
			setMessengerText(OperationText.getText(), false);
			messenger.setVisible(true);
			LinkedList<String> logs = new LinkedList<>();
			nlpSystem.executeWordOperation(messageFilter(OperationText.getText()), stateSystem, logs);
			for (String log : logs) {
				setMessengerText(log, false);
			}
			displayUpdate();
		}
	}

	private class SwingAction_8 extends AbstractAction {
		public SwingAction_8() {
			putValue(NAME, "Close");
			putValue(SHORT_DESCRIPTION, "Some short description");
		}

		public void actionPerformed(ActionEvent e) {
			if (!isPlanningExecute)
				return;
			isPlanningExecute = false;
			for (JFrame frame : planDisplays) {
				frame.dispose();
			}
			planDisplays = null;
			displayUpdate();
		}
	}

	private class SwingAction_9 extends AbstractAction {
		public SwingAction_9() {
			putValue(NAME, "Add");
			putValue(SHORT_DESCRIPTION, "形や色を定義しない場合、名前に？を付けてください");
		}

		public void actionPerformed(ActionEvent e) {
			if (isPlanningExecute)
				return;
			System.out.println("Add");
			String symbol = addObjNameToGoal.getText();
			if (symbol == null) {
				System.out.println("Same symbol can not be used or symbol is null");
				return;
			}
			String colorName = getSelectedButtonText(colorButtonGroupForGoal);
			String shapeName = getSelectedButtonText(shapeButtonGroupForGoal);

			if (colorName == null || shapeName == null)
				return;

			SupportedColor colorTag = SupportedColor.getTagFromString(colorName);
			SupportedShape shapeTag = SupportedShape.getTagFromString(shapeName);

			if (colorTag == null || shapeTag == null)
				return;

			if (symbol.equals("?")) {
				ObjectCharactor addObj = new ObjectCharactor(symbol, shapeTag, colorTag);
				stateSystem.addNotDefinedObjToGoalState(addObj);
			} else {
				stateSystem.addDefinedObjToGoalState(symbol);
			}
			// displayUpdate();
			goalStateDisplay.repaint();
		}
	}

	private class SwingAction_10 extends AbstractAction {
		public SwingAction_10() {
			putValue(NAME, "Reset");
			putValue(SHORT_DESCRIPTION, "Some short description");
		}

		public void actionPerformed(ActionEvent e) {
			if (isPlanningExecute)
				return;
			stateSystem.goalStateInit();
			// displayUpdate();
			goalStateDisplay.repaint();
			System.out.println(stateSystem.getGoalState());
		}
	}
}