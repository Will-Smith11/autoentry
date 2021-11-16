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

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((boundingPoly == null) ? 0 : boundingPoly.hashCode());
		result = prime * result + ((detectedSmybolList == null) ? 0 : detectedSmybolList.hashCode());
		result = prime * result + ((pageNum == null) ? 0 : pageNum.hashCode());
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
		DetectedWord other = (DetectedWord) obj;
		if (boundingPoly == null)
		{
			if (other.boundingPoly != null)
				return false;
		}
		else if (!boundingPoly.equals(other.boundingPoly))
			return false;
		if (detectedSmybolList == null)
		{
			if (other.detectedSmybolList != null)
				return false;
		}
		else if (!detectedSmybolList.equals(other.detectedSmybolList))
			return false;
		if (pageNum == null)
		{
			if (other.pageNum != null)
				return false;
		}
		else if (!pageNum.equals(other.pageNum))
			return false;
		return true;
	}

}
