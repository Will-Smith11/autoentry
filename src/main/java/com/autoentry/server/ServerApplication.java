package com.autoentry.server;

import java.io.IOException;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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

	public static void main(String[] args)
	{
		SpringApplication.run(ServerApplication.class, args);
	}

	@GetMapping("/document")
	public ResponseEntity<HashMap<Label, DetectedDocumentData>> document() throws IOException
	{
		return new ResponseEntity<>(bDoc.getResults().blockingGet(), HttpStatus.OK);
	}

}
