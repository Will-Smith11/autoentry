package com.autoentry.server.service;

import java.util.Vector;

import com.autoentry.server.entities.BoundingBox;
import com.autoentry.server.entities.Line;

import io.reactivex.rxjava3.core.Single;

public interface ConcurrentBoundingBoxGenService
{
	Single<Vector<BoundingBox>> getPageBoundingBox(Vector<Line> lines);
}
