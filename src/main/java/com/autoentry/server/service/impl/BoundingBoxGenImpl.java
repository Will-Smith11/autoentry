package com.autoentry.server.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Stack;
import java.util.function.Function;

import org.opencv.core.Point;
import org.springframework.stereotype.Component;

import com.autoentry.server.entities.BoundingBox;
import com.autoentry.server.entities.Line;
import com.autoentry.server.entities.RelitivePoint;
import com.autoentry.server.service.BoundingBoxGenService;
import com.autoentry.server.util.LineSegmentLineSegmentIntersection;
import com.autoentry.server.util.LineSegmentLineSegmentIntersection.Pt;

import io.reactivex.rxjava3.core.Completable;

@Component
public class BoundingBoxGenImpl implements BoundingBoxGenService
{
	private double variance;
	private List<RelitivePoint> allPoints = new ArrayList<>();
	private List<RelitivePoint> copy;
	public List<Line> lines = new ArrayList<>();
	private List<BoundingBox> boxes = new ArrayList<>();
	private List<Function<RelitivePoint, RelitivePoint>> rdlu = new ArrayList<>();
	private List<Function<RelitivePoint, RelitivePoint>> dlur = new ArrayList<>();
	private List<Function<RelitivePoint, RelitivePoint>> lurd = new ArrayList<>();
	private List<Function<RelitivePoint, RelitivePoint>> urdl = new ArrayList<>();

	@Override
	public List<BoundingBox> getBoundingBoxes(List<Line> lines, double ePs)
	{
		preLineBuild(lines, ePs).subscribe();
		return getBoxes();
	}

	private Completable preLineBuild(List<Line> li, double ePS)
	{
		return Completable.fromAction(() -> {
			this.variance = ePS;
			buildSearchFuncitons();
			for (Line l : li)
			{
				allPoints.add(l.getA());
				allPoints.add(l.getB());
				lines.add(l);
			}
			genIntersectionPoints();
			copy = new ArrayList<>(allPoints);
			genBoxesV2();
		});
	}

	private void buildSearchFuncitons()
	{
		rdlu.add(this::rd);
		rdlu.add(this::dl);
		rdlu.add(this::lu);
		rdlu.add(this::ur);
		dlur.add(this::dl);
		dlur.add(this::lu);
		dlur.add(this::ur);
		dlur.add(this::rd);
		lurd.add(this::lu);
		lurd.add(this::ur);
		lurd.add(this::rd);
		lurd.add(this::dl);
		urdl.add(this::ur);
		urdl.add(this::rd);
		urdl.add(this::dl);
		urdl.add(this::lu);
	}

	private List<RelitivePoint> getAllPoints()
	{
		return copy;
	}

	private List<BoundingBox> getBoxes()
	{
		return boxes;
	}

	private void genIntersectionPoints()
	{
		for (Line l1 : lines)
		{
			for (Line l2 : lines)
			{
				if (!l1.equals(l2))
				{
					Pt[] points = LineSegmentLineSegmentIntersection.lineSegmentLineSegmentIntersection(new Pt(l1.a.x, l1.a.y, variance),
							new Pt(l1.b.x, l1.b.y, variance),
							new Pt(l2.a.x, l2.a.y, variance), new Pt(l2.b.x, l2.b.y, variance), variance);
					for (int i = 0; i < points.length; i++)
					{
						Pt point = points[i];
						allPoints.add(new RelitivePoint(point.x, point.y, variance));
					}

				}
			}
		}
	}

	private void genBoxesV2()
	{

		while (!allPoints.isEmpty())
		{
			Collections.sort(allPoints, new Comparator<RelitivePoint>() {
				@Override
				public int compare(RelitivePoint o1, RelitivePoint o2)
				{
					int result = (int) (o1.x - o2.x);
					if (result == 0)
					{
						return (int) (o1.y - o2.y);
					}
					return result;
				}
			});

			RelitivePoint start = allPoints.get(0);
			allPoints.remove(0);
			if (buildFromPoint(start, rdlu))
			{
			}
			else if (buildFromPoint(start, dlur))
			{
			}
			else if (buildFromPoint(start, lurd))
			{
			}
			else if (buildFromPoint(start, urdl))
			{
			}
		}
	}

	private boolean fullStack(Stack<RelitivePoint> pointStack)
	{
		if (!(pointStack.size() == 4))
		{
			return false;
		}
		boolean result = true;
		for (Point p : pointStack)
		{
			if (p == null)
			{
				return false;
			}
		}
		return result;
	}

	private boolean buildFromPoint(RelitivePoint p, List<Function<RelitivePoint, RelitivePoint>> functionList)
	{
		Stack<RelitivePoint> pointStack = new Stack<>();
		pointStack.push(p);

		int i = 0;
		RelitivePoint prev = p;
		while (true)
		{
			if (i == -1 || i > 3)
			{
				break;
			}

			RelitivePoint np = functionList.get(i).apply(prev);

			if (np != null)
			{
				if (i == 3)
				{
					if (np.equals(p))
					{
						i++;
					}
					else
					{
						prev = pointStack.pop();
						i--;
					}
				}
				else
				{
					prev = np;
					pointStack.push(np);
					i++;
				}
			}
			else
			{
				if (!pointStack.peek().equals(p))
				{
					prev = pointStack.pop();
				}
				i--;
			}
		}

		if (fullStack(pointStack))
		{
			if (validateStack(pointStack.toArray(new RelitivePoint[pointStack.size()])))
			{
				buildBoundBox(pointStack.toArray(new RelitivePoint[pointStack.size()]));
				return true;
			}
		}
		return false;
	}

	private RelitivePoint rd(RelitivePoint p) // returns right point that has an avaible down point
	{
		if (p == null)
		{
			return p;
		}
		RelitivePoint rp = getRight(p);
		if (rp != null)
		{

			RelitivePoint dp = getDown(rp);
			if (dp != null)
			{
				return rp;
			}
			else
			{
				return rd(rp);
			}
		}
		else
		{
			return null;
		}

	}

	private RelitivePoint dl(RelitivePoint p)
	{
		if (p == null)
		{
			return p;
		}
		RelitivePoint rp = getDown(p);
		if (rp != null)
		{

			RelitivePoint dp = getLeft(rp);
			if (dp != null)
			{
				return rp;
			}
			else
			{
				return dl(rp);
			}
		}
		else
		{
			return null;
		}
	}

	private RelitivePoint lu(RelitivePoint p)
	{
		if (p == null)
		{
			return p;
		}

		RelitivePoint rp = getLeft(p);
		if (rp != null)
		{

			RelitivePoint dp = getUp(rp);
			if (dp != null)
			{
				return rp;
			}
			else
			{
				return lu(rp);
			}
		}
		else
		{
			return null;
		}
	}

	private RelitivePoint ur(RelitivePoint p)
	{
		if (p == null)
		{
			return p;
		}

		RelitivePoint rp = getUp(p);
		if (rp != null)
		{

			RelitivePoint dp = getRight(rp);
			if (dp != null)
			{
				return rp;
			}
			else
			{
				return ur(rp);
			}
		}
		else
		{
			return null;
		}
	}

	private void buildBoundBox(RelitivePoint[] points) // TODO add check for same box
	{
		//		for (int i = 0; i < points.length; i++)
		//		{
		//			RelitivePoint p = points[i];
		//			allPoints.remove(p);
		//		}
		allPoints.remove(points[0]);
		BoundingBox b = new BoundingBox();
		b.setPoints(points);
		//		b.pointVals = points;
		//		if (!boxes.contains(b))
		//		{
		boxes.add(b);
		//		}
	}

	private boolean validateStack(RelitivePoint[] points) //TODO test fix
	{
		RelitivePoint[][] sameXPairs = new RelitivePoint[2][2];
		RelitivePoint[][] sameYPairs = new RelitivePoint[2][2];
		RelitivePoint[][] diffPair = new RelitivePoint[2][2]; // 0 = diff x, 1 = diff y

		sameXPairs[0][0] = points[0];
		sameYPairs[0][0] = points[0];
		diffPair[0][0] = points[0];

		for (int i = 1; i < points.length; i++) //start at 1 since first point was added manually
		{
			RelitivePoint p = points[i];
			//same x case
			if (Math.abs(sameXPairs[0][0].x - p.x) < variance && sameXPairs[0][1] == null) //|| sameXPairs[0][0].x - p.x < variance
			{
				sameXPairs[0][1] = p;
			}
			else if (sameXPairs[1][0] == null) //if different x and its not already set
			{
				sameXPairs[1][0] = p;
			}
			else
			{
				sameXPairs[1][1] = p;
			}

			//Same y case
			if (Math.abs(sameYPairs[0][0].y - p.y) < variance && sameYPairs[0][1] == null)//|| sameYPairs[0][0].y - p.y < variance)
			{
				sameYPairs[0][1] = p;
			}
			else if (sameYPairs[1][0] == null)
			{
				sameYPairs[1][0] = p;
			}
			else
			{
				sameYPairs[1][1] = p;
			}

			//different case
			if (Math.abs(diffPair[0][0].x - p.x) > variance && Math.abs(diffPair[0][0].y - p.y) > variance)
			{
				diffPair[0][1] = p;
			}

			else if (diffPair[1][0] == null)
			{
				diffPair[1][0] = p;
			}
			else
			{
				diffPair[1][1] = p;
			}
		}

		return (fullArray(sameXPairs) && fullArray(sameYPairs) && fullArray(diffPair));
	}

	private boolean fullArray(RelitivePoint[][] arr)
	{
		boolean isFull = true;
		for (int i = 0; i < 2; i++)
		{
			for (int j = 0; j < 2; j++)
			{
				if (arr[i][j] == null)
				{
					isFull = false;
					break;
				}
			}
		}
		return isFull;
	}

	private RelitivePoint getRight(RelitivePoint p)
	{
		Collections.sort(allPoints, new Comparator<RelitivePoint>() {

			@Override
			public int compare(RelitivePoint o1, RelitivePoint o2)
			{
				return (int) (o1.x - o2.x);
			}
		});
		for (int i = 0; i < allPoints.size(); i++)
		{
			RelitivePoint otherP = allPoints.get(i);
			if ((otherP.x - p.x) > variance && otherP.relitiveEqualsY(p))
			{
				return otherP;
			}
		}
		return null;
	}

	private RelitivePoint getLeft(RelitivePoint p)
	{
		Collections.sort(allPoints, new Comparator<RelitivePoint>() {

			@Override
			public int compare(RelitivePoint o1, RelitivePoint o2)
			{
				return (int) (o2.x - o1.x);
			}
		});

		for (int i = 0; i < allPoints.size(); i++)
		{
			RelitivePoint otherP = allPoints.get(i);
			if ((otherP.x - p.x) < -variance && otherP.relitiveEqualsY(p))
			{
				return otherP;
			}
		}
		return null;
	}

	private RelitivePoint getDown(RelitivePoint p)
	{
		Collections.sort(allPoints, new Comparator<RelitivePoint>() {

			@Override
			public int compare(RelitivePoint o1, RelitivePoint o2)
			{
				return (int) (o1.y - o2.y);
			}
		});

		for (int i = 0; i < allPoints.size(); i++)
		{
			RelitivePoint otherP = allPoints.get(i);
			if ((otherP.y - p.y) > variance && otherP.relitiveEqualsX(p))
			{
				return otherP;
			}
		}
		return null;
	}

	private RelitivePoint getUp(RelitivePoint p)
	{
		Collections.sort(allPoints, new Comparator<RelitivePoint>() {

			@Override
			public int compare(RelitivePoint o1, RelitivePoint o2)
			{
				return (int) (o2.y - o1.y);
			}
		});

		for (int i = 0; i < allPoints.size(); i++)
		{
			RelitivePoint otherP = allPoints.get(i);
			if ((otherP.y - p.y) < -variance && otherP.relitiveEqualsX(p))
			{
				return otherP;
			}
		}
		return null;
	}
}
