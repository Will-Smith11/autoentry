package com.autoentry.server.service;

import java.util.List;

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

}
