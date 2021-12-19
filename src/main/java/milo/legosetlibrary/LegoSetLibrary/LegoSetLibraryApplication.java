package milo.legosetlibrary.LegoSetLibrary;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class LegoSetLibraryApplication {

	public static void main(String[] args) {
		SpringApplication.run(LegoSetLibraryApplication.class, args);
	}
}
