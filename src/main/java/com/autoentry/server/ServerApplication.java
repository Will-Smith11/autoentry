package com.autoentry.server;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.autoentry.server.beans.Document;
import com.autoentry.server.entities.DetectedDocumentData;
import com.autoentry.server.entities.Label;
import com.autoentry.server.interfaces.BaseDocument;

@SpringBootApplication
@RestController
@ComponentScan(basePackages = { "com.autoentry.server" })
public class ServerApplication
{

	@Autowired
	Document doc;

	@Autowired
	BaseDocument bDoc;

	@Bean
	public CommandLineRunner commandLineRunner(ApplicationContext ctx)
	{
		return args -> {
			String[] beanNames = ctx.getBeanDefinitionNames();
			Arrays.sort(beanNames);
			for (String beanName : beanNames)
			{
				System.out.println(beanName);
			}

		};
	}

	public static void main(String[] args)
	{
		SpringApplication.run(ServerApplication.class, args);

	}

	@GetMapping("/hello")
	public String hello(@RequestParam(value = "name", defaultValue = "World") String name)
	{
		return String.format("Hello %s!", name);
	}

	@GetMapping("/document")
	public ResponseEntity<HashMap<Label, DetectedDocumentData>> document()
	{

		try
		{
			bDoc.genMeta();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		bDoc.processMeta();

		return new ResponseEntity<>(bDoc.getResults(), HttpStatus.OK);
	}

}
