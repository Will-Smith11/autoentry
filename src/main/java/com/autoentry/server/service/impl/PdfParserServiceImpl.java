package com.autoentry.server.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.autoentry.server.beans.Document;
import com.autoentry.server.entities.Line;
import com.autoentry.server.service.PageReaderListenerService;
import com.autoentry.server.service.PdfParserService;

@Component
public class PdfParserServiceImpl implements PdfParserService
{
	@Autowired
	Document mDoc;

	@Override
	public List<Line> run(PDDocument doc) throws IOException
	{
		List<Line> results = new ArrayList<>();
		PDPage p = doc.getPage(0);
		mDoc.setHeight((int) p.getBBox().getHeight());
		mDoc.setWidth((int) p.getBBox().getWidth());
		PageReaderListenerService reader = new PageReaderListenerServiceImpl(p, 0);
		results.addAll(reader.getLines());
		doc.close();
		return results;
	}

}
