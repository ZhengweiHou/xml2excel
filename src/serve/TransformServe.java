package serve;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;

import javax.xml.crypto.Data;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
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

	private LinkedList<String> node_colIndex = new LinkedList<String>(); // 记录每个节点的位置
	private int depth; // 当前深度
	private int colIndex; // 当前列数
	private int rowIndex; // 当前行数
	private HSSFWorkbook workbook; // 声明一个工作簿
	private HSSFRow editRow; // 当前编辑行
	private String title = "NewExcel";
	private String msg = "0"; // 返回消息

	/**
	 * 将files文件转换成excel保存到filePath文件夹下
	 * 
	 * @param filePath
	 * @param files
	 */
	public String doXml2Excel(File filePath, File[] files) {
		this.getHeadInfo(files);
		this.createExcelHead();
		this.putData(files);
		this.saveExcel(filePath);
		return msg;
	}

	/**
	 * 获取表头信息
	 * 
	 * @param files
	 */
	public void getHeadInfo(File[] files) {
		for (File file : files) {
			colIndex = -1; // 每次遍历新文件前，重置列下标
			// 循环遍历所有文件
			try {
				SAXReader sax = new SAXReader();// 创建一个SAXReader对象
				Document document;
				document = sax.read(file);
				// 获取document对象,如果文档无节点，则会抛出Exception提前结束
				Element root = document.getRootElement();// 获取根节点
				this.getNodesInfo(root);// 从根节点开始遍历所有节点
			} catch (DocumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * 递归遍历所有节点,收集表头信息
	 * 
	 * @param node
	 */
	public void getNodesInfo(Element node) {
		depth++; // 进入一个节点当前深度加一

		// 当前节点的名称、文本内容和属性
		if (node.isTextOnly()) {
			colIndex++;
			if (node_colIndex.indexOf(node.getName()) == -1) {
				// 方案一：若集合中没有给节点则保存节点到当前位置
				// node_colIndex.add(colIndex, node.getName());

				// 方案二：若集合中没有给节点则保存节点到列尾
				node_colIndex.add(node.getName());
			}
		}

		// 递归遍历当前节点所有的子节点
		List<Element> listElement = node.elements();// 当前节点的所有一级子节点的list
		for (Element e : listElement) {// 遍历所有一级子节点
			this.getNodesInfo(e);// 递归
		}

		depth--; // 离开一个节点当前深度减一
	}

	/**
	 * 递归遍历所有节点,写出节点数据到表中
	 * 
	 * @param files
	 */
	public void putData(File[] files) {
		for (File file : files) {
			// 循环遍历所有文件
			rowIndex++; // 每次遍历行坐标加一
			editRow = workbook.getSheet(title).createRow(rowIndex);
			try {
				SAXReader sax = new SAXReader();// 创建一个SAXReader对象
				Document document;
				document = sax.read(file);
				// 获取document对象,如果文档无节点，则会抛出Exception提前结束
				Element root = document.getRootElement();// 获取根节点
				this.putNodesInfo(root);// 从根节点开始遍历所有节点
			} catch (DocumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			editRow = null;
		}
	}

	/**
	 * 递归遍历所有节点,收集表头信息
	 * 
	 * @param node
	 */
	public void putNodesInfo(Element node) {
		// 当前节点的名称、文本内容和属性
		if (node.isTextOnly()) {
			HSSFCell cell = editRow.createCell(node_colIndex.indexOf(node
					.getName()));

			HSSFRichTextString richString = new HSSFRichTextString(
					node.getTextTrim());
			// 设置字体样式
			// HSSFFont font3 = workbook.createFont();
			// font3.setColor(HSSFColor.BLUE.index);
			// richString.applyFont(font3);
			cell.setCellValue(richString);

		}

		// 递归遍历当前节点所有的子节点
		List<Element> listElement = node.elements();// 当前节点的所有一级子节点的list
		for (Element e : listElement) {// 遍历所有一级子节点
			this.putNodesInfo(e);// 递归
		}
	}

	/**
	 * 创建excel表格，并初始化表头
	 */
	public void createExcelHead() {
		// 声明一个工作薄
		workbook = new HSSFWorkbook();
		// 生成一个表格
		HSSFSheet sheet = workbook.createSheet(title);
		// 设置表格默认列宽度为15个字节
		sheet.setDefaultColumnWidth((short) 15);
		// 生成一个样式
		HSSFCellStyle style = workbook.createCellStyle();
		// 设置这些样式
		style.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);
		style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style.setBorderTop(HSSFCellStyle.BORDER_THIN);
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		// 生成一个字体
		HSSFFont font = workbook.createFont();
		font.setColor(HSSFColor.BLACK.index);
		font.setFontHeightInPoints((short) 12);
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		// 把字体应用到当前的样式
		style.setFont(font);

		// 产生表格标题行
		HSSFRow row = sheet.createRow(rowIndex);
		for (int i = 0; i < node_colIndex.size(); i++) {
			HSSFCell cell = row.createCell(i);
			cell.setCellStyle(style);
			HSSFRichTextString text = new HSSFRichTextString(node_colIndex.get(
					i).toString());
			cell.setCellValue(text);
		}
	}

	/**
	 * 保存excel文件
	 * 
	 * @param filePath
	 */
	public void saveExcel(File filePath) {
		OutputStream out = null;
		try {
			String path = filePath.getAbsolutePath() + "\\"
					+ title 
					+System.currentTimeMillis()
//					+ new Timestamp(System.currentTimeMillis())
					+ ".xls";
			out = new FileOutputStream(path);
			workbook.write(out);
			// System.out.println(filePath.getAbsolutePath()+"//all.xls");
			msg = path;

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			msg = "0";
//			e.printStackTrace();
		} catch (IOException e) {
			msg = "0";
//			e.printStackTrace();
		} finally {
			try {
				out.flush();
				out.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				msg = "0";
				e.printStackTrace();
			}

		}
	}
}
