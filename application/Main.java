package application;
	
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JFileChooser;
import javax.swing.JComboBox;
import javax.swing.JCheckBox;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.Timer;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.Element;

import javax.swing.UIManager;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.Color;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.event.WindowStateListener;
import java.awt.event.WindowAdapter;

public final class Main extends JFrame implements ActionListener, ItemListener, WindowStateListener {
	static UIManager UIManager = new UIManager();
	JFileChooser fileChooser_Java = new JFileChooser(System.getProperty("user.dir"));
	JFileChooser fileChooser_CSharp = new JFileChooser(System.getProperty("user.dir"));
	
	JSplitPane mainPanel1 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);

    JSplitPane sub2PanelLeft = new JSplitPane(JSplitPane.VERTICAL_SPLIT); 
    JSplitPane sub3PanelLeft1 = new JSplitPane(JSplitPane.VERTICAL_SPLIT); 
    JSplitPane sub3PanelLeft2 = new JSplitPane(JSplitPane.VERTICAL_SPLIT); 
    JSplitPane sub2PanelRight = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
    JSplitPane sub3PanelRight1 = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
    JSplitPane sub3PanelRight2 = new JSplitPane(JSplitPane.VERTICAL_SPLIT); 
    
    JPanel buttonPanelLeft = new JPanel();
    JPanel buttonPanelRight = new JPanel();    
    
    JPanel subP_JavaTextArea = new JPanel();
    JPanel subP_CSharpTextArea = new JPanel();
    JPanel subP_LeftButtons = new JPanel();
    JPanel subP_RightButtons = new JPanel();
    
    JMenuBar menuBar = new JMenuBar();
	
	JLabel label_Java = new JLabel("Java");
	JTextArea textArea_Java = new JTextArea(12, 27);
	JTextArea margin_Java = new JTextArea("1");
	JTextField textField_Java = new JTextField("Lines: ; Characters: ", 27);
	JScrollPane jsp_Java = new JScrollPane();
	JButton button_JavaSave = new JButton("Save");
	JButton button_JavaLoad = new JButton("Load");
	
	JLabel label_CSharp = new JLabel("C#");
	JTextArea textArea_CSharp = new JTextArea(12, 27);
	JTextArea margin_CSharp = new JTextArea("1");
	JTextField textField_CSharp = new JTextField("Lines: ; Characters: ", 27);
	JScrollPane jsp_CSharp = new JScrollPane();
	JButton button_CSharpTrans = new JButton("Translate"); // Translate Java to C#
	JButton button_CSharpSave = new JButton("Save");
	
	translator translate = new translator();
	
	public Main() {
		setTitle("Java to C# Cross-Compiler");
		setSize(650,540);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
	    addWindowListener(new WindowAdapter() {
	    	@Override
	    	public void windowClosing(WindowEvent e) {
	    		if(JOptionPane.showConfirmDialog(null, "Would you like to close the program?", "Closing Cross-Compiler", 0, 0, null)==0) {
	    			setVisible(false);
	    			dispose();
					System.exit(0);
	    		}
	    	}	    	
	    });
	    
	    fileChooser_Java.setFileFilter(new FileNameExtensionFilter("Java File (.java)", "java"));
		fileChooser_Java.setMultiSelectionEnabled(false);
		
	    fileChooser_CSharp.setFileFilter(new FileNameExtensionFilter("C# File (.cs)", "cs"));
		fileChooser_CSharp.setMultiSelectionEnabled(false);
	    
	    label_Java.setFont(new Font("Calibri", Font.PLAIN, 40));
	    label_CSharp.setFont(new Font("Calibri", Font.PLAIN, 40));
	    
	    // --
	    
	    button_JavaLoad.addActionListener(this);
	    button_JavaLoad.setActionCommand("LoadJava");
	    
	    button_JavaSave.addActionListener(this);
	    button_JavaSave.setActionCommand("SaveJava");
	    
	    button_CSharpSave.addActionListener(this);
	    button_CSharpSave.setActionCommand("SaveCSharp");
	    
	    button_CSharpTrans.addActionListener(this);
	    button_CSharpTrans.setActionCommand("Translate");
	    
	    
		textArea_Java.getDocument().addDocumentListener(new DocumentListener() {
			public String getText() {
				int docLength = textArea_Java.getDocument().getLength();
				Element root = textArea_Java.getDocument().getDefaultRootElement();
				String text = "1" + System.getProperty("line.separator");
				for (int i=2;i<root.getElementIndex(docLength)+2;i++) {
					text += i + System.getProperty("line.separator");
				}
				return text;
			}
			
			@Override
			public void changedUpdate(DocumentEvent e) {
				updateCharCount('J');
				margin_Java.setText(getText());
			}
			@Override
			public void insertUpdate(DocumentEvent e) {
				updateCharCount('J');	
				margin_Java.setText(getText());
			}
			@Override
			public void removeUpdate(DocumentEvent e) {
				updateCharCount('J');	
				margin_Java.setText(getText());
			}
		});		
		jsp_Java.getViewport().add(textArea_Java);
		jsp_Java.setRowHeaderView(margin_Java);
		
		textArea_CSharp.getDocument().addDocumentListener(new DocumentListener() {
			public String getText() {
				int docLength = textArea_CSharp.getDocument().getLength();
				Element root = textArea_CSharp.getDocument().getDefaultRootElement();
				String text = "1" + System.getProperty("line.separator");
				for (int i=2;i<root.getElementIndex(docLength)+2;i++) {
					text += i + System.getProperty("line.separator");
				}
				return text;
			}
			
			@Override
			public void changedUpdate(DocumentEvent e) {
				updateCharCount('C');
				margin_CSharp.setText(getText());
			}
			@Override
			public void insertUpdate(DocumentEvent e) {
				updateCharCount('C');	
				margin_CSharp.setText(getText());
			}
			@Override
			public void removeUpdate(DocumentEvent e) {
				updateCharCount('C');	
				margin_CSharp.setText(getText());
			}
		});
		jsp_CSharp.getViewport().add(textArea_CSharp);
		jsp_CSharp.setRowHeaderView(margin_CSharp);
	    
	    // --
	    
	    mainPanel1.resetToPreferredSizes();
	    mainPanel1.setDividerLocation(325);
	    mainPanel1.setDividerSize(-1);
	    
	    sub2PanelLeft.resetToPreferredSizes();
	    sub2PanelLeft.setDividerLocation(460);
	    sub2PanelLeft.setDividerSize(-1);
	    
	    sub3PanelLeft1.resetToPreferredSizes();
	    sub3PanelLeft1.setDividerLocation(440);
	    sub3PanelLeft1.setDividerSize(-1);
	    
	    sub3PanelLeft2.resetToPreferredSizes();
	    sub3PanelLeft2.setDividerLocation(90);
	    sub3PanelLeft2.setDividerSize(-1);
	    
	    sub2PanelRight.resetToPreferredSizes();
	    sub2PanelRight.setDividerLocation(460);
	    sub2PanelRight.setDividerSize(-1);
	    
	    sub3PanelRight1.resetToPreferredSizes();
	    sub3PanelRight1.setDividerLocation(440);
	    sub3PanelRight1.setDividerSize(-1);
	    
	    sub3PanelRight2.resetToPreferredSizes();
	    sub3PanelRight2.setDividerLocation(90);
	    sub3PanelRight2.setDividerSize(-1);
	    
	    textArea_CSharp.setEditable(false);
	    margin_Java.setEditable(false);
	    margin_CSharp.setEditable(false);
	    textField_Java.setEditable(false);
	    textField_CSharp.setEditable(false);
	    
	    
	    
	    // --
	    
	    buttonPanelLeft.add(button_JavaSave);
	    buttonPanelLeft.add(button_JavaLoad);
	    
	    sub3PanelLeft1.add(jsp_Java);
	    sub3PanelLeft1.add(textField_Java);
	    sub3PanelLeft2.add(label_Java);
	    sub3PanelLeft2.add(buttonPanelLeft);
	    
	    sub2PanelLeft.add(sub3PanelLeft1);
	    sub2PanelLeft.add(sub3PanelLeft2);
	    
	    // --
	    
	    buttonPanelRight.add(button_CSharpTrans);
	    buttonPanelRight.add(button_CSharpSave);
	    
	    sub3PanelRight1.add(jsp_CSharp);
	    sub3PanelRight1.add(textField_CSharp);
	    sub3PanelRight2.add(label_CSharp);
	    sub3PanelRight2.add(buttonPanelRight);
	    
	    sub2PanelRight.add(sub3PanelRight1);
	    sub2PanelRight.add(sub3PanelRight2);
	    
	    // --
	    
	    mainPanel1.add(sub2PanelLeft);
	    mainPanel1.add(sub2PanelRight);
	    
	   // textArea_CSharp.setBackground(Color.BLACK);
	    
		setJMenuBar(menuBar);
	    add(mainPanel1);
	    addComponentListener(new ComponentAdapter() {
	    	public void componentResized(ComponentEvent e) {
	    		resizeComponents(getSize().getHeight(), getSize().getWidth());	    	    
	    	}
	    });
	    addWindowStateListener(this);
		setVisible(true);
	}
	
	//Checks Actions
	@Override
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		int returnVal;
			if (cmd.equals("LoadJava")) {
	            returnVal = fileChooser_Java.showOpenDialog(this);
	            if (returnVal==0) {
	            	String currentFile = fileChooser_Java.getSelectedFile().getAbsolutePath();
	            	String fetchedCode = getTextFrom("", currentFile);
	            	if (fetchedCode != null) {
	            		textArea_Java.setText(fetchedCode);
	            	}	            	
	            }
			} else if (cmd.equals("SaveJava")) {			
				returnVal = fileChooser_Java.showSaveDialog(this);
	        	if (returnVal==0) {
	        		String currentFile = fileChooser_Java.getSelectedFile().getAbsolutePath();
	        		String fileName = fileChooser_Java.getSelectedFile().getName();
	            	String extension = currentFile.substring(currentFile.length()-5, currentFile.length());
	            	if (!(extension.equals(".java"))) {
	            		currentFile += ".java";
	            		fileName += ".java";
	            	}
	            	writeTextTo(textArea_Java.getText(), currentFile);
	        	}
			} else if (cmd.equals("SaveCSharp")) {				
				returnVal = fileChooser_CSharp.showSaveDialog(this);
	        	if (returnVal==0) {
	        		String currentFile = fileChooser_CSharp.getSelectedFile().getAbsolutePath();
	        		String fileName = fileChooser_CSharp.getSelectedFile().getName();
	            	String extension = currentFile.substring(currentFile.length()-3, currentFile.length());
	            	if (!(extension.equals(".cs"))) {
	            		currentFile += ".cs";
	            		fileName += ".cs";
	            	}
	            	writeTextTo(textArea_CSharp.getText(), currentFile);
	        	}
			} else if (cmd.equals("Translate")) {
				translate.translate(textArea_Java, textArea_CSharp);				
			} else if (cmd.equals("UpdateSize")) {
				resizeComponents(getSize().getHeight(), getSize().getWidth());
			}
	}
	
	// Checks All State Changes
	public void itemStateChanged(ItemEvent e) {
		
	}
	
		
	@Override
	public void windowStateChanged(WindowEvent e) {				
		Timer count = new Timer(50, this);
		count.setActionCommand("UpdateSize");
		count.setRepeats(false);
		count.start();
	}
	
	private void resizeComponents(double height, double width) {
		int nWidth = (int) (width * .5);
		int nHeight1 = (int) (height - 190);
		int nHeight2 = (int) (nHeight1 - 20);
		
	    mainPanel1.setDividerLocation(nWidth);	    	    
	    sub2PanelLeft.setDividerLocation(nHeight1);	    	    
	    sub3PanelLeft1.setDividerLocation(nHeight2);	    	     	    
	    sub2PanelRight.setDividerLocation(nHeight1);	    	    
	    sub3PanelRight1.setDividerLocation(nHeight2);
	}
	
	private void updateCharCount(char key) {
		if (key == 'J') {
			int charCount = 0;
			int lineCount = 1;
			String text = textArea_Java.getText();
			
			for (int i=0;i<text.length();i++) {
				if (text.substring(i, i+1).equals("\n")) {
					lineCount++;
					charCount--;
				}
				charCount++;
			}
			
			textField_Java.setText("Lines: " + lineCount + "; Characters: " + charCount);
		} else if (key == 'C') {
			int charCount = 0;
			int lineCount = 1;
			String text = textArea_CSharp.getText();
			
			for (int i=0;i<text.length();i++) {
				if (text.substring(i, i+1).equals("\n")) {
					lineCount++;
					charCount--;
				}
				charCount++;
			}
			
			textField_CSharp.setText("Lines: " + lineCount + "; Characters: " + charCount);
		}
	}
	
	// Starts-Up the App	
	public static void main(String args[]) {
		int fontSize = 12;
		String[] keys = new String[]{"Label.font",
									 "TextField.font",
									 "TextArea.font",
									 "Button.font",
									 "CheckBox.font",
									 "ComboBox.font",
									 "MenuBar.font",
									 "Menu.font",
									 "MenuItem.font",
									 "ScrollPane.font",
									 "Frame.font",
									 "Panel.font",
									 "OptionPane.font",
									 "FileChooser.font",
									 "CheckBox.font",
									 "CheckBoxMenuItem.font",
									 "List.font",
									 "SplitPane.font",
									 "TabbedPane.font"};
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
			for (int i=0;i<keys.length;i++) {
				UIManager.put(keys[i], new Font("Sans_Serif",Font.PLAIN,fontSize));
			}        
		} catch(Exception e) {}
		Main app = new Main();
	}
	
	public String getTextFrom(String header, String loca) {
		String newText = "";
		try {
			BufferedReader br = new BufferedReader(new FileReader(loca));
			String s;
			while ((s = br.readLine()) != null) {
				newText += s + "\n";
			}
			if ((newText.length()<header.length())||(!newText.substring(0, header.length()).equals(header))) {
				newText = null;
			}
			br.close();
		} catch(Exception e) {
			newText = null;
		}
		return(newText);
	}
	
	public void writeTextTo(String text, String loca) {
		System.out.println("Starting Write");
		try {
			FileWriter fr = new FileWriter(loca);
			fr.write(text);
			fr.close();
		} catch(Exception e) {
			System.out.println("Error");
		}
	}
}
