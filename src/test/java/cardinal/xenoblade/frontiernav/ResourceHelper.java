package cardinal.xenoblade.frontiernav;

import java.net.URISyntaxException;
import java.nio.file.Path;

public class ResourceHelper {

	private ResourceHelper() {
	}

	public static Path getResourcePath(String name) {
		try {
			return Path.of(ResourceHelper.class.getClassLoader().getResource(name).toURI());
		} catch (URISyntaxException e) {
			throw new IllegalStateException(e);
		}
	}

}
