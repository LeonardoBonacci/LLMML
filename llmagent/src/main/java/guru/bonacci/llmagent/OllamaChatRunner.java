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

	  CallResponseSpec resp = chatClient.prompt(
	  		"""
	  			Call the MCP tool `ping` and show me the answer. 
	  		""")
  		.call();


    System.out.println(resp.content());
	}  
}