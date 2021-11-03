package com.autoentry.server.entities;

import org.opencv.core.Point;

public class RelitivePoint extends Point
{
	private final double variance;

	public RelitivePoint(double x, double y, double variance)
	{
		super(x, y);
		this.variance = variance;
	}

	/**
	 * if the two points are within the variance range, is true
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (!(obj instanceof RelitivePoint))
		{
			return false;
		}
		RelitivePoint rp = (RelitivePoint) obj;
		return (Math.abs(this.x - rp.x) < variance && Math.abs(this.y - rp.y) < variance);
	}

	public boolean relitiveLNX(RelitivePoint p)
	{
		return (this.x - p.x) <= variance;
	}

	public boolean relitiveLNY(RelitivePoint p)
	{
		return (this.y - p.y) <= variance;
	}

	public boolean relitiveGNX(RelitivePoint p)
	{
		return (this.x - p.x) >= -variance;
	}

	public boolean relitiveGNY(RelitivePoint p)
	{
		return (this.y - p.y) >= -variance;
	}

	public boolean relitiveEqualsY(RelitivePoint p)
	{
		return Math.abs(this.y - p.y) <= variance;
	}

	public boolean relitiveEqualsX(RelitivePoint p)
	{
		return Math.abs(this.x - p.x) <= variance;
	}

	public boolean bb(RelitivePoint p)
	{
		return this.x <= p.x && this.y <= p.y;
	}

	public boolean sb(RelitivePoint p)
	{
		return this.x >= p.x && this.y <= p.y;
	}

	public boolean ss(RelitivePoint p)
	{
		return this.x >= p.x && this.y >= p.y;
	}

	public boolean bs(RelitivePoint p)
	{
		return this.x <= p.x && this.y >= p.y;
	}
}
