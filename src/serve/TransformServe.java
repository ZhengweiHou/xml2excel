package serve;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * �ļ�ת������
 * 
 * @author houzw
 * 
 */
public class TransformServe {

	private int leafSize; // Ҷ�ӽڵ������
	private Map<String, Integer> nodesSonNodeSize = new HashMap<String, Integer>(); // ���ڵ����Ҷ�ӽڵ�ĸ���
	private int maxdepth; // ������
	private int depth; // ��ǰ���

	/**
	 * ��files�ļ�ת����excel���浽filePath�ļ�����
	 * 
	 * @param filePath
	 * @param files
	 */
	public void doXml2Excel(File filePath, File[] files) {
		for (File file : files) {
			try {
				System.out
						.println("#############################################");
				SAXReader sax = new SAXReader();// ����һ��SAXReader����
				Document document;
				document = sax.read(file);
				// ��ȡdocument����,����ĵ��޽ڵ㣬����׳�Exception��ǰ����
				Element root = document.getRootElement();// ��ȡ���ڵ�
				this.getNodes(root);// �Ӹ��ڵ㿪ʼ�������нڵ�
			} catch (DocumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	public void getNodes(Element node) {
		depth++; // ����һ���ڵ㵱ǰ��ȼ�һ
		System.out.println("---------��ȣ�"+depth+"-----------");
		
		List<Element> listElement = node.elements();// ��ǰ�ڵ������һ���ӽڵ��list
		// ��ǰ�ڵ�����ơ��ı����ݺ�����
		if (!node.isTextOnly()) {
			System.out.println("***��ǰ�ڵ�Ϊ"+node.getName()+"�����ӽڵ�����" + listElement.size());
		} else {
			System.out.println("��ǰ�ڵ����ƣ�" + node.getName());// ��ǰ�ڵ�����
			System.out.println("��ǰ�ڵ�����ݣ�" + node.getTextTrim());// ��ǰ�ڵ�����
		}
		
		List<Attribute> listAttr = node.attributes();// ��ǰ�ڵ���������Ե�list
		for (Attribute attr : listAttr) {// ������ǰ�ڵ����������
			String name = attr.getName();// ��������
			String value = attr.getValue();// ���Ե�ֵ
			System.out.println("�������ƣ�" + name + "����ֵ��" + value);
		}

		
		// �ݹ������ǰ�ڵ����е��ӽڵ�
		for (Element e : listElement) {// ��������һ���ӽڵ�
			this.getNodes(e);// �ݹ�
		}

		depth--; // �뿪һ���ڵ㵱ǰ��ȼ�һ
	}

}
