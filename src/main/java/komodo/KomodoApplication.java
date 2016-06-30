package komodo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class KomodoApplication {

	public static void main(String[] args) {
		SpringApplication.run(KomodoApplication.class, args);
	}
}
