package uk.me.ruthmills.boglindisplay;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BoglinDisplayApplication {

	public static void main(String[] args) {
		SpringApplication.run(BoglinDisplayApplication.class, args);
	}
}
