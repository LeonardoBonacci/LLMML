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

		 // Use the chat client to prompt the model to call both MCP tools
    CallResponseSpec resp = chatClient.prompt("""
        You have access to the MCP server with the following tools:

        1. `ping` - no arguments, returns a simple string.
        2. `validate-transaction` - takes arguments:
            - amount (float)
            - country (int)
            - merchant (int)
          and returns a boolean indicating if the transaction is valid.

        Please:
        - Call the `ping` tool and show the result.
        - Call `validate-transaction` with example values: amount=100.5, country=1, merchant=42.
        - Show both results in a structured way.
        """).call();

    System.out.println(resp.content());
	}  
}