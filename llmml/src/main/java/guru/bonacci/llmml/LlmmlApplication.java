package guru.bonacci.llmml;

import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class LlmmlApplication {

	public static void main(String[] args) {
		SpringApplication.run(LlmmlApplication.class, args);
	}

	@Bean
	ToolCallbackProvider weatherToolProvider(TransactionValidationTool tool) {
		return MethodToolCallbackProvider.builder().toolObjects(tool).build();
	}
}
