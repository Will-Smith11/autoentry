package com.autoentry.server.interfaces;

import java.io.IOException;
import java.util.List;

import com.autoentry.server.beans.Document;
import com.autoentry.server.entities.BoundingBox;
import com.google.cloud.vision.v1.Block;
import com.google.cloud.vision.v1.Paragraph;
import com.google.cloud.vision.v1.Symbol;
import com.google.cloud.vision.v1.Word;

public interface BaseDocument
{
	String getSourcePath();

	String getResultPath();

	String getUploadBucketName();

	String getProjectId();

	String getGcsSrcPath();

	String getGcsDestPath();

	void setHeight(int height);

	void setWidth(int width);

	void genMeta() throws IOException;

	void processMeta();

	float getWidth();

	double getEPS();

	float getWidthDiv();

	float getHeight();

	float getHeightDiv();

	List<BoundingBox> getBoundingBoxes();

	List<Block> getBlocks();

	List<Paragraph> getParagraphs();

	List<Word> getWords();

	List<Symbol> getSmybols();

	void setBlocks(List<Block> block);

	void setParagrpah(List<Paragraph> para);

	void setWords(List<Word> words);

	void setSymbols(List<Symbol> symbols);

	Document getBean();

}
