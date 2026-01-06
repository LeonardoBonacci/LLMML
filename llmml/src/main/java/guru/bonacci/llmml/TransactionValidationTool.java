package guru.bonacci.llmml;

import java.nio.file.Path;
import java.util.Random;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class TransactionValidationTool {

	@Tool(name = "validate-transaction", description = "Validate a transaction using an ONNX ML model")
	public boolean validate(float amount, int country, int merchant) throws Exception {
		Path modelPath = OnnxModelLoader.loadFromClasspath("validator_model.onnx");

		// sorry - unspring-like hack
		TransactionValidator validator =
		        new TransactionValidator(modelPath.toString());
		
		var result = validator.isValidTransaction(amount, country, merchant);
		log.warn("------------- {}", result);
		return result;
	}
	
	@Tool(name = "generate-random-transaction", description = "Generates random transaction data for testing")
	public TransactionData generateRandomTransaction() {
	    Random rand = new Random();

	    float amount = 10 + rand.nextFloat() * 1000; // 10.0 -> 1010.0
	    int country = 1 + rand.nextInt(250);         // country IDs 1-250
	    int merchant = 1 + rand.nextInt(1000);       // merchant IDs 1-1000

	    var tx = new TransactionData(amount, country, merchant);
	    log.warn("!!!!!!!!!!! {}", tx);
	    return tx;
	}
	
	@Value
	public static class TransactionData {
   private final float amount;
   private final int country;
   private final int merchant;
	}
}