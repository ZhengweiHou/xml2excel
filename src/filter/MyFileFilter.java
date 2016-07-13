package filter;

import javax.swing.filechooser.FileFilter;

public class MyFileFilter extends FileFilter {
	public boolean accept(java.io.File f) {
		if (f.isDirectory())
			return true;
		return f.getName().endsWith(".xml"); // 设置为选择以.xml为后缀的文件
	}

	public String getDescription() {
		return ".xml";
	}

}