/*
 * Copyright (c) 2026
 * All rights reserved.
 */
package com.osrsprogressionplotter.progressiontracker;

import java.time.Instant;

public class ProgressionTrackerEntry
{
	public enum Type
	{
		ITEM,
		QUEST,
		SKILL,
		DIARY
	}

	private Type type;
	private String key;
	private String name;
	private String source;
	private Instant obtainedAt;
	private String detail;

	public ProgressionTrackerEntry()
	{
	}

	public ProgressionTrackerEntry(Type type, String key, String name, String source, Instant obtainedAt, String detail)
	{
		this.type = type;
		this.key = key;
		this.name = name;
		this.source = source;
		this.obtainedAt = obtainedAt;
		this.detail = detail;
	}

	public Type getType()
	{
		return type;
	}

	public String getKey()
	{
		return key;
	}

	public String getName()
	{
		return name;
	}

	public String getSource()
	{
		return source;
	}

	public Instant getObtainedAt()
	{
		return obtainedAt;
	}

	public String getDetail()
	{
		return detail;
	}
}
