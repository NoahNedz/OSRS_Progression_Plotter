/*
 * Copyright (c) 2026
 * All rights reserved.
 */
package com.osrsprogressionplotter.progressiontracker;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ProgressionTrackerJournal
{
	private final Map<String, ProgressionTrackerEntry> entries = new LinkedHashMap<>();

	public boolean record(ProgressionTrackerEntry entry)
	{
		if (entries.containsKey(entry.getKey()))
		{
			return false;
		}

		entries.put(entry.getKey(), entry);
		return true;
	}

	public boolean recordItem(String itemKey, String itemName, String source, Instant obtainedAt, String detail)
	{
		return record(new ProgressionTrackerEntry(ProgressionTrackerEntry.Type.ITEM, itemKey, itemName, source, obtainedAt, detail));
	}

	public boolean recordQuest(String questKey, String questName, Instant obtainedAt, String detail)
	{
		return record(new ProgressionTrackerEntry(ProgressionTrackerEntry.Type.QUEST, questKey, questName, "Quest log", obtainedAt, detail));
	}

	public boolean recordSkill(String skillKey, String skillName, Instant obtainedAt, String detail)
	{
		return record(new ProgressionTrackerEntry(ProgressionTrackerEntry.Type.SKILL, skillKey, skillName, "Skill tracker", obtainedAt, detail));
	}

	public boolean recordDiary(String diaryKey, String diaryName, Instant obtainedAt, String detail)
	{
		return record(new ProgressionTrackerEntry(ProgressionTrackerEntry.Type.DIARY, diaryKey, diaryName, "Achievement Diary", obtainedAt, detail));
	}

	public List<ProgressionTrackerEntry> getEntries()
	{
		return new ArrayList<>(entries.values());
	}

	public int size()
	{
		return entries.size();
	}

	public void clear()
	{
		entries.clear();
	}

	public void load(Collection<ProgressionTrackerEntry> loadedEntries)
	{
		entries.clear();
		for (ProgressionTrackerEntry entry : loadedEntries)
		{
			record(entry);
		}
	}
}
