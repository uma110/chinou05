import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;

public class QuestionSystemGUI {

	private QuestionSystem qs;
	private JFrame frame;

	private JScrollPane scrollPane;
	private JTextPane AddData;
	private JTextField DeleteData;
	private JLabel lblPleaseWriteData_1;
	private JButton btnNewButton;
	private JButton btnNewButton_1;
	private JScrollPane scrollPane_1;
	private JTextArea DataInfo;
	private JLabel lblNewLabel;
	private JScrollPane scrollPane_2;
	private JTextArea SearchData;
	private JButton btnNewButton_2;
	private JLabel lblPleaseWriteData;
	private JButton btnNewButton_3;
	private final Action action = new SwingAction();
	private final Action action_1 = new SwingAction_1();
	private final Action action_2 = new SwingAction_2();
	private final Action action_3 = new SwingAction_3();
	private JLabel lblNewLabel_1;


	private void dataInfoUpdate(){
		File file = new File(QuestionSystem.datafileName);
		byte[] fileContent = null;
		try {
			fileContent = Files.readAllBytes(file.toPath());
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		String fileContentStr = new String(fileContent,StandardCharsets.UTF_8);
		DataInfo.setText(fileContentStr);
	}

	private void inputInformation(){
		File file = new File(QuestionSystem.datafileName);
		byte[] fileContent = null;
		try{
			fileContent = Files.readAllBytes(file.toPath());
		}catch(IOException e){
			e.printStackTrace();
		}
		String fileContentStr = new String(fileContent,StandardCharsets.UTF_8);
		String[] splitFileContent = fileContentStr.trim().split("\n",0);
		qs.addData(splitFileContent,false);
	}
	
	private void displayResultOnGUI(String question,String result){
		JFrame frameTmp = new JFrame();
		frameTmp.setBounds(100, 100, 600, 400);
		frameTmp.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		frameTmp.getContentPane().setLayout(null);
		
		JScrollPane scrollPane_3 = new JScrollPane();
		scrollPane_3.setBounds(12, 45, 558, 97);
		frameTmp.getContentPane().add(scrollPane_3);

		JTextArea questionText = new JTextArea();
		questionText.setLineWrap(true);
		scrollPane_3.setViewportView(questionText);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(12, 185, 558, 155);
		frameTmp.getContentPane().add(scrollPane);
		
		JTextArea resultText = new JTextArea();
		resultText.setLineWrap(true);
		scrollPane.setViewportView(resultText);
		
		JLabel lblResult = new JLabel("Question");
		lblResult.setFont(new Font("MS UI Gothic", Font.PLAIN, 20));
		lblResult.setBounds(12, 13, 81, 31);
		frameTmp.getContentPane().add(lblResult);
		
		JLabel lblNewLabel = new JLabel("Result");
		lblNewLabel.setFont(new Font("MS UI Gothic", Font.PLAIN, 20));
		lblNewLabel.setBounds(12, 150, 81, 31);
		frameTmp.getContentPane().add(lblNewLabel);
		
		questionText.setText(question);
		resultText.setText(result);
		frameTmp.setVisible(true);
	}
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					QuestionSystemGUI window = new QuestionSystemGUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public QuestionSystemGUI() {
		initialize();
		qs = new QuestionSystem();
		inputInformation();
		qs.sn.printLinks();
		qs.sn.printNodes();
		dataInfoUpdate();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 900, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		scrollPane_2 = new JScrollPane();
		scrollPane_2.setBounds(36, 83, 236, 57);
		frame.getContentPane().add(scrollPane_2);

		SearchData = new JTextArea();
		SearchData.setRows(2);
		scrollPane_2.setViewportView(SearchData);

		scrollPane = new JScrollPane();
		scrollPane.setBounds(36, 204, 236, 198);
		frame.getContentPane().add(scrollPane);

		AddData = new JTextPane();
		AddData.setFont(new Font("MS UI Gothic", Font.PLAIN, 17));
		scrollPane.setViewportView(AddData);

		DeleteData = new JTextField();
		DeleteData.setFont(new Font("MS UI Gothic", Font.PLAIN, 17));
		DeleteData.setBounds(36, 473, 232, 33);
		frame.getContentPane().add(DeleteData);
		DeleteData.setColumns(10);

		lblPleaseWriteData = new JLabel("Please write data you want to add");
		lblPleaseWriteData.setFont(new Font("MS UI Gothic", Font.PLAIN, 19));
		lblPleaseWriteData.setBounds(12, 158, 276, 33);
		frame.getContentPane().add(lblPleaseWriteData);

		lblPleaseWriteData_1 = new JLabel("Please write data you want to delete");
		lblPleaseWriteData_1.setFont(new Font("MS UI Gothic", Font.PLAIN, 19));
		lblPleaseWriteData_1.setBounds(12, 415, 289, 45);
		frame.getContentPane().add(lblPleaseWriteData_1);

		btnNewButton = new JButton("Add");
		btnNewButton.setAction(action_1);
		btnNewButton.setFont(new Font("MS UI Gothic", Font.PLAIN, 20));
		btnNewButton.setBounds(299, 255, 129, 45);
		frame.getContentPane().add(btnNewButton);

		btnNewButton_1 = new JButton("Delete");
		btnNewButton_1.setAction(action_2);
		btnNewButton_1.setFont(new Font("MS UI Gothic", Font.PLAIN, 20));
		btnNewButton_1.setBounds(299, 465, 129, 45);
		frame.getContentPane().add(btnNewButton_1);

		scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(456, 64, 400, 476);
		frame.getContentPane().add(scrollPane_1);

		DataInfo = new JTextArea();
		DataInfo.setEditable(false);
		DataInfo.setFont(new Font("Monospaced", Font.PLAIN, 15));
		scrollPane_1.setViewportView(DataInfo);

		lblNewLabel = new JLabel("Data Infomation");
		lblNewLabel.setFont(new Font("MS UI Gothic", Font.PLAIN, 19));
		lblNewLabel.setBounds(458, 27, 129, 23);
		frame.getContentPane().add(lblNewLabel);

		btnNewButton_2 = new JButton("Search");
		btnNewButton_2.setAction(action_3);
		btnNewButton_2.setFont(new Font("MS UI Gothic", Font.PLAIN, 20));
		btnNewButton_2.setBounds(299, 83, 129, 45);
		frame.getContentPane().add(btnNewButton_2);

		btnNewButton_3 = new JButton("Update");
		btnNewButton_3.setAction(action);
		btnNewButton_3.setFont(new Font("MS UI Gothic", Font.PLAIN, 18));
		btnNewButton_3.setBounds(616, 18, 129, 40);
		frame.getContentPane().add(btnNewButton_3);
		
		lblNewLabel_1 = new JLabel("Query");
		lblNewLabel_1.setFont(new Font("MS UI Gothic", Font.PLAIN, 20));
		lblNewLabel_1.setBounds(36, 32, 236, 38);
		frame.getContentPane().add(lblNewLabel_1);
	}

	private class SwingAction extends AbstractAction {
		public SwingAction() {
			putValue(NAME, "UPDATE");
			putValue(SHORT_DESCRIPTION, "Some short description");
		}
		public void actionPerformed(ActionEvent e) {
			dataInfoUpdate();
			qs.sn.networkInit();
			inputInformation();
			qs.sn.printLinks();
			qs.sn.printNodes();
		}
	}
	private class SwingAction_1 extends AbstractAction {
		public SwingAction_1() {
			putValue(NAME, "ADD");
			putValue(SHORT_DESCRIPTION, "Some short description");
		}
		public void actionPerformed(ActionEvent e) {
			String[] addDataStr = AddData.getText().split("\n",0);
			if(addDataStr[0].isEmpty()) return;
			qs.addData(addDataStr,true);
			dataInfoUpdate();
		}
	}
	private class SwingAction_2 extends AbstractAction {
		public SwingAction_2() {
			putValue(NAME, "DELETE");
			putValue(SHORT_DESCRIPTION, "Some short description");
		}
		public void actionPerformed(ActionEvent e) {
			String str[] = QuestionSystem.strSplit3line(DeleteData.getText());
			if(str == null){
				System.out.println("deletedata is invalid");
				return;
			}
			String deleteData = str[0]+" "+str[1]+" "+str[2];
			System.out.println(deleteData);
			qs.DeleteData(deleteData);
			dataInfoUpdate();
			qs.sn.networkInit();
			inputInformation();
		}
	}
	private class SwingAction_3 extends AbstractAction {
		public SwingAction_3() {
			putValue(NAME, "SEARCH");
			putValue(SHORT_DESCRIPTION, "Some short description");
		}
		public void actionPerformed(ActionEvent e) {
			qs.ResetQuery();
			String[] queryStr = SearchData.getText().split("\n",0);
			System.out.println(queryStr.length);
			for(String str:queryStr){
				qs.addQuery(str);
			}
			displayResultOnGUI(qs.query.toString().replace(",",",\n"),qs.Search().replace("},", "},\n"));
		}
	}
}
