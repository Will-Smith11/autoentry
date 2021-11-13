package com.autoentry.server.entities;

import java.util.List;

public class DetectedParagraph
{
	private Integer pageNum;
	private String paragraphData;
	private List<DetectedWord> detectedWordList;
	private BoundingBox paragraphBoundingBox;

	public DetectedParagraph(Integer pageNum, String data, List<DetectedWord> detectedWordList, BoundingBox paragraphBB)
	{
		this.pageNum = pageNum;
		this.paragraphData = data;
		this.detectedWordList = detectedWordList;
		this.paragraphBoundingBox = paragraphBB;
	}

	public Integer getPageNum()
	{
		return pageNum;
	}

	public String getParagraphData()
	{
		return paragraphData;
	}

	public void setParagraphData(String paragraphData)
	{
		this.paragraphData = paragraphData;
	}

	public List<DetectedWord> getDetectedWordList()
	{
		return detectedWordList;
	}

	public void setDetectedWordList(List<DetectedWord> detectedWordList)
	{
		this.detectedWordList = detectedWordList;
	}

	public BoundingBox getParagraphBoundingBox()
	{
		return paragraphBoundingBox;
	}

	public void setParagraphBoundingBox(BoundingBox paragraphBoundingBox)
	{
		this.paragraphBoundingBox = paragraphBoundingBox;
	}

}
