package com.autoentry.server.service;

import java.util.List;

import com.autoentry.server.entities.DetectedBlock;
import com.autoentry.server.entities.DetectedParagraph;
import com.autoentry.server.entities.DetectedSymbol;
import com.autoentry.server.entities.DetectedWord;
import com.autoentry.server.entities.RelatedBoundingBox;
import com.google.cloud.vision.v1.Block;
import com.google.cloud.vision.v1.Paragraph;
import com.google.cloud.vision.v1.Symbol;
import com.google.cloud.vision.v1.Word;

public interface RelatedBoundingBoxGenService
{
	public List<RelatedBoundingBox<Block>> getBlockBox();

	public List<RelatedBoundingBox<Paragraph>> getParaBox();

	public List<RelatedBoundingBox<Word>> getWordBox();

	public List<RelatedBoundingBox<Symbol>> getSymbolBox();

	public List<RelatedBoundingBox<DetectedBlock>> getDBlockBox(int pgNum);

	public List<RelatedBoundingBox<DetectedParagraph>> getDParaBox(int pgNum);

	public List<RelatedBoundingBox<DetectedWord>> getDWordBox(int pgNum);

	public List<RelatedBoundingBox<DetectedSymbol>> getDSymbolBox(int pgNum);

}
