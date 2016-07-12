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
 * 文件转换服务
 * 
 * @author houzw
 * 
 */
public class TransformServe {

	private int leafSize; // 叶子节点最大数
	private Map<String, Integer> nodesSonNodeSize = new HashMap<String, Integer>(); // 各节点的子叶子节点的个数
	private int maxdepth; // 最大深度
	private int depth; // 当前深度

	/**
	 * 将files文件转换成excel保存到filePath文件夹下
	 * 
	 * @param filePath
	 * @param files
	 */
	public void doXml2Excel(File filePath, File[] files) {
		for (File file : files) {
			try {
				System.out
						.println("#############################################");
				SAXReader sax = new SAXReader();// 创建一个SAXReader对象
				Document document;
				document = sax.read(file);
				// 获取document对象,如果文档无节点，则会抛出Exception提前结束
				Element root = document.getRootElement();// 获取根节点
				this.getNodes(root);// 从根节点开始遍历所有节点
			} catch (DocumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	public void getNodes(Element node) {
		depth++; // 进入一个节点当前深度加一
		System.out.println("---------深度："+depth+"-----------");
		
		List<Element> listElement = node.elements();// 当前节点的所有一级子节点的list
		// 当前节点的名称、文本内容和属性
		if (!node.isTextOnly()) {
			System.out.println("***当前节点为"+node.getName()+"含有子节点数：" + listElement.size());
		} else {
			System.out.println("当前节点名称：" + node.getName());// 当前节点名称
			System.out.println("当前节点的内容：" + node.getTextTrim());// 当前节点名称
		}
		
		List<Attribute> listAttr = node.attributes();// 当前节点的所有属性的list
		for (Attribute attr : listAttr) {// 遍历当前节点的所有属性
			String name = attr.getName();// 属性名称
			String value = attr.getValue();// 属性的值
			System.out.println("属性名称：" + name + "属性值：" + value);
		}

		
		// 递归遍历当前节点所有的子节点
		for (Element e : listElement) {// 遍历所有一级子节点
			this.getNodes(e);// 递归
		}

		depth--; // 离开一个节点当前深度减一
	}

}
