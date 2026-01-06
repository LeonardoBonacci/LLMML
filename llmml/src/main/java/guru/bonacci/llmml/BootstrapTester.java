package guru.bonacci.llmml;

import java.nio.file.Path;
import java.util.Random;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class BootstrapTester implements CommandLineRunner {

	@Override
	public void run(String... args) throws Exception {

		
		Path modelPath = OnnxModelLoader.loadFromClasspath("validator_model.onnx");

		TransactionValidator validator =
		        new TransactionValidator(modelPath.toString());

		Random rnd = new Random();
		for (int i=0; i<10; i++) {
	    float amount = 50f + rnd.nextFloat() * 300f;   // 50 → 350
	    float country = rnd.nextBoolean() ? 1f : 0f;
	    float merchant = rnd.nextBoolean() ? 1f : 0f;

	    float prob = validator.predictProbability(amount, country, merchant);
	    System.out.printf(
	        "amount=%.2f country=%.0f merchant=%.0f -> %s%n",
	        amount, country, merchant, prob
	    );		

	    boolean valid = validator.isValidTransaction(
	        amount,
	        country,
	        merchant
	    );

	    System.out.printf(
	        "amount=%.2f country=%.0f merchant=%.0f -> %s%n",
	        amount, country, merchant, valid
	    );		}

		validator.close();
	}

}
