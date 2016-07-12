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
 * 主界面
 * 
 * @author houzw
 * 
 */
public class MainPage implements ActionListener {
	private JFrame mainFrame; // 主容器
	private JTabbedPane tabPane;// 选项卡布局
	// private JFileChooser jfc;// 文件选择器
	private FileChooserPanel fcp; // 文件选择pane
	private JButton transformButtom; // 转换按钮
	private TransformServe transformServe;  // 服务
	
	/**
	 * 启动方法
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
	 * 界面初始化方法
	 */
	public void init() {

		transformServe = new TransformServe();
		
		mainFrame = new JFrame("xml2excel"); // 主容器
		tabPane = new JTabbedPane();// 选项卡布局
		fcp = new FileChooserPanel();// 文件选择pane
		transformButtom = new JButton("转换按钮");

		transformButtom.addActionListener(this);

		// 下面两行是取得屏幕的高度和宽度
		double lx = Toolkit.getDefaultToolkit().getScreenSize().getWidth();
		double ly = Toolkit.getDefaultToolkit().getScreenSize().getHeight();
		mainFrame.setLocation(new Point((int) (lx / 2) - 150,
				(int) (ly / 2) - 300));// 设定窗口出现位置
		mainFrame.setSize(300, 300);// 设定窗口大小
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.add(tabPane, BorderLayout.CENTER);
		mainFrame.add(transformButtom, BorderLayout.SOUTH);

		// mainFrame.setContentPane(tabPane);// 设置布局

		tabPane.add("文件选择", fcp);

	}

	/**
	 * 界面显示方法
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
				// System.out.println("请选择文件！");
				JOptionPane.showMessageDialog(null, "请选择文件！", "警告信息",
						JOptionPane.WARNING_MESSAGE);
			} else {
				JFileChooser fileSavePath = new JFileChooser();
				fileSavePath.setDialogTitle("选择保存路径");
				fileSavePath
						.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int state = fileSavePath.showOpenDialog(null);
				if (state == 1) {
					return;// 撤销则返回
				} else {
					File filePath = fileSavePath.getSelectedFile();
					transformServe.doXml2Excel(filePath, files);
					
//					System.out.println(this.getClass().getName() + ":保存位置为"
//							+ filePath.getAbsolutePath());
//					System.out.println(this.getClass().getName() + ":文件个数为"
//							+ files.length);
				}

			}
		}

	}

}
