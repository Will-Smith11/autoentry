package com.autoentry.server.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.autoentry.server.entities.BoundingBox;
import com.autoentry.server.entities.RelatedBoundingBox;
import com.autoentry.server.entities.RelitivePoint;
import com.autoentry.server.interfaces.BaseDocument;
import com.autoentry.server.service.RelatedBoundingBoxGenService;
import com.google.cloud.vision.v1.Block;
import com.google.cloud.vision.v1.BoundingPoly;
import com.google.cloud.vision.v1.NormalizedVertex;
import com.google.cloud.vision.v1.Paragraph;
import com.google.cloud.vision.v1.Symbol;
import com.google.cloud.vision.v1.Vertex;
import com.google.cloud.vision.v1.Word;

@Component
public class RelatedBoundingBoxGenImpl implements RelatedBoundingBoxGenService
{
	@Autowired
	private BaseDocument d;

	@Override
	public List<RelatedBoundingBox<Block>> getBlockBox()
	{
		return buildBlockBoxes();
	}

	@Override
	public List<RelatedBoundingBox<Paragraph>> getParaBox()
	{
		return buildParagraphBoxes();
	}

	@Override
	public List<RelatedBoundingBox<Word>> getWordBox()
	{
		return buildWordBoxes();
	}

	@Override
	public List<RelatedBoundingBox<Symbol>> getSymbolBox()
	{
		return buildSymbolBoxes();
	}

	private List<RelatedBoundingBox<Block>> buildBlockBoxes()
	{
		List<RelatedBoundingBox<Block>> blockBox = new ArrayList<>();
		for (BoundingBox b : d.getBoundingBoxes())
		{
			for (Block p : d.getBlocks())
			{
				if (containsV2(p.getBoundingBox(), b))
				{
					RelatedBoundingBox<Block> test = new RelatedBoundingBox<>(b, p);
					if (blockBox.contains(test))
					{
						blockBox.get(blockBox.indexOf(test)).addContent(p);
					}
					else
					{
						blockBox.add(test);
					}
				}
			}
		}
		return blockBox;
	}

	private List<RelatedBoundingBox<Paragraph>> buildParagraphBoxes()
	{
		List<RelatedBoundingBox<Paragraph>> paraBox = new ArrayList<>();
		for (BoundingBox b : d.getBoundingBoxes())
		{
			for (Paragraph p : d.getParagraphs())
			{
				if (containsV2(p.getBoundingBox(), b))
				{
					RelatedBoundingBox<Paragraph> test = new RelatedBoundingBox<>(b, p);
					if (paraBox.contains(test))
					{
						paraBox.get(paraBox.indexOf(test)).addContent(p);
					}
					else
					{
						paraBox.add(test);
					}
				}
			}
		}
		return paraBox;
	}

	private List<RelatedBoundingBox<Word>> buildWordBoxes()
	{
		List<RelatedBoundingBox<Word>> wordBox = new ArrayList<>();
		for (BoundingBox b : d.getBoundingBoxes())
		{
			for (Word p : d.getWords())
			{
				if (containsV2(p.getBoundingBox(), b))
				{
					RelatedBoundingBox<Word> test = new RelatedBoundingBox<>(b, p);
					if (wordBox.contains(test))
					{
						wordBox.get(wordBox.indexOf(test)).addContent(p);
					}
					else
					{
						wordBox.add(test);
					}
				}
			}
		}
		return wordBox;
	}

	private List<RelatedBoundingBox<Symbol>> buildSymbolBoxes()
	{
		List<RelatedBoundingBox<Symbol>> symbolBox = new ArrayList<>();
		for (BoundingBox b : d.getBoundingBoxes())
		{
			for (Symbol p : d.getSmybols())
			{
				if (containsV2(p.getBoundingBox(), b))
				{
					RelatedBoundingBox<Symbol> test = new RelatedBoundingBox<>(b, p);
					if (symbolBox.contains(test))
					{
						symbolBox.get(symbolBox.indexOf(test)).addContent(p);
					}
					else
					{
						symbolBox.add(test);
					}
				}
			}
		}
		return symbolBox;
	}

	/**
	 * if we read the boundingbox points from a pdf file, we need to invert
	 *  the y coordinates as the bottem left instead of top left is 0,0
	 */

	private boolean containsV2(BoundingPoly boundingPoly, BoundingBox b)
	{

		RelitivePoint p1;
		RelitivePoint p2;
		RelitivePoint p3;
		RelitivePoint p4;

		if (!boundingPoly.getVerticesList().isEmpty())
		{
			List<Vertex> points = boundingPoly.getVerticesList();
			p1 = new RelitivePoint(points.get(0).getX() / d.getWidthDiv(), points.get(0).getY() / d.getHeightDiv(), d.getEPS());
			p2 = new RelitivePoint(points.get(1).getX() / d.getWidthDiv(), points.get(1).getY() / d.getHeightDiv(), d.getEPS());
			p3 = new RelitivePoint(points.get(2).getX() / d.getWidthDiv(), points.get(2).getY() / d.getHeightDiv(), d.getEPS());
			p4 = new RelitivePoint(points.get(3).getX() / d.getWidthDiv(), points.get(3).getY() / d.getHeightDiv(), d.getEPS());
		}
		else if (!boundingPoly.getNormalizedVerticesList().isEmpty())
		{
			List<NormalizedVertex> points = boundingPoly.getNormalizedVerticesList();

			p1 = new RelitivePoint((points.get(0).getX() * d.getWidth()) / d.getWidthDiv(), (points.get(0).getY() * d.getHeight()) / d.getHeightDiv(),
					d.getEPS());
			p2 = new RelitivePoint((points.get(1).getX() * d.getWidth()) / d.getWidthDiv(), (points.get(1).getY() * d.getHeight()) / d.getHeightDiv(),
					d.getEPS());
			p3 = new RelitivePoint((points.get(2).getX() * d.getWidth()) / d.getWidthDiv(), (points.get(2).getY() * d.getHeight()) / d.getHeightDiv(),
					d.getEPS());
			p4 = new RelitivePoint((points.get(3).getX() * d.getWidth()) / d.getWidthDiv(), (points.get(3).getY() * d.getHeight()) / d.getHeightDiv(),
					d.getEPS());
		}
		else
		{
			return false;
		}

		RelitivePoint[] inner = { smallestPoint(Arrays.asList(p1, p2, p3, p4)), biggestPoint(Arrays.asList(p1, p2, p3, p4)) };

		RelitivePoint[] outer = { smallestPoint(Arrays.asList(b.getPoints())), biggestPoint(Arrays.asList(b.getPoints())) };

		if (inner[0].relitiveGNX(outer[0]) && inner[1].relitiveLNX(outer[1]))
		{
			if (inner[0].relitiveGNY(outer[0]) && inner[1].relitiveLNY(outer[1]))
			{
				return true;
			}
		}
		return false;
	}

	private RelitivePoint smallestPoint(List<RelitivePoint> b)
	{
		RelitivePoint result = null;
		double sum = d.getHeight() + d.getWidth();
		for (RelitivePoint bp : b)
		{
			if ((bp.x + bp.y) < sum)
			{
				result = bp;
				sum = bp.x + bp.y;
			}
		}
		return result;
	}

	private RelitivePoint biggestPoint(List<RelitivePoint> b)
	{
		RelitivePoint result = null;
		double sum = 0;
		for (RelitivePoint bp : b)
		{
			if ((bp.x + bp.y) > sum)
			{
				result = bp;
				sum = bp.x + bp.y;
			}
		}
		return result;
	}
}
