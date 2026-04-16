package com.promptlabs.autenticacion_seguridad;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class AutenticacionSeguridadApplication {

	public static void main(String[] args) {
		SpringApplication.run(AutenticacionSeguridadApplication.class, args);
	}

}
