package guru.bonacci.llmagent;


import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.ChatClient.CallResponseSpec;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class OllamaChatRunner implements CommandLineRunner {

	private final ChatClient chatClient;
	
	 public OllamaChatRunner(ChatClient.Builder chatClientBuilder, ToolCallbackProvider tools) {

     this.chatClient = chatClientBuilder
             .defaultToolCallbacks(tools)
             .build();
	}

	@Override
	public void run(String... args) throws Exception {
		System.out.println("Starting Ollama 3.2 chat POC...");

		CallResponseSpec resp = chatClient.prompt("""
        You have access to the following MCP tools:

        1. `generate-random-transaction` - returns JSON with `amount`, `country`, `merchant`.
        2. `validate-transaction` - takes amount (float), country (int), merchant (int), returns boolean.

        Please:
        - Call `generate-random-transaction` to get random transaction values.
        - Call `validate-transaction` using the generated values.
        - Return both the generated transaction and validation result in a JSON format.
        """).call();
		
    System.out.println(resp.content());
	}  
}