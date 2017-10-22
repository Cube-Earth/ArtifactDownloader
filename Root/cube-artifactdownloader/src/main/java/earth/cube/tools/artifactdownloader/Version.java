package earth.cube.tools.artifactdownloader;

import java.util.ArrayList;
import java.util.List;

public class Version {
	
	public final static Version EMPTY = new Version(null);
	
	private int[] _naVersion;
	
	public Version(String sVersion) {
		_naVersion = sVersion == null || sVersion.length() == 0 ? new int[0] : parse(sVersion);
	}
	
	private int[] convert(List<Integer> src) {
		int n = src.size();
		int[] dst = new int[n];
		for(int i = 0; i < n; i++)
			dst[i] = src.get(i);
		return dst;
	}

	private int[] parse(String sVersion) {
		List<Integer> version = new ArrayList<>();
		
		int j = sVersion.indexOf('-');
		if(j != -1)
			sVersion = sVersion.substring(0, j);
		
		String[] saVersion = sVersion.split("\\.");
		try {
			for(int i = 0; i < saVersion.length; i++)
				version.add(Integer.parseInt(saVersion[i]));
		}
		catch(NumberFormatException e) {
		}
		return convert(version);
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for(int nVer : _naVersion) {
			if(sb.length() != 0)
				sb.append('.');
			sb.append(nVer);
		}
		return sb.toString();
	}
	
	public int getCount() {
		return _naVersion.length;
	}

	public int get(int nIdx) {
		return nIdx >= _naVersion.length ? 0 : _naVersion[nIdx];
	}
	
	public int compareTo(Version v) {
		if(v == null)
			v = Version.EMPTY;
		int n = Math.max(getCount(), v.getCount());
		for(int i = 0; i < n; i++) {
			int m = Integer.compare(get(i), v.get(i));
			if(m != 0)
				return m;
		}
		return 0;
	}
}
