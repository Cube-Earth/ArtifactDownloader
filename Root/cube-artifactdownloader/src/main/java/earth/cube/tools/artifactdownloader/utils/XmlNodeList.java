package earth.cube.tools.artifactdownloader.utils;

import java.util.Iterator;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XmlNodeList implements Iterable<Node>, Iterator<Node> {
	
	private NodeList _nodes;
	private int _nCount;
	private int _nIdx;
	
	public XmlNodeList(NodeList nodes) {
		_nodes = nodes;
		_nCount = nodes.getLength();
	}

	@Override
	public Iterator<Node> iterator() {
		return this;
	}

	@Override
	public boolean hasNext() {
		return _nIdx < _nCount;
	}

	@Override
	public Node next() {
		return _nodes.item(_nIdx++);
	}

}
