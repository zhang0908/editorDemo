package editorDemo;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;

import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;

/**
 * 
 * @Description 
 * 记事本DIY 
 * 要求： 
 *   1. 用图形用户界面实现； 
 *   2. 能实现编辑、保存、另存为、查找替换等功能； 
 *   3. 提示：使用文件输入输出流；
 * 
 * @version 1.0.0
 * @Date 2019-1-1
 * @author Jinjie
 *
 */
public class EditorMainFrm extends JFrame {
	
	// 按钮事件监听器listener
	private ActionListener actionListener = new EditorActionListener(this);
	
	private JComboBox fontNameCbx = new JComboBox();
	private JComboBox fontSizeCbx = new JComboBox();
	private JTextPane contentTxr = new JTextPane();
	private JCheckBox fontStyleBoldCbx = new JCheckBox("粗体");
	private JCheckBox fontStyleItalicCbx = new JCheckBox("斜体");
	private JRadioButton redColorRbt = new JRadioButton("红");
	private JRadioButton yellowColorRbt = new JRadioButton("黄");
	private JRadioButton blueColorRbt = new JRadioButton("蓝");
	private JRadioButton blackColorRbt = new JRadioButton("黑");

	private ButtonGroup colorBGroup = new ButtonGroup();
	private JPanel colorPnl = new JPanel();
	
	private JMenuBar menuBar = new JMenuBar();
	private JPanel northBarPanel = new JPanel();
	private JPanel toolBarPanel = new JPanel();
	private JPanel findBarPanel = new JPanel();
	
	private JMenu fileMenu = new JMenu("文件(F)");
	private JMenu helpMenu = new JMenu("帮助(H)");
	private JMenuItem saveIMenu = new JMenuItem("保存(S)");
	private JMenuItem saveAsIMenu = new JMenuItem("另存为(A)");
	private JMenuItem openIMenu = new JMenuItem("打开(O)");
	private JMenuItem helpIMenu = new JMenuItem("说明(U)");
	private JMenuItem exitIMenu = new JMenuItem("退出");
	
	private Label fontLbl = new Label("字体:");
	private Label findLbl = new Label("查找:");
	private Label blankLbl1 = new Label("              ");
	private Label blankLbl2 = new Label("              ");
	private Label blankLbl3 = new Label("              ");
	
	private JTextField findTxt = new JTextField();
	private JTextField replaceTxt = new JTextField();

	private JButton findBtn = new JButton("查找");
	private JButton replaceBtn = new JButton("替换");

	// 字体大小
	private String fontSize[] = { "18", "20", "22", "28", "30", "34", "38", "40" };


	/**
	 * 构造函数
	 */
	public EditorMainFrm() {
		
		// 标题TITLE
		super("记事本V1.0.0---靳杰");
		
		// 初始化控件
		initComponent();
		
		// 控件添加事件监听器
		initComponentListener();
		
		//设置初始焦点
		this.addWindowListener(new WindowAdapter() {
			
			public void windowOpened(java.awt.event.WindowEvent evt) {
				
				contentTxr.requestFocus();
				
		}});
		
	}
	

	/**
	 * 控件初始化
	 */
	private void initComponent() {
		
		Dimension dim = this.getToolkit().getScreenSize();
		this.setBounds((int)(dim.width * 0.2), (int)(dim.height * 0.2), (int)(dim.width * 0.6), (int)(dim.height * 0.6));
		
		this.getContentPane().setLayout(new BorderLayout());
		
		fileMenu.add(openIMenu);
		fileMenu.add(saveIMenu);
		fileMenu.add(saveAsIMenu);
		fileMenu.addSeparator();
		fileMenu.add(exitIMenu);
		
		helpMenu.add(helpIMenu);
		
		menuBar.add(fileMenu);
		menuBar.add(helpMenu);
		
		setJMenuBar(menuBar);
		
		northBarPanel.setLayout(new GridLayout(2, 1));
		
		toolBarPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		
		toolBarPanel.add(fontLbl);
		toolBarPanel.add(fontNameCbx);
		blankLbl1.setSize(200, 20);
		toolBarPanel.add(blankLbl1);
		toolBarPanel.add(fontSizeCbx);
		toolBarPanel.add(blankLbl3);
		toolBarPanel.add(fontStyleBoldCbx);
		toolBarPanel.add(fontStyleItalicCbx);
		toolBarPanel.add(blankLbl2);
		colorBGroup.add(redColorRbt);
		colorBGroup.add(yellowColorRbt);
		colorBGroup.add(blueColorRbt);
		colorBGroup.add(blackColorRbt);
		
		colorPnl.add(redColorRbt);
		colorPnl.add(yellowColorRbt);
		colorPnl.add(blueColorRbt);
		colorPnl.add(blackColorRbt);
		
		toolBarPanel.add(colorPnl);
		
		findBarPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		findBarPanel.add(findLbl);
		findTxt.setColumns(21);
		findBarPanel.add(findTxt);
		replaceTxt.setColumns(21);
		findBarPanel.add(replaceTxt);
		findBarPanel.add(findBtn);
		findBarPanel.add(replaceBtn);
		
		northBarPanel.add(toolBarPanel);
		northBarPanel.add(findBarPanel);
		
		this.getContentPane().add(northBarPanel, BorderLayout.NORTH);
		
		// 设置字体CheckBox列表项
		fontNameCbx.setModel(new DefaultComboBoxModel(GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames()));
		
		// 设置字体大小CheckBox列表项
		fontSizeCbx.setModel(new DefaultComboBoxModel(fontSize));
		
		// 编辑器
		contentTxr.setContentType("text/html");
		JScrollPane textareascrollPane = new JScrollPane(contentTxr);
		this.getContentPane().add(textareascrollPane);
		this.getContentPane().add(textareascrollPane, "Center");
		textareascrollPane.setVerticalScrollBarPolicy(textareascrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		
		// 设置各菜单的快捷键
		fileMenu.setMnemonic('F');
		helpMenu.setMnemonic('H');
		
		openIMenu.setMnemonic('O');
		saveIMenu.setMnemonic('S');
		saveAsIMenu.setMnemonic('A');
		helpIMenu.setMnemonic('U');
		
		this.getContentPane().add(blankLbl1, BorderLayout.SOUTH);
		
	}

	/**
	 * 控件添加事件监听器
	 */
	private void initComponentListener() {
		
		openIMenu.addActionListener(actionListener);
		saveIMenu.addActionListener(actionListener);
		saveAsIMenu.addActionListener(actionListener);
		exitIMenu.addActionListener(actionListener);
		
		fontNameCbx.addActionListener(actionListener);
		fontSizeCbx.addActionListener(actionListener);
		fontStyleBoldCbx.addActionListener(actionListener);
		fontStyleItalicCbx.addActionListener(actionListener);
		redColorRbt.addActionListener(actionListener);
		yellowColorRbt.addActionListener(actionListener);
		blueColorRbt.addActionListener(actionListener);
		blackColorRbt.addActionListener(actionListener);
		helpIMenu.addActionListener(actionListener);
		
		findBtn.addActionListener(actionListener);
		replaceBtn.addActionListener(actionListener);
		
	}


	public JComboBox getFontNameCbx() {
		return fontNameCbx;
	}

	public void setFontNameCbx(JComboBox fontNameCbx) {
		this.fontNameCbx = fontNameCbx;
	}

	public JComboBox getFontSizeCbx() {
		return fontSizeCbx;
	}

	public void setFontSizeCbx(JComboBox fontSizeCbx) {
		this.fontSizeCbx = fontSizeCbx;
	}

	public JTextPane getContentTxr() {
		return contentTxr;
	}

	public void setContentTxr(JTextPane contentTxr) {
		this.contentTxr = contentTxr;
	}

	public JCheckBox getFontStyleBoldCbx() {
		return fontStyleBoldCbx;
	}

	public void setFontStyleBoldCbx(JCheckBox fontStyleBoldCbx) {
		this.fontStyleBoldCbx = fontStyleBoldCbx;
	}

	public JCheckBox getFontStyleItalicCbx() {
		return fontStyleItalicCbx;
	}

	public void setFontStyleItalicCbx(JCheckBox fontStyleItalicCbx) {
		this.fontStyleItalicCbx = fontStyleItalicCbx;
	}

	public JRadioButton getRedColorRbt() {
		return redColorRbt;
	}

	public void setRedColorRbt(JRadioButton redColorRbt) {
		this.redColorRbt = redColorRbt;
	}

	public JRadioButton getYellowColorRbt() {
		return yellowColorRbt;
	}

	public void setYellowColorRbt(JRadioButton yellowColorRbt) {
		this.yellowColorRbt = yellowColorRbt;
	}

	public JRadioButton getBlueColorRbt() {
		return blueColorRbt;
	}

	public void setBlueColorRbt(JRadioButton blueColorRbt) {
		this.blueColorRbt = blueColorRbt;
	}

	public ButtonGroup getColorBGroup() {
		return colorBGroup;
	}
	
	public JRadioButton getBlackColorRbt() {
		return blackColorRbt;
	}


	public void setBlackColorRbt(JRadioButton blackColorRbt) {
		this.blackColorRbt = blackColorRbt;
	}

	public void setColorBGroup(ButtonGroup colorBGroup) {
		this.colorBGroup = colorBGroup;
	}

	public JPanel getColorPnl() {
		return colorPnl;
	}

	public void setColorPnl(JPanel colorPnl) {
		this.colorPnl = colorPnl;
	}

	public static void main(String[] args) {
		EditorMainFrm editor = new EditorMainFrm();
		editor.setVisible(true);
	}
	

	public JTextField getFindTxt() {
		return findTxt;
	}


	public void setFindTxt(JTextField findTxt) {
		this.findTxt = findTxt;
	}


	public JButton getFindBtn() {
		return findBtn;
	}


	public void setFindBtn(JButton findBtn) {
		this.findBtn = findBtn;
	}


	public JButton getReplaceBtn() {
		return replaceBtn;
	}


	public void setReplaceBtn(JButton replaceBtn) {
		this.replaceBtn = replaceBtn;
	}
	
	public JTextField getReplaceTxt() {
		return replaceTxt;
	}


	public void setReplaceTxt(JTextField replaceTxt) {
		this.replaceTxt = replaceTxt;
	}
}