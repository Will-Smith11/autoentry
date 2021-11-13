package com.autoentry.server.exceptions;

public class PageDoesNotExistException extends RuntimeException
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PageDoesNotExistException(String msg)
	{
		super(msg);
	}

}
