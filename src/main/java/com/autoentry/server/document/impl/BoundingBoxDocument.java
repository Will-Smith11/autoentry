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
import com.autoentry.server.entities.DPage;
import com.autoentry.server.entities.DetectedBlock;
import com.autoentry.server.entities.DetectedDocumentData;
import com.autoentry.server.entities.DetectedParagraph;
import com.autoentry.server.entities.DetectedSymbol;
import com.autoentry.server.entities.DetectedWord;
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
	public float getPageWidth(Integer pgNum)
	{
		return doc.getPage(pgNum).getPageWidth();
	}

	@Override
	public float getPageHeight(Integer pgNum)
	{
		return doc.getPage(pgNum).getPageHeight();
	}

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

	private List<Line> fixLines(List<Line> l, DPage page)
	{

		final int h = page.getPageHeight();
		return l.stream().map(v -> {
			v.a.y = h - v.a.y;
			v.b.y = h - v.b.y;
			return v;
		}).collect(Collectors.toList());
	}

	@Override
	public Single<HashMap<Label, DetectedDocumentData>> getResults()
	{
		try
		{
			return genMeta().andThen(processMeta());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Single<List<HashMap<Label, DetectedDocumentData>>> getBetaResults()
	{
		try
		{
			return genMeta().andThen(processMetaV2().map(v -> v.stream().map(p -> p.getPageResults()).collect(Collectors.toList())));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	public Completable genMeta() throws Exception
	{
		return Completable.fromAction(() -> {
			PDDocument d = PdfTransferUtil.getDoc(doc.getProjectId(), doc.getUploadBucketName(), "test1");
			parser.run(d).blockingSubscribe();
			d.close();
			for (DPage page : doc.getPages())
			{
				page.setBoundingBoxes(bbg.getBoundingBoxes(fixLines(page.getLines(), page), doc.getEPS()));
			}
			//			this.boundingBoxes = bbg.getBoundingBoxes(fixLines(l), doc.getEPS());
			ocr.run().blockingAwait();
		});

	}

	@SuppressWarnings("unlikely-arg-type")
	public Single<List<DPage>> processMetaV2()
	{
		return Single.create(singleSub -> {
			for (DPage page : doc.getPages())
			{
				List<RelatedBoundingBox<DetectedBlock>> e = genRelation.getDBlockBox(page.getPageNum());
				List<DetectedDocumentData> dataFound = new ArrayList<>();
				List<Label> labelsFound = new ArrayList<>();
				for (RelatedBoundingBox<DetectedBlock> b : e)
				{

					String pageText = "";
					for (DetectedBlock block : b.getContent())
					{
						String blockText = "";
						List<DetectedParagraph> added = new ArrayList<>(block.getDetectedParagraphList());
						added.addAll(genRelation.getDParaBox(page.getPageNum())
								.stream()
								.filter(v -> v.getBoundingBox().equals(b.getBoundingBox()))
								.flatMap(v -> v.getContent().stream())
								.collect(Collectors.toList()));

						for (DetectedParagraph para : added)
						{
							String paraText = "";
							List<DetectedWord> addedW = new ArrayList<>(para.getDetectedWordList());
							addedW.addAll(genRelation.getDWordBox(page.getPageNum())
									.stream()
									.filter(v -> v.getBoundingBox().equals(b.getBoundingBox()))
									.flatMap(v -> v.getContent().stream())
									.collect(Collectors.toList()));

							for (DetectedWord word : addedW)
							{
								String wordText = "";

								List<DetectedSymbol> addedS = new ArrayList<>(word.getDetectedSmybolList());
								addedS.addAll(genRelation.getDSymbolBox(page.getPageNum())
										.stream()
										.filter(v -> v.getBoundingBox().equals(b.getBoundingBox()))
										.flatMap(v -> v.getContent().stream())
										.collect(Collectors.toList()));

								for (DetectedSymbol symbol : addedS)
								{
									wordText = wordText + symbol.getSymbolData();
								}
								paraText = String.format("%s %s", paraText, wordText);
							}
							blockText = blockText + paraText;
						}
						pageText = pageText + blockText;
					}
					boolean isLabel = false;

					for (String l : doc.getLabels()) // sees if detected text is a label
					{
						if (pageText.contains(l))
						{
							//						pageText.ind
							String otherText = "";
							if (pageText.startsWith(l))
							{
								otherText = pageText.substring(l.length());
							}
							else
							{
								otherText = pageText;
							}

							if (otherText.length() < 1)
							{
								isLabel = true;
							}

							Label lb = new Label(l, b.getBoundingBox());
							if (!labelsFound.contains(lb))
							{
								labelsFound.add(lb);
							}
							pageText = otherText;
							break;
						}
					}
					if (!isLabel)
					{
						DetectedDocumentData d = new DetectedDocumentData(b.getBoundingBox(), pageText);
						if (!dataFound.contains(d))
						{
							dataFound.add(d);
						}
						else
						{
							DetectedDocumentData got = dataFound.get(dataFound.indexOf(d));
							got.addText(pageText);
						}
					}
				}

				for (Label label : labelsFound)
				{
					if (dataFound.contains(label))
					{
						DetectedDocumentData d = dataFound.get(dataFound.indexOf(label));
						page.addResult(label, d);
					}
				}

			}
			singleSub.onSuccess(doc.getPages());
		});
	}

	@Deprecated
	@SuppressWarnings("unlikely-arg-type")
	public Single<HashMap<Label, DetectedDocumentData>> processMeta()
	{
		return Single.create(singleSubscriber -> {

			HashMap<Label, DetectedDocumentData> gResults = new HashMap<>();
			List<Label> labelsFound = new ArrayList<>();
			List<DetectedDocumentData> dataFound = new ArrayList<>();

			List<RelatedBoundingBox<Block>> e = genRelation.getBlockBox();
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
				boolean isLabel = false;

				for (String l : doc.getLabels()) // sees if detected text is a label
				{
					if (pageText.contains(l))
					{
						//						pageText.ind
						String otherText = "";
						if (pageText.startsWith(l))
						{
							otherText = pageText.substring(l.length());
						}
						else
						{
							otherText = pageText;
						}

						if (otherText.length() < 1)
						{
							isLabel = true;
						}

						Label lb = new Label(l, b.getBoundingBox());
						if (!labelsFound.contains(lb))
						{
							labelsFound.add(lb);
						}
						pageText = otherText;
						break;
					}
				}
				if (!isLabel)
				{
					DetectedDocumentData d = new DetectedDocumentData(b.getBoundingBox(), pageText);
					if (!dataFound.contains(d))
					{
						dataFound.add(d);
					}
					else
					{
						DetectedDocumentData got = dataFound.get(dataFound.indexOf(d));
						got.addText(pageText);
					}
				}
			}

			for (Label label : labelsFound)
			{
				if (dataFound.contains(label))
				{
					DetectedDocumentData d = dataFound.get(dataFound.indexOf(label));
					gResults.put(label, d);
				}
			}

			singleSubscriber.onSuccess(gResults);
		});
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

	public List<DPage> getPages()
	{
		return doc.getPages();
	}

	public void setPages(List<DPage> pages)
	{
		doc.setPages(pages);
	}

	@Override
	public void addPage(DPage page)
	{
		doc.addPage(page);
	}

	@Override
	public List<DetectedBlock> getDBlocks(Integer pgNum)
	{
		return doc.getPage(pgNum).getBlocks();
	}

	@Override
	public List<DetectedParagraph> GetDParagraphs(Integer pgNum)
	{
		return doc.getPage(pgNum).getParagraphs();
	}

	@Override
	public List<DetectedWord> getDWords(Integer pgNum)
	{
		return doc.getPage(pgNum).getWords();
	}

	@Override
	public List<DetectedSymbol> getDSymbols(Integer pgNum)
	{
		return doc.getPage(pgNum).getSmybols();
	}

	@Override
	public List<BoundingBox> getDPageBoundingBoxes(Integer pgNum)
	{
		return doc.getPage(pgNum).getBoundingBoxes();
	}
}
