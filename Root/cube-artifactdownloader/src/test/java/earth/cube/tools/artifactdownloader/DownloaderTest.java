package earth.cube.tools.artifactdownloader;

import java.io.File;
import java.io.IOException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class DownloaderTest {
	
	private File _targetDir;
	
	@Before
	public void printSeparator() {
		System.out.println("===============");
	}

	private void cleanTargetDir() {
		_targetDir = new File("./bin/tests/downloads");
		if(_targetDir.exists())
			for(File file : _targetDir.listFiles())
				file.delete();
		else
			_targetDir.mkdirs();
		Assert.assertEquals(0, _targetDir.list().length);
	}
	
	private String getDownloadedFiles() {
		StringBuilder sb = new StringBuilder();
		_targetDir = new File("./bin/tests/downloads");
		if(_targetDir.exists())
			for(File file : _targetDir.listFiles())
				if(sb.length() == 0)
					sb.append(file.getName());
				else
					sb.append(", ").append(file.getName());
		return sb.length() == 0 ? "-none-" : sb.toString();
	}
	
	private void checkFile(int i, String sFileName) {
		File file = new File(_targetDir, sFileName);
		Assert.assertTrue(i + ": " + sFileName, file.exists());
		Assert.assertTrue(i + ": " + sFileName, file.isFile());
		Assert.assertTrue(i + ": " + sFileName, file.length() > 1000);
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
			checkFile(i, saExpectedFileNames[i]);
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
			checkFile(i, saExpectedFileNames[i]);
		}
		
	}
	
	@Test
	public void test_3() throws IOException {
		cleanTargetDir();
		
		Downloader.main(new String[] { _targetDir.getAbsolutePath(), "earth.cube.logkeeper:cube-logkeeper-loggers:1.0-SNAPSHOT" });
		
		String[] saExpectedFileNames = { 
		    "commons-lang3-3.4.jar",
		    "cube-logkeeper-core-1.0-SNAPSHOT.jar",
		    "cube-logkeeper-loggers-1.0-SNAPSHOT.jar",
		    "jackson-annotations-2.8.0.jar",
		    "jackson-core-2.8.0.jar",
		    "jackson-databind-2.8.0.jar",
		    "jackson-dataformat-yaml-2.1.3.jar",
		    "jeromq-0.3.4.jar"
		};
		
		Assert.assertEquals(getDownloadedFiles(), saExpectedFileNames.length, _targetDir.list().length);
		
		for(int i = 0; i < saExpectedFileNames.length; i++) {
			checkFile(i, saExpectedFileNames[i]);
		}
		
	}
	

	@Test
	public void test_4() throws IOException {
		cleanTargetDir();
		
		Downloader.main(new String[] { _targetDir.getAbsolutePath(), "org.apache.logging.log4j:log4j-api:2.6.2" });
		
		String[] saExpectedFileNames = { 
				"log4j-api-2.6.2.jar",
		};
		
		Assert.assertEquals(saExpectedFileNames.length, _targetDir.list().length);
		
		for(int i = 0; i < saExpectedFileNames.length; i++) {
			checkFile(i, saExpectedFileNames[i]);
		}
		
	}

/*
	@Test
	public void test_clean_1() throws IOException {
		cleanTargetDir();
		
		String[] saActualFileNames = { 
				"artifact2.jar",
				"artifact.jar",
				"artifact-1.jar",
				"artifact-1.1.jar",
				"artifact-1.1.2-SNAPSHOT.jar",
				"artifact-1.1.RELEASE.jar",
		};
		
		for(String sFileName : saActualFileNames) {
			File f = new File(_targetDir, sFileName);
			f.createNewFile();
			Assert.assertTrue(f.exists());
		}

		Downloader.main(new String[] { _targetDir.getAbsolutePath(), "-" });

		String[] saExpectedFileNames = { 
				"artifact2.jar",
				"artifact-1.1.2.RELEASE.jar",
		};
		
		Assert.assertEquals(saExpectedFileNames.length, _targetDir.list().length);
		
		for(int i = 0; i < saExpectedFileNames.length; i++) {
			File f = new File(_targetDir, saExpectedFileNames[i]);
			f.createNewFile();
			Assert.assertTrue(f.exists());
		}
		
	}
*/
	
}
