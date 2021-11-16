package com.autoentry.server.service.impl;

import java.util.Collections;
import java.util.Comparator;
import java.util.Stack;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicInteger;

import org.opencv.core.Point;
import org.springframework.stereotype.Service;

import com.autoentry.server.entities.BoundingBox;
import com.autoentry.server.entities.Line;
import com.autoentry.server.entities.RelitivePoint;
import com.autoentry.server.service.ConcurrentBoundingBoxGenService;
import com.autoentry.server.util.LineSegmentLineSegmentIntersection;
import com.autoentry.server.util.LineSegmentLineSegmentIntersection.Pt;

import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.functions.BiFunction;

/**
 * the purpose of this class is to re-write the previous boundingBoxGenService and to make it thread safe so we can run it concurrently
 * and not come across multi-threading issues that arose from the previous bounding box generator
 */
@Service
public class ConcurrentBoundingBoxGenImpl implements ConcurrentBoundingBoxGenService
{
	final double variance = 3.0;

	@Override
	public Single<Vector<BoundingBox>> getPageBoundingBox(Vector<Line> lines)
	{
		return Single.create(source -> {
			Vector<RelitivePoint> allPoints = new Vector<>();
			Vector<BiFunction<RelitivePoint, Vector<RelitivePoint>, RelitivePoint>> rdlu = buildRdlu();
			Vector<BiFunction<RelitivePoint, Vector<RelitivePoint>, RelitivePoint>> dlur = buildDlur();
			Vector<BiFunction<RelitivePoint, Vector<RelitivePoint>, RelitivePoint>> lurd = buildLurd();
			Vector<BiFunction<RelitivePoint, Vector<RelitivePoint>, RelitivePoint>> urdl = buildUrdl();
			for (Line l : lines)
			{
				allPoints.add(l.getA());
				allPoints.add(l.getB());
			}
			allPoints = genIntersectionPoints(lines, allPoints);
			source.onError(new Throwable("some error occured while building boundingboxes"));
			source.onSuccess(genBoxes(allPoints, rdlu, dlur, lurd, urdl));
		});
	}

	private Vector<BiFunction<RelitivePoint, Vector<RelitivePoint>, RelitivePoint>> buildRdlu()
	{
		Vector<BiFunction<RelitivePoint, Vector<RelitivePoint>, RelitivePoint>> result = new Vector<>();
		result.add(this::rd);
		result.add(this::dl);
		result.add(this::lu);
		result.add(this::ur);
		return result;
	}

	private Vector<BiFunction<RelitivePoint, Vector<RelitivePoint>, RelitivePoint>> buildDlur()
	{
		Vector<BiFunction<RelitivePoint, Vector<RelitivePoint>, RelitivePoint>> result = new Vector<>();
		result.add(this::dl);
		result.add(this::lu);
		result.add(this::ur);
		result.add(this::rd);
		return result;
	}

	private Vector<BiFunction<RelitivePoint, Vector<RelitivePoint>, RelitivePoint>> buildLurd()
	{
		Vector<BiFunction<RelitivePoint, Vector<RelitivePoint>, RelitivePoint>> result = new Vector<>();
		result.add(this::lu);
		result.add(this::ur);
		result.add(this::rd);
		result.add(this::dl);
		return result;
	}

	private Vector<BiFunction<RelitivePoint, Vector<RelitivePoint>, RelitivePoint>> buildUrdl()
	{
		Vector<BiFunction<RelitivePoint, Vector<RelitivePoint>, RelitivePoint>> result = new Vector<>();
		result.add(this::ur);
		result.add(this::rd);
		result.add(this::dl);
		result.add(this::lu);
		return result;
	}

	private Vector<BoundingBox> genBoxes(Vector<RelitivePoint> allPoints, Vector<BiFunction<RelitivePoint, Vector<RelitivePoint>, RelitivePoint>> rdlu, Vector<BiFunction<RelitivePoint, Vector<RelitivePoint>, RelitivePoint>> dlur, Vector<BiFunction<RelitivePoint, Vector<RelitivePoint>, RelitivePoint>> lurd, Vector<BiFunction<RelitivePoint, Vector<RelitivePoint>, RelitivePoint>> urdl)
			throws Throwable
	{
		Vector<BoundingBox> results = new Vector<>();
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

			BoundingBox b = buildFromPoint(start, rdlu, allPoints);
			if (b != null)
			{
				results.add(b);
			}
			else
			{
				b = buildFromPoint(start, dlur, allPoints);
				if (b != null)
				{
					results.add(b);
				}
				else
				{
					b = buildFromPoint(start, lurd, allPoints);
					if (b != null)
					{
						results.add(b);
					}
					else
					{
						b = buildFromPoint(start, urdl, allPoints);
						if (b != null)
						{
							results.add(b);
						}
					}
				}
			}
		}
		return results;
	}

	private BoundingBox buildFromPoint(RelitivePoint p, Vector<BiFunction<RelitivePoint, Vector<RelitivePoint>, RelitivePoint>> functionList, Vector<RelitivePoint> allPoints)
			throws Throwable
	{
		Stack<RelitivePoint> pointStack = new Stack<>();
		pointStack.push(p);

		AtomicInteger i = new AtomicInteger(0);
		RelitivePoint prev = p;
		while (true)
		{
			if (i.get() == -1 || i.get() > 3)
			{
				break;
			}

			RelitivePoint np = functionList.get(i.get()).apply(prev, allPoints);

			if (np != null)
			{
				if (i.get() == 3)
				{
					if (np.equals(p))
					{
						i.incrementAndGet();
					}
					else
					{
						prev = pointStack.pop();
						i.decrementAndGet();
					}
				}
				else
				{
					prev = np;
					pointStack.push(np);
					i.incrementAndGet();
				}
			}
			else
			{
				if (!pointStack.peek().equals(p))
				{
					prev = pointStack.pop();
				}
				i.decrementAndGet();
			}
		}

		if (fullStack(pointStack))
		{
			if (validateStack(pointStack.toArray(new RelitivePoint[pointStack.size()])))
			{
				return buildBoundBox(pointStack.toArray(new RelitivePoint[pointStack.size()]), allPoints);
			}
		}
		return null;
	}

	private BoundingBox buildBoundBox(RelitivePoint[] points, Vector<RelitivePoint> allPoints) // TODO add check for same box
	{
		allPoints.remove(points[0]);
		BoundingBox b = new BoundingBox();
		b.setPoints(points);
		return b;
	}

	private Vector<RelitivePoint> genIntersectionPoints(Vector<Line> lines, Vector<RelitivePoint> allPoints)
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
					for (AtomicInteger i = new AtomicInteger(0); i.get() < points.length; i.incrementAndGet())
					{
						Pt point = points[i.get()];
						allPoints.add(new RelitivePoint(point.x, point.y, variance));
					}

				}
			}
		}
		return allPoints;
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

	private RelitivePoint rd(RelitivePoint p, Vector<RelitivePoint> allPoints)
	{
		if (p == null)
		{
			return p;
		}
		RelitivePoint rp = getRight(p, allPoints);
		if (rp != null)
		{

			RelitivePoint dp = getDown(rp, allPoints);
			if (dp != null)
			{
				return rp;
			}
			else
			{
				return rd(rp, allPoints);
			}
		}
		else
		{
			return null;
		}

	}

	private RelitivePoint dl(RelitivePoint p, Vector<RelitivePoint> allPoints)
	{
		if (p == null)
		{
			return p;
		}
		RelitivePoint rp = getDown(p, allPoints);
		if (rp != null)
		{

			RelitivePoint dp = getLeft(rp, allPoints);
			if (dp != null)
			{
				return rp;
			}
			else
			{
				return dl(rp, allPoints);
			}
		}
		else
		{
			return null;
		}
	}

	private RelitivePoint lu(RelitivePoint p, Vector<RelitivePoint> allPoints)
	{
		if (p == null)
		{
			return p;
		}

		RelitivePoint rp = getLeft(p, allPoints);
		if (rp != null)
		{

			RelitivePoint dp = getUp(rp, allPoints);
			if (dp != null)
			{
				return rp;
			}
			else
			{
				return lu(rp, allPoints);
			}
		}
		else
		{
			return null;
		}
	}

	private RelitivePoint ur(RelitivePoint p, Vector<RelitivePoint> allPoints)
	{
		if (p == null)
		{
			return p;
		}

		RelitivePoint rp = getUp(p, allPoints);
		if (rp != null)
		{

			RelitivePoint dp = getRight(rp, allPoints);
			if (dp != null)
			{
				return rp;
			}
			else
			{
				return ur(rp, allPoints);
			}
		}
		else
		{
			return null;
		}
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

	private RelitivePoint getRight(RelitivePoint p, Vector<RelitivePoint> allPoints)
	{
		Collections.sort(allPoints, new Comparator<RelitivePoint>() {

			@Override
			public int compare(RelitivePoint o1, RelitivePoint o2)
			{
				return (int) (o1.x - o2.x);
			}
		});
		for (AtomicInteger i = new AtomicInteger(0); i.get() < allPoints.size(); i.incrementAndGet())
		{
			RelitivePoint otherP = allPoints.get(i.get());
			if ((otherP.x - p.x) > variance && otherP.relitiveEqualsY(p))
			{
				return otherP;
			}
		}
		return null;
	}

	private RelitivePoint getLeft(RelitivePoint p, Vector<RelitivePoint> allPoints)
	{
		Collections.sort(allPoints, new Comparator<RelitivePoint>() {

			@Override
			public int compare(RelitivePoint o1, RelitivePoint o2)
			{
				return (int) (o2.x - o1.x);
			}
		});

		for (AtomicInteger i = new AtomicInteger(0); i.get() < allPoints.size(); i.incrementAndGet())
		{
			RelitivePoint otherP = allPoints.get(i.get());
			if ((otherP.x - p.x) < -variance && otherP.relitiveEqualsY(p))
			{
				return otherP;
			}
		}
		return null;
	}

	private RelitivePoint getDown(RelitivePoint p, Vector<RelitivePoint> allPoints)
	{
		Collections.sort(allPoints, new Comparator<RelitivePoint>() {

			@Override
			public int compare(RelitivePoint o1, RelitivePoint o2)
			{
				return (int) (o1.y - o2.y);
			}
		});

		for (AtomicInteger i = new AtomicInteger(0); i.get() < allPoints.size(); i.incrementAndGet())
		{
			RelitivePoint otherP = allPoints.get(i.get());
			if ((otherP.y - p.y) > variance && otherP.relitiveEqualsX(p))
			{
				return otherP;
			}
		}
		return null;
	}

	private RelitivePoint getUp(RelitivePoint p, Vector<RelitivePoint> allPoints)
	{
		Collections.sort(allPoints, new Comparator<RelitivePoint>() {

			@Override
			public int compare(RelitivePoint o1, RelitivePoint o2)
			{
				return (int) (o2.y - o1.y);
			}
		});

		for (AtomicInteger i = new AtomicInteger(0); i.get() < allPoints.size(); i.incrementAndGet())
		{
			RelitivePoint otherP = allPoints.get(i.get());
			if ((otherP.y - p.y) < -variance && otherP.relitiveEqualsX(p))
			{
				return otherP;
			}
		}
		return null;
	}

}
