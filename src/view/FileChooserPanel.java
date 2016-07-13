package view;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import filter.MyFileFilter;

/**
 * 文件选择panel
 * 
 * @author houzw
 * 
 */
public class FileChooserPanel extends JPanel implements ActionListener {
	private JButton fileChosseButton; // 选择文件按钮
	private JButton fileCleanButton; // 清除按钮
	private JScrollPane jsp; // 滚动pane
	private JPanel buttonPanel; // 按钮pane
	private JList fileJList; // 选择文件显示列表
	private JFileChooser jfc; // 文件选择器
	private String startFilePath; // 文件选择器的初始目录
	private File[] files;
	private FileFilter filter; // 文件过滤器

	public FileChooserPanel() {
		super();
		this.startFilePath = "d:\\"; // 文件选择器的初始位置

		this.setLayout(new BorderLayout());

		fileJList = new JList();
		jsp = new JScrollPane(fileJList);
		// filter = new FileNameExtensionFilter("XML file", "xml");
		filter = new MyFileFilter();

		buttonPanel = new JPanel();
		fileChosseButton = new JButton("选择文件...");
		fileChosseButton.addActionListener(this);
		fileCleanButton = new JButton("清除");
		fileCleanButton.addActionListener(this);
		buttonPanel.add(fileChosseButton);
		buttonPanel.add(fileCleanButton);
		this.add(jsp, BorderLayout.CENTER);
		this.add(buttonPanel, BorderLayout.SOUTH);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(fileChosseButton)) {
			// 文件选择按钮操作
			jfc = new JFileChooser(startFilePath);
			jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
			// jfc.addChoosableFileFilter(filter);
			jfc.setFileFilter(filter);
			jfc.setMultiSelectionEnabled(true);
			int state = jfc.showOpenDialog(null);
			if (state == 1) {
				return;// 撤销则返回
			} else {
				files = jfc.getSelectedFiles(); // 获得选择的文件

				if (!this.validateFileType(files)) {
//					this.fileJList.setListData(new String[] {"含有非法文件!"});
					JOptionPane.showMessageDialog(null, "含有非法文件！", "警告信息",
							JOptionPane.WARNING_MESSAGE);
				} else {
					this.fileJList.setListData(files);
				}

			}

		} else if (e.getSource().equals(fileCleanButton)) {
			// TODO 清除按钮操作
			files = null;
			this.fileJList.setListData(new Vector());

		}

	}

	/**
	 * 验证文件格式
	 * 
	 * @param files
	 * @return
	 */
	public Boolean validateFileType(File[] files) {
		for (File file : files) {
//			String a = file.getName();
//			String[] temp = a.split(".");
			String fileName=file.getName();
		    String prefix=fileName.substring(fileName.lastIndexOf(".")+1);		
			if (!prefix.equals("xml")) {
				return false;
			}
		}

		return true;
	}

	public File[] getFiles() {
		return files;
	}

}
