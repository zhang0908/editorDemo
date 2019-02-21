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
 * ���±�DIY 
 * Ҫ�� 
 *   1. ��ͼ���û�����ʵ�֣� 
 *   2. ��ʵ�ֱ༭�����桢���Ϊ�������滻�ȹ��ܣ� 
 *   3. ��ʾ��ʹ���ļ������������
 * 
 * @version 1.0.0
 * @Date 2019-1-1
 * @author Jinjie
 *
 */
public class EditorMainFrm extends JFrame {
	
	// ��ť�¼�������listener
	private ActionListener actionListener = new EditorActionListener(this);
	
	private JComboBox fontNameCbx = new JComboBox();
	private JComboBox fontSizeCbx = new JComboBox();
	private JTextPane contentTxr = new JTextPane();
	private JCheckBox fontStyleBoldCbx = new JCheckBox("����");
	private JCheckBox fontStyleItalicCbx = new JCheckBox("б��");
	private JRadioButton redColorRbt = new JRadioButton("��");
	private JRadioButton yellowColorRbt = new JRadioButton("��");
	private JRadioButton blueColorRbt = new JRadioButton("��");
	private JRadioButton blackColorRbt = new JRadioButton("��");

	private ButtonGroup colorBGroup = new ButtonGroup();
	private JPanel colorPnl = new JPanel();
	
	private JMenuBar menuBar = new JMenuBar();
	private JPanel northBarPanel = new JPanel();
	private JPanel toolBarPanel = new JPanel();
	private JPanel findBarPanel = new JPanel();
	
	private JMenu fileMenu = new JMenu("�ļ�(F)");
	private JMenu helpMenu = new JMenu("����(H)");
	private JMenuItem saveIMenu = new JMenuItem("����(S)");
	private JMenuItem saveAsIMenu = new JMenuItem("���Ϊ(A)");
	private JMenuItem openIMenu = new JMenuItem("��(O)");
	private JMenuItem helpIMenu = new JMenuItem("˵��(U)");
	private JMenuItem exitIMenu = new JMenuItem("�˳�");
	
	private Label fontLbl = new Label("����:");
	private Label findLbl = new Label("����:");
	private Label blankLbl1 = new Label("              ");
	private Label blankLbl2 = new Label("              ");
	private Label blankLbl3 = new Label("              ");
	
	private JTextField findTxt = new JTextField();
	private JTextField replaceTxt = new JTextField();

	private JButton findBtn = new JButton("����");
	private JButton replaceBtn = new JButton("�滻");

	// �����С
	private String fontSize[] = { "18", "20", "22", "28", "30", "34", "38", "40" };


	/**
	 * ���캯��
	 */
	public EditorMainFrm() {
		
		// ����TITLE
		super("���±�V1.0.0---����");
		
		// ��ʼ���ؼ�
		initComponent();
		
		// �ؼ�����¼�������
		initComponentListener();
		
		//���ó�ʼ����
		this.addWindowListener(new WindowAdapter() {
			
			public void windowOpened(java.awt.event.WindowEvent evt) {
				
				contentTxr.requestFocus();
				
		}});
		
	}
	

	/**
	 * �ؼ���ʼ��
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
		
		// ��������CheckBox�б���
		fontNameCbx.setModel(new DefaultComboBoxModel(GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames()));
		
		// ���������СCheckBox�б���
		fontSizeCbx.setModel(new DefaultComboBoxModel(fontSize));
		
		// �༭��
		contentTxr.setContentType("text/html");
		JScrollPane textareascrollPane = new JScrollPane(contentTxr);
		this.getContentPane().add(textareascrollPane);
		this.getContentPane().add(textareascrollPane, "Center");
		textareascrollPane.setVerticalScrollBarPolicy(textareascrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		
		// ���ø��˵��Ŀ�ݼ�
		fileMenu.setMnemonic('F');
		helpMenu.setMnemonic('H');
		
		openIMenu.setMnemonic('O');
		saveIMenu.setMnemonic('S');
		saveAsIMenu.setMnemonic('A');
		helpIMenu.setMnemonic('U');
		
		this.getContentPane().add(blankLbl1, BorderLayout.SOUTH);
		
	}

	/**
	 * �ؼ�����¼�������
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