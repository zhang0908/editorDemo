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
 * 记事本DIY 
 * 要求： 
 *   1. 用图形用户界面实现； 
 *   2. 能实现编辑、保存、另存为、查找替换等功能； 
 *   3. 提示：使用文件输入输出流；
 * 
 * 按钮事件监听器listener
 * 
 * @version 1.0.0
 * @Date 2019-1-1
 * @author Jinjie
 *
 */
public class EditorActionListener implements ActionListener {
	
	// 父窗口
	private EditorMainFrm editorFrm;
	
	// 当前编辑的文件
	private File curFile;
	
	private int findTxtIndex4Replace;
	
	// 剪切板工具
	private Clipboard clipBoard4Paste = Toolkit.getDefaultToolkit().getSystemClipboard();
	
	public EditorActionListener(EditorMainFrm editorFrm) {
		
		this.editorFrm = editorFrm;
		
	}
	
	/**
	 * 事件监听器及处理
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		
		if (e.getSource() instanceof JMenuItem) {
			
			String itemCmd = e.getActionCommand();
			
			if ("打开(O)".equalsIgnoreCase(itemCmd)) {
			
				openFile();
			
			} else if ("保存(S)".equalsIgnoreCase(itemCmd)) {
			
				save2File();
			
			} else if ("另存为(A)".equalsIgnoreCase(itemCmd)) {
			
				saveAs2File();
			
			} else if ("退出".equalsIgnoreCase(itemCmd)) {
			
				exit();
			
			} else if ("说明(U)".equalsIgnoreCase(itemCmd)) {
				
				illustratAction();
				
			}
		}
		
		int offset = editorFrm.getContentTxr().getSelectionStart();
		int length = editorFrm.getContentTxr().getSelectionEnd() - offset;
		
		// 对字体风格和文字大小的处理
		if (e.getSource() instanceof JComboBox) {
			
			// 设置字体大小事件
			if (e.getSource().equals(editorFrm.getFontSizeCbx())) {
				
				fontSizeAction(Integer.parseInt(editorFrm.getFontSizeCbx().getSelectedItem().toString()), offset, length);
				
			// 设置字体事件
			} else if (e.getSource().equals(editorFrm.getFontNameCbx())) {
				
				fontStyleAction(editorFrm.getFontNameCbx().getSelectedItem().toString(), offset, length);
				
			}
		// 设置粗体字事件
		} else if (e.getSource().equals(editorFrm.getFontStyleBoldCbx())) {
			
			setFontStyleBold(editorFrm.getFontStyleBoldCbx().isSelected(), offset, length);
			
		// 设置斜体字事件
		} else if (e.getSource().equals(editorFrm.getFontStyleItalicCbx())) {
			
			setFontStyleItalic(editorFrm.getFontStyleItalicCbx().isSelected(), offset, length);
		
		// 设置红色字体事件
		} else if (e.getSource().equals(editorFrm.getRedColorRbt())) {
			
			setFontColor(Color.RED, offset, length);
		
		// 设置黄色字体事件
		} else if (e.getSource().equals(editorFrm.getYellowColorRbt())) {
			
			setFontColor(Color.YELLOW, offset, length);
			
		// 设置绿色字体事件
		} else if (e.getSource().equals(editorFrm.getBlueColorRbt())) {
			
			setFontColor(Color.BLUE, offset, length);
			
		// 设置黑色字体事件
		} else if (e.getSource().equals(editorFrm.getBlackColorRbt())) {
			
			setFontColor(Color.BLACK, offset, length);
		
		// 查找事件
		} else if (e.getSource().equals(editorFrm.getFindBtn())) {
			
			findAction(editorFrm.getContentTxr().getCaretPosition());
			
		// 替换事件
		} else if (e.getSource().equals(editorFrm.getReplaceBtn())) {
			
			replaceAction();
			
		}
	}

	/**
	 *  替换事件
	 */
	private void replaceAction() {
		
		String editorText = editorFrm.getContentTxr().getText();
		
		// 记事本内容为空，不能替换
		if (editorText == null || editorText.trim().length() == 0) {
			
			JOptionPane.showMessageDialog(editorFrm, "记事本内容为空, 请先输入.", "查找", JOptionPane.WARNING_MESSAGE);
			
			editorFrm.getContentTxr().requestFocus();
			
			findTxtIndex4Replace = -1;
			
			return;
			
		}
		
		String replaceText = editorFrm.getReplaceTxt().getText();
		// 检查待替换的内容
		if (replaceText == null || replaceText.trim().length() == 0) {
			
			JOptionPane.showMessageDialog(editorFrm, "请输入替换内容", "替换", JOptionPane.WARNING_MESSAGE);
			
			editorFrm.getReplaceTxt().requestFocus();
			
			findTxtIndex4Replace = -1;
			
			return;
			
		}
		
		// 检查：需要先查找后，才能对查找的内容进行替换
		if (findTxtIndex4Replace < 0) {
			
			JOptionPane.showMessageDialog(editorFrm, "请先执行查找, 再逐个替换.", "替换", JOptionPane.WARNING_MESSAGE);
			
			findTxtIndex4Replace = -1;
			
			return;
			
		}
		

		// 替换查找选中的内容
		editorFrm.getContentTxr().replaceSelection(replaceText);
		
		editorFrm.getContentTxr().setSelectionStart(findTxtIndex4Replace);
		
		editorFrm.getContentTxr().setSelectionEnd(findTxtIndex4Replace + replaceText.length());
		
		editorFrm.getContentTxr().requestFocus();
		
		findTxtIndex4Replace = -1;
	}

	/**
	 *  查找事件处理
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
		
		// 检查记事本内容是否为空
		if (editorText == null || editorText.trim().length() == 0) {
			
			JOptionPane.showMessageDialog(editorFrm, "记事本内容为空, 请先输入.", "查找", JOptionPane.WARNING_MESSAGE);
			
			editorFrm.getContentTxr().requestFocus();
			
			findTxtIndex4Replace = -1;
			
			return;
			
		}
		
		// 检查待查找字符串
		String findTxt = editorFrm.getFindTxt().getText();
		if (findTxt == null || findTxt.trim().length() == 0) {
			
			JOptionPane.showMessageDialog(editorFrm, "请输入查找内容", "查找", JOptionPane.WARNING_MESSAGE);
			
			editorFrm.getFindTxt().requestFocus();
			
			findTxtIndex4Replace = -1;
			
			return;
			
		}
		
		// 已查找到结尾，返回从头开始循环查询
		if (start < 0 || start >= editorFrm.getContentTxr().getText().length()) {
			start = 0;
		}
		

		String str = editorText.substring(start);
		
		int findstart = str.indexOf(findTxt);
		
		// 本轮查找结束，提示已查找到结尾
		if (start <= 0 && findstart == -1) {
		
			JOptionPane.showMessageDialog(editorFrm, "查找结束, 未查找到.");
			
			editorFrm.getContentTxr().setSelectionStart(0);
			
			editorFrm.getContentTxr().setSelectionEnd(0);
			
			editorFrm.getContentTxr().setCaretPosition(0);
		
		// 本轮查找结束，返回从头开发循环查找
		} else if (start > 0 && findstart == -1) {
			
			findAction(0);
		
		// 设置查找到的内容选中高亮显示
		} else {
		
			editorFrm.getContentTxr().setSelectionStart(start + findstart);
			
			int findend = start + findstart + findTxt.length();
			
			editorFrm.getContentTxr().setSelectionEnd(findend);
			
			editorFrm.getContentTxr().requestFocus();
			
			findTxtIndex4Replace = start + findstart;
		
		}
		
	}

	/**
	 * 设置字体颜色
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
	 * 设置斜体
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
	 * 设置粗体
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
	 * 设置说明提示
	 */
	private void illustratAction() {
		
		String content = "\r\n靳杰的记事本小程序说明：                                                                                                                                                    \r\n\r\n" + 
				"1. 用户图形界面使用SWING组件实现；\r\n" + 
				"2. 支持编辑、保存、另存为、查找替换等功能； \r\n" + 
				"3. 文件打开和保存，使用文件输入输出流实现；\r\n\r\n\r\n\r\n\r\n";
		
		JOptionPane.showMessageDialog(editorFrm, content, "记事本小程序说明",JOptionPane.WARNING_MESSAGE);
		
	}

	/**
	 * 设置字体
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
	 * 设置字体大小
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
	 * 打开文件
	 * 
	 */
	public void openFile() {
		
		JFileChooser fileChooser = new JFileChooser("d:\\");
		
		// 文本过滤器，过滤txt类型文件
		FileFilter filter = new FileNameExtensionFilter("Txt文件(*.txt)", "txt");
		
		fileChooser.setFileFilter(filter);
		
		// 只文件可选
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		
		int returnVal = fileChooser.showOpenDialog(editorFrm);
		
		// 确定后，字符流读取文件内容
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			
			Document docs = editorFrm.getContentTxr().getDocument();
			
			MutableAttributeSet as = editorFrm.getContentTxr().getInputAttributes();
			
			curFile = fileChooser.getSelectedFile();
			
			FileInputStream inputStream = null;
			
			BufferedReader readerBuffer = null;
			
			try {
				
				FileReader fr = new FileReader(curFile);
				Document oldDoc = editorFrm.getContentTxr().getDocument();
				
				// 清空记事本原有内容
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
				
			// 关闭文件流
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
		
		// 文件类型过滤，只txt类型文件
		FileFilter filter = new FileNameExtensionFilter("Txt文件(*.txt)", "txt");
		
		fileChooser.setFileFilter(filter);
		
		int returnVal = fileChooser.showSaveDialog(editorFrm);
		
		// 确定，字符流保存文件
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			
			File file = fileChooser.getSelectedFile();
			
			String filePath = file.getAbsolutePath();
			
			// 自动追加后缀名.txt
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
				JOptionPane.showMessageDialog(null, "保存成功.");
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				
				//关闭文件流
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
	 * 保存文件
	 */
	public void save2File() {
		
		// 直接保存到原有文件中
		if (curFile != null) {
			
			// 确定，字符流保存文件
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
				JOptionPane.showMessageDialog(null, "保存成功。");
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				
				//关闭文件流
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
	 * 退出记事本程序
	 */
	public void exit() {
		if (JOptionPane.showConfirmDialog(editorFrm, "退出记事本程序？") == 0) {
			System.exit(0);
		}
	}

}
