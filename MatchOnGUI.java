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
import javax.swing.UIManager;

public class MatchOnGUI {
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
	private JTextPane txtpnSearchData;
	private JButton btnNewButton_2;
	private JLabel lblPleaseWriteData;
	private final Action SearchAct = new SwingAction();
	private final Action AddDataAct = new SwingAction_1();
	private final Action DeleteDataAct = new SwingAction_2();
	private JButton btnNewButton_3;
	private final Action DataUpdate = new SwingAction_3();

	private void displayResultOnGUI(String result){
		JFrame frameTmp = new JFrame();
		frameTmp.setBounds(100, 100, 600, 200);
		frameTmp.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		frameTmp.getContentPane().setLayout(null);

		JScrollPane scrollPane_3 = new JScrollPane();
		scrollPane_3.setBounds(12, 45, 558, 95);
		frameTmp.getContentPane().add(scrollPane_3);

		JTextArea textArea_1 = new JTextArea();
		textArea_1.setLineWrap(true);
		scrollPane_3.setViewportView(textArea_1);

		JLabel lblResult = new JLabel("Result");
		lblResult.setFont(new Font("MS UI Gothic", Font.PLAIN, 20));
		lblResult.setBounds(12, 13, 81, 31);
		frameTmp.getContentPane().add(lblResult);

		textArea_1.setText(result);

		frameTmp.setVisible(true);
	}

	private void dataInfoUpdate(){
		File file = new File(MatchForGUI.datafileName);
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

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MatchOnGUI window = new MatchOnGUI();
					window.frame.setVisible(true);
					window.dataInfoUpdate();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MatchOnGUI() {
		initialize();
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
		btnNewButton.setFont(new Font("MS UI Gothic", Font.PLAIN, 20));
		btnNewButton.setAction(AddDataAct);
		btnNewButton.setBounds(299, 255, 129, 45);
		frame.getContentPane().add(btnNewButton);

		btnNewButton_1 = new JButton("Delete");
		btnNewButton_1.setFont(new Font("MS UI Gothic", Font.PLAIN, 20));
		btnNewButton_1.setAction(DeleteDataAct);
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

		txtpnSearchData = new JTextPane();
		txtpnSearchData.setBackground(UIManager.getColor("Button.background"));
		txtpnSearchData.setFont(new Font("MS UI Gothic", Font.PLAIN, 19));
		txtpnSearchData.setText("Search Data\r\nDon't write more than 3 lines");
		txtpnSearchData.setBounds(12, 27, 276, 43);
		frame.getContentPane().add(txtpnSearchData);

		btnNewButton_2 = new JButton("Search");
		btnNewButton_2.setFont(new Font("MS UI Gothic", Font.PLAIN, 20));
		btnNewButton_2.setAction(SearchAct);
		btnNewButton_2.setBounds(299, 83, 129, 45);
		frame.getContentPane().add(btnNewButton_2);
		
		btnNewButton_3 = new JButton("Update");
		btnNewButton_3.setFont(new Font("MS UI Gothic", Font.PLAIN, 18));
		btnNewButton_3.setAction(DataUpdate);
		btnNewButton_3.setBounds(616, 18, 129, 40);
		frame.getContentPane().add(btnNewButton_3);
	}
	private class SwingAction extends AbstractAction {
		public SwingAction() {
			putValue(NAME, "Search");
			putValue(SHORT_DESCRIPTION, "Some short description");
		}
		public void actionPerformed(ActionEvent e) {
			String[] searchDataStr = SearchData.getText().trim().split("\n",0);
			if(searchDataStr.length > 2 || searchDataStr.length ==0){
				System.out.println("SearchData is Invalid");
				return;
			}else if(searchDataStr[0].isEmpty()){
				System.out.println("Blank is Invalid");
				return;
			}
			dataInfoUpdate();
			displayResultOnGUI(MatchForGUI.Search(searchDataStr));
		}
	}
	private class SwingAction_1 extends AbstractAction {
		public SwingAction_1() {
			putValue(NAME, "Add");
			putValue(SHORT_DESCRIPTION, "Some short description");
		}
		public void actionPerformed(ActionEvent e) {
			String[] addDataStr = AddData.getText().split("\n\n",0);
			MatchForGUI.Add(addDataStr);
			dataInfoUpdate();
		}
	}
	private class SwingAction_2 extends AbstractAction {
		public SwingAction_2() {
			putValue(NAME, "Delete");
			putValue(SHORT_DESCRIPTION, "Some short description");
		}
		public void actionPerformed(ActionEvent e) {
			String deleteDataStr = DeleteData.getText();
 			MatchForGUI.Delete(deleteDataStr);
 			dataInfoUpdate();
		}
	}
	private class SwingAction_3 extends AbstractAction {
		public SwingAction_3() {
			putValue(NAME, "DataUpdate");
			putValue(SHORT_DESCRIPTION, "Some short description");
		}
		public void actionPerformed(ActionEvent e) {
			dataInfoUpdate();
		}
	}
}
