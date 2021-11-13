package com.autoentry.server.entities;

import java.util.List;

public class DetectedWord
{
	private Integer pageNum;
	private BoundingBox WordBoundingBox;
	private String wordData;
	private List<DetectedSymbol> detectedSmybolList;

	public DetectedWord(Integer pageNum, BoundingBox wordBoundingBox, String wordData, List<DetectedSymbol> detectedSmybolList)
	{
		this.pageNum = pageNum;
		WordBoundingBox = wordBoundingBox;
		this.wordData = wordData;
		this.detectedSmybolList = detectedSmybolList;
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

	public String getWordData()
	{
		return wordData;
	}

	public void setWordData(String wordData)
	{
		this.wordData = wordData;
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
