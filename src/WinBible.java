import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.JTextField;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class WinBible extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JComboBox cbBook;
	private JComboBox cbChapter;
	private JComboBox cbVerse;
	private JLabel lblName;
	private JTextPane lblcontent;
	private JTextField tfQuickSearch;
	private JTextField tfWordSearch;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			WinBible dialog = new WinBible();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public WinBible() {
		setTitle("Bible Project");
		setBounds(100, 100, 355, 541);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		cbBook = new JComboBox();
		cbBook.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				selectedItem();
				
			}
		});
		cbBook.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Class.forName("com.mysql.cj.jdbc.Driver");
					Connection conn = DriverManager.getConnection
							("jdbc:mysql://localhost:3306/bibledb", "root","1234");
					
					String sql = "SELECT MAX(chapter) FROM bibletbl WHERE book='"+cbBook.getSelectedItem()+"'";
					Statement stmt = conn.createStatement();
					ResultSet rs = stmt.executeQuery(sql);
					
					if(rs.next()) {
						int cnt = rs.getInt(1);
						cbChapter.removeAllItems();
						for(int i =1; i<=cnt; i++)
							cbChapter.addItem(Integer.toString(i)); 
					}
					
					
					} catch (Exception e1) {
					e1.printStackTrace();
					}
				
			}
		});
		cbBook.setModel(new DefaultComboBoxModel(new String[] {}));
		cbBook.setBounds(41, 40, 95, 23);
		contentPanel.add(cbBook);
		
		cbChapter = new JComboBox();
		cbChapter.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				selectedItem();
			}
		});
	
		cbChapter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Class.forName("com.mysql.cj.jdbc.Driver");
					Connection conn = DriverManager.getConnection
							("jdbc:mysql://localhost:3306/bibledb", "root","1234");
					
					String sql = "SELECT MAX(verse) FROM bibletbl WHERE book='"+cbBook.getSelectedItem()+"' AND chapter='"+cbChapter.getSelectedItem()+"'";
					Statement stmt = conn.createStatement();
					ResultSet rs = stmt.executeQuery(sql);
					
					if(rs.next()) {
						int cnt = rs.getInt(1);
						cbVerse.removeAllItems();
						for(int i =1; i<=cnt; i++)
							cbVerse.addItem(Integer.toString(i)); 
					}
					
					
					} catch (Exception e1) {
					e1.printStackTrace();
					}
			}
		});
		cbChapter.setBounds(148, 40, 65, 23);
		contentPanel.add(cbChapter);
		
		cbVerse = new JComboBox();
		cbVerse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Class.forName("com.mysql.cj.jdbc.Driver");
					Connection conn = DriverManager.getConnection
							("jdbc:mysql://localhost:3306/bibledb", "root","1234");
					
					String sql = "SELECT verse, content FROM bibletbl WHERE book='"+cbBook.getSelectedItem()+"' AND chapter='"+cbChapter.getSelectedItem()+"'";
					Statement stmt = conn.createStatement();
					ResultSet rs = stmt.executeQuery(sql);
					
					String temp = "";
					
					while(rs.next()) {
						String verse = rs.getString("verse");
						String sverse = (String)cbVerse.getSelectedItem();
						if(sverse!=null) {
							if(verse.equals(sverse)) {
								temp = temp + "<span style='font:bold 1.2em Malgun Goldic;'>";
								if(cbBook.getSelectedItem().equals("??????")) {
									temp = temp + cbChapter.getSelectedItem() + "??? ";
								}
								else {
									temp = temp + cbChapter.getSelectedItem() + "??? ";
								}
								temp = temp + verse +"???<br></span>";
								temp = temp + "<span style='font:bold Malgun Goldic; color:blue'>";
								temp = temp + rs.getString("content")+"<br></span>";
							}else {
								temp = temp + "<span style='font:bold 1.2em Malgun Goldic;'>";
								if(cbBook.getSelectedItem().equals("??????")) {
									temp = temp + cbChapter.getSelectedItem() + "??? ";
								}
								else {
									temp = temp + cbChapter.getSelectedItem() + "??? ";
								}
								temp = temp + rs.getString("verse") +"???<br></span>";
								temp = temp + "<span style='font:Malgun Goldic;'>"+rs.getString("content")+"<br></span>";
							}
						}
					}
					lblcontent.setText(temp);
					lblcontent.setCaretPosition(0);
					lblName.setText("["+cbBook.getSelectedItem()+" "+cbChapter.getSelectedItem()+":"+cbVerse.getSelectedItem()+"]");
					
					} catch (Exception e1) {
					e1.printStackTrace();
					}
			}
		});
		
		cbVerse.setBounds(225, 40, 65, 23);
		contentPanel.add(cbVerse);
		
		lblName = new JLabel("");
		lblName.setHorizontalAlignment(SwingConstants.CENTER);
		
		lblName.setBounds(116, 147, 113, 25);
		contentPanel.add(lblName);
		
		JButton btnNewButton = new JButton("???");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int verse = cbVerse.getSelectedIndex();
				
				if(verse < 0) {
					verse = cbVerse.getItemCount()-2;
				}
				else {
					verse--;
				}
				cbVerse.setSelectedIndex(verse);
			}
		});
		btnNewButton.setBounds(127, 453, 45, 23);
		contentPanel.add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton("???");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int verse = cbVerse.getSelectedIndex();
				
				if(verse+2 > cbVerse.getItemCount()) {
					verse = 0;
				}
				else {
					verse++;
				}
				cbVerse.setSelectedIndex(verse);
			}
		});
		btnNewButton_1.setBounds(184, 453, 45, 23);
		contentPanel.add(btnNewButton_1);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(41, 182, 260, 261);
		contentPanel.add(scrollPane);
		
		lblcontent = new JTextPane();
		lblcontent.setContentType("text/html");
		scrollPane.setViewportView(lblcontent);
		
		tfQuickSearch = new JTextField();
		tfQuickSearch.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyChar() == KeyEvent.VK_ENTER) {
					String full[] = {"?????????","????????????","?????????","?????????","?????????","????????????","?????????","??????","????????????","????????????","????????????","????????????","?????????","?????????","?????????","????????????","?????????","??????","??????","??????","?????????","??????","?????????","????????????","??????????????????","?????????","?????????","?????????","??????","?????????","?????????","??????","??????","??????","?????????","?????????","??????","?????????","?????????","????????????","????????? ??????","????????????","????????????","????????????","????????????","????????????","?????????","???????????????","???????????????","???????????????","????????????","????????????","????????????","?????????????????????","?????????????????????","???????????????","???????????????","?????????","????????????","????????????","????????????","???????????????","???????????????","??????1???","??????1???","??????2???","??????2???","??????3???","??????3???","?????????","???????????????"};
					String abbr[] = {"???","???","???","???","???","???","???","???","??????","??????","??????","??????","??????","??????","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","","","???","???","???","???","???","???","??????","??????","???","???","???","???","??????","??????","??????","??????","???","???","???","???","??????","??????","??????","???1","??????","???2","??????","???3","???","???"};
					int index = 0;
					
					String search = tfQuickSearch.getText();
					String sBook = tfQuickSearch.getText().substring(0,search.indexOf(" "));
					String sChapter = tfQuickSearch.getText().substring(search.indexOf(" ")+1,search.indexOf(":"));
					String sVerse = tfQuickSearch.getText().substring(search.indexOf(":")+1);
					for(int i = 0; i<abbr.length; i++) {
						if(sBook.equals(abbr[i])) {
							index = i;
						}
					}
					cbBook.setSelectedItem(full[index]); 
					cbChapter.setSelectedItem(sChapter);
					cbVerse.setSelectedItem(sVerse);
				}
			}
		});
		tfQuickSearch.setToolTipText("???) ??? 1:5");
		tfQuickSearch.setBounds(113, 73, 116, 21);
		contentPanel.add(tfQuickSearch);
		tfQuickSearch.setColumns(10);
		
		tfWordSearch = new JTextField();
		tfWordSearch.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyChar() == KeyEvent.VK_ENTER) {
					try {
						Class.forName("com.mysql.cj.jdbc.Driver");
						Connection conn = DriverManager.getConnection
								("jdbc:mysql://localhost:3306/bibledb", "root","1234");
						
						String word = tfWordSearch.getText();
						
						String sql = "SELECT * FROM bibletbl WHERE content LIKE '%"+word+"%'";
						Statement stmt = conn.createStatement();
						ResultSet rs = stmt.executeQuery(sql);
						String temp = "";
						
						
						
						while(rs.next()) {
							
							temp = temp + "<span style='font:bold 1.2em Malgun Goldic;'>";
							temp = temp + rs.getString("book") +" ";
							if(rs.getString("book").equals("??????")) {
								temp = temp + rs.getString("chapter") + "??? ";
							}
							else {
								temp = temp + rs.getString("chapter") + "??? ";
							}
							temp = temp + rs.getString("verse") +"???<br></span>";
							String scontent = rs.getString("content").replace(word, "<span style='font:Malgun Goldic; color:red;'>"+word+"</span>");
							temp = temp + "<span style='font:Malgun Goldic;'>"+scontent+"<br></span>";
						}
						
						lblcontent.setText(temp);
						lblcontent.setCaretPosition(0);
						
						} catch (Exception e1) {
						e1.printStackTrace();
						}
				}	
				
			}
		});
		tfWordSearch.setToolTipText("???) ??????");
		tfWordSearch.setColumns(10);
		tfWordSearch.setBounds(113, 116, 116, 21);
		contentPanel.add(tfWordSearch);
		showBibleBook();
	}
	
	private void showBibleBook() {
		// TODO Auto-generated method stub
			try {
				Class.forName("com.mysql.cj.jdbc.Driver");
				Connection conn = DriverManager.getConnection
						("jdbc:mysql://localhost:3306/bibledb", "root","1234");
				
				String sql = "SELECT DISTINCT book FROM bibletbl";
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql);
				
				while(rs.next()) {
					cbBook.addItem(rs.getString(1));
				}
				
				
				} catch (Exception e1) {
				e1.printStackTrace();
				}
	}
	
	private void selectedItem() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection conn = DriverManager.getConnection
					("jdbc:mysql://localhost:3306/bibledb", "root","1234");
			
			String sql = "SELECT verse, content FROM bibletbl WHERE book='"+cbBook.getSelectedItem()+"' AND chapter='"+cbChapter.getSelectedItem()+"'";
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			
			String temp = "";
			
			while(rs.next()) {
				temp = temp + "<span style='font:bold 1.2em Malgun Goldic;'>";
				if(cbBook.getSelectedItem().equals("??????")) {
					temp = temp + cbChapter.getSelectedItem() + "??? ";
				}
				else {
					temp = temp + cbChapter.getSelectedItem() + "??? ";
				}
				temp = temp + rs.getString("verse") +"???<br></span>";
				temp = temp + "<span style='font:Malgun Goldic;'>"+rs.getString("content")+"<br></span>";
			}
			lblcontent.setText(temp);
			lblcontent.setCaretPosition(0);
			lblName.setText("["+cbBook.getSelectedItem()+" "+cbChapter.getSelectedItem()+":"+cbVerse.getSelectedItem()+"]");
			
			} catch (Exception e1) {
			e1.printStackTrace();
			}
	}
	
}
