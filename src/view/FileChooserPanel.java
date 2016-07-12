package view;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

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
		filter = new FileNameExtensionFilter("XML file", "xml");

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
			jfc.addChoosableFileFilter(filter);
			jfc.setMultiSelectionEnabled(true);
			int state = jfc.showOpenDialog(null);
			if (state == 1) {
				return;// 撤销则返回
			} else {
				files = jfc.getSelectedFiles(); // 获得选择的文件
				this.fileJList.setListData(files);
				System.out.println(this.getClass().getName() + ":文件个数为"
						+ files.length);
			}

		} else if (e.getSource().equals(fileCleanButton)) {
			// TODO 清除按钮操作
			files = null;
			this.fileJList.setListData(new Vector());

		}

	}

	public File[] getFiles() {
		return files;
	}

}
