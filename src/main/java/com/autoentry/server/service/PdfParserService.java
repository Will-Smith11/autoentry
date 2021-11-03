package com.autoentry.server.service;

import java.io.IOException;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;

import com.autoentry.server.entities.Line;

public interface PdfParserService
{
	public List<Line> run(PDDocument doc) throws IOException;
}
