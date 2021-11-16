package com.autoentry.server.interfaces;

import java.util.HashMap;
import java.util.List;

import com.autoentry.server.entities.BoundingBox;
import com.autoentry.server.entities.DPage;
import com.autoentry.server.entities.DetectedBlock;
import com.autoentry.server.entities.DetectedDocumentData;
import com.autoentry.server.entities.DetectedParagraph;
import com.autoentry.server.entities.DetectedSymbol;
import com.autoentry.server.entities.DetectedWord;
import com.autoentry.server.entities.Label;
import com.google.cloud.vision.v1.Block;
import com.google.cloud.vision.v1.Paragraph;
import com.google.cloud.vision.v1.Symbol;
import com.google.cloud.vision.v1.Word;

import io.reactivex.rxjava3.core.Single;

public interface BaseDocument
{
	Single<HashMap<Label, DetectedDocumentData>> getResults();

	String getSourcePath();

	String getResultPath();

	String getUploadBucketName();

	String getProjectId();

	String getGcsSrcPath();

	String getGcsDestPath();

	void setHeight(int height);

	void setWidth(int width);

	float getWidth();

	double getEPS();

	float getWidthDiv();

	float getHeight();

	float getHeightDiv();

	float getPageWidth(Integer pgNum);

	float getPageHeight(Integer pgNum);

	List<BoundingBox> getBoundingBoxes();

	List<BoundingBox> getDPageBoundingBoxes(Integer pgNum);

	List<Block> getBlocks();

	List<Paragraph> getParagraphs();

	List<Word> getWords();

	List<Symbol> getSmybols();

	List<DetectedBlock> getDBlocks(Integer pgNum);

	List<DetectedParagraph> GetDParagraphs(Integer pgNum);

	List<DetectedWord> getDWords(Integer pgNum);

	List<DetectedSymbol> getDSymbols(Integer pgNum);

	void setBlocks(List<Block> block);

	void setParagrpah(List<Paragraph> para);

	void setWords(List<Word> words);

	void setSymbols(List<Symbol> symbols);

	void addPage(DPage page);

	Single<List<HashMap<Label, DetectedDocumentData>>> getBetaResults();

	String getDocUploadName();

}
