package com.internmaker.internmaker_backend;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class InternmakerBackendApplication {

	public static void main(String[] args) {
		// Only load .env if it exists (for local development)
		try {
			Dotenv dotenv = Dotenv.configure()
					.ignoreIfMissing()
					.load();

			dotenv.entries().forEach(entry -> {
				if (System.getProperty(entry.getKey()) == null && System.getenv(entry.getKey()) == null) {
					System.setProperty(entry.getKey(), entry.getValue());
				}
			});
		} catch (Exception e) {
			// Fail silently if .env is not present (e.g. on Render)
		}

		SpringApplication.run(InternmakerBackendApplication.class, args);
	}

}
