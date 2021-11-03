package com.autoentry.server.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.stereotype.Service;

import com.autoentry.server.entities.Line;
import com.autoentry.server.service.PageReaderListenerService;
import com.autoentry.server.service.PdfParserService;

@Service
public class PdfParserServiceImpl implements PdfParserService
{

	@Override
	public List<Line> run(PDDocument doc) throws IOException
	{
		List<Line> results = new ArrayList<>();
		for (int i = 0; i < doc.getNumberOfPages(); i++)
		{
			PageReaderListenerService reader = new PageReaderListenerServiceImpl(doc.getPage(i), i);
			results.addAll(reader.getLines());
		}
		return results;
	}

}
