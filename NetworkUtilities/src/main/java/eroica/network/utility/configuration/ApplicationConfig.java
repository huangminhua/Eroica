package eroica.network.utility.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

/**
 * Configurations of this project.
 * @author Minhua HUANG
 *
 */
@Configuration
@Setter
@Getter
public class ApplicationConfig {
	@Value("${spring.application.name}")
	private String applicationName;
	
}
