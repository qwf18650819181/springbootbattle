package pactera.tf.chat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class WebsocketChatApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebsocketChatApplication.class, args);
	}

}
