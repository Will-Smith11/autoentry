package com.autoentry.server.entities;

import org.opencv.core.Point;

public class Line
{
	public RelitivePoint a;
	public RelitivePoint b;
	double bVal;
	double slope;
	final double variance = 0.3;
	int page;

	public Line(RelitivePoint a, RelitivePoint b, int page)
	{
		this.a = a;
		this.b = b;
		this.slope = genSlope();
		this.bVal = genBVal();
		this.page = page;
	}

	private double genBVal()
	{
		return a.y - (slope * a.x);
	}

	private double genSlope()
	{
		return ((b.y - a.y) / (b.x - a.x));
	}

	public Point intersectlinePoint(Line l) // missing case where vertical line crosses horizontal line
	{
		// check to make sure its is in the scope of the lines
		// compare and use variance to see how close they get
		if (hasIntersection(l))
		{
			double x = (int) ((l.getBVal() - bVal) / (slope - l.slope));
			double y = (int) (slope * x + bVal);
			return new Point(x, y);
		}
		return null;

	}

	public boolean hasIntersection(Line l)
	{
		try
		{
			double x = (l.getBVal() - bVal) / (slope - l.slope);
			double y = slope * x + bVal;
			if (a.x < b.x) // b is bigger x value
			{
				if ((a.x - variance) <= x && (b.x + variance) >= x)
				{
					return true;
				}
			}
			else
			{
				if ((a.x + variance) >= x && (b.x - variance) <= x)
				{
					return true;
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
		return false;
	}

	public RelitivePoint getA()
	{
		return a;
	}

	public RelitivePoint getB()
	{
		return b;
	}

	public double getSlope()
	{
		return slope;
	}

	public double getBVal()
	{
		return bVal;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj instanceof Line)
		{
			Line l = (Line) obj;
			return (l.a.x == a.x && l.a.y == a.y && l.b.x == b.x && l.b.y == b.y);
		}
		else
		{
			return false;
		}
	}

	@Override
	public String toString()
	{
		return "( Point a: x:" + a.x + " y:" + a.y + " Point b: x:" + b.x + " y:" + b.y + ")";
	}
}
