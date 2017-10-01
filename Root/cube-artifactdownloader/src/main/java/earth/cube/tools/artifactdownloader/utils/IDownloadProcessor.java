package earth.cube.tools.artifactdownloader.utils;

import java.io.IOException;
import java.io.InputStream;

public interface IDownloadProcessor<T> {

	T processDownload(InputStream in) throws IOException;
	
}
