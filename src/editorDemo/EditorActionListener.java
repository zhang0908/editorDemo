package editorDemo;

import java.awt.Color;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Document;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;

/**
 * 
 * @Description 
 * ���±�DIY 
 * Ҫ�� 
 *   1. ��ͼ���û�����ʵ�֣� 
 *   2. ��ʵ�ֱ༭�����桢���Ϊ�������滻�ȹ��ܣ� 
 *   3. ��ʾ��ʹ���ļ������������
 * 
 * ��ť�¼�������listener
 * 
 * @version 1.0.0
 * @Date 2019-1-1
 * @author Jinjie
 *
 */
public class EditorActionListener implements ActionListener {
	
	// ������
	private EditorMainFrm editorFrm;
	
	// ��ǰ�༭���ļ�
	private File curFile;
	
	private int findTxtIndex4Replace;
	
	// ���а幤��
	private Clipboard clipBoard4Paste = Toolkit.getDefaultToolkit().getSystemClipboard();
	
	public EditorActionListener(EditorMainFrm editorFrm) {
		
		this.editorFrm = editorFrm;
		
	}
	
	/**
	 * �¼�������������
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		
		if (e.getSource() instanceof JMenuItem) {
			
			String itemCmd = e.getActionCommand();
			
			if ("��(O)".equalsIgnoreCase(itemCmd)) {
			
				openFile();
			
			} else if ("����(S)".equalsIgnoreCase(itemCmd)) {
			
				save2File();
			
			} else if ("���Ϊ(A)".equalsIgnoreCase(itemCmd)) {
			
				saveAs2File();
			
			} else if ("�˳�".equalsIgnoreCase(itemCmd)) {
			
				exit();
			
			} else if ("˵��(U)".equalsIgnoreCase(itemCmd)) {
				
				illustratAction();
				
			}
		}
		
		int offset = editorFrm.getContentTxr().getSelectionStart();
		int length = editorFrm.getContentTxr().getSelectionEnd() - offset;
		
		// ������������ִ�С�Ĵ���
		if (e.getSource() instanceof JComboBox) {
			
			// ���������С�¼�
			if (e.getSource().equals(editorFrm.getFontSizeCbx())) {
				
				fontSizeAction(Integer.parseInt(editorFrm.getFontSizeCbx().getSelectedItem().toString()), offset, length);
				
			// ���������¼�
			} else if (e.getSource().equals(editorFrm.getFontNameCbx())) {
				
				fontStyleAction(editorFrm.getFontNameCbx().getSelectedItem().toString(), offset, length);
				
			}
		// ���ô������¼�
		} else if (e.getSource().equals(editorFrm.getFontStyleBoldCbx())) {
			
			setFontStyleBold(editorFrm.getFontStyleBoldCbx().isSelected(), offset, length);
			
		// ����б�����¼�
		} else if (e.getSource().equals(editorFrm.getFontStyleItalicCbx())) {
			
			setFontStyleItalic(editorFrm.getFontStyleItalicCbx().isSelected(), offset, length);
		
		// ���ú�ɫ�����¼�
		} else if (e.getSource().equals(editorFrm.getRedColorRbt())) {
			
			setFontColor(Color.RED, offset, length);
		
		// ���û�ɫ�����¼�
		} else if (e.getSource().equals(editorFrm.getYellowColorRbt())) {
			
			setFontColor(Color.YELLOW, offset, length);
			
		// ������ɫ�����¼�
		} else if (e.getSource().equals(editorFrm.getBlueColorRbt())) {
			
			setFontColor(Color.BLUE, offset, length);
			
		// ���ú�ɫ�����¼�
		} else if (e.getSource().equals(editorFrm.getBlackColorRbt())) {
			
			setFontColor(Color.BLACK, offset, length);
		
		// �����¼�
		} else if (e.getSource().equals(editorFrm.getFindBtn())) {
			
			findAction(editorFrm.getContentTxr().getCaretPosition());
			
		// �滻�¼�
		} else if (e.getSource().equals(editorFrm.getReplaceBtn())) {
			
			replaceAction();
			
		}
	}

	/**
	 *  �滻�¼�
	 */
	private void replaceAction() {
		
		String editorText = editorFrm.getContentTxr().getText();
		
		// ���±�����Ϊ�գ������滻
		if (editorText == null || editorText.trim().length() == 0) {
			
			JOptionPane.showMessageDialog(editorFrm, "���±�����Ϊ��, ��������.", "����", JOptionPane.WARNING_MESSAGE);
			
			editorFrm.getContentTxr().requestFocus();
			
			findTxtIndex4Replace = -1;
			
			return;
			
		}
		
		String replaceText = editorFrm.getReplaceTxt().getText();
		// �����滻������
		if (replaceText == null || replaceText.trim().length() == 0) {
			
			JOptionPane.showMessageDialog(editorFrm, "�������滻����", "�滻", JOptionPane.WARNING_MESSAGE);
			
			editorFrm.getReplaceTxt().requestFocus();
			
			findTxtIndex4Replace = -1;
			
			return;
			
		}
		
		// ��飺��Ҫ�Ȳ��Һ󣬲��ܶԲ��ҵ����ݽ����滻
		if (findTxtIndex4Replace < 0) {
			
			JOptionPane.showMessageDialog(editorFrm, "����ִ�в���, ������滻.", "�滻", JOptionPane.WARNING_MESSAGE);
			
			findTxtIndex4Replace = -1;
			
			return;
			
		}
		

		// �滻����ѡ�е�����
		editorFrm.getContentTxr().replaceSelection(replaceText);
		
		editorFrm.getContentTxr().setSelectionStart(findTxtIndex4Replace);
		
		editorFrm.getContentTxr().setSelectionEnd(findTxtIndex4Replace + replaceText.length());
		
		editorFrm.getContentTxr().requestFocus();
		
		findTxtIndex4Replace = -1;
	}

	/**
	 *  �����¼�����
	 * @param start
	 */
	private void findAction(int start) {
		
		
		Document doc = editorFrm.getContentTxr().getDocument();
		
		String editorText = "";
		try {
			editorText = doc.getText(0, doc.getLength() - 1);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
		
		// �����±������Ƿ�Ϊ��
		if (editorText == null || editorText.trim().length() == 0) {
			
			JOptionPane.showMessageDialog(editorFrm, "���±�����Ϊ��, ��������.", "����", JOptionPane.WARNING_MESSAGE);
			
			editorFrm.getContentTxr().requestFocus();
			
			findTxtIndex4Replace = -1;
			
			return;
			
		}
		
		// ���������ַ���
		String findTxt = editorFrm.getFindTxt().getText();
		if (findTxt == null || findTxt.trim().length() == 0) {
			
			JOptionPane.showMessageDialog(editorFrm, "�������������", "����", JOptionPane.WARNING_MESSAGE);
			
			editorFrm.getFindTxt().requestFocus();
			
			findTxtIndex4Replace = -1;
			
			return;
			
		}
		
		// �Ѳ��ҵ���β�����ش�ͷ��ʼѭ����ѯ
		if (start < 0 || start >= editorFrm.getContentTxr().getText().length()) {
			start = 0;
		}
		

		String str = editorText.substring(start);
		
		int findstart = str.indexOf(findTxt);
		
		// ���ֲ��ҽ�������ʾ�Ѳ��ҵ���β
		if (start <= 0 && findstart == -1) {
		
			JOptionPane.showMessageDialog(editorFrm, "���ҽ���, δ���ҵ�.");
			
			editorFrm.getContentTxr().setSelectionStart(0);
			
			editorFrm.getContentTxr().setSelectionEnd(0);
			
			editorFrm.getContentTxr().setCaretPosition(0);
		
		// ���ֲ��ҽ��������ش�ͷ����ѭ������
		} else if (start > 0 && findstart == -1) {
			
			findAction(0);
		
		// ���ò��ҵ�������ѡ�и�����ʾ
		} else {
		
			editorFrm.getContentTxr().setSelectionStart(start + findstart);
			
			int findend = start + findstart + findTxt.length();
			
			editorFrm.getContentTxr().setSelectionEnd(findend);
			
			editorFrm.getContentTxr().requestFocus();
			
			findTxtIndex4Replace = start + findstart;
		
		}
		
	}

	/**
	 * ����������ɫ
	 * @param color
	 * @param offset
	 * @param length
	 */
	private void setFontColor(Color color, int offset, int length) {
		
		MutableAttributeSet as = editorFrm.getContentTxr().getInputAttributes();
		
		StyleConstants.setForeground(as, color);
		
		((DefaultStyledDocument)editorFrm.getContentTxr().getDocument()).setCharacterAttributes(offset, length, as, false);
		
	}

	/**
	 * ����б��
	 * @param isItalic
	 * @param offset
	 * @param length
	 */
	private void setFontStyleItalic(boolean isItalic, int offset, int length) {
		
		MutableAttributeSet as = editorFrm.getContentTxr().getInputAttributes();
		
		StyleConstants.setItalic(as, isItalic);
		
		((DefaultStyledDocument)editorFrm.getContentTxr().getDocument()).setCharacterAttributes(offset, length, as, false);
		
	}

	/**
	 * ���ô���
	 * @param isBold
	 * @param offset
	 * @param length
	 */
	private void setFontStyleBold(boolean isBold, int offset, int length) {
		
		MutableAttributeSet as = editorFrm.getContentTxr().getInputAttributes();
		
		StyleConstants.setBold(as, isBold);
		
		((DefaultStyledDocument)editorFrm.getContentTxr().getDocument()).setCharacterAttributes(offset, length, as, false);
		
	}

	/**
	 * ����˵����ʾ
	 */
	private void illustratAction() {
		
		String content = "\r\n���ܵļ��±�С����˵����                                                                                                                                                    \r\n\r\n" + 
				"1. �û�ͼ�ν���ʹ��SWING���ʵ�֣�\r\n" + 
				"2. ֧�ֱ༭�����桢���Ϊ�������滻�ȹ��ܣ� \r\n" + 
				"3. �ļ��򿪺ͱ��棬ʹ���ļ����������ʵ�֣�\r\n\r\n\r\n\r\n\r\n";
		
		JOptionPane.showMessageDialog(editorFrm, content, "���±�С����˵��",JOptionPane.WARNING_MESSAGE);
		
	}

	/**
	 * ��������
	 * @param fontStyle
	 * @param offset
	 * @param length
	 */
	private void fontStyleAction(String fontStyle, int offset, int length) {
		
		MutableAttributeSet as = editorFrm.getContentTxr().getInputAttributes();
		
		StyleConstants.setFontFamily(as, fontStyle);
		
		((DefaultStyledDocument)editorFrm.getContentTxr().getDocument()).setCharacterAttributes(offset, length, as, false);
		
	}

	/**
	 * ���������С
	 * @param fontSize
	 * @param offset
	 * @param length
	 */
	private void fontSizeAction(int fontSize, int offset, int length) {
		
		MutableAttributeSet as = editorFrm.getContentTxr().getInputAttributes();
		
		StyleConstants.setFontSize(as, fontSize);
		
		((DefaultStyledDocument)editorFrm.getContentTxr().getDocument()).setCharacterAttributes(offset, length, as, false);
		
	}
	
	/**
	 * ���ļ�
	 * 
	 */
	public void openFile() {
		
		JFileChooser fileChooser = new JFileChooser("d:\\");
		
		// �ı�������������txt�����ļ�
		FileFilter filter = new FileNameExtensionFilter("Txt�ļ�(*.txt)", "txt");
		
		fileChooser.setFileFilter(filter);
		
		// ֻ�ļ���ѡ
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		
		int returnVal = fileChooser.showOpenDialog(editorFrm);
		
		// ȷ�����ַ�����ȡ�ļ�����
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			
			Document docs = editorFrm.getContentTxr().getDocument();
			
			MutableAttributeSet as = editorFrm.getContentTxr().getInputAttributes();
			
			curFile = fileChooser.getSelectedFile();
			
			FileInputStream inputStream = null;
			
			BufferedReader readerBuffer = null;
			
			try {
				
				FileReader fr = new FileReader(curFile);
				Document oldDoc = editorFrm.getContentTxr().getDocument();
				
				// ��ռ��±�ԭ������
				oldDoc.remove(0, docs.getLength());
				
				HTMLEditorKit editorKit = new HTMLEditorKit();
				Document document = (HTMLDocument) editorKit.createDefaultDocument();
				editorKit.read(fr, document, 0);
				editorFrm.getContentTxr().setDocument(document);
				/*
				 * inputStream = new FileInputStream(file);
				 * 
				 * readerBuffer = new BufferedReader(new InputStreamReader(inputStream));
				 * 
				 * String tempreader = "";
				 * 
				 * 
				 * while ((tempreader = readerBuffer.readLine()) != null) {
				 * 
				 * docs.insertString(docs.getLength(), tempreader, as);
				 * 
				 * }
				 */
				
			} catch (FileNotFoundException e1) {
				
				e1.printStackTrace();
				
			} catch (IOException e1) {
				
				e1.printStackTrace();
			
			} catch (BadLocationException e) {
				
				e.printStackTrace();
				
			// �ر��ļ���
			} finally {
				
				try {
					
					if (readerBuffer != null) {
						
						readerBuffer.close();
						
					}
					
					if (inputStream != null) {
						
						inputStream.close();
						
					}
					
				} catch (Exception e) {
					
					e.printStackTrace();
				
				}
				
			}
			
		}
		
	}
	
	private void saveAs2File() {
		
		JFileChooser fileChooser = new JFileChooser("d:\\");
		
		// �ļ����͹��ˣ�ֻtxt�����ļ�
		FileFilter filter = new FileNameExtensionFilter("Txt�ļ�(*.txt)", "txt");
		
		fileChooser.setFileFilter(filter);
		
		int returnVal = fileChooser.showSaveDialog(editorFrm);
		
		// ȷ�����ַ��������ļ�
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			
			File file = fileChooser.getSelectedFile();
			
			String filePath = file.getAbsolutePath();
			
			// �Զ�׷�Ӻ�׺��.txt
			filePath = filePath.endsWith(".txt") ? filePath : filePath + ".txt";
			
			FileOutputStream outputStream = null;
			
			BufferedWriter writerBuffer = null;
			
			try {
				
				outputStream = new FileOutputStream(filePath);
				
				writerBuffer = new BufferedWriter(new OutputStreamWriter(outputStream));
				
				for (String textareatemp : editorFrm.getContentTxr().getText().split("\n")) {
					
					writerBuffer.write(textareatemp);
					
					writerBuffer.newLine();
					
				}
				writerBuffer.flush();
				JOptionPane.showMessageDialog(null, "����ɹ�.");
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				
				//�ر��ļ���
				try {
					
					if (writerBuffer != null) {
						
						writerBuffer.close();
						
					}
					if (outputStream != null) {
						
						outputStream.close();
						
					}
					
				} catch (Exception e) {
					
					e.printStackTrace();
					
				}
				
			}
			
		}
		
	}

	/**
	 * �����ļ�
	 */
	public void save2File() {
		
		// ֱ�ӱ��浽ԭ���ļ���
		if (curFile != null) {
			
			// ȷ�����ַ��������ļ�
			FileOutputStream outputStream = null;
			
			BufferedWriter writerBuffer = null;
			
			try {
				
				outputStream = new FileOutputStream(curFile);
				
				writerBuffer = new BufferedWriter(new OutputStreamWriter(outputStream));
				
				for (String textareatemp : editorFrm.getContentTxr().getText().split("\n")) {
					
					writerBuffer.write(textareatemp);
					
					writerBuffer.newLine();
					
				}
				writerBuffer.flush();
				JOptionPane.showMessageDialog(null, "����ɹ���");
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				
				//�ر��ļ���
				try {
					
					if (writerBuffer != null) {
						
						writerBuffer.close();
						
					}
					if (outputStream != null) {
						
						outputStream.close();
						
					}
					
				} catch (Exception e) {
					
					e.printStackTrace();
					
				}
				
			}
			
		} else {
			
			saveAs2File();
			
		}
		
	}

	/**
	 * �˳����±�����
	 */
	public void exit() {
		if (JOptionPane.showConfirmDialog(editorFrm, "�˳����±�����") == 0) {
			System.exit(0);
		}
	}

}
