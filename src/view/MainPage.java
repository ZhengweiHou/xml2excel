package view;

import java.awt.BorderLayout;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;

import serve.TransformServe;

/**
 * ������
 * 
 * @author houzw
 * 
 */
public class MainPage implements ActionListener {
	private JFrame mainFrame; // ������
	private JTabbedPane tabPane;// ѡ�����
	// private JFileChooser jfc;// �ļ�ѡ����
	private FileChooserPanel fcp; // �ļ�ѡ��pane
	private JButton transformButtom; // ת����ť
	private TransformServe transformServe;  // ����
	
	/**
	 * ��������
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		new MainPage().show();
	}

	public MainPage() {
		super();
	}

	/**
	 * �����ʼ������
	 */
	public void init() {

		transformServe = new TransformServe();
		
		mainFrame = new JFrame("xml2excel"); // ������
		tabPane = new JTabbedPane();// ѡ�����
		fcp = new FileChooserPanel();// �ļ�ѡ��pane
		transformButtom = new JButton("ת����ť");

		transformButtom.addActionListener(this);

		// ����������ȡ����Ļ�ĸ߶ȺͿ��
		double lx = Toolkit.getDefaultToolkit().getScreenSize().getWidth();
		double ly = Toolkit.getDefaultToolkit().getScreenSize().getHeight();
		mainFrame.setLocation(new Point((int) (lx / 2) - 150,
				(int) (ly / 2) - 300));// �趨���ڳ���λ��
		mainFrame.setSize(300, 300);// �趨���ڴ�С
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.add(tabPane, BorderLayout.CENTER);
		mainFrame.add(transformButtom, BorderLayout.SOUTH);

		// mainFrame.setContentPane(tabPane);// ���ò���

		tabPane.add("�ļ�ѡ��", fcp);

	}

	/**
	 * ������ʾ����
	 */
	public void show() {
		this.init();
		this.mainFrame.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(transformButtom)) {
			File[] files = fcp.getFiles();
			if (files == null || files.length <= 0) {
				// System.out.println("��ѡ���ļ���");
				JOptionPane.showMessageDialog(null, "��ѡ���ļ���", "������Ϣ",
						JOptionPane.WARNING_MESSAGE);
			} else {
				JFileChooser fileSavePath = new JFileChooser();
				fileSavePath.setDialogTitle("ѡ�񱣴�·��");
				fileSavePath
						.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int state = fileSavePath.showOpenDialog(null);
				if (state == 1) {
					return;// �����򷵻�
				} else {
					File filePath = fileSavePath.getSelectedFile();
					transformServe.doXml2Excel(filePath, files);
					
//					System.out.println(this.getClass().getName() + ":����λ��Ϊ"
//							+ filePath.getAbsolutePath());
//					System.out.println(this.getClass().getName() + ":�ļ�����Ϊ"
//							+ files.length);
				}

			}
		}

	}

}
