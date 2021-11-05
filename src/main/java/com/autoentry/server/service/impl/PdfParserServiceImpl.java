package com.autoentry.server.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.springframework.stereotype.Component;

import com.autoentry.server.entities.Line;
import com.autoentry.server.service.PdfParserService;

@Component
public class PdfParserServiceImpl implements PdfParserService
{

	@Override
	public List<Line> run(PDDocument doc) throws IOException
	{
		List<Line> results = new ArrayList<>();
		//		for (int i = 0; i < doc.getNumberOfPages(); i++)
		//		{

		PDPage p = doc.getPage(0);
		System.out.println(p.getBleedBox().toString());
		PageReaderListenerServiceImpl reader = new PageReaderListenerServiceImpl(p, 0);
		results.addAll(reader.getLines());
		doc.close();
		//		}
		return results;
	}

}
