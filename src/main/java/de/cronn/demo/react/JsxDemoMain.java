package de.cronn.demo.react;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;

/**
 * Demo Spring Boot app
 *
 * @author Hanno Fellmann, cronn GmbH
 */
@EnableAutoConfiguration
@ComponentScan
@Controller
public class JsxDemoMain {
	public static void main(final String[] args) throws Exception {
		SpringApplication.run(JsxDemoMain.class, args);
	}
}
