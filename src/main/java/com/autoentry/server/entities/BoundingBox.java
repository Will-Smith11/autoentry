package com.autoentry.server.entities;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.opencv.core.Point;

public class BoundingBox
{
	private RelitivePoint[] pointVals;

	public void setPoints(RelitivePoint[] p)
	{
		pointVals = p;
	}

	public RelitivePoint[] getPoints()
	{
		return pointVals;
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

		if (obj instanceof BoundingBox)
		{
			BoundingBox b = (BoundingBox) obj;
			List<Point> localArray = Arrays.asList(pointVals);
			List<Point> objArray = Arrays.asList(b.pointVals); // TODO fix so that we account for eps of the points so that similar boxes don't overlap
			Collections.sort(localArray, new BoundBoxComparator());
			Collections.sort(objArray, new BoundBoxComparator());
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
