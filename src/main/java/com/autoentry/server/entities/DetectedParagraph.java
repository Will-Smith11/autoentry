package com.autoentry.server.entities;

import java.util.List;

import com.google.cloud.vision.v1.BoundingPoly;

public class DetectedParagraph
{
	private Integer pageNum;
	private String paragraphData;
	private List<DetectedWord> detectedWordList;
	private BoundingBox paragraphBoundingBox;
	private BoundingPoly boundingPoly;

	public DetectedParagraph(Integer pageNum, List<DetectedWord> detectedWordList, BoundingPoly boundingPoly)
	{
		this.pageNum = pageNum;
		//		this.paragraphData = data;
		this.detectedWordList = detectedWordList;
		this.boundingPoly = boundingPoly;
		//		this.paragraphBoundingBox = paragraphBB;
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

	public BoundingPoly getBoundingPoly()
	{
		return boundingPoly;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((boundingPoly == null) ? 0 : boundingPoly.hashCode());
		result = prime * result + ((detectedWordList == null) ? 0 : detectedWordList.hashCode());
		result = prime * result + ((pageNum == null) ? 0 : pageNum.hashCode());
		result = prime * result + ((paragraphData == null) ? 0 : paragraphData.hashCode());
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
		DetectedParagraph other = (DetectedParagraph) obj;
		if (boundingPoly == null)
		{
			if (other.boundingPoly != null)
				return false;
		}
		else if (!boundingPoly.equals(other.boundingPoly))
			return false;
		if (detectedWordList == null)
		{
			if (other.detectedWordList != null)
				return false;
		}
		else if (!detectedWordList.equals(other.detectedWordList))
			return false;
		if (pageNum == null)
		{
			if (other.pageNum != null)
				return false;
		}
		else if (!pageNum.equals(other.pageNum))
			return false;
		if (paragraphData == null)
		{
			if (other.paragraphData != null)
				return false;
		}
		else if (!paragraphData.equals(other.paragraphData))
			return false;
		return true;
	}

}
