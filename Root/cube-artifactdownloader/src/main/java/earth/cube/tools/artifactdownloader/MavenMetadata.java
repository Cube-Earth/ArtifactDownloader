package earth.cube.tools.artifactdownloader;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import earth.cube.tools.artifactdownloader.utils.XmlUtil;

public class MavenMetadata {
	
	private Repository _repository;
	private String _sTimeStamp;
	private String _sBuildNo;

	public MavenMetadata(Repository repository, InputStream in) throws IOException {
		_repository = repository;
		read(in);
	}

	public void read(InputStream in) throws IOException {
		Document doc = null;
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			doc = builder.parse(in);
			doc.normalize();
		}
		catch(IOException | SAXException | ParserConfigurationException e) {
			throw new IOException(e);
		}
		Element root = doc.getDocumentElement();
		read(root);
	}

	public void read(Element config) {
		Element snapshot = XmlUtil.selectSingleNode(config, "versioning/snapshot");
		_sTimeStamp = XmlUtil.getText(snapshot, "timestamp", null);
		_sBuildNo = XmlUtil.getText(snapshot, "buildNumber", null);
	}
	
	public String getSnapshotId() {
		return _sTimeStamp + "-" + _sBuildNo;
	}
	
}
