package earth.cube.tools.artifactdownloader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import earth.cube.tools.artifactdownloader.utils.Spooler;

public class Repository {
	
	private String _sUrl;

	public Repository(String sUrl) {
		_sUrl = sUrl;
	}
	
	protected URL buildUrl(Artifact artifact, String sPackaging) throws IOException {
		try {
			return new URL(String.format("%1$s/%2$s/%3$s/%4$s/%3$s-%4$s.%5$s", _sUrl, artifact.getGroupId().replace('.', '/'), artifact.getArtifactId(), artifact.getVersion(), sPackaging));
		} catch (MalformedURLException e) {
			throw new IOException(e);
		}
	}
	
	protected <T> T downloadArtifact(Artifact artifact, String sPackaging, IDownloadProcessor<T> processor) throws IOException {
		T result;
		URL url = buildUrl(artifact, sPackaging == null ? artifact.getPackaging() : sPackaging);
		HttpURLConnection conn = (HttpURLConnection)url.openConnection();
		try {
			conn.setRequestMethod("GET");
			conn.connect();
			int nRC = conn.getResponseCode();
			if(nRC == 404)
				return null;
			if(nRC != 200)
				throw new IOException(String.format("HTTP request failed. Response: %s - %s", nRC, conn.getResponseMessage()));
			InputStream in = conn.getInputStream();
			try {
				result = processor.processDownload(in);
			}
			finally {
				in.close();
			}
		}
		finally {
			conn.disconnect();
		}
		return result;
	}
	
	public Project loadProject(Artifact artifact) throws IOException {
		return downloadArtifact(artifact, "pom", new IDownloadProcessor<Project>() {

			@Override
			public Project processDownload(InputStream in) throws IOException {
				return new Project(Repository.this, in);
			}
		});
	}
	
	public boolean downloadArtifact(Artifact artifact, File targetDir) throws IOException {
		File downloadedFile = new File(targetDir, artifact.getFileName());
		final File tmpFile = new File(targetDir, artifact.getFileName() + ".partial");
		if (downloadArtifact(artifact, null, new IDownloadProcessor<Boolean>() {

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
