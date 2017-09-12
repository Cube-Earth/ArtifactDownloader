package earth.cube.tools.artifactdownloader;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import earth.cube.tools.artifactdownloader.utils.XmlUtil;

public class Project {
	
	private Artifact _artifact;
	private List<Dependency> _dependencies = new ArrayList<>();
	private Repository _repository;

	
	public Project(Repository repository, InputStream in) throws IOException {
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
		_artifact = new Artifact(_repository, config);

		for(Node dependency : XmlUtil.selectNodes(config, "dependencies/dependency"))
			_dependencies.add(new Dependency((Element) dependency));
	}
	
	public Artifact getArtifact() {
		return _artifact;
	}
	
	public List<Dependency> getDependencies() {
		return Collections.unmodifiableList(_dependencies);
	}
	
	public Repository getRepository() {
		return _repository;
	}

}
