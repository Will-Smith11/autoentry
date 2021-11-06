package com.autoentry.server.entities;

import java.util.ArrayList;
import java.util.List;

public class RelatedBoundingBox<T>
{
	protected BoundingBox box;
	protected List<T> content = new ArrayList<>();

	public RelatedBoundingBox(BoundingBox b, T content)
	{
		this.box = b;
		this.content.add(content);
	}

	public BoundingBox getBoundingBox()
	{
		return this.box;
	}

	public void addContent(T content)
	{
		if (!this.content.contains(content))
		{
			this.content.add(content);
		}
	}

	public List<T> getContent()
	{
		return this.content;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj instanceof RelatedBoundingBox)
		{
			RelatedBoundingBox<?> b = (RelatedBoundingBox<?>) obj;
			return b.box.equals(box);
		}
		return false;
	}
}
