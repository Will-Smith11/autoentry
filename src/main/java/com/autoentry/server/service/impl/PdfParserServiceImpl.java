package com.autoentry.server.service.impl;

import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.autoentry.server.beans.Document;
import com.autoentry.server.entities.DPage;
import com.autoentry.server.service.PageReaderListenerService;
import com.autoentry.server.service.PdfParserService;

import io.reactivex.rxjava3.core.Completable;

@Component
public class PdfParserServiceImpl implements PdfParserService
{
	@Autowired
	Document mDoc;

	@Override
	public Completable run(PDDocument doc) throws IOException
	{
		return Completable.fromAction(() -> {
			int i = 0;
			for (PDPage p : doc.getPages())
			{
				DPage page = new DPage(i, (int) p.getBBox().getHeight(), (int) p.getBBox().getWidth());
				PageReaderListenerService reader = new PageReaderListenerServiceImpl(p, i);
				page.setLines(reader.getLines());
				mDoc.addPage(page);
				mDoc.pageCountAdd();
				i++;
			}
			doc.close();
		});
		//		int i = 0;
		//		for (PDPage p : doc.getPages())
		//		{
		//			DPage page = new DPage(i, (int) p.getBBox().getHeight(), (int) p.getBBox().getWidth());
		//			PageReaderListenerService reader = new PageReaderListenerServiceImpl(p, i);
		//			page.setLines(reader.getLines());
		//			mDoc.addPage(page);
		//			i++;
		//		}
		//		doc.close();
		//		doc.close();
		//		PDPage p = doc.getPage(0);
		//		mDoc.setHeight((int) p.getBBox().getHeight());
		//		mDoc.setWidth((int) p.getBBox().getWidth());
		//		PageReaderListenerService reader = new PageReaderListenerServiceImpl(p, 0);
		//		results.addAll(reader.getLines());
		//		doc.close();
		//		return results;
	}

}
