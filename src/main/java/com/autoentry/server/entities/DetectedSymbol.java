package com.autoentry.server.entities;

import com.google.cloud.vision.v1.BoundingPoly;

public class DetectedSymbol
{
	private Integer pageNum;
	private BoundingBox symbolBoundingBox;
	private String symbolData;
	private BoundingPoly boundingPoly;

	public DetectedSymbol(Integer pageNum, BoundingPoly boundingPoly, String symbolData)
	{
		this.pageNum = pageNum;
		//		this.setSymbolBoundingBox(symbolBoundingBox);
		this.boundingPoly = boundingPoly;
		this.setSymbolData(symbolData);
	}

	public String getSymbolData()
	{
		return symbolData;
	}

	public void setSymbolData(String symbolData)
	{
		this.symbolData = symbolData;
	}

	public BoundingBox getSymbolBoundingBox()
	{
		return symbolBoundingBox;
	}

	public void setSymbolBoundingBox(BoundingBox symbolBoundingBox)
	{
		this.symbolBoundingBox = symbolBoundingBox;
	}

	public Integer getPageNum()
	{
		return pageNum;
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
		result = prime * result + ((pageNum == null) ? 0 : pageNum.hashCode());
		result = prime * result + ((symbolData == null) ? 0 : symbolData.hashCode());
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
		DetectedSymbol other = (DetectedSymbol) obj;
		if (boundingPoly == null)
		{
			if (other.boundingPoly != null)
				return false;
		}
		else if (!boundingPoly.equals(other.boundingPoly))
			return false;
		if (pageNum == null)
		{
			if (other.pageNum != null)
				return false;
		}
		else if (!pageNum.equals(other.pageNum))
			return false;
		if (symbolData == null)
		{
			if (other.symbolData != null)
				return false;
		}
		else if (!symbolData.equals(other.symbolData))
			return false;
		return true;
	}

}
