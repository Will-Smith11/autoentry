package com.autoentry.server.entities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DPage
{
	private int pageNum;
	private int pageHeight;
	private int pageWidth;
	private List<DetectedBlock> blocks = new ArrayList<>();
	private List<DetectedParagraph> paragraphs = new ArrayList<>();
	private List<DetectedWord> words = new ArrayList<>();
	private List<DetectedSymbol> smybols = new ArrayList<>();
	private List<BoundingBox> boundingBoxes = new ArrayList<>();
	private List<Line> lines = new ArrayList<>();
	private HashMap<Label, DetectedDocumentData> pageResults = new HashMap<>();

	public DPage(int pageNum, int pageHeight, int pageWidth)
	{
		this.pageNum = pageNum;
		this.pageHeight = pageHeight;
		this.pageWidth = pageWidth;
	}

	public int getPageNum()
	{
		return pageNum;
	}

	public void setPageNum(int pageNum)
	{
		this.pageNum = pageNum;
	}

	public int getPageHeight()
	{
		return pageHeight;
	}

	public void setPageHeight(int pageHeight)
	{
		this.pageHeight = pageHeight;
	}

	public int getPageWidth()
	{
		return pageWidth;
	}

	public void setPageWidth(int pageWidth)
	{
		this.pageWidth = pageWidth;
	}

	public List<DetectedBlock> getBlocks()
	{
		return blocks;
	}

	public void setBlocks(List<DetectedBlock> blocks)
	{
		this.blocks = blocks;
	}

	public List<DetectedParagraph> getParagraphs()
	{
		return paragraphs;
	}

	public void setParagraphs(List<DetectedParagraph> paragraphs)
	{
		this.paragraphs = paragraphs;
	}

	public List<DetectedWord> getWords()
	{
		return words;
	}

	public void setWords(List<DetectedWord> words)
	{
		this.words = words;
	}

	public List<DetectedSymbol> getSmybols()
	{
		return smybols;
	}

	public void setSmybols(List<DetectedSymbol> smybols)
	{
		this.smybols = smybols;
	}

	public List<BoundingBox> getBoundingBoxes()
	{
		return boundingBoxes;
	}

	public void setBoundingBoxes(List<BoundingBox> boundingBoxes)
	{
		this.boundingBoxes = boundingBoxes;
	}

	public List<Line> getLines()
	{
		return lines;
	}

	public void setLines(List<Line> lines)
	{
		this.lines = lines;
	}

	public HashMap<Label, DetectedDocumentData> getPageResults()
	{
		return pageResults;
	}

	public void addResult(Label l, DetectedDocumentData d)
	{
		pageResults.put(l, d);
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj instanceof DPage)
		{
			DPage page = (DPage) obj;
			return page.getPageNum() == this.pageNum;
		}
		else if (obj instanceof Integer)
		{
			int pgnum = (Integer) obj;
			return this.pageNum == pgnum;
		}
		return false;
	}
}
