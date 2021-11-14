package com.autoentry.server.entities;

import java.util.ArrayList;
import java.util.List;

import com.google.cloud.vision.v1.Block;
import com.google.cloud.vision.v1.Paragraph;
import com.google.cloud.vision.v1.Symbol;
import com.google.cloud.vision.v1.Word;

public class DPage
{
	private int pageNum;
	private int pageHeight;
	private int pageWidth;
	private List<Block> blocks = new ArrayList<>();
	private List<Paragraph> paragraphs = new ArrayList<>();
	private List<Word> words = new ArrayList<>();
	private List<Symbol> smybols = new ArrayList<>();
	private List<BoundingBox> boundingBoxes = new ArrayList<>();
	private List<Line> lines = new ArrayList<>();

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

	public List<Block> getBlocks()
	{
		return blocks;
	}

	public void setBlocks(List<Block> blocks)
	{
		this.blocks = blocks;
	}

	public List<Paragraph> getParagraphs()
	{
		return paragraphs;
	}

	public void setParagraphs(List<Paragraph> paragraphs)
	{
		this.paragraphs = paragraphs;
	}

	public List<Word> getWords()
	{
		return words;
	}

	public void setWords(List<Word> words)
	{
		this.words = words;
	}

	public List<Symbol> getSmybols()
	{
		return smybols;
	}

	public void setSmybols(List<Symbol> smybols)
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

	public List<Line> getLines()
	{
		return lines;
	}

	public void setLines(List<Line> lines)
	{
		this.lines = lines;
	}
}
