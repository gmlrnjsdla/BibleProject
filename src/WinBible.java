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
								if(cbBook.getSelectedItem().equals("시편")) {
									temp = temp + cbChapter.getSelectedItem() + "편 ";
								}
								else {
									temp = temp + cbChapter.getSelectedItem() + "장 ";
								}
								temp = temp + verse +"절<br></span>";
								temp = temp + "<span style='font:bold Malgun Goldic; color:blue'>";
								temp = temp + rs.getString("content")+"<br></span>";
							}else {
								temp = temp + "<span style='font:bold 1.2em Malgun Goldic;'>";
								if(cbBook.getSelectedItem().equals("시편")) {
									temp = temp + cbChapter.getSelectedItem() + "편 ";
								}
								else {
									temp = temp + cbChapter.getSelectedItem() + "장 ";
								}
								temp = temp + rs.getString("verse") +"절<br></span>";
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
		
		JButton btnNewButton = new JButton("◀");
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
		
		JButton btnNewButton_1 = new JButton("▶");
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
					String full[] = {"창세기","출애굽기","레위기","민수기","신명기","여호수아","사사기","룻기","사무엘상","사무엘하","열왕기상","열왕기하","역대상","역대하","에스라","느헤미야","에스더","욥기","시편","잠언","전도서","아가","이사야","예레미야","예레미야애가","에스겔","다니엘","호세아","요엘","아모스","오바댜","요나","미가","나훔","하박국","스바냐","학개","스가랴","말라기","신약성경","성경책 이름","마태복음","마가복음","누가복음","요한복음","사도행전","로마서","고린도전서","고린도후서","갈라디아서","에베소서","빌립보서","골로새서","데살로니가전서","데살로니가후서","디모데전서","디모데후서","디도서","빌레몬서","히브리서","야고보서","베드로전서","베드로후서","요한1서","요한1서","요한2서","요한2서","요한3서","요한3서","유다서","요한계시록"};
					String abbr[] = {"창","출","레","민","신","수","삿","룻","삼상","삼하","왕상","왕하","대상","대하","스","느","에","욥","시","잠","전","아","사","렘","애","겔","단","호","욜","암","옵","욘","미","나","합","습","학","슥","말","","","마","막","눅","요","행","롬","고전","고후","갈","엡","빌","골","살전","살후","딤전","딤후","딛","몬","히","약","벧전","벧후","요일","요1","요이","요2","요삼","요3","유","계"};
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
		tfQuickSearch.setToolTipText("예) 창 1:5");
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
							if(rs.getString("book").equals("시편")) {
								temp = temp + rs.getString("chapter") + "편 ";
							}
							else {
								temp = temp + rs.getString("chapter") + "장 ";
							}
							temp = temp + rs.getString("verse") +"절<br></span>";
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
		tfWordSearch.setToolTipText("예) 천사");
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
				if(cbBook.getSelectedItem().equals("시편")) {
					temp = temp + cbChapter.getSelectedItem() + "편 ";
				}
				else {
					temp = temp + cbChapter.getSelectedItem() + "장 ";
				}
				temp = temp + rs.getString("verse") +"절<br></span>";
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
