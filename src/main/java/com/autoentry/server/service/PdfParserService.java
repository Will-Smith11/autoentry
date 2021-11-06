package com.autoentry.server.service;

import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;

import io.reactivex.rxjava3.core.Completable;

public interface PdfParserService
{
	public Completable run(PDDocument doc) throws IOException;
}
