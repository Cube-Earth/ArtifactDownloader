package earth.cube.tools.artifactdownloader;

import java.io.File;
import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

public class DownloaderTest {
	
	private File _targetDir;

	private void cleanTargetDir() {
		_targetDir = new File("./bin/tests/downloads");
		if(_targetDir.exists())
			for(File file : _targetDir.listFiles())
				file.delete();
		else
			_targetDir.mkdirs();
		Assert.assertEquals(0, _targetDir.list().length);
	}
	
	private void checkFile(String sFileName) {
		File file = new File(_targetDir, sFileName);
		Assert.assertTrue(sFileName, file.exists());
		Assert.assertTrue(sFileName, file.isFile());
		Assert.assertTrue(sFileName, file.length() > 1000);
	}
	
	@Test
	public void test_1() throws IOException {
		cleanTargetDir();
		
		Downloader.main(new String[] { _targetDir.getAbsolutePath(), "javax.servlet:servlet-api:3.0-alpha-1", "org.apache.tomcat:tomcat-catalina:8.5.9" });
		
		String[] saExpectedFileNames = { 
			"servlet-api-3.0-alpha-1.jar",
			"tomcat-annotations-api-8.5.9.jar",
			"tomcat-api-8.5.9.jar",
			"tomcat-catalina-8.5.9.jar",
			"tomcat-coyote-8.5.9.jar",
			"tomcat-el-api-8.5.9.jar",
			"tomcat-jaspic-api-8.5.9.jar",
			"tomcat-jni-8.5.9.jar",
			"tomcat-jsp-api-8.5.9.jar",
			"tomcat-juli-8.5.9.jar",
			"tomcat-servlet-api-8.5.9.jar",
			"tomcat-util-8.5.9.jar",
			"tomcat-util-scan-8.5.9.jar"
		};
		
		Assert.assertEquals(saExpectedFileNames.length, _targetDir.list().length);
		
		for(int i = 0; i < saExpectedFileNames.length; i++) {
			checkFile(saExpectedFileNames[i]);
		}
		
	}

	@Test
	public void test_2() throws IOException {
		cleanTargetDir();
		
		Downloader.main(new String[] { _targetDir.getAbsolutePath(), "earth.cube.logkeeper:cube-logkeeper-loggers-delegates:1.0-SNAPSHOT" });
		
		String[] saExpectedFileNames = { 
			"cube-logkeeper-loggers-delegates-1.0-SNAPSHOT.jar"
		};
		
		Assert.assertEquals(saExpectedFileNames.length, _targetDir.list().length);
		
		for(int i = 0; i < saExpectedFileNames.length; i++) {
			checkFile(saExpectedFileNames[i]);
		}
		
	}
}
