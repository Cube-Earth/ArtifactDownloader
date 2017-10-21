package earth.cube.tools.artifactdownloader.utils;

import java.util.Properties;

public class PropertiesLookup implements ILookup {
	
	private Properties _props;
	
	public PropertiesLookup(Properties props) {
		_props = props;
	}


	@Override
	public String lookup(String sName) {
		return _props.getProperty(sName);
	}

}
