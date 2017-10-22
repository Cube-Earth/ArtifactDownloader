package earth.cube.tools.artifactdownloader;

import org.junit.Assert;
import org.junit.Test;

public class ArtifactFileTest {
	
	@Test
	public void test_new_artifact_1() {
		Artifact a = new Artifact("group:artifact:1.0-SNAPSHOT");
		ArtifactFile f = new ArtifactFile(a);
		Assert.assertEquals("artifact-1.0-SNAPSHOT.jar", f.getFileName());
		Assert.assertEquals("artifact", f.getArtifactId());
		Assert.assertEquals("1.0-SNAPSHOT", f.getVersion());
	}
	
	@Test
	public void test_new_file_1() {
		ArtifactFile f = new ArtifactFile("artifact-1.0.jar");
		Assert.assertEquals("artifact-1.0.jar", f.getFileName());
		Assert.assertEquals("artifact", f.getArtifactId());
		Assert.assertEquals("1.0", f.getVersion());
	}

	@Test
	public void test_new_file_2() {
		ArtifactFile f = new ArtifactFile("prefix-artifact-1.0.jar");
		Assert.assertEquals("prefix-artifact-1.0.jar", f.getFileName());
		Assert.assertEquals("prefix-artifact", f.getArtifactId());
		Assert.assertEquals("1.0", f.getVersion());
	}
	
	@Test
	public void test_new_file_3() {
		ArtifactFile f = new ArtifactFile("artifact-1.0-SNAPSHOT.jar");
		Assert.assertEquals("artifact-1.0-SNAPSHOT.jar", f.getFileName());
		Assert.assertEquals("artifact", f.getArtifactId());
		Assert.assertEquals("1.0-SNAPSHOT", f.getVersion());
	}

	@Test
	public void test_new_file_4() {
		ArtifactFile f = new ArtifactFile("prefix-artifact-1.0-SNAPSHOT.jar");
		Assert.assertEquals("prefix-artifact-1.0-SNAPSHOT.jar", f.getFileName());
		Assert.assertEquals("prefix-artifact", f.getArtifactId());
		Assert.assertEquals("1.0-SNAPSHOT", f.getVersion());
	}
	
	@Test
	public void test_new_file_5() {
		ArtifactFile f = new ArtifactFile("prefix-artifact.jar");
		Assert.assertEquals("prefix-artifact.jar", f.getFileName());
		Assert.assertEquals("prefix-artifact", f.getArtifactId());
		Assert.assertNull(f.getVersion());
	}

	@Test
	public void test_new_file_6() {
		ArtifactFile f = new ArtifactFile("artifact.jar");
		Assert.assertEquals("artifact.jar", f.getFileName());
		Assert.assertEquals("artifact", f.getArtifactId());
		Assert.assertNull(f.getVersion());
	}
	
	@Test
	public void test_new_file_error_1() {
		try {
			new ArtifactFile("artifact");
			Assert.fail();
		}
		catch(IllegalArgumentException e) {
		}
	}

	@Test
	public void test_new_file_error_2() {
		try {
			new ArtifactFile(".jar");
			Assert.fail();
		}
		catch(IllegalArgumentException e) {
		}
	}

	@Test
	public void test_equals_1() {
		Artifact a = new Artifact("group:artifact:1.0-SNAPSHOT");
		ArtifactFile f = new ArtifactFile(a);
		Assert.assertEquals(new ArtifactFile("artifact-1.0-SNAPSHOT.jar"), f);
		Assert.assertEquals(new ArtifactFile("artifact-1.2.jar"), f);
		Assert.assertNotEquals(new ArtifactFile("artifact2-1.2.jar"), f);
		Assert.assertEquals(new ArtifactFile("artifact.jar"), f);
	}

	@Test
	public void test_getId_1() {
		Assert.assertEquals("artifact:1.0-SNAPSHOT", new ArtifactFile("artifact-1.0-SNAPSHOT.jar").getId());
		Assert.assertEquals("artifact:1.0.1", new ArtifactFile("artifact-1.0.1.jar").getId());
		Assert.assertEquals("artifact:1.0.RELEASE", new ArtifactFile("artifact-1.0.RELEASE.jar").getId());
		Assert.assertEquals("artifact-2.1:1.0.RELEASE", new ArtifactFile("artifact-2.1-1.0.RELEASE.jar").getId());
		Assert.assertEquals("artifact-2.1-api:1.0.RELEASE", new ArtifactFile("artifact-2.1-api-1.0.RELEASE.jar").getId());
	}

}
