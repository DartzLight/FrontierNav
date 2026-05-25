package cardinal.xenoblade.frontiernav.utils;

import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;

public class FileHelper {

	private FileHelper() {
	}

	public static Path findOrDefault(Path pathToFind, Path pathToDefault) throws NoSuchFileException {
		if (Files.exists(pathToFind)) {
			return pathToFind;
		}
		if (Files.exists(pathToDefault)) {
			return pathToDefault;
		}
		throw new NoSuchFileException(pathToDefault.toString(), pathToFind.toString(), "At least one of these files must exist");
	}
	
}
