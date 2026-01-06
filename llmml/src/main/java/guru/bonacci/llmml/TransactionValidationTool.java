package guru.bonacci.llmml;

import java.nio.file.Path;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class TransactionValidationTool {

	@Tool(name = "validate-transaction", description = "Validate a transaction using an ONNX ML model")
	public boolean validate(float amount, int country, int merchant) throws Exception {
		Path modelPath = OnnxModelLoader.loadFromClasspath("validator_model.onnx");

		TransactionValidator validator =
		        new TransactionValidator(modelPath.toString());
		
		// sorry - hack
		return validator.isValidTransaction(amount, country, merchant);
	}
	
	@Tool(name = "ping")
	public String ping() { 
		log.info("called");
		return "pang"; 
	}
}