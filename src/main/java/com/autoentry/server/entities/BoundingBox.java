package com.autoentry.server.entities;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.opencv.core.Point;

public class BoundingBox
{
	Line[] vals = new Line[4];
	public RelitivePoint[] pointVals;
	int index = 0;
	private double EPS;

	public RelitivePoint[] getPoints()
	{
		return pointVals;
	}

	public BoundingBox(double ePS)
	{
		this.EPS = ePS;
	}

	public boolean isComplete()
	{
		return (index > 3);
	}

	public boolean addLine(Line l)
	{
		if (isComplete())
		{
			return false;
		}
		vals[index] = l;
		index++;
		return true;
	}

	public boolean contains(Line l2)
	{
		for (Line l : vals)
		{
			if (l != null)
			{
				if (l.equals(l2))
				{
					return true;
				}
			}
		}
		return false;
	}

	public boolean hasIntersection(Line l2)
	{
		for (Line l : vals)
		{
			if (l != null)
			{
				if (l.hasIntersection(l2))
				{
					return true;
				}
			}
		}
		return false;
	}

	public Line[] getLines()
	{
		return vals;
	}

	@Override
	public String toString()
	{
		String result = "";
		for (Point l : pointVals)
		{
			if (l != null)
			{
				result += l.toString() + " ";
			}
		}
		result += "\n";
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		System.out.println("Bounding box equals");

		if (obj instanceof BoundingBox)
		{
			BoundingBox b = (BoundingBox) obj;
			List<Point> localArray = Arrays.asList(pointVals);
			List<Point> objArray = Arrays.asList(b.pointVals); // TODO fix so that we account for eps of the points so that similar boxes don't overlap
			Collections.sort(localArray, new BoundBoxComparator());
			Collections.sort(objArray, new BoundBoxComparator());
			System.out.println(localArray);
			System.out.println(objArray);
			return localArray.equals(objArray);

		}
		else
		{
			return false;
		}
	}

	protected class BoundBoxComparator implements Comparator<Point>
	{
		@Override
		public int compare(Point o1, Point o2)
		{
			int result = (int) (o1.x - o2.x);
			if (result == 0)
			{
				return (int) (o1.y - o2.y);
			}
			return result;
		}
	}
}
