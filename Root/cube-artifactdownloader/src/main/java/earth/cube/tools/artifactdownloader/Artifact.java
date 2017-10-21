package earth.cube.tools.artifactdownloader;

import java.io.File;
import java.io.IOException;

import org.w3c.dom.Element;

import earth.cube.tools.artifactdownloader.utils.StringResolver;
import earth.cube.tools.artifactdownloader.utils.XmlUtil;

public class Artifact {
	
	private String _sGroupId;
	private String _sArtifactId;
	private String _sVersion;
	private String _sPackaging = "jar";
	private Repository _repository;
	private boolean _bSnapshot;
	private String _sSnapshotId;
	
	public Artifact() {
	}

	public Artifact(Repository repository, Element config, StringResolver resolver, Artifact parent) throws IOException {
		_repository = repository;
		if(parent != null) {
			_sGroupId = parent.getGroupId();
			_sVersion = parent.getVersion();
		}
		read(config, resolver);
	}

	public Artifact(String sId) {
		setId(sId);
	}

	public void setId(String sGAV) {
		String[] saId = sGAV.split(":");
		_sGroupId = saId[0];
		_sArtifactId = saId[1];
		_sVersion = saId[2];
		_bSnapshot = _sVersion.endsWith("-SNAPSHOT");
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
		_bSnapshot = _sVersion.endsWith("-SNAPSHOT");
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
	
	public boolean isComplete() {
		return _sGroupId != null && _sGroupId.length() != 0 && _sArtifactId != null && _sArtifactId.length() != 0 && _sVersion != null && _sVersion.length() != 0;
	}
	
	public boolean isSnapshot() {
		return _bSnapshot;
	}
	
	public void setSnapshotId(String sId) {
		_sSnapshotId = sId;
	}
	
	public String getSnapshotId() {
		return _sSnapshotId;
	}

	public String getFileVersion() {
		return _bSnapshot ? _sVersion.replaceFirst("-SNAPSHOT$", "-" + _sSnapshotId) : _sVersion;
	}
	
	public String getPackaging() {
		return _sPackaging.equalsIgnoreCase("bundle") ? "jar" : _sPackaging;
	}
	
	public void read(Element config, StringResolver resolver) throws IOException {			
		_sGroupId = resolver.resolve(XmlUtil.getText(config, "groupId", _sGroupId));
		_sArtifactId = resolver.resolve(XmlUtil.getText(config, "artifactId", null));
		_sVersion = resolver.resolve(XmlUtil.getText(config, "version", _sVersion));
		_bSnapshot = _sVersion == null ? false :  _sVersion.endsWith("-SNAPSHOT");
		_sPackaging = resolver.resolve(XmlUtil.getText(config, "packaging", "jar"));
	}
	
	public void setRepository(Repository repository) {
		_repository = repository;
	}
	
	public Repository getRepository() {
		return _repository;
	}
	
	public String getFileName() {
		return String.format("%s-%s.%s", _sArtifactId, _sVersion, _sPackaging.equalsIgnoreCase("bundle") ? "jar" : _sPackaging);
	}
	
	public void download(File targetDir) throws IOException {
		if(_repository == null)
			throw new IllegalStateException("Repository is mandatory!");
		_repository.downloadArtifact(this, targetDir);
	}
	
	@Override
	public int hashCode() {
		return (_sGroupId + '~' + _sArtifactId).hashCode();
	}
	
	@Override
	public boolean equals(Object o) {
		if(!(o instanceof Artifact))
			return false;
		Artifact a = (Artifact) o;
		return _sGroupId.equals(a.getGroupId()) && _sArtifactId.equals(a.getArtifactId());
	}
	
	@Override
	public String toString() {
		return String.format("%s:%s:%s", _sGroupId, _sArtifactId, _sVersion);
	}


}
