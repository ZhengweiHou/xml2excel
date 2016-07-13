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
 * �ļ�ת������
 * 
 * @author houzw
 * 
 */
public class TransformServe {

	private LinkedList<String> node_colIndex = new LinkedList<String>(); // ��¼ÿ���ڵ��λ��
	private int depth; // ��ǰ���
	private int colIndex; // ��ǰ����
	private int rowIndex; // ��ǰ����
	private HSSFWorkbook workbook; // ����һ��������
	private HSSFRow editRow; // ��ǰ�༭��
	private String title = "NewExcel";
	private String msg = "0"; // ������Ϣ

	/**
	 * ��files�ļ�ת����excel���浽filePath�ļ�����
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
	 * ��ȡ��ͷ��Ϣ
	 * 
	 * @param files
	 */
	public void getHeadInfo(File[] files) {
		for (File file : files) {
			colIndex = -1; // ÿ�α������ļ�ǰ���������±�
			// ѭ�����������ļ�
			try {
				SAXReader sax = new SAXReader();// ����һ��SAXReader����
				Document document;
				document = sax.read(file);
				// ��ȡdocument����,����ĵ��޽ڵ㣬����׳�Exception��ǰ����
				Element root = document.getRootElement();// ��ȡ���ڵ�
				this.getNodesInfo(root);// �Ӹ��ڵ㿪ʼ�������нڵ�
			} catch (DocumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * �ݹ�������нڵ�,�ռ���ͷ��Ϣ
	 * 
	 * @param node
	 */
	public void getNodesInfo(Element node) {
		depth++; // ����һ���ڵ㵱ǰ��ȼ�һ

		// ��ǰ�ڵ�����ơ��ı����ݺ�����
		if (node.isTextOnly()) {
			colIndex++;
			if (node_colIndex.indexOf(node.getName()) == -1) {
				// ����һ����������û�и��ڵ��򱣴�ڵ㵽��ǰλ��
				// node_colIndex.add(colIndex, node.getName());

				// ����������������û�и��ڵ��򱣴�ڵ㵽��β
				node_colIndex.add(node.getName());
			}
		}

		// �ݹ������ǰ�ڵ����е��ӽڵ�
		List<Element> listElement = node.elements();// ��ǰ�ڵ������һ���ӽڵ��list
		for (Element e : listElement) {// ��������һ���ӽڵ�
			this.getNodesInfo(e);// �ݹ�
		}

		depth--; // �뿪һ���ڵ㵱ǰ��ȼ�һ
	}

	/**
	 * �ݹ�������нڵ�,д���ڵ����ݵ�����
	 * 
	 * @param files
	 */
	public void putData(File[] files) {
		for (File file : files) {
			// ѭ�����������ļ�
			rowIndex++; // ÿ�α����������һ
			editRow = workbook.getSheet(title).createRow(rowIndex);
			try {
				SAXReader sax = new SAXReader();// ����һ��SAXReader����
				Document document;
				document = sax.read(file);
				// ��ȡdocument����,����ĵ��޽ڵ㣬����׳�Exception��ǰ����
				Element root = document.getRootElement();// ��ȡ���ڵ�
				this.putNodesInfo(root);// �Ӹ��ڵ㿪ʼ�������нڵ�
			} catch (DocumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			editRow = null;
		}
	}

	/**
	 * �ݹ�������нڵ�,�ռ���ͷ��Ϣ
	 * 
	 * @param node
	 */
	public void putNodesInfo(Element node) {
		// ��ǰ�ڵ�����ơ��ı����ݺ�����
		if (node.isTextOnly()) {
			HSSFCell cell = editRow.createCell(node_colIndex.indexOf(node
					.getName()));

			HSSFRichTextString richString = new HSSFRichTextString(
					node.getTextTrim());
			// ����������ʽ
			// HSSFFont font3 = workbook.createFont();
			// font3.setColor(HSSFColor.BLUE.index);
			// richString.applyFont(font3);
			cell.setCellValue(richString);

		}

		// �ݹ������ǰ�ڵ����е��ӽڵ�
		List<Element> listElement = node.elements();// ��ǰ�ڵ������һ���ӽڵ��list
		for (Element e : listElement) {// ��������һ���ӽڵ�
			this.putNodesInfo(e);// �ݹ�
		}
	}

	/**
	 * ����excel��񣬲���ʼ����ͷ
	 */
	public void createExcelHead() {
		// ����һ��������
		workbook = new HSSFWorkbook();
		// ����һ�����
		HSSFSheet sheet = workbook.createSheet(title);
		// ���ñ��Ĭ���п��Ϊ15���ֽ�
		sheet.setDefaultColumnWidth((short) 15);
		// ����һ����ʽ
		HSSFCellStyle style = workbook.createCellStyle();
		// ������Щ��ʽ
		style.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);
		style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style.setBorderTop(HSSFCellStyle.BORDER_THIN);
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		// ����һ������
		HSSFFont font = workbook.createFont();
		font.setColor(HSSFColor.BLACK.index);
		font.setFontHeightInPoints((short) 12);
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		// ������Ӧ�õ���ǰ����ʽ
		style.setFont(font);

		// ������������
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
	 * ����excel�ļ�
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
