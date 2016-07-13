package filter;

import javax.swing.filechooser.FileFilter;

public class MyFileFilter extends FileFilter {
	public boolean accept(java.io.File f) {
		if (f.isDirectory())
			return true;
		return f.getName().endsWith(".xml"); // ����Ϊѡ����.xmlΪ��׺���ļ�
	}

	public String getDescription() {
		return ".xml";
	}

}