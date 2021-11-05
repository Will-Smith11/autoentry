package com.autoentry.server;

import java.io.IOException;
import java.util.Arrays;

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
import com.autoentry.server.interfaces.BaseDocument;

@SpringBootApplication
@RestController
@ComponentScan(basePackages = { "com.autoentry.server" })
public class ServerApplication
{
	final static String sourcePath = "C:\\Users\\Will\\git\\com.autoentry.server\\examples\\CD-00000067Docs202109101643.pdf";
	final static String resultPath = "C:\\Users\\Will\\git\\com.autoentry\\results\\";
	final static String projectId = "nodal-plexus-325621";
	final static String uploadBucketName = "temp-upload-test";
	final static String downloadBucketName = "temp-download-test";

	@Autowired
	Document doc;

	@Autowired
	BaseDocument bDoc;

	@Bean
	public CommandLineRunner commandLineRunner(ApplicationContext ctx)
	{
		return args -> {

			System.out.println("Let's inspect the beans provided by Spring Boot:");

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
	public ResponseEntity<Document> document()
	{

		//		Document d = new Document(sourcePath, resultPath, projectId, uploadBucketName, "gs://temp-upload-test/test",
		//				"gs://temp-download-test/test", true);

		try
		{
			bDoc.genMeta();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		bDoc.processMeta();

		return new ResponseEntity<>(bDoc.getBean(), HttpStatus.OK);
	}

}
