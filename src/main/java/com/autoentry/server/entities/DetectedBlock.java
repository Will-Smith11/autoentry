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

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((boundingPoly == null) ? 0 : boundingPoly.hashCode());
		result = prime * result + ((detectedParagraphList == null) ? 0 : detectedParagraphList.hashCode());
		result = prime * result + pageNum;
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DetectedBlock other = (DetectedBlock) obj;
		if (boundingPoly == null)
		{
			if (other.boundingPoly != null)
				return false;
		}
		else if (!boundingPoly.equals(other.boundingPoly))
			return false;
		if (detectedParagraphList == null)
		{
			if (other.detectedParagraphList != null)
				return false;
		}
		else if (!detectedParagraphList.equals(other.detectedParagraphList))
			return false;
		if (pageNum != other.pageNum)
			return false;
		return true;
	}

}
