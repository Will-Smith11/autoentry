package com.autoentry.server.service;

import java.io.IOException;
import java.util.List;

import com.autoentry.server.entities.Line;

public interface PageReaderListenerService
{
	public List<Line> getLines() throws IOException;
}
