package com.autoentry.server.service.impl;

import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.stereotype.Service;

import com.autoentry.server.service.PdfParserService;

import io.reactivex.rxjava3.core.Completable;

@Service
public class PdfParserServiceImpl implements PdfParserService
{

	@Override
	public Completable run(PDDocument doc) throws IOException
	{
		// TODO Auto-generated method stub
		return null;
	}

}
