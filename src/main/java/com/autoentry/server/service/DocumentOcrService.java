package com.autoentry.server.service;

import io.reactivex.rxjava3.core.Completable;

public interface DocumentOcrService
{
	public Completable run() throws Exception;

}
