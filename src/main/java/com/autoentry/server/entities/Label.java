package com.autoentry.server.entities;

public class Label
{

	private BoundingBox outline;
	private String label;
	private BoundingBox labelLocation;

	public Label(String label, BoundingBox outline)
	{
		this.outline = outline;
		this.label = label;
	}

	public Label(String label, BoundingBox outline, BoundingBox labelLocation)
	{
		this.outline = outline;
		this.label = label;
		this.labelLocation = labelLocation;
	}

	public BoundingBox getOutline()
	{
		return outline;
	}

	public void setOutline(BoundingBox outline)
	{
		this.outline = outline;
	}

	public String getLabel()
	{
		return label;
	}

	public BoundingBox getLabelLocation()
	{
		return labelLocation;
	}

	public void setLabelLocation(BoundingBox labelLocation)
	{
		this.labelLocation = labelLocation;
	}

	@Override
	public String toString()
	{
		return label;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj instanceof Label)
		{
			Label l = (Label) obj;
			return this.label.equals(l.getLabel());
		}
		else if (obj instanceof BoundingBox)
		{
			BoundingBox b = (BoundingBox) obj;
			return (this.outline.equals(b) || this.labelLocation.equals(b));
		}
		else if (obj instanceof DetectedDocumentData)
		{
			DetectedDocumentData d = (DetectedDocumentData) obj;
			return this.outline.equals(d.getBoundingBox());
		}
		else
		{
			return false;
		}
	}

}
