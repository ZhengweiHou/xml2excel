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
 * �ļ�ѡ��panel
 * 
 * @author houzw
 * 
 */
public class FileChooserPanel extends JPanel implements ActionListener {
	private JButton fileChosseButton; // ѡ���ļ���ť
	private JButton fileCleanButton; // �����ť
	private JScrollPane jsp; // ����pane
	private JPanel buttonPanel; // ��ťpane
	private JList fileJList; // ѡ���ļ���ʾ�б�
	private JFileChooser jfc; // �ļ�ѡ����
	private String startFilePath; // �ļ�ѡ�����ĳ�ʼĿ¼
	private File[] files;
	private FileFilter filter; // �ļ�������

	public FileChooserPanel() {
		super();
		this.startFilePath = "d:\\"; // �ļ�ѡ�����ĳ�ʼλ��

		this.setLayout(new BorderLayout());

		fileJList = new JList();
		jsp = new JScrollPane(fileJList);
		// filter = new FileNameExtensionFilter("XML file", "xml");
		filter = new MyFileFilter();

		buttonPanel = new JPanel();
		fileChosseButton = new JButton("ѡ���ļ�...");
		fileChosseButton.addActionListener(this);
		fileCleanButton = new JButton("���");
		fileCleanButton.addActionListener(this);
		buttonPanel.add(fileChosseButton);
		buttonPanel.add(fileCleanButton);
		this.add(jsp, BorderLayout.CENTER);
		this.add(buttonPanel, BorderLayout.SOUTH);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(fileChosseButton)) {
			// �ļ�ѡ��ť����
			jfc = new JFileChooser(startFilePath);
			jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
			// jfc.addChoosableFileFilter(filter);
			jfc.setFileFilter(filter);
			jfc.setMultiSelectionEnabled(true);
			int state = jfc.showOpenDialog(null);
			if (state == 1) {
				return;// �����򷵻�
			} else {
				files = jfc.getSelectedFiles(); // ���ѡ����ļ�

				if (!this.validateFileType(files)) {
//					this.fileJList.setListData(new String[] {"���зǷ��ļ�!"});
					JOptionPane.showMessageDialog(null, "���зǷ��ļ���", "������Ϣ",
							JOptionPane.WARNING_MESSAGE);
				} else {
					this.fileJList.setListData(files);
				}

			}

		} else if (e.getSource().equals(fileCleanButton)) {
			// TODO �����ť����
			files = null;
			this.fileJList.setListData(new Vector());

		}

	}

	/**
	 * ��֤�ļ���ʽ
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
