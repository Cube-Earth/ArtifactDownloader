package earth.cube.tools.artifactdownloader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import earth.cube.tools.artifactdownloader.utils.HttpUtil;
import earth.cube.tools.artifactdownloader.utils.IDownloadProcessor;
import earth.cube.tools.artifactdownloader.utils.Spooler;

public class Repository {
	
	private String _sUrl;

	public Repository(String sUrl) {
		_sUrl = sUrl;
	}
	
	protected URL buildArtifactUrl(Artifact artifact, String sPackaging) throws IOException {
		try {
			return new URL(String.format("%1$s/%2$s/%3$s/%4$s/%3$s-%5$s.%6$s", _sUrl, artifact.getGroupId().replace('.', '/'), artifact.getArtifactId(), artifact.getVersion(), artifact.getFileVersion(), sPackaging));
		} catch (MalformedURLException e) {
			throw new IOException(e);
		}
	}

	protected URL buildMetadataUrl(Artifact artifact) throws IOException {
		try {
			return new URL(String.format("%1$s/%2$s/%3$s/%4$s/maven-metadata.xml", _sUrl, artifact.getGroupId().replace('.', '/'), artifact.getArtifactId(), artifact.getVersion()));
		} catch (MalformedURLException e) {
			throw new IOException(e);
		}
	}
	
	//http://repository.sonatype.org/service/local/artifact/maven/redirect?r=snapshots&g=org.sonatype.nexus&a=nexus-utils&v=LATEST
	// https://oss.sonatype.org/service/local/artifact/maven/redirect?r=snapshots&g=earth.cube.logkeeper&a=cube-logkeeper-loggers-delegates&v=LATEST
	// http://central.maven.org/maven2/service/local/artifact/maven/redirect?r=snapshots&g=org.apache.tomcat&a=tomcat-catalina&v=LATEST
	// https://oss.sonatype.org/content/repositories/snapshots/earth/cube/logkeeper/cube-logkeeper-loggers-delegates/1.0-SNAPSHOT/cube-logkeeper-loggers-delegates-LATEST.jar

	protected MavenMetadata loadMetadata(Artifact artifact) throws IOException {
		URL url = buildMetadataUrl(artifact);
		return HttpUtil.download(url, new IDownloadProcessor<MavenMetadata>() {

			@Override
			public MavenMetadata processDownload(InputStream in) throws IOException {
				return new MavenMetadata(Repository.this, in);
			}
		});
	}
 	
	public Project loadProject(Artifact artifact, final boolean bDrillDown) throws IOException {
		if(artifact.isSnapshot()) {
			MavenMetadata metadata = loadMetadata(artifact);
			if(metadata == null)
				return null;
			artifact.setSnapshotId(metadata.getSnapshotId());
		}
		
		URL url = buildArtifactUrl(artifact, "pom");
		System.out.println("   Analyzing " + url + " ...");
		Project project = HttpUtil.download(url, new IDownloadProcessor<Project>() {

			@Override
			public Project processDownload(InputStream in) throws IOException {
				return new Project(Repository.this, in, bDrillDown);
			}
		});
		if(project != null)
			project.getArtifact().setSnapshotId(artifact.getSnapshotId());
		return project;
	}
	
	public boolean downloadArtifact(Artifact artifact, File targetDir) throws IOException {
		if(!"jar".equals(artifact.getPackaging()))
			return false;
		
		File downloadedFile = new File(targetDir, artifact.getFileName());
		final File tmpFile = new File(targetDir, artifact.getFileName() + ".partial");
		URL url = buildArtifactUrl(artifact, artifact.getPackaging());
		if (HttpUtil.download(url, new IDownloadProcessor<Boolean>() {

			@Override
			public Boolean processDownload(InputStream in) throws IOException {
				Spooler.spool(in, new FileOutputStream(tmpFile), false, true);
				return true;
			}
		}) == null)
			throw new IllegalStateException("POM file found, but not the contained artifact!");
		tmpFile.renameTo(downloadedFile);
		return true;
	}

	@Override
	public String toString() {
		return _sUrl;
	}
	
}
