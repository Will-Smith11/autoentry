package com.autoentry.server.service.impl;

import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.contentstream.PDFGraphicsStreamEngine;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.graphics.image.PDImage;

import com.autoentry.server.entities.Line;
import com.autoentry.server.entities.RelitivePoint;
import com.autoentry.server.service.PageReaderListenerService;

public class PageReaderListenerServiceImpl extends PDFGraphicsStreamEngine implements PageReaderListenerService
{

	private List<Line> lines = new ArrayList<>();
	private List<RelitivePoint> allPoints = new ArrayList<>();
	private List<RelitivePoint> pushedPoints = new ArrayList<>();
	private int pageNum;

	protected PageReaderListenerServiceImpl(PDPage page, int pageNum)
	{
		super(page);
		this.pageNum = pageNum;
	}

	@Override
	public List<Line> getLines() throws IOException
	{
		processPage(getCurrentPage());
		return lines;
	}

	public void processPage() throws IOException
	{
		processPage(getCurrentPage());
	}

	private void buildLine()
	{
		lines.add(new Line(pushedPoints.get(0), pushedPoints.get(1), pageNum));
	}

	@Override
	public void appendRectangle(Point2D p0, Point2D p1, Point2D p2, Point2D p3) throws IOException
	{
		//		throw new NotImplementedException("not done mf");
	}

	@Override
	public void drawImage(PDImage pdImage) throws IOException
	{
		//		throw new NotImplementedException("not done mf");

	}

	@Override
	public void clip(int windingRule) throws IOException
	{
		//		throw new NotImplementedException("not done mf");

	}

	@Override
	public void moveTo(float x, float y) throws IOException
	{
		RelitivePoint p = new RelitivePoint(x, y, 0);
		allPoints.add(p);
		pushedPoints.add(p);
	}

	@Override
	public void lineTo(float x, float y) throws IOException
	{
		RelitivePoint p = new RelitivePoint(x, y, 0);
		allPoints.add(p);
		pushedPoints.add(p);
		buildLine();
	}

	@Override
	public void curveTo(float x1, float y1, float x2, float y2, float x3, float y3) throws IOException
	{
		//		throw new NotImplementedException("not done mf");

	}

	@Override
	public Point2D getCurrentPoint() throws IOException
	{
		return new Point2D() {

			@Override
			public void setLocation(double x, double y)
			{
				// TODO Auto-generated method stub

			}

			@Override
			public double getY()
			{
				// TODO Auto-generated method stub
				return pushedPoints.get(-1).y;
			}

			@Override
			public double getX()
			{
				// TODO Auto-generated method stub
				return pushedPoints.get(-1).x;
			}
		};
	}

	@Override
	public void closePath() throws IOException
	{
		//		throw new NotImplementedException("not done mf");

	}

	@Override
	public void endPath() throws IOException
	{
		//		throw new NotImplementedException("not done mf");
		pushedPoints.clear();
	}

	@Override
	public void strokePath() throws IOException
	{
		//		throw new NotImplementedException("not done mf");
		pushedPoints.clear();
	}

	@Override
	public void fillPath(int windingRule) throws IOException
	{
		//		throw new NotImplementedException("not done mf");

	}

	@Override
	public void fillAndStrokePath(int windingRule) throws IOException
	{
		//		throw new NotImplementedException("not done mf");

	}

	@Override
	public void shadingFill(COSName shadingName) throws IOException
	{
		//		throw new NotImplementedException("not done mf");

	}

}
