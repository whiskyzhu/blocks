/**
 * Copyright 2010 ZTEsoft Inc. All Rights Reserved.
 *
 * This software is the proprietary information of ZTEsoft Inc.
 * Use is subject to license terms.
 * 
 * $Tracker List
 * 
 * $TaskId: $ $Date: 9:24:36 AM (May 9, 2008) $comments: create 
 * $TaskId: $ $Date: 3:56:36 PM (SEP 13, 2010) $comments: upgrade jvm to jvm1.5 
 *  
 *  
 */
package com.blocks.framework.utils.date;

import com.ztesoft.uboss.coomon.exception.BaseException;
import org.dom4j.*;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.xml.sax.InputSource;

import java.io.*;
import java.net.URL;
import java.util.Date;
import java.util.Iterator;
import java.util.List;


/**
 * XML文档组装和解析工具类 它不负责具体对象数据的组装和解析，交给它们具体的对象类实现。
 * 
 * 
 * 
 * <p>
 * Title: XML帮助类
 * 
 * 
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: ZTESoft
 * </p>
 * 
 * @author lu.zhen
 * @version 1.0
 */
public class XMLDom4jUtils {

	private static Document _DOC_TEMP = DocumentHelper.createDocument();

	/** 缺省字符集 */
	public static final String DEFAULT_ENCODING = "UTF-8";// Common.DEFAULT_CHARSET;

	/** 私有构造函数，阻止非法调用构造函数 */
	private XMLDom4jUtils() {
	}

	/**
	 * Return the child element with the given name. The element must be in the
	 * same name space as the parent element.
	 * 
	 * @param element
	 *            The parent element
	 * @param name
	 *            The child element name
	 * @return The child element
	 */
	public static Element child(Element element, String name) {
		return element.element(new QName(name, element.getNamespace()));
	}

	/**
	 * 得到给定结点下的子节点
	 * 
	 * 
	 * 
	 * @param element
	 *            节点
	 * @param name
	 *            子节点名称
	 * 
	 * 
	 * 
	 * @param optional
	 *            是否是可选的
	 * @return 子节点
	 * 
	 * 
	 * 
	 * @throws XMLDocException
	 * @throws BaseException
	 */
	public static Element child(Element element, String name, boolean optional)
			throws BaseException {
		Element child = element
				.element(new QName(name, element.getNamespace()));
		if (child == null && !optional) {
			throw new BaseException("UTIL-0001", name
					+ " element expected as child of " + element.getName()
					+ ".");
		}
		return child;
	}

	/**
	 * Return the child elements with the given name. The elements must be in
	 * the same name space as the parent element.
	 * 
	 * @param element
	 *            The parent element
	 * @param name
	 *            The child element name
	 * @return The child elements
	 */
	public static List<Element> children(Element element, String name) {
		return element.elements(new QName(name, element.getNamespace()));
	}

	/**
	 * 得到某个节点下的属性信息
	 * 
	 * 
	 * 
	 * @param element
	 *            节点
	 * @param name
	 *            属性名
	 * @param optional
	 *            是否是可选的
	 * @return 值
	 * 
	 * 
	 * 
	 * @throws XMLDocException
	 * @throws BaseException
	 */
	public static String getAttribute(Element element, String name,
			boolean optional) throws BaseException {
		Attribute attr = element.attribute(name);
		if (attr == null && !optional) {
			throw new BaseException("UTIL-0001", "Attribute " + name + " of "
					+ element.getName() + " expected.");
		} else if (attr != null) {
			return attr.getValue();
		} else {
			return null;
		}
	}

	/**
	 * 得到节点属性值，并且作为日期型返回
	 * 
	 * 
	 * 
	 * @param element
	 *            节点
	 * @param name
	 *            属性名
	 * @param optional
	 *            是否是可选的
	 * @return 值
	 * 
	 * 
	 * 
	 * @throws XMLDocException
	 * @throws BaseException
	 */
	public static Date getAttributeAsDate(Element element, String name,
			boolean optional) throws BaseException {
		String value = getAttribute(element, name, optional);
		if ((optional) && ((value == null) || (value.equals("")))) {
			return null;
		} else {
			try {
				return DateUtil.string2SQLDate(value);
			} catch (Exception exception) {
				throw new BaseException("UTIL-0001", element.getName() + "/@"
						+ name + " attribute: value format error.", exception);
			}
		}
	}

	/**
	 * 得到某个节点下的属性信息，值以字符串的形式返回
	 * 
	 * @param element
	 *            节点
	 * @param name
	 *            属性名
	 * @param optional
	 *            是否是可选的
	 * @return 值
	 * 
	 * 
	 * 
	 * @throws XMLDocException
	 * @throws BaseException
	 */
	public static String getAttributeAsString(Element element, String name,
			boolean optional) throws BaseException {
		return getAttribute(element, name, optional);
	}

	/**
	 * 得到某个节点下的属性信息，值以整数的形式返回。
	 * 
	 * 
	 * 
	 * 如果没有值或是转化为整形，那么抛出异常。
	 * 
	 * 
	 * 
	 * @param element
	 *            节点
	 * @param name
	 *            属性名
	 * @param optional
	 *            是否是可选的
	 * @return 值
	 * 
	 * 
	 * 
	 * @throws XMLDocException
	 * @throws BaseException
	 */
	public static int getAttributeAsInt(Element element, String name,
			boolean optional) throws BaseException {
		try {
			return Integer.parseInt(getAttribute(element, name, optional));
		} catch (NumberFormatException exception) {
			throw new BaseException("UTIL-0001", element.getName() + "/@"
					+ name + " attribute: value format error.", exception);
		}
	}

	/**
	 * 得到某个节点下的属性信息，值以整数的形式返回。
	 * 
	 * 
	 * 
	 * 如果该值是可选的，并且没有该值的话，就返回调用者提供缺省值。
	 * 
	 * 
	 * 
	 * @param element
	 *            节点
	 * @param name
	 *            属性名
	 * @param defaultValue
	 *            缺省值
	 * 
	 * 
	 * 
	 * @param optional
	 *            是否是可选的
	 * @return 值
	 * 
	 * 
	 * 
	 * @throws XMLDocException
	 * @throws BaseException
	 */
	public static int getAttributeAsInt(Element element, String name,
			int defaultValue, boolean optional) throws BaseException {
		String value = getAttribute(element, name, optional);
		if ((optional) && ((value == null) || (value.equals("")))) {
			return defaultValue;
		} else {
			try {
				return Integer.parseInt(value);
			} catch (NumberFormatException exception) {
				throw new BaseException("UTIL-0001", element.getName() + "/@"
						+ name + " attribute: value format error.", exception);
			}
		}
	}

	/**
	 * 得到某个节点下的属性信息，值以float的形式返回。
	 * 
	 * 
	 * 
	 * 如果没有值或是转化为float，那么抛出异常。
	 * 
	 * 
	 * 
	 * @param element
	 *            节点
	 * @param name
	 *            属性名
	 * @param optional
	 *            是否是可选的
	 * @return 值
	 * 
	 * 
	 * 
	 * @throws XMLDocException
	 * @throws BaseException
	 */
	public static float getAttributeAsFloat(Element element, String name,
			boolean optional) throws BaseException {
		try {
			return Float.parseFloat(getAttribute(element, name, optional));
		} catch (NumberFormatException exception) {
			throw new BaseException("UTIL-0001", element.getName() + "/@"
					+ name + " attribute: value format error.", exception);
		}
		
	}

	/**
	 * 得到某个节点下的属性信息，值以float的形式返回。
	 * 
	 * 
	 * 
	 * 如果没有值,返回缺省值；如果有，那么转化为float，如果不能转化那么抛出异常。
	 * 
	 * 
	 * 
	 * @param element
	 *            节点
	 * @param name
	 *            属性名
	 * @param defaultValue
	 *            缺省值
	 * 
	 * 
	 * 
	 * @param optional
	 *            是否是可选的
	 * @return 值
	 * 
	 * 
	 * 
	 * @throws XMLDocException
	 * @throws BaseException
	 */
	public static float getAttributeAsFloat(Element element, String name,
			float defaultValue, boolean optional) throws BaseException {
		String value = getAttribute(element, name, optional);
		if ((optional) && ((value == null) || (value.equals("")))) {
			return defaultValue;
		} else {
			try {
				return Float.parseFloat(value);
			} catch (NumberFormatException exception) {
				throw new BaseException("UTIL-0001", element.getName() + "/@"
						+ name + " attribute: value format error.", exception);
			}
		}
	}

	/**
	 * �õ�ĳ��ڵ��µ�������Ϣ��ֵ�Գ��������ʽ���ء� ���û��ֵ����ת��Ϊ���Σ���ô�׳��쳣��
	 * 
	 * 
	 * 
	 * @param element
	 *            �ڵ�
	 * @param name
	 *            ������
	 * @param optional
	 *            �Ƿ��ǿ�ѡ��
	 * @return ֵ
	 * @throws XMLDocException
	 * @throws BaseException
	 */
	public static long getAttributeAsLong(Element element, String name,
			boolean optional) throws BaseException {
		try {
			return Long.parseLong(getAttribute(element, name, optional));
		} catch (NumberFormatException exception) {
			throw new BaseException("UTIL-0001", element.getName() + "/@"
					+ name + " attribute: value format error.", exception);
		}
	}

	/**
	 * �õ�ĳ��ڵ��µ�������Ϣ��ֵ���������ʽ���ء�
	 * ����ֵ�ǿ�ѡ�ģ�����û�и�ֵ�Ļ����ͷ��ص������ṩȱʡֵ��
	 * 
	 * @param element
	 *            �ڵ�
	 * @param name
	 *            ������
	 * @param defaultValue
	 *            ȱʡֵ
	 * @param optional
	 *            �Ƿ��ǿ�ѡ��
	 * @return ֵ
	 * @throws XMLDocException
	 * @throws BaseException
	 */
	public static long getAttributeAsLong(Element element, String name,
			long defaultValue, boolean optional) throws BaseException {
		String value = getAttribute(element, name, optional);
		if ((optional) && ((value == null) || (value.equals("")))) {
			return defaultValue;
		} else {
			try {
				return Long.parseLong(value);
			} catch (NumberFormatException exception) {
				throw new BaseException("UTIL-0001", element.getName() + "/@"
						+ name + " attribute: value format error.", exception);
			}
		}
	}

	/**
	 * �õ�ĳ��ڵ��µ�ĳ���ֵĵ�һ���ӽڵ�
	 * 
	 * 
	 * 
	 * @param element
	 *            �ڵ�
	 * @param name
	 *            �ӽڵ����
	 * 
	 * 
	 * 
	 * @param optional
	 *            �Ƿ��ǿ�ѡ��
	 * @return ֵ
	 * @throws XMLDocException
	 * @throws BaseException
	 */
	public static Element getFirstChild(Element element, String name,
			boolean optional) throws BaseException {
		List list = element.elements(new QName(name, element
				.getNamespace()));
		// �����Ŀ����0����ôֱ��ȡ��һ��Ϳ�����

		if (list.size() > 0) {
			return (Element) list.get(0);
		} else {
			if (!optional) {
				throw new BaseException(
						"UTIL-0001",
						name + " element expected as first child of "
								+ element.getName() + ".");
			} else {
				return null;
			}
		}
	}

	/**
	 * �õ�ͬ���ֵܽڵ�,ͬ��ĵ�һ��ڵ㣬�������Լ�
	 *
	 * @param element
	 *            �ڵ�
	 * @param optional
	 *            �Ƿ��ǿ�ѡ��
	 * @return �ڵ�
	 * @throws XMLDocException
	 * @throws BaseException
	 */
	public static Element getSibling(Element element, boolean optional)
			throws BaseException {
		return getSibling(element, element.getName(), optional);
	}

	/**
	 * ����Ƶõ��ֵܽڵ�
	 *
	 *
	 *
	 * @param element
	 *            �ڵ�
	 * @param name
	 *            �ӽڵ����
	 *
	 *
	 *
	 * @param optional
	 *            �Ƿ��ǿ�ѡ��
	 * @return �ڵ�
	 * @throws XMLDocException
	 * @throws BaseException
	 */
	public static Element getSibling(Element element, String name,
			boolean optional) throws BaseException {
		List list = element.getParent().elements(name);
		if (list.size() > 0) {
			return (Element) list.get(0);
		} else {
			if (!optional) {
				throw new BaseException("UTIL-0001", name
						+ " element expected after " + element.getName() + ".");
			} else {
				return null;
			}
		}
	}

	/**
	 * �õ���ڵ��ֵ,���ַ���
	 *
	 * @param element
	 *            �ڵ�
	 * @param optional
	 *            �Ƿ��ǿ�ѡ��
	 * @return ֵ
	 * @throws XMLDocException
	 * @throws BaseException
	 */
	public static String getContent(Element element, boolean optional)
			throws BaseException {
		String content = element.getText();
		if (content == null && !optional) {
			throw new BaseException("UTIL-0001", element.getName()
					+ " element: content expected.");
		} else {
			return content;
		}
	}

	/**
	 * �õ���ڵ��ֵ,���ַ���
	 *
	 * @param element
	 *            �ڵ�
	 * @param optional
	 *            �Ƿ��ǿ�ѡ��
	 * @return ֵ
	 * @throws XMLDocException
	 * @throws BaseException
	 */
	public static String getContentAsString(Element element, boolean optional)
			throws BaseException {
		return getContent(element, optional);
	}

	/**
	 * �õ���ڵ��ֵ,���������ͷ���
	 *
	 * @param element
	 *            �ڵ�
	 * @param optional
	 *            �Ƿ��ǿ�ѡ��
	 * @return ֵ
	 * @throws XMLDocException
	 * @throws BaseException
	 */
	public static int getContentAsInt(Element element, boolean optional)
			throws BaseException {
		try {
			return Integer.parseInt(getContent(element, optional));
		} catch (NumberFormatException exception) {
			throw new BaseException("UTIL-0001", element.getName()
					+ " element: content format error.", exception);
		}
	}

	/**
	 * �õ���ڵ��ֵ,���������ͷ���
	 *
	 * @param element
	 *            �ڵ�
	 * @param defaultValue
	 *            ȱʡֵ
	 * @param optional
	 *            �Ƿ��ǿ�ѡ��
	 * @return ֵ
	 * @throws XMLDocException
	 * @throws BaseException
	 */
	public static int getContentAsInt(Element element, int defaultValue,
			boolean optional) throws BaseException {
		String value = getContent(element, optional);
		if ((optional) && (value == null || value.equals(""))) {
			return defaultValue;
		} else {
			try {
				return Integer.parseInt(value);
			} catch (NumberFormatException exception) {
				throw new BaseException("UTIL-0001", element.getName()
						+ " element: content format error.", exception);
			}
		}
	}

	/**
	 * �õ���ڵ��ֵ,�Գ������ͷ���
	 *
	 * @param element
	 *            �ڵ�
	 * @param optional
	 *            �Ƿ��ǿ�ѡ��
	 * @return ֵ
	 * @throws XMLDocException
	 * @throws BaseException
	 */
	public static long getContentAsLong(Element element, boolean optional)
			throws BaseException {
		try {
			return Long.parseLong(getContent(element, optional));
		} catch (NumberFormatException exception) {
			throw new BaseException("UTIL-0001", element.getName()
					+ " element: content format error.", exception);
		}
	}

	/**
	 * �õ���ڵ��ֵ,���������ͷ���
	 *
	 * @param element
	 *            �ڵ�
	 * @param defaultValue
	 *            ȱʡֵ
	 * @param optional
	 *            �Ƿ��ǿ�ѡ��
	 * @return ֵ
	 * @throws XMLDocException
	 * @throws BaseException
	 */
	public static long getContentAsLong(Element element, long defaultValue,
			boolean optional) throws BaseException {
		String value = getContent(element, optional);
		if ((optional) && (value == null || value.equals(""))) {
			return defaultValue;
		} else {
			try {
				return Long.parseLong(value);
			} catch (NumberFormatException exception) {
				throw new BaseException("UTIL-0001", element.getName()
						+ " element: content format error.", exception);
			}
		}
	}

	/**
	 * �õ���ڵ��ֵ,�Ը������ͷ���
	 *
	 * @param element
	 *            �ڵ�
	 * @param optional
	 *            �Ƿ��ǿ�ѡ��
	 * @return ֵ
	 * @throws XMLDocException
	 * @throws BaseException
	 */
	public static float getContentAsFloat(Element element, boolean optional)
			throws BaseException {
		try {
			return Float.parseFloat(getContent(element, optional));
		} catch (NumberFormatException exception) {
			throw new BaseException("UTIL-0001", element.getName()
					+ " element: content format error.", exception);
		}
	}

	/**
	 * �õ���ڵ��ֵ,�Ը������ͷ���
	 *
	 * @param element
	 *            �ڵ�
	 * @param defaultValue
	 *            ȱʡֵ
	 * @param optional
	 *            �Ƿ��ǿ�ѡ��
	 * @return ֵ
	 * @throws XMLDocException
	 * @throws BaseException
	 */
	public static float getContentAsFloat(Element element, float defaultValue,
			boolean optional) throws BaseException {
		String value = getContent(element, optional);
		if ((optional) && (value == null || value.equals(""))) {
			return defaultValue;
		} else {
			try {
				return Float.parseFloat(value);
			} catch (NumberFormatException exception) {
				throw new BaseException("UTIL-0001", element.getName()
						+ " element: content format error.", exception);
			}
		}
	}

	/**
	 * �õ���ڵ��ֵ,���������ͷ���
	 *
	 * @param element
	 *            �ڵ�
	 * @param optional
	 *            �Ƿ��ǿ�ѡ��
	 * @return ֵ
	 * @throws XMLDocException
	 * @throws BaseException
	 */
	public static Date getContentAsDate(Element element,
			boolean optional) throws BaseException {
		String value = getContent(element, optional);
		if ((optional) && (value == null || value.equals(""))) {
			return null;
		} else {
			try {
				return DateUtil.string2SQLDate(value);
			} catch (Exception exception) {
				throw new BaseException("UTIL-0001", element.getName()
						+ " element: content format error.", exception);
			}
		}
	}

	/**
	 * ���ڵ���ӽڵ���ƣ��õ��ӽڵ�ֵ
	 *
	 * @param root
	 *            ���ڵ�
	 * @param subTagName
	 *            �ӽڵ�
	 * @return ֵ
	 */
	public static String getSubTagValue(Element root, String subTagName) {
		String returnString = root.elementText(subTagName);
		return returnString;
	}

	/**
	 * ���ڵ㣬�ӽڵ���ƣ���ڵ���ƣ��õ�ֵ
	 *
	 *
	 *
	 * @param root
	 *            ���ڵ�
	 * @param tagName
	 *            �ӽڵ����
	 *
	 *
	 *
	 * @param subTagName
	 *            ��ڵ����
	 * @return ֵ
	 */
	public static String getSubTagValue(Element root, String tagName,
			String subTagName) {
		Element child = root.element(tagName);
		String returnString = child.elementText(subTagName);
		return returnString;
	}

	/**
	 * ��Element�ڵ㣬ֵΪString����
	 *
	 * @param parent
	 *            ���ڵ�
	 * @param name
	 *            �½ڵ����
	 *
	 *
	 *
	 * @param value
	 *            �½ڵ�ֵ
	 * @return element
	 * @throws XMLDocException
	 */
	public static Element appendChild(Element parent, String name, String value) {
		Element element = parent.addElement(new QName(name, parent
				.getNamespace()));
		if (value != null) {
			element.addText(value);
		}
		return element;
	}

	/**
	 * �����Element�ڵ㣬��ֵ
	 *
	 * @param parent
	 *            ���ڵ�
	 * @param name
	 *            �½ڵ����
	 *
	 *
	 *
	 * @return Element �½��ڵ�
	 * @throws XMLDocException
	 */
	public static Element appendChild(Element parent, String name) {
		return parent.addElement(new QName(name, parent.getNamespace()));
	}

	/**
	 * �����Element�ڵ㣬ֵΪint����
	 *
	 * @param parent
	 *            ���ڵ�
	 * @param name
	 *            �½ڵ����
	 *
	 *
	 *
	 * @param value
	 *            �½ڵ�ֵ
	 * @return element
	 * @throws XMLDocException
	 */
	public static Element appendChild(Element parent, String name, int value) {
		return appendChild(parent, name, String.valueOf(value));
	}

	/**
	 * �����Element�ڵ㣬ֵΪ������
	 *
	 * @param parent
	 *            ���ڵ�
	 * @param name
	 *            �½ڵ����
	 *
	 *
	 *
	 * @param value
	 *            �½ڵ�ֵ
	 * @return element
	 * @throws XMLDocException
	 */
	public static Element appendChild(Element parent, String name, long value) {
		return appendChild(parent, name, String.valueOf(value));
	}

	/**
	 * �¼�һ��floatֵ���͵Ľڵ㣬ֵΪ������
	 *
	 * @param parent
	 *            ���ڵ�
	 * @param name
	 *            �½ڵ�����
	 * @param value
	 *            �½ڵ��ֵ
	 *
	 *
	 *
	 * @return element
	 * @throws XMLDocException
	 */
	public static Element appendChild(Element parent, String name, float value) {
		return appendChild(parent, name, String.valueOf(value));
	}

	/**
	 * �����Element�ڵ㣬ֵΪ������
	 *
	 * @param parent
	 *            ���ڵ�
	 * @param name
	 *            �½ڵ����
	 *
	 *
	 *
	 * @param value
	 *            �½ڵ�ֵ
	 * @return element
	 * @throws XMLDocException
	 */
	public static Element appendChild(Element parent, String name,
			Date value) {
		return appendChild(parent, name, DateUtil.date2String(value));
	}

	/**
	 * ����ĵ�dtd�����Ƿ���ȷ
	 *
	 * @param document
	 *            �ĵ��ڵ�
	 * @param dtdPublicId
	 *            dtd����
	 * @return boolean ��ͬ����true,����false
	 */
	public static boolean checkDocumentType(Document document,
			String dtdPublicId) {
		DocumentType documentType = document.getDocType();
		if (documentType != null) {
			String publicId = documentType.getPublicID();
			return publicId != null && publicId.equals(dtdPublicId);
		}
		return true;
	}

	/**
	 * �½��ĵ�
	 *
	 * @return Document �ĵ��ڵ�
	 * @throws XMLDocException
	 */
	public static Document createDocument() {
		DocumentFactory factory = new DocumentFactory();
		Document document = factory.createDocument();
		return document;
	}

	/**
	 * ͨ��Reader��ȡDocument�ĵ� ���encodingStrΪnull����""����ô����ȱʡ����GB2312
	 *
	 * @param in
	 *            Reader��
	 * @param encoding
	 *            ������
	 * @return documment
	 * @throws XMLDocException
	 * @throws BaseException
	 */
	public static Document fromXML(Reader in, String encoding)
			throws BaseException {
		try {
			if (encoding == null || encoding.equals("")) {
				encoding = DEFAULT_ENCODING;
			}
			SAXReader reader = new SAXReader();
            InputSource source = new InputSource(in);
            source.setEncoding(encoding);
            Document document = reader.read(source);
			document.setXMLEncoding(encoding);
			return document;
		} catch (Exception ex) {
			throw new BaseException("UTIL-0001", ex);
		}


	}

	/**
	 * ���������ȡXML��Document�� ���encodingStrΪnull����""����ô����ȱʡ����GB2312
	 *
	 * @param inputSource
	 *            ����Դ
	 * @param encoding
	 *            ������
	 * @return document
	 * @throws XMLDocException
	 * @throws BaseException
	 */
	public static Document fromXML(InputStream inputSource, String encoding)
			throws BaseException {
		try {
			if (encoding == null || encoding.equals("")) {
				encoding = DEFAULT_ENCODING;
			}
			SAXReader reader = new SAXReader();
			Document document = reader.read(inputSource);
			document.setXMLEncoding(encoding);
			return document;
		} catch (Exception ex) {
			throw new BaseException("UTIL-0001", ex);
		}
	}

	/**
	 * 直接从字符串得到XML的Document
	 *
	 * @param source
	 *            把一个字符串文本转化为XML的Document对象
	 * @param encoding
	 *            编码器
	 *
	 *
	 *
	 * @return <code>Document</code>
	 * @throws XMLDocException
	 * @throws BaseException
	 */
	public static Document fromXML(String source, String encoding)
			throws BaseException {
		return fromXML(new StringReader(source), encoding);
	}

	/**
	 * 把XML的Document转化为java.io.Writer输出流
	 *
	 *
	 *
	 * 不支持给定Schema文件的校验
	 *
	 *
	 *
	 * @param document
	 *            XML文档
	 * @param outWriter
	 *            输出写入器
	 *
	 *
	 *
	 * @param encoding
	 *            编码类型
	 * @throws XMLDocException
	 *             如果有任何异常转化为该异常输出
	 *
	 *
	 * @throws BaseException
	 *
	 */
	public static void toXML(Document document, java.io.Writer outWriter,
			String encoding) throws BaseException {
		//
		OutputFormat outformat = OutputFormat.createPrettyPrint();
		if (encoding == null || encoding.trim().equals("")) {
			encoding = DEFAULT_ENCODING;
		}
		// 设置编码类型
		outformat.setEncoding(encoding);
		XMLWriter xmlWriter = null;
		try {
			xmlWriter = new XMLWriter(outWriter, outformat);
			xmlWriter.write(document);
			xmlWriter.flush();
		} catch (IOException ex) {
			throw new BaseException("UTIL-0001", ex);
		} finally {
			if (xmlWriter != null) {
				try {
					xmlWriter.close();
				} catch (IOException ex) {
				}
			}
		}
	}

	/**
	 * 把XML的Document转化为java.io.Writer输出流
	 *
	 *
	 *
	 * 不支持给定Schema文件的校验
	 *
	 *
	 *
	 * @param document
	 *            XML文档
	 * @param outStream
	 *            输出写入器
	 *
	 *
	 *
	 * @param encoding
	 *            编码类型
	 * @throws XMLDocException
	 *             如果有任何异常转化为该异常输出
	 *
	 *
	 * @throws BaseException
	 *
	 */
	public static void toXML(Document document, java.io.OutputStream outStream,
			String encoding) throws BaseException {
		//
		OutputFormat outformat = new OutputFormat();
		outformat.setIndentSize(0);
		outformat.setNewlines(true);
		outformat.setTrimText(true);

		// OutputFormat outformat = OutputFormat.createPrettyPrint();
		if (encoding == null || encoding.trim().equals("")) {
			encoding = DEFAULT_ENCODING;
		}
		// 设置编码类型
		outformat.setEncoding(encoding);
		XMLWriter xmlWriter = null;
		try {
			xmlWriter = new XMLWriter(new OutputStreamWriter(outStream),
					outformat);
			xmlWriter.write(document);
			xmlWriter.flush();
		} catch (IOException ex) {
			throw new BaseException("UTIL-0001", ex);
		} finally {
			if (xmlWriter != null) {
				try {
					xmlWriter.close();
				} catch (IOException ex) {
				}
			}
		}
	}

	public static void element2XML(Element element,
			java.io.OutputStream outStream, String encoding)
			throws BaseException {
		//
		OutputFormat outformat = new OutputFormat();
		outformat.setIndentSize(0);
		outformat.setNewlines(false);
		outformat.setTrimText(true);

		// OutputFormat outformat = OutputFormat.createPrettyPrint();
		if (encoding == null || encoding.trim().equals("")) {
			encoding = DEFAULT_ENCODING;
		}
		// 设置编码类型
		outformat.setEncoding(encoding);
		XMLWriter xmlWriter = null;
		try {
			xmlWriter = new XMLWriter(new OutputStreamWriter(outStream),
					outformat);
			xmlWriter.write(element);
			xmlWriter.flush();
		} catch (IOException ex) {
			throw new BaseException("UTIL-0001", ex);
		} finally {
			if (xmlWriter != null) {
				try {
					xmlWriter.close();
				} catch (IOException ex) {
				}
			}
		}
	}

	public static String element2XML(Element element, String encoding)
			throws BaseException {
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		element2XML(element, stream, encoding);
		if (stream != null) {
			try {
				stream.close();
			} catch (IOException ex) {
			}
		}
		return new String(stream.toByteArray());
	}

	/**
	 * 把XML文档转化为String返回
	 *
	 * @param document
	 *            要转化的XML的Document
	 * @param encoding
	 *            编码类型
	 * @return <code>String</code>
	 * @throws XMLDocException
	 *             如果有任何异常转化为该异常输出
	 *
	 *
	 * @throws BaseException
	 *
	 */
	public static byte[] toXML(Document document, String encoding)
			throws BaseException {
		ByteArrayOutputStream stream = new ByteArrayOutputStream();

		toXML(document, stream, encoding);
		if (stream != null) {
			try {
				stream.close();
			} catch (IOException ex) {
			}
		}
		return stream.toByteArray();
	}

	/**
	 * Create dom4j document from xmlSource
	 *
	 * @param xmlSource
	 *            URL、XML字符串、XML文件名等 Object
	 * @param validate
	 *            boolean
	 * @param encoding
	 *            String
	 * @throws DocumentException
	 * @throws IOException
	 * @return Document
	 * @throws BaseException
	 */
	public static Document createDocument(Object xmlSource, boolean validate,
			String encoding) throws BaseException {

		// Use xerces and validate XML file
		if (xmlSource instanceof Document)
			return (Document) xmlSource;
		Document doc = null;
		SAXReader saxReader = new SAXReader(true);
		saxReader.setValidation(validate);
		if (encoding == null || encoding.equals("")) {
			encoding = DEFAULT_ENCODING;
		}

		// Check input source type
		if (xmlSource instanceof StringBuffer)
			xmlSource = ((StringBuffer) xmlSource).toString();

		try {
			if (xmlSource instanceof String) {
				if (((String) xmlSource).startsWith("<")) {
					// Treat the String as XML code
					StringReader reader = new StringReader(xmlSource.toString());
					DocumentHelper.parseText(xmlSource.toString());
					doc = saxReader.read(reader, encoding);
				} else {
					doc = saxReader.read(new File((String) xmlSource));
				}
			} else if (xmlSource instanceof File) {
				doc = saxReader.read((File) xmlSource);
			} else if (xmlSource instanceof InputStream) {
				doc = saxReader.read((InputStream) xmlSource);
			} else if (xmlSource instanceof Reader) {
				doc = saxReader.read((Reader) xmlSource);
			} else if (xmlSource instanceof URL) {
				doc = saxReader.read((URL) xmlSource);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new BaseException("UTIL-0001", ex);
		}

		return doc;
	}

	// 自测试代码

	public static void main(String[] args) {
		try {

			Document document = XMLDom4jUtils.createDocument();
			Element root = document.getRootElement();

			String test02 = "中华人民共和国";
			String test2 = "PHS(小灵通)";

			Document doc = XMLDom4jUtils.createDocument();
			root = doc.addElement("address");
			for (int i = 0; i < 1; i++) {
				XMLDom4jUtils.appendChild(root, "name", test02);
				XMLDom4jUtils.appendChild(root, "city", "nj");
			}
			XMLDom4jUtils.appendChild(root, "state", test2);
			XMLDom4jUtils.appendChild(root, "sysDate", new Date());
			XMLDom4jUtils.appendChild(root, "intValue", 100);
			XMLDom4jUtils.toXML(doc, System.out, "utf-8");
			 
			System.out.println("-------------------");
//			XMLDom4jUtils.element2XML(root, System.out, "utf-8");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * This method will generate XML file in a StringBuffer based on the given
	 * Dom4j object.
	 * 
	 * @param xmlObj
	 *            Object
	 * @param encoding
	 *            String
	 * @throws IOException
	 * @return StringBuffer
	 * @throws BaseException
	 */
	public static StringBuffer generateXMLStringBuffer(Object xmlObj,
			String encoding) throws BaseException {
		StringWriter writer = new StringWriter();
		OutputFormat outformat = OutputFormat.createPrettyPrint();

		// 设置编码类型
		if (encoding == null || encoding.trim().equals("")) {
			encoding = DEFAULT_ENCODING;
		}
		outformat.setEncoding(encoding);

		// dom4j 支持直接输出OBJECT
		XMLWriter xmlWriter = null;
		xmlWriter = new XMLWriter(writer, outformat);

		try {
			xmlWriter.write(xmlObj);
			xmlWriter.flush();
		} catch (Exception ex) {
			throw new BaseException("UTIL-0002", ex);
		}

		return writer.getBuffer();
	}

	/**
	 * 把XML信息输出到文件中
	 * 
	 * @param xmlObj
	 * @param encoding
	 * @param filename
	 * @return
	 * @throws BaseException
	 */
	public static boolean generateXMLFile(Object xmlObj, String encoding,
			String filename) throws BaseException {
		FileWriter writer = null;
		OutputFormat outformat = OutputFormat.createPrettyPrint();

		// 设置编码类型
		if (encoding == null || encoding.trim().equals("")) {
			encoding = DEFAULT_ENCODING;
		}
		outformat.setEncoding(encoding);

		// dom4j 支持直接输出OBJECT
		try {
			writer = new FileWriter(filename);
			XMLWriter xmlWriter = null;
			xmlWriter = new XMLWriter(writer, outformat);
			xmlWriter.write(xmlObj);
			xmlWriter.flush();
		} catch (Exception ex) {
			throw new BaseException("UTIL-0004", ex);
		} finally {
			if (writer != null)
				try {
					writer.close();
				} catch (IOException e) {
				}
		}

		return true;
	}

	/**
	 * 取Element节点下的所有子节点内容，转换成字符串
	 * 
	 * 
	 * 
	 * 
	 * 
	 * @param element
	 *            Element
	 * @return String
	 */
	public static String getElementContext(Element element) {
		if (element == null) {
			return null;
		}
		String str = element.getText();
		Element tmp = null;
		for (Iterator i = element.elementIterator(); i.hasNext();) {
			tmp = (Element) i.next();
			str = str + tmp.asXML();
			// do something
		}
		return str;
	}

	/**
	 * 
	 * @param element
	 *            Element
	 * @param path
	 *            String
	 * @param attr
	 *            String
	 * @return String
	 */
	public static String getElementContext(Element element, String path,
			String attr) {
		Element el = element.element(path);
		if (attr == null || attr.trim().equals("")) {
			return el.getText();
		} else {
			return el.attributeValue(attr);
		}
	}

	/**
	 * 根据XPATH 获取元素内容，text。 "/path/@seq" 获取属性值 "/path/" 获取元素
	 * 
	 * @param element
	 *            Element
	 * @param path
	 *            String
	 * @return String
	 * @throws BaseException
	 */
	public static String getElementContext(Element element, String path)
			throws BaseException {
		if (element == null || path == null) {
			return null;
		}

		Object o = element.selectSingleNode(path);
		if (o == null) { // 无此节点
			return null;
		}

		if (o instanceof Element) { // 1、元素 Element
			Element e = (Element) o;
			if (e.isTextOnly()) { // text only
				return e.getText();
			} else { // element
				return generateXMLStringBuffer(e, "").toString();
			}
		} else if (o instanceof Attribute) { // 2、属性 Attribute
			return ((Attribute) o).getValue();
		} else { // 3、其他 Other node
			return generateXMLStringBuffer(o, "").toString();
		}
	}

	/**
	 * 根据XPATH 获取元素内容，text。 "/path/@seq" 获取属性值 "/path/" 获取元素
	 * 
	 * @param element
	 *            Element
	 * @param path
	 *            String
	 * @return String
	 * @throws BaseException
	 */
	public static String[] getElementContextArray(Element element, String path)
			throws BaseException {
		if (element == null || path == null) {
			return null;
		}
		List nodes = element.selectNodes(path);
		String[] eleContext = new String[nodes.size()];
		Iterator iter = nodes.iterator();
		int length = 0;
		Object o = null;
		Element e = null;
		while (iter.hasNext()) {
			o = (Object) iter.next();
			if (o instanceof Element) { // 1、元素 Element
				e = (Element) o;
				if (e.isTextOnly()) { // text only
					eleContext[length] = e.getText();
					length++;
				} else { // element
					eleContext[length] = generateXMLStringBuffer(e, "")
							.toString();
					length++;
				}
			} else if (o instanceof Attribute) { // 2、属性 Attribute
				eleContext[length] = ((Attribute) o).getValue();
				length++;
			} else { // 3、其他 Other node
				eleContext[length] = generateXMLStringBuffer(o, "").toString();
				length++;
			}
		}

		return eleContext;
	}

	/**
	 * 添加属性
	 * 
	 * 
	 * 
	 * 
	 * @param ele
	 *            Element
	 * @param attributeName
	 *            String
	 * @param attributeValue
	 *            String
	 * @return Element
	 */
	public static Element appendAttribute(Element ele, String attributeName,
			String attributeValue) {
		if (ele == null) {
			return null;
		}

		ele.addAttribute(attributeName, attributeValue);
		return ele;
	}

	/**
	 * 删除属性
	 * 
	 * 
	 * 
	 * 
	 * @param ele
	 *            Element
	 * @param attributeName
	 *            String
	 * @return Element
	 */
	public static Element removeAttribute(Element ele, String attributeName) {
		if (ele == null) {
			return null;
		}

		Attribute att = ele.attribute(attributeName);
		ele.remove(att);
		return ele;
	}

	/**
	 * 修改属性
	 * 
	 * 
	 * 
	 * 
	 * @param ele
	 *            Element
	 * @param attributeName
	 *            String
	 * @param attributeValue
	 *            String
	 * @return Element
	 */
	public static Element setAttribute(Element ele, String attributeName,
			String attributeValue) {
		if (ele == null) {
			return null;
		}

		Attribute att = ele.attribute(attributeName);
		if (att != null) {
			att.setText(attributeValue);
		} else {
			appendAttribute(ele, attributeName, attributeValue);
		}
		return ele;
	}
} // end of class
