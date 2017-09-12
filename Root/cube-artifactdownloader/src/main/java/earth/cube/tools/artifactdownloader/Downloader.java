package earth.cube.tools.artifactdownloader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

public class Downloader {
	
	// http://central.maven.org/maven2/javax/servlet/servlet-api/3.0-alpha-1/servlet-api-3.0-alpha-1.jar
	// http://central.maven.org/maven2/org/apache/tomcat/tomcat-catalina/8.5.9/tomcat-catalina-8.5.9.jar
	
	private List<Repository> _repositories = new ArrayList<>();
	private List<String> _ids = new ArrayList<>();
	private Set<Artifact> _artifacts = new HashSet<>();
	private File _targetDir;
	
	protected void loadRepositories() throws IOException {
		_repositories.add(new Repository("http://central.maven.org/maven2"));
		_repositories.add(new Repository("http://repo.maven.apache.org/maven2"));
		
		InputStream in = getClass().getClassLoader().getResourceAsStream("repositories.properties");
		if(in != null) {
			Properties props = new Properties();
			props.load(in);
			for(Entry<Object, Object> entry : props.entrySet()) {
				if(entry.getKey().toString().matches("repository\\.\\d+"))
					_repositories.add(new Repository(entry.getValue().toString()));
			}
		}
		
	}
	
	public void setTargetDir(File targetDir) {
		_targetDir = targetDir;
	}
	
	public void addId(String sId) {
		_ids.add(sId);
	}
	
	private Project challenge(Artifact artifact) throws IOException {
		for(Repository repository : new ArrayList<>(_repositories)) {
			Project project = repository.loadProject(artifact);
			return project;
		}
		throw new IllegalStateException("Artifact '" + artifact + "' could not be found!");
	}

	private void process(Artifact artifact) throws IOException {
		if(_artifacts.contains(artifact))
			return;
		Project project = challenge(artifact);
		artifact = project.getArtifact();
		_artifacts.add(artifact);
		System.out.println(String.format("Downloading '%s' from %s ...", artifact, artifact.getRepository()));
		artifact.download(_targetDir);
		for(Dependency dependency : project.getDependencies()) {
			if("compile".equalsIgnoreCase(dependency.getScope()))
				process(dependency);
		}
	}

	public void execute() throws IOException {
		loadRepositories();
		
		_targetDir.mkdirs();
		
		for(String sId : _ids) {
			process(new Artifact(sId));
		}
	}
	
	public static void main(String[] saArgs) throws IOException {
		Downloader runner = new Downloader();
		runner.setTargetDir(new File(saArgs[0]));
		for(int i = 1; i < saArgs.length; i++)
			runner.addId(saArgs[i]);
		runner.execute();
	}

}
