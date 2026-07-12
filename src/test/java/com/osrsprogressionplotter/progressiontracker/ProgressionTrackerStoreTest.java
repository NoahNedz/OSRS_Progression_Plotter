/*
 * Copyright (c) 2026
 * All rights reserved.
 */
package com.osrsprogressionplotter.progressiontracker;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import org.junit.Test;

public class ProgressionTrackerStoreTest
{
	@Test
	public void exportHtmlProducesSingleColumnTimelineWithTimestamps()
	{
		ProgressionTrackerJournal journal = new ProgressionTrackerJournal();
		journal.recordItem("item:1", "Graceful hood", "Skilling", Instant.parse("2026-01-01T12:30:00Z"), "Skilling unlock found in bank");
		journal.recordQuest("quest:1", "Barrows gloves", Instant.parse("2026-01-01T12:35:00Z"), "Quest completed");

		String html = ProgressionTrackerStore.exportHtml("profile-key", journal);

		assertTrue(html.contains("Progression Timeline"));
		assertTrue(html.contains("timeline"));
		assertTrue(html.contains("Graceful hood"));
		assertTrue(html.contains("Barrows gloves"));
		assertTrue(html.contains("2026-01-01"));
		assertTrue(html.contains("<section class=\"timeline\">"));
		assertTrue(html.contains("<article class=\"entry\">"));
	}

	@Test
	public void rawExportImportRoundTripPreservesEntries() throws IOException
	{
		ProgressionTrackerJournal journal = new ProgressionTrackerJournal();
		journal.recordItem("item:1", "Graceful hood", "Skilling", Instant.parse("2026-01-01T12:30:00Z"), "Skilling unlock found in bank");
		journal.recordSkill("skill:AGILITY:70", "Agility", Instant.parse("2026-01-01T12:40:00Z"), "Reached level 70");

		Path tempFile = Files.createTempFile("progression-raw", ".json");
		try
		{
			ProgressionTrackerStore.exportRaw(tempFile.toFile(), journal);
			ProgressionTrackerJournal imported = ProgressionTrackerStore.importRaw(tempFile.toFile());

			assertEquals(2, imported.size());
			assertEquals("Graceful hood", imported.getEntries().get(0).getName());
			assertEquals("Agility", imported.getEntries().get(1).getName());
		}
		finally
		{
			Files.deleteIfExists(tempFile);
		}
	}
}
