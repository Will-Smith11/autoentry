package com.autoentry.server.entities;

import java.util.List;

import com.google.cloud.vision.v1.BoundingPoly;

public class DetectedBlock
{
	private BoundingBox blockBoundingBox;
	private List<DetectedParagraph> detectedParagraphList;
	private int pageNum;
	private BoundingPoly boundingPoly;

	public DetectedBlock(List<DetectedParagraph> detectedParaList, int pageNum, BoundingPoly boundingPoly)
	{
		//		this.blockBoundingBox = blockBB;
		this.detectedParagraphList = detectedParaList;
		this.pageNum = pageNum;
		this.boundingPoly = boundingPoly;
	}

	public BoundingPoly getBoundingPoly()
	{
		return boundingPoly;
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

	public List<DetectedParagraph> getDetectedParagraphList()
	{
		return detectedParagraphList;
	}

	public void setDetectedParagraphList(List<DetectedParagraph> detectedParagraphList)
	{

		this.detectedParagraphList = detectedParagraphList;
	}
}
