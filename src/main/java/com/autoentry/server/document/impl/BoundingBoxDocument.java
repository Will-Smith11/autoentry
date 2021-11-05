package com.autoentry.server.document.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.autoentry.server.beans.Document;
import com.autoentry.server.entities.BoundingBox;
import com.autoentry.server.entities.DetectedDocumentData;
import com.autoentry.server.entities.Label;
import com.autoentry.server.entities.Line;
import com.autoentry.server.entities.RelatedBoundingBox;
import com.autoentry.server.interfaces.BaseDocument;
import com.autoentry.server.service.BoundingBoxGenService;
import com.autoentry.server.service.DocumentOcrService;
import com.autoentry.server.service.PdfParserService;
import com.autoentry.server.service.RelatedBoundingBoxGenService;
import com.autoentry.server.util.PdfTransferUtil;
import com.google.cloud.vision.v1.Block;
import com.google.cloud.vision.v1.Paragraph;
import com.google.cloud.vision.v1.Symbol;
import com.google.cloud.vision.v1.Word;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

@Component
public class BoundingBoxDocument implements BaseDocument
{
	@Autowired
	Document doc;

	@Autowired
	PdfParserService parser;

	@Autowired
	DocumentOcrService ocr;

	@Autowired
	BoundingBoxGenService bbg;

	@Autowired
	RelatedBoundingBoxGenService genRelation;

	private List<Block> blocks = new ArrayList<>();
	private List<Paragraph> paragraphs = new ArrayList<>();
	private List<Word> words = new ArrayList<>();
	private List<Symbol> smybols = new ArrayList<>();
	private List<BoundingBox> boundingBoxes = new ArrayList<>();

	@Override
	public String getSourcePath()
	{
		return doc.getSourcePath();
	}

	@Override
	public String getResultPath()
	{
		return doc.getResultPath();
	}

	@Override
	public String getUploadBucketName()
	{
		return doc.getUploadBucketName();
	}

	@Override
	public String getProjectId()
	{
		return doc.getProjectId();
	}

	@Override
	public String getGcsSrcPath()
	{
		return doc.getGcsSrcPath();
	}

	@Override
	public String getGcsDestPath()
	{
		return doc.getGcsDestPath();
	}

	@Override
	public void setHeight(int height)
	{
		doc.setHeight(height);

	}

	@Override
	public void setWidth(int width)
	{
		doc.setWidth(width);
	}

	private List<Line> fixLines(List<Line> l)
	{

		final int h = doc.getHeight();
		return l.stream().map(v -> {
			v.a.y = h - v.a.y;
			v.b.y = h - v.b.y;
			return v;
		}).collect(Collectors.toList());
	}

	@Override
	public Single<HashMap<Label, DetectedDocumentData>> getResults()
	{
		//		return null;
		try
		{
			return genMeta().andThen(processMeta());
			//			return processMeta();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		//		return doc.getResults();
		return null;
	}

	public Completable genMeta() throws Exception
	{
		//		Single<PDDocument> single = Single.create(singleSubscriber -> {
		//			PDDocument d = PdfTransferUtil.getDoc(doc.getProjectId(), doc.getUploadBucketName(), "test1");
		//			ocr.run().subscribe();
		//			singleSubscriber.onSuccess(d);
		//		});
		//
		//		single.map(d -> {
		//			List<Line> r = parser.run(d);
		//			d.close();
		//			return r;
		//		}).map(l -> fixLines(l)).map(l -> bbg.getBoundingBoxes(l, doc.getEPS())).doOnSuccess(b -> boundingBoxes = b)
		//				.blockingSubscribe();

		return Completable.fromAction(() -> {
			PDDocument d = PdfTransferUtil.getDoc(doc.getProjectId(), doc.getUploadBucketName(), "test1");
			List<Line> l = parser.run(d);
			d.close();
			this.boundingBoxes = bbg.getBoundingBoxes(fixLines(l), doc.getEPS());
			ocr.run().blockingAwait();
		});

		//		ocr.run().blockingSubscribe();

		//		AtomicReference<PDDocument> pddoc = new AtomicReference<>();
		//		//		return Completable.fromAction(() -> {
		//		Completable.fromAction(() -> pddoc.set(PdfTransferUtil.getDoc(doc.getProjectId(), doc.getUploadBucketName(), "test")))
		//				.blockingSubscribe(() -> {
		//					boundingBoxes = bbg.getBoundingBoxes(fixLines(parser.run(pddoc.get())), doc.getEPS());
		//					//				doc.getEPS());
		//				});
		//		PDDocument pddoc = PdfTransferUtil.getDoc(doc.getProjectId(), doc.getUploadBucketName(), "test");

		//		System.out.println(pddoc.getNumberOfPages());
		//		boundingBoxes = bbg.getBoundingBoxes(fixLines(parser.run(), doc),
		//				doc.getEPS());

		//		});
		//		try
		//		{
		//			ocr.run().subscribe();
		//		}
		//		catch (Exception e)
		//		{
		//			// TODO Auto-generated catch block
		//			e.printStackTrace();
		//		}
	}

	public Single<HashMap<Label, DetectedDocumentData>> processMeta()
	{
		return Single.create(singleSubscriber -> {
			HashMap<Label, DetectedDocumentData> gResults = new HashMap<>();
			List<RelatedBoundingBox<Block>> e = genRelation.getBlockBox();
			int i = 0;
			for (RelatedBoundingBox<Block> b : e)
			{

				String pageText = "";
				for (Block block : b.getContent())
				{
					String blockText = "";
					List<Paragraph> added = new ArrayList<>(block.getParagraphsList());
					added.addAll(genRelation.getParaBox()
							.stream()
							.filter(v -> v.getBoundingBox().equals(b.getBoundingBox()))
							.flatMap(v -> v.getContent().stream())
							.collect(Collectors.toList()));

					for (Paragraph para : added)
					{
						String paraText = "";
						List<Word> addedW = new ArrayList<>(para.getWordsList());
						addedW.addAll(genRelation.getWordBox()
								.stream()
								.filter(v -> v.getBoundingBox().equals(b.getBoundingBox()))
								.flatMap(v -> v.getContent().stream())
								.collect(Collectors.toList()));

						for (Word word : addedW)
						{
							String wordText = "";

							List<Symbol> addedS = new ArrayList<>(word.getSymbolsList());
							addedS.addAll(genRelation.getSymbolBox()
									.stream()
									.filter(v -> v.getBoundingBox().equals(b.getBoundingBox()))
									.flatMap(v -> v.getContent().stream())
									.collect(Collectors.toList()));

							for (Symbol symbol : addedS)
							{
								wordText = wordText + symbol.getText();
							}
							paraText = String.format("%s %s", paraText, wordText);
						}
						blockText = blockText + paraText;
					}
					pageText = pageText + blockText;
				}
				gResults.put(new Label("test" + i, b.getBoundingBox()), new DetectedDocumentData(b.getBoundingBox(), pageText));
				//			doc.addResult(new Label("test" + i, b.getBoundingBox()), new DetectedDocumentData(b.getBoundingBox(), pageText));
				i++;
			}
			singleSubscriber.onSuccess(gResults);
		});

		//		HashMap<Label, DetectedDocumentData> gResults = new HashMap<>();
		//		List<RelatedBoundingBox<Block>> e = genRelation.getBlockBox();
		//
		//		int i = 0;
		//		for (RelatedBoundingBox<Block> b : e)
		//		{
		//
		//			String pageText = "";
		//			for (Block block : b.getContent())
		//			{
		//				String blockText = "";
		//				List<Paragraph> added = new ArrayList<>(block.getParagraphsList());
		//				added.addAll(genRelation.getParaBox()
		//						.stream()
		//						.filter(v -> v.getBoundingBox().equals(b.getBoundingBox()))
		//						.flatMap(v -> v.getContent().stream())
		//						.collect(Collectors.toList()));
		//
		//				for (Paragraph para : added)
		//				{
		//					String paraText = "";
		//					List<Word> addedW = new ArrayList<>(para.getWordsList());
		//					addedW.addAll(genRelation.getWordBox()
		//							.stream()
		//							.filter(v -> v.getBoundingBox().equals(b.getBoundingBox()))
		//							.flatMap(v -> v.getContent().stream())
		//							.collect(Collectors.toList()));
		//
		//					for (Word word : addedW)
		//					{
		//						String wordText = "";
		//
		//						List<Symbol> addedS = new ArrayList<>(word.getSymbolsList());
		//						addedS.addAll(genRelation.getSymbolBox()
		//								.stream()
		//								.filter(v -> v.getBoundingBox().equals(b.getBoundingBox()))
		//								.flatMap(v -> v.getContent().stream())
		//								.collect(Collectors.toList()));
		//
		//						for (Symbol symbol : addedS)
		//						{
		//							wordText = wordText + symbol.getText();
		//						}
		//						paraText = String.format("%s %s", paraText, wordText);
		//					}
		//					blockText = blockText + paraText;
		//				}
		//				pageText = pageText + blockText;
		//			}
		//			gResults.put(new Label("test" + i, b.getBoundingBox()), new DetectedDocumentData(b.getBoundingBox(), pageText));
		//			//			doc.addResult(new Label("test" + i, b.getBoundingBox()), new DetectedDocumentData(b.getBoundingBox(), pageText));
		//			i++;
		//		}

	}

	@Override
	public float getWidth()
	{
		return doc.getWidth();
	}

	@Override
	public double getEPS()
	{
		return doc.getEPS();
	}

	@Override
	public float getWidthDiv()
	{
		return doc.getWidthDiv();
	}

	@Override
	public float getHeight()
	{
		return doc.getHeight();
	}

	@Override
	public float getHeightDiv()
	{
		return doc.getHeightDiv();
	}

	@Override
	public List<BoundingBox> getBoundingBoxes()
	{
		return boundingBoxes;
	}

	@Override
	public List<Block> getBlocks()
	{
		return blocks;
	}

	@Override
	public List<Paragraph> getParagraphs()
	{
		return paragraphs;
	}

	@Override
	public List<Word> getWords()
	{
		return words;
	}

	@Override
	public List<Symbol> getSmybols()
	{
		return smybols;
	}

	@Override
	public void setBlocks(List<Block> block)
	{
		this.blocks = block;

	}

	@Override
	public void setParagrpah(List<Paragraph> para)
	{
		this.paragraphs = para;

	}

	@Override
	public void setWords(List<Word> words)
	{
		this.words = words;

	}

	@Override
	public void setSymbols(List<Symbol> symbols)
	{
		this.smybols = symbols;

	}
}
