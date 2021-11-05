package com.autoentry.server.entities;

import java.io.Serializable;

public class DetectedDocumentData implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8598853071261903152L;
	private String pgText;
	private BoundingBox boundingBox;

	public DetectedDocumentData(BoundingBox boundingBox, String pageText)
	{
		this.pgText = pageText;
		this.boundingBox = boundingBox;
	}

	public String getPgText()
	{
		return pgText;
	}

	public void setPgText(String pgText)
	{
		this.pgText = pgText;
	}

	public BoundingBox getB()
	{
		return boundingBox;
	}

	public void setB(BoundingBox b)
	{
		this.boundingBox = b;
	}

	@Override
	public String toString()
	{
		return pgText;
	}

}
