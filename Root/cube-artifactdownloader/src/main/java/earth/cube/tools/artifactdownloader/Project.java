package earth.cube.tools.artifactdownloader;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import earth.cube.tools.artifactdownloader.utils.ILookup;
import earth.cube.tools.artifactdownloader.utils.StringResolver;
import earth.cube.tools.artifactdownloader.utils.XmlUtil;

public class Project implements ILookup {
	
	private Artifact _artifact;
	private List<Dependency> _dependencies = new ArrayList<>();
	private Repository _repository;
	private Project _parent;
	private Map<Dependency,Dependency> _managedDependencies = new HashMap<>();
	private Properties _props = new Properties();

	
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

	public void read(Element config) throws IOException {
		StringResolver resolver = new StringResolver(this);
		
		Artifact parent = null;
		Element parentElem = XmlUtil.selectSingleNode(config, "parent", false);
		if(parentElem != null) {
			_parent = _repository.loadProject(new Artifact(_repository, parentElem, resolver, null));
			parent = _parent.getArtifact();
		}

		_artifact = new Artifact(_repository, config, resolver, parent);

		for(Node propNode : XmlUtil.selectNodes(config, "properties/*")) {
			String sName = propNode.getNodeName();
			String sValue = resolver.resolve(propNode.getTextContent());
			_props.setProperty(sName, sValue);
		}

		for(Node dependencyNode : XmlUtil.selectNodes(config, "dependencyManagement/dependencies/dependency")) {
			Dependency dependency = new Dependency((Element) dependencyNode, resolver);
			_managedDependencies.put(dependency, dependency);
		}

		for(Node dependencyNode : XmlUtil.selectNodes(config, "dependencies/dependency")) {
			Dependency dependency = new Dependency((Element) dependencyNode, resolver);
			if(!dependency.isComplete()) {
				Dependency managed = getManagedDependency(dependency);
				if(managed != null) {
					dependency.setVersion(managed.getVersion());
				}
			}
			_dependencies.add(dependency);
		}
	}
	
	protected Dependency getManagedDependency(Dependency dependency) {
		Dependency managed = _managedDependencies.get(dependency);
		if(managed == null && _parent != null)
			managed = _parent.getManagedDependency(dependency);
		return managed;
	}
	
	@Override
	public String lookup(String sName) {
		String s = _props.getProperty(sName);
		if(s == null && "project.version".equals(sName)) {
			s = _artifact.getVersion();
		}
		if(s == null && _parent != null)
			s = _parent.lookup(sName);
		return s;
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
