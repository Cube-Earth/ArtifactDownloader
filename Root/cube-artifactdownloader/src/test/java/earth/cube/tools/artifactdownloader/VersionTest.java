package earth.cube.tools.artifactdownloader;

import org.junit.Assert;
import org.junit.Test;

public class VersionTest {
	
	@Test
	public void test_toString_1() {
		Assert.assertEquals("1.0", new Version("1.0-SNAPSHOT").toString());
		Assert.assertEquals("1.0.1", new Version("1.0.1").toString());
		Assert.assertEquals("1.0", new Version("1.0.RELEASE").toString());
	}

	@Test
	public void test_compareTo_1() {
		Assert.assertEquals(0, new Version("1.0-SNAPSHOT").compareTo(new Version("1.0.RELEASE")));
		Assert.assertEquals(0, new Version("1.0").compareTo(new Version("1")));
		Assert.assertEquals(0, new Version("0").compareTo(Version.EMPTY));
		Assert.assertEquals(-1, new Version("1.0").compareTo(new Version("1.0.1")));
		Assert.assertEquals(1, new Version("1.0.1").compareTo(new Version("1.0.0.0.0.0")));
		Assert.assertEquals(-1, new Version("1.0.1-SNAPSHOT").compareTo(new Version("1.0.2.RELEASE")));
	}

}
