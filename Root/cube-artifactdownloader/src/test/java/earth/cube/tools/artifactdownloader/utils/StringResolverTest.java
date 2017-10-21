package earth.cube.tools.artifactdownloader.utils;

import java.util.Properties;

import org.junit.Assert;
import org.junit.Test;

public class StringResolverTest {
	
	private static final Properties _props;
	
	static {
		Properties props = new Properties();
		props.setProperty("a", "1");
		props.setProperty("a.b", "23");
		_props = props;
	}
	
	@Test
	public void test_1() {
		StringResolver resolver = new StringResolver(new PropertiesLookup(_props));
		
		Assert.assertEquals("abc", resolver.resolve("abc"));
		Assert.assertEquals("abc1", resolver.resolve("abc${a}"));
		Assert.assertEquals("1", resolver.resolve("${a}"));
		Assert.assertEquals("1abc231${b}e", resolver.resolve("${a}abc${a.b}${a}${b}e"));
	}

}
