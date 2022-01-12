package mednafen;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class MainWindow extends JFrame {
	
	static int INPUT_COLUMN_WIDTH = 20;
	
	// Measurements are in pixels.
	static int WINDOW_WIDTH = 900; 
	static int WINDOW_HEIGHT = 600;
	static int TABLE_ROW_HEIGHT = 23;
	
	
	private Game game;
	private JTextField input = new JTextField(INPUT_COLUMN_WIDTH);
	private JButton reset = new JButton("Reset");
	
	private DefaultTableCellRenderer headerRenderer;
	private DefaultTableCellRenderer columnRenderer;
	
	private class JTableModel extends DefaultTableModel {
		public JTableModel() {
		      super();
		}
		
		public boolean isCellEditable(int row, int column) {
		     return false;
		}
	};
	
	private JTableModel model = new JTableModel();
	private JTable output = new JTable(model);
	String[] column_names = {"Games"};
	
	private String[] generateList(HashMap<Integer, String> map) {
		String str[] = map.values().toArray(new String[0]);
		String list[] = new String[str.length+1];
		for (int i = 0; i < str.length; i++) {
			list[i+1] = str[i];
		}
		list[0] = new String("All");
		return list;
	}
	
	private void renderTable() {
		output.getColumnModel().getColumn(0).setCellRenderer(columnRenderer);
		output.getTableHeader().setDefaultRenderer(headerRenderer);
		output.getColumnModel().getColumn(0).setPreferredWidth(WINDOW_WIDTH);
		output.setRowHeight(TABLE_ROW_HEIGHT);
	}
	
	private void display(String name) {
		model.setDataVector(game.search(name), column_names);
		renderTable();
	}
	
	public MainWindow(String games_dir) throws IOException {

		game = Game.parseGames(games_dir);
		display("");
		
		setLayout(new BorderLayout(20, 20));
		
		{ // top panel
			JPanel top = new JPanel();
			top.setLayout(new FlowLayout());
			
			ActionListener listener = new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					display(input.getText());
				}
			};
			
			reset.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					input.setText("");
					display("");
				}
			});
			
			input.addKeyListener(new KeyListener() {
				
				@Override
				public void keyTyped(KeyEvent e) {
					// TODO Auto-generated method stub	
				}
				
				@Override
				public void keyReleased(KeyEvent e) {
					display(input.getText());
				}
				
				@Override
				public void keyPressed(KeyEvent e) {				
				}
				
			});
			
			top.add(new JLabel("Enter game name:"));
			top.add(input);
			top.add(reset);
			
			this.add(top, BorderLayout.NORTH);
		}
		
		{ // bottom panel
			JPanel bot = new JPanel();
			JScrollPane scroll = new JScrollPane(output); 
			scroll.setPreferredSize(new Dimension(WINDOW_WIDTH - 20, WINDOW_HEIGHT - 75));
			
			Color background_color = new Color(240, 240, 240);
			headerRenderer = new DefaultTableCellRenderer() {
				@Override
				public Component getTableCellRendererComponent(JTable table, Object
						value, boolean isSelected, boolean hasFocus, int row, int column) {
	                	
					super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
	                
					setBackground(background_color);
					Border border = BorderFactory.createLineBorder(Color.black);
					setHorizontalAlignment(JLabel.CENTER);
					setBorder(border);
					return this;
	           	 }
			};
			
			columnRenderer = new DefaultTableCellRenderer() {
				@Override
				public Component getTableCellRendererComponent(JTable table, Object
						value, boolean isSelected, boolean hasFocus, int row, int column) {
	                	
					super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
	                
					Border border = BorderFactory.createMatteBorder(0, 0, 1, 0, background_color);
					setBorder(border);
					
					setHorizontalAlignment(JLabel.CENTER);
					return this;
	           	 }
			};
			
			renderTable();
	
			output.setRowSelectionAllowed(true);
			output.setColumnSelectionAllowed(false);
			output.setEnabled(true);
			
			output.addMouseListener(new MouseListener(){
		     
		        public void mouseClicked(MouseEvent e) {
		        	if (e.getClickCount() == 2) {
		        		String name = output.getValueAt(output.getSelectedRow(), 0).toString();
		        		 try {
		        			System.out.println(game.getPath(name));
		        			Runtime.getRuntime().exec("mednafen \"" + game.getPath(name) +"\"");
						} catch (IOException e1) {
							e1.printStackTrace();
						}
	        		}
	        	}

				@Override
				public void mousePressed(MouseEvent e) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void mouseReleased(MouseEvent e) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void mouseEntered(MouseEvent e) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void mouseExited(MouseEvent e) {
					// TODO Auto-generated method stub
					
				}
		    });
			
			bot.add(scroll);
			this.add(bot, BorderLayout.SOUTH);
		}
		
		setResizable(false);
		setTitle("Mednafen Game Manager");
		setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
	}

}
