/*
 * Copyright (c) 2026
 * All rights reserved.
 */
package com.osrsprogressionplotter.progressiontracker;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import java.time.Instant;
import org.junit.Test;

public class ProgressionTrackerJournalTest
{
	@Test
	public void deduplicatesByStableKey()
	{
		ProgressionTrackerJournal journal = new ProgressionTrackerJournal();

		assertTrue(journal.recordItem("item:123", "Graceful hood", "Skilling", Instant.parse("2026-01-01T00:00:00Z"), "baseline"));
		assertEquals(1, journal.size());
		assertTrue(!journal.recordItem("item:123", "Graceful hood", "Skilling", Instant.parse("2026-01-01T00:05:00Z"), "duplicate"));
		assertEquals(1, journal.size());
	}

	@Test
	public void loadsEntriesWithoutDuplicatingThem()
	{
		ProgressionTrackerJournal journal = new ProgressionTrackerJournal();
		journal.recordQuest("quest:barrows_gloves", "Barrows gloves", Instant.parse("2026-01-01T00:00:00Z"), "Quest completed");

		ProgressionTrackerJournal copy = new ProgressionTrackerJournal();
		copy.load(journal.getEntries());

		assertEquals(1, copy.size());
		assertTrue(!copy.recordQuest("quest:barrows_gloves", "Barrows gloves", Instant.parse("2026-01-01T00:01:00Z"), "duplicate"));
		assertEquals(1, copy.size());
	}
}
