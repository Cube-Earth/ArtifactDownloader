package earth.cube.tools.artifactdownloader.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringResolver {
	
	private ILookup _lookup;

	public StringResolver(ILookup lookup) {
		_lookup = lookup;
	}
	
	public String resolve(String sSource) {
		if(sSource == null || sSource.length() == 0)
			return sSource;
		
		StringBuilder sb = new StringBuilder();
		Pattern p = Pattern.compile("(.*?)\\$\\{([^}]+)\\}");
		Matcher m = p.matcher(sSource);
		int i = 0;
		while(m.find()) {
			sb.append(m.group(1));
			String s = _lookup.lookup(m.group(2));
			if(s == null)
				sb.append("${").append(m.group(2)).append('}');
			else
				sb.append(s);
			i = m.end();
		}
		
		sb.append(sSource.substring(i));
		return sb.toString();
	}

}
