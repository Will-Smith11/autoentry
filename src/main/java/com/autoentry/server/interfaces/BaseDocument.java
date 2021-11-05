package com.autoentry.server.interfaces;

import java.util.HashMap;
import java.util.List;

import com.autoentry.server.entities.BoundingBox;
import com.autoentry.server.entities.DetectedDocumentData;
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

	List<BoundingBox> getBoundingBoxes();

	List<Block> getBlocks();

	List<Paragraph> getParagraphs();

	List<Word> getWords();

	List<Symbol> getSmybols();

	void setBlocks(List<Block> block);

	void setParagrpah(List<Paragraph> para);

	void setWords(List<Word> words);

	void setSymbols(List<Symbol> symbols);

	//	HashMap<Label, DetectedDocumentData> getResults();

}
