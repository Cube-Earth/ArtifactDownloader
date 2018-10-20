package earth.cube.tools.artifactdownloader.utils;

import javax.xml.namespace.QName;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XmlUtil {

	public static String getText(Element parent, String sXPath, String sDefault) {
		Node node = selectSingleNode(parent, sXPath, false);
		return node == null ? sDefault : node.getTextContent().trim();
	}
	
	protected static Object evaluate(Object context, String sXPath, QName returnType) {
		try {
			XPathFactory xPathfactory = XPathFactory.newInstance();
			XPath xpath = xPathfactory.newXPath();
			XPathExpression expr = xpath.compile(sXPath);
			Object result = expr.evaluate(context, returnType);
			return result;
		} catch (XPathExpressionException e) {
			throw new IllegalStateException(e);
		}
	}

	public static Element selectSingleNode(Element parent, String sXPath, boolean bCheckExisting) {
		Node node = (Node) evaluate(parent, sXPath, XPathConstants.NODE);
		if(bCheckExisting && node == null)
			throw new IllegalStateException("No elements with XPath '" + sXPath + "' found!");
		return (Element) node;
	}

	public static Element selectSingleNode(Element parent, String sXPath) {
		return selectSingleNode(parent, sXPath, true);
	}

	public static XmlNodeList selectNodes(Element parent, String sXPath) {
		return new XmlNodeList((NodeList) evaluate(parent, sXPath, XPathConstants.NODESET));
	}
}
