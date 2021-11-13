package com.autoentry.server.entities;

public class DetectedSymbol
{
	private Integer pageNum;
	private BoundingBox symbolBoundingBox;
	private String symbolData;

	public DetectedSymbol(Integer pageNum, BoundingBox symbolBoundingBox, String symbolData)
	{
		this.pageNum = pageNum;
		this.setSymbolBoundingBox(symbolBoundingBox);
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
}
