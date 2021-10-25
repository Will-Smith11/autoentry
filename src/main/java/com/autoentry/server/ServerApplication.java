package com.autoentry.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.autoentry.server.beans.Document;

@SpringBootApplication
@RestController
public class ServerApplication
{

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
		return new ResponseEntity<>(new Document(), HttpStatus.OK);
	}

}
