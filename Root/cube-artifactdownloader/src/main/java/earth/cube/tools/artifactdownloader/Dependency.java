package earth.cube.tools.artifactdownloader;

import java.io.IOException;

import org.w3c.dom.Element;

import earth.cube.tools.artifactdownloader.utils.StringResolver;
import earth.cube.tools.artifactdownloader.utils.XmlUtil;

public class Dependency extends Artifact {
	
	private String _sScope = "compile";
	private boolean _bScope;
	private String _sType;
	private boolean _bOptional;

	public Dependency() {
	}

	public Dependency(Element config, StringResolver resolver) throws IOException {
		read(config, resolver);
	}

	public boolean hasScope() {
		return _bScope;
	}

	public void setScope(String sScope) {
		_sScope  = sScope;
		_bScope = false;
	}
	
	public String getScope() {
		return _sScope;
	}

	public void setType(String sType) {
		_sType  = sType;
	}
	
	public String getType() {
		return _sType;
	}

	public void setOptional(boolean bOptional) {
		_bOptional  = bOptional;
	}
	
	public boolean isOptional() {
		return _bOptional;
	}

	public void read(Element config, StringResolver resolver) throws IOException {
		super.read(config, resolver);
		_sScope = resolver.resolve(XmlUtil.getText(config, "scope", null));
		if(_sScope == null)
			_sScope = "compile";
		else
			_bScope = true;
		
		_sType = resolver.resolve(XmlUtil.getText(config, "type", null));
		_bOptional = "true".equalsIgnoreCase(resolver.resolve(XmlUtil.getText(config, "optional", "false")));
	}

}
