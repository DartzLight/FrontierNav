package cardinal.xenoblade.frontiernav.utils;

import org.junit.jupiter.api.Test;

import java.nio.file.NoSuchFileException;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class FileHelperTest {

	private static final Path FILE_PATH = Path.of("src/test/resources/file_helper/file.txt");
	private static final Path DEFAULT_PATH = Path.of("src/test/resources/file_helper/default/file.txt");
	private static final Path INVALID_FILE_PATH = Path.of("src/test/resources/file_helper/invalid.txt");
	private static final Path INVALID_DEFAULT_PATH = Path.of("src/test/resources/file_helper/default/invalid_default.txt");

	@Test
	void check_file_exists() throws NoSuchFileException {
		assertThat(FileHelper.findOrDefault(FILE_PATH, DEFAULT_PATH))
				.isEqualTo(FILE_PATH)
				.exists();
	}

	@Test
	void check_default_exists() throws NoSuchFileException {
		assertThat(FileHelper.findOrDefault(INVALID_FILE_PATH, DEFAULT_PATH))
				.isEqualTo(DEFAULT_PATH)
				.exists();
	}

	@Test
	void check_error_if_no_file_nor_default_exists() {
		assertThatThrownBy(() -> FileHelper.findOrDefault(INVALID_FILE_PATH, INVALID_DEFAULT_PATH))
				.isInstanceOf(NoSuchFileException.class)
				.hasMessageContaining(INVALID_DEFAULT_PATH.toString())
				.hasMessageContaining(INVALID_FILE_PATH.toString());
	}
}