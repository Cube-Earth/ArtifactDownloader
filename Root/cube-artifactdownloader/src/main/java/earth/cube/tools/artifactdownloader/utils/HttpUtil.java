package earth.cube.tools.artifactdownloader.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpUtil {

	public static <T> T download(URL url, IDownloadProcessor<T> processor) throws IOException {
		T result;
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
	
}
