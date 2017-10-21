package earth.cube.tools.artifactdownloader;

import java.io.IOException;

import org.w3c.dom.Element;

import earth.cube.tools.artifactdownloader.utils.StringResolver;
import earth.cube.tools.artifactdownloader.utils.XmlUtil;

public class Dependency extends Artifact {
	
	private String _sScope = "compile";

	public Dependency() {
	}

	public Dependency(Element config, StringResolver resolver) throws IOException {
		read(config, resolver);
	}

	public void setScope(String sScope) {
		_sScope  = sScope;
	}
	
	public String getScope() {
		return _sScope;
	}

	public void read(Element config, StringResolver resolver) throws IOException {
		super.read(config, resolver);
		_sScope = resolver.resolve(XmlUtil.getText(config, "scope", "compile"));
	}

}
