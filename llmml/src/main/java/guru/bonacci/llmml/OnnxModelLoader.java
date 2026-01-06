package guru.bonacci.llmml;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import org.springframework.core.io.ClassPathResource;

public class OnnxModelLoader {

  public static Path loadFromClasspath(String resourcePath) throws Exception {
    ClassPathResource resource = new ClassPathResource(resourcePath);

    Path tempFile = Files.createTempFile("onnx-model-", ".onnx");
    tempFile.toFile().deleteOnExit();

    try (InputStream is = resource.getInputStream()) {
        Files.copy(is, tempFile, StandardCopyOption.REPLACE_EXISTING);
    }

    return tempFile;
}
}
