package earth.cube.tools.artifactdownloader;

import java.util.HashMap;
import java.util.Map;

public class Context {
	
	private Context _parent;
	private Map<Artifact,Artifact> _artifacts = new HashMap<>();
	
	public Context() {
	}

	public Context(Context parent) {
		_parent = parent;
	}
	
	public boolean exists(Artifact artifact) {
		return _artifacts.containsKey(artifact) || (_parent != null ? _parent.exists(artifact) : false);
	}

	public void add(Artifact artifact) {
		if(!exists(artifact))
			_artifacts.put(artifact, artifact);
	}
	
	public Artifact get(Artifact artifact) {
		Artifact existingArtifact = _artifacts.get(artifact);
		if(existingArtifact == null && _parent != null)
			existingArtifact = _artifacts.get(artifact);
		return existingArtifact;
	}	

}
