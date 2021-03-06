package earth.cube.tools.artifactdownloader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

public class Downloader {
	
	// http://central.maven.org/maven2/javax/servlet/servlet-api/3.0-alpha-1/servlet-api-3.0-alpha-1.jar
	// http://central.maven.org/maven2/org/apache/tomcat/tomcat-catalina/8.5.9/tomcat-catalina-8.5.9.jar
	
	private List<Repository> _repositories = new ArrayList<>();
	private List<String> _ids = new ArrayList<>();
	private Set<Artifact> _artifacts = new HashSet<>();
	private Map<ArtifactFile,File> _provided = new HashMap<>();
	private Map<ArtifactFile,Artifact> _downloaded = new HashMap<>();
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
	
	private Project challenge(Artifact artifact, boolean bDrillDown) throws IOException {
		for(Repository repository : new ArrayList<>(_repositories)) {
			Project project = repository.loadProject(artifact, bDrillDown);
			if(project != null)
				return project;
		}
//		throw new IllegalStateException("Artifact '" + artifact + "' could not be found!");
		return null;
	}

	private void process(Artifact artifact) throws IOException {
		if(_artifacts.contains(artifact))
			return;
		Project project = challenge(artifact, true);
		if(project == null) {
			_artifacts.add(artifact);
			return;
		}
		
		artifact = project.getArtifact();
		_artifacts.add(artifact);
		System.out.println(String.format("Downloading '%s' from %s ...", artifact, artifact.getRepository()));
		ArtifactFile artifactFile = new ArtifactFile(artifact);
		File provided = _provided.get(artifactFile);
		if(provided != null) {
			System.out.println(String.format("   WARNING: name or version conflict with existing library '%s'! skipping ...", provided.getName()));
			return;
		}
		Artifact nameConflict = _downloaded.get(artifactFile);
		if(nameConflict != null) {
			System.out.println(String.format("   WARNING: name conflict with downloaded artifact '%s'! skipping ...", nameConflict));
			return;
		}
		_downloaded.put(artifactFile, artifact);

		artifact.download(_targetDir);
		for(Dependency dependency : project.getDependencies()) {
			if("compile".equalsIgnoreCase(dependency.getScope()))
				process(dependency);
		}
	}

	private void loadProvided() {
		for(File file : _targetDir.listFiles()) {
			if(file.isFile() && file.getName().toLowerCase().endsWith(".jar")) {
				_provided.put(new ArtifactFile(file), file);
			}
		}
	}
	
	private void cleanJars() {
		System.out.println("Cleaning JAR files ...");
		Map<ArtifactFile,ArtifactFile> jars = new HashMap<>();
		for(File file : _targetDir.listFiles()) {
			if(file.isFile() && file.getName().toLowerCase().endsWith(".jar")) {
				ArtifactFile a1 = new ArtifactFile(file);
				ArtifactFile a2 = jars.get(a1);
				if(a2 != null) {
					Version v1 = new Version(a1.getVersion());
					Version v2 = new Version(a2.getVersion());
					switch(v1.compareTo(v2)) {
						case -1:
							System.out.println(String.format("   removing file '%s' (conflicts with '%s') ...", a1.getFileName(), a2.getFileName()));
							a1.getFile().delete();
							break;

						case +1:
							System.out.println(String.format("   removing file '%s' (conflicts with '%s') ...", a2.getFileName(), a1.getFileName()));
							a2.getFile().delete();
							jars.put(a1, a1);
							break;
							
						case 0:
							System.out.println(String.format("   removing file '%s' (conflicts with '%s') ...", a1.getFileName(), a2.getFileName()));
							a1.getFile().delete();
							break;
					}
				}
				else
					jars.put(a1, a1);
			}
		}
	}

	public void execute() throws IOException {
		loadRepositories();
		
		if(!_targetDir.mkdirs())
			loadProvided();
		
		for(String sId : _ids) {
			if("-".equals(sId))
				cleanJars();
			else
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
