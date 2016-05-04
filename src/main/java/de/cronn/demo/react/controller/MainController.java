package de.cronn.demo.react.controller;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Demo page
 *
 * @author Hanno Fellmann, cronn GmbH
 */
@Controller
public class MainController {
	@RequestMapping("/")
    public String index() {
		return "index";
	}
}
