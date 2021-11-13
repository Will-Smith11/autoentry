package com.autoentry.server.entities;

import java.util.List;

public class DetectedBlock
{
	private BoundingBox blockBoundingBox;
	private String data;
	private List<DetectedParagraph> detectedParagraphList;
	private int pageNum;

	public DetectedBlock(BoundingBox blockBB, String data, List<DetectedParagraph> detectedParaList, int pageNum)
	{
		this.blockBoundingBox = blockBB;
		this.data = data;
		this.detectedParagraphList = detectedParaList;
		this.pageNum = pageNum;
	}

	public int getPageNum()
	{
		return this.pageNum;
	}

	public BoundingBox getBlockBoundingBox()
	{
		return blockBoundingBox;
	}

	public void setBlockBoundingBox(BoundingBox blockBoundingBox)
	{
		this.blockBoundingBox = blockBoundingBox;
	}

	public String getData()
	{
		return data;
	}

	public void setData(String data)
	{
		this.data = data;
	}

	public List<DetectedParagraph> getDetectedParagraphList()
	{
		return detectedParagraphList;
	}

	public void setDetectedParagraphList(List<DetectedParagraph> detectedParagraphList)
	{
		this.detectedParagraphList = detectedParagraphList;
	}

}
