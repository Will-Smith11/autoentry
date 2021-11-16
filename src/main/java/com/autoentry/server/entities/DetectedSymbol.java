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
}
