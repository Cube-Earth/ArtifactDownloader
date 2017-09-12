package earth.cube.tools.artifactdownloader;

import java.io.File;
import java.io.IOException;

import org.w3c.dom.Element;

import earth.cube.tools.artifactdownloader.utils.XmlUtil;

public class Artifact {
	
	private String _sGroupId;
	private String _sArtifactId;
	private String _sVersion;
	private String _sPackaging = "jar";
	private Repository _repository;
	
	public Artifact() {
	}

	public Artifact(Repository repository, Element config) {
		_repository = repository;
		read(config);
	}

	public Artifact(String sId) {
		setId(sId);
	}

	public void setId(String sGAV) {
		String[] saId = sGAV.split(":");
		_sGroupId = saId[0];
		_sArtifactId = saId[1];
		_sVersion = saId[2];
		if(saId.length > 3)
			_sPackaging = saId[3];
	}
	
	public void setGroupId(String sGroupId) {
		_sGroupId = sGroupId;
	}
	
	public void setArtifactId(String sArtifactId) {
		_sArtifactId = sArtifactId;
	}

	public void setVersion(String sVersion) {
		_sVersion = sVersion;
	}
	
	public void setPackaging (String sPackaging) {
		_sPackaging = sPackaging;
	}
	
	public String getGroupId() {
		return _sGroupId;
	}
	
	public String getArtifactId() {
		return _sArtifactId;
	}
	
	public String getVersion() {
		return _sVersion;
	}
	
	public String getPackaging() {
		return _sPackaging;
	}
	
	public void read(Element config) {
		_sGroupId = XmlUtil.getText(config, "groupId", null);
		_sArtifactId = XmlUtil.getText(config, "artifactId", null);
		_sVersion = XmlUtil.getText(config, "version", null);
		_sPackaging = XmlUtil.getText(config, "packaging", "jar");
	}
	
	public void setRepository(Repository repository) {
		_repository = repository;
	}
	
	public Repository getRepository() {
		return _repository;
	}
	
	public String getFileName() {
		return String.format("%s-%s.%s", _sArtifactId, _sVersion, _sPackaging);
	}
	
	public void download(File targetDir) throws IOException {
		if(_repository == null)
			throw new IllegalStateException("Repository is mandatory!");
		_repository.downloadArtifact(this, targetDir);
	}
	
	@Override
	public int hashCode() {
		return (_sGroupId + '~' + _sArtifactId + '~' + _sVersion).hashCode();
	}
	
	@Override
	public boolean equals(Object o) {
		if(!(o instanceof Artifact))
			return false;
		Artifact a = (Artifact) o;
		return _sGroupId.equals(a.getGroupId()) && _sArtifactId.equals(a.getArtifactId()) && _sVersion.equals(a.getVersion());
	}
	
	@Override
	public String toString() {
		return String.format("%s:%s:%s", _sGroupId, _sArtifactId, _sVersion);
	}


}
