package earth.cube.tools.artifactdownloader;

import org.w3c.dom.Element;

import earth.cube.tools.artifactdownloader.utils.XmlUtil;

public class Dependency extends Artifact {
	
	private String _sScope = "compile";

	public Dependency() {
	}

	public Dependency(Element config) {
		read(config);
	}

	public void setScope(String sScope) {
		_sScope  = sScope;
	}
	
	public String getScope() {
		return _sScope;
	}

	public void read(Element config) {
		super.read(config);
		_sScope = XmlUtil.getText(config, "scope", "compile");
	}

}
