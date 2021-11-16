package com.autoentry.server.entities;

import java.util.List;

import com.google.cloud.vision.v1.BoundingPoly;

public class DetectedWord
{
	private Integer pageNum;
	private BoundingBox WordBoundingBox;
	private List<DetectedSymbol> detectedSmybolList;
	private BoundingPoly boundingPoly;

	public DetectedWord(Integer pageNum, List<DetectedSymbol> detectedSmybolList, BoundingPoly boundingPoly)
	{
		this.pageNum = pageNum;
		//		WordBoundingBox = wordBoundingBox;

		this.detectedSmybolList = detectedSmybolList;
		this.boundingPoly = boundingPoly;
	}

	public Integer getPageNum()
	{
		return pageNum;
	}

	public BoundingBox getWordBoundingBox()
	{
		return WordBoundingBox;
	}

	public void setWordBoundingBox(BoundingBox wordBoundingBox)
	{
		WordBoundingBox = wordBoundingBox;
	}

	public BoundingPoly getBoundingPoly()
	{
		return this.boundingPoly;
	}

	public List<DetectedSymbol> getDetectedSmybolList()
	{
		return detectedSmybolList;
	}

	public void setDetectedSmybolList(List<DetectedSymbol> detectedSmybolList)
	{
		this.detectedSmybolList = detectedSmybolList;
	}

}
