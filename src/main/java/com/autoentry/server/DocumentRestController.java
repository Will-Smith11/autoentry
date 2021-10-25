package com.autoentry.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.autoentry.server.interfaces.BaseDocument;

@RestController
public class DocumentRestController
{
	@Autowired
	BaseDocument doc;

	public DocumentRestController(BaseDocument doc)
	{
		this.doc = doc;
	}
}
