package com.autoentry.server.service;

import java.util.List;

import com.autoentry.server.entities.BoundingBox;
import com.autoentry.server.entities.Line;

public interface BoundingBoxGenService
{
	public List<BoundingBox> getBoundingBoxes(List<Line> lines, double ePs);
}
