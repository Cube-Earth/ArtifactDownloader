package earth.cube.tools.artifactdownloader;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ArtifactFile {
	
	private String _sFileName;
	private String _sArtifactId;
	private String _sVersion;
	private File _file;

	public ArtifactFile(File file) {
		_file = file;
		init(file.getName());
	}
	
	public ArtifactFile(String sFileName) {
		init(sFileName);
	}

	public ArtifactFile(Artifact artifact) {
		_sFileName = artifact.getFileName();
		_sArtifactId = artifact.getArtifactId();
		_sVersion = artifact.getVersion();
	}
	
	private void init(String sFileName) {
		_sFileName = sFileName;
//		Pattern p = Pattern.compile("(.+?)(?:-([0-9].*))?\\.jar", Pattern.CASE_INSENSITIVE);
		Pattern p = Pattern.compile("(.+?)(?:-([0-9]+(\\.[0-9]+)*([-.][^0-9][^-]+)?))?\\.jar", Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(sFileName);
		if(m.matches()) {
			_sArtifactId = m.group(1);
			_sVersion = m.group(2);
		}
		else
			throw new IllegalArgumentException("File name '" + sFileName + "' is malformed!");
	}
	
	public String getFileName() {
		return _sFileName;
	}

	public String getArtifactId() {
		return _sArtifactId;
	}

	public String getVersion() {
		return _sVersion;
	}

	public String getId() {
		return _sArtifactId + ':' + _sVersion;
	}
	
	public File getFile() {
		return _file;
	}

	@Override
	public int hashCode() {
		return _sArtifactId.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof ArtifactFile) {
			ArtifactFile file = (ArtifactFile) obj;
			return _sArtifactId.equals(file.getArtifactId());
		}
		else
			return false;
	}

}
