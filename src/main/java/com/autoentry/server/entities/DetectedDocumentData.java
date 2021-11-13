package com.autoentry.server.entities;

public class DetectedDocumentData
{
	private String pgText;
	private BoundingBox boundingBox;
	private Integer pgNum;

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

	public void addText(String text)
	{
		pgText += " " + text;
	}

	public BoundingBox getBoundingBox()
	{
		return boundingBox;
	}

	public void setBoundingBox(BoundingBox b)
	{
		this.boundingBox = b;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj instanceof DetectedDocumentData)
		{
			DetectedDocumentData d = (DetectedDocumentData) obj;
			return (d.getPgText().equals(this.pgText) || d.getBoundingBox().equals(this.boundingBox));
		}
		else if (obj instanceof Label)
		{
			Label l = (Label) obj;
			return l.getOutline().equals(this.boundingBox);
		}
		return false;
	}

	@Override
	public String toString()
	{
		return pgText;
	}

	public Integer getPgNum()
	{
		return pgNum;
	}

	public void setPgNum(Integer pgNum)
	{
		this.pgNum = pgNum;
	}

}
