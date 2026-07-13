/*
 * Copyright (c) 2026
 * All rights reserved.
 */
package com.osrsprogressionplotter.progressiontracker;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import net.runelite.client.RuneLite;

final class ProgressionTrackerStore
{
	private static final DateTimeFormatter TIMESTAMP_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss z");

	private ProgressionTrackerStore()
	{
	}

	static ProgressionTrackerJournal load(String profileKey, Gson gson)
	{
		ProgressionTrackerJournal journal = new ProgressionTrackerJournal();
		Gson configuredGson = configuredGson(gson);
		Path file = storageFile(profileKey).toPath();
		if (!Files.exists(file))
		{
			return journal;
		}

		try
		{
			String json = Files.readString(file, StandardCharsets.UTF_8);
			ProgressionTrackerState state = configuredGson.fromJson(json, ProgressionTrackerState.class);
			if (state != null && state.entries != null)
			{
				journal.load(state.entries);
			}
		}
		catch (IOException | RuntimeException ex)
		{
			journal.clear();
		}

		return journal;
	}

	static void save(String profileKey, ProgressionTrackerJournal journal, Gson gson)
	{
		Path file = storageFile(profileKey).toPath();
		Gson configuredGson = configuredGson(gson);
		try
		{
			Files.createDirectories(file.getParent());
			ProgressionTrackerState state = new ProgressionTrackerState();
			state.entries = journal.getEntries();
			Files.writeString(file, configuredGson.toJson(state), StandardCharsets.UTF_8);
		}
		catch (IOException ex)
		{
			// Best effort only; the in-memory journal remains authoritative.
		}
	}

	static String exportHtml(String profileLabel, ProgressionTrackerJournal journal)
	{
		String displayProfileLabel = profileLabel == null ? "default" : profileLabel;
		StringBuilder html = new StringBuilder();
		html.append("<!doctype html><html><head><meta charset=\"utf-8\">")
			.append("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">")
			.append("<title>Progression Timeline</title>")
			.append("<style>")
			.append(":root{color-scheme:dark;--bg:#0f1115;--panel:#171b22;--line:#2f3847;--text:#e8edf5;--muted:#91a0b8;--accent:#7dd3fc;--skill:#60a5fa;--quest:#f59e0b;--item:#34d399;--diary:#f472b6;}")
			.append("*{box-sizing:border-box}body{margin:0;font-family:system-ui,-apple-system,Segoe UI,Roboto,sans-serif;background:linear-gradient(180deg,#0f1115 0%,#121826 100%);color:var(--text);} ")
			.append("main{max-width:980px;margin:0 auto;padding:32px 20px 60px;}h1{margin:0 0 8px;font-size:2rem;} .sub{color:var(--muted);margin:0 0 28px;} .timeline{position:relative;padding-left:30px;} .timeline:before{content:'';position:absolute;left:10px;top:4px;bottom:4px;width:2px;background:linear-gradient(180deg,var(--accent),transparent);} .entry{position:relative;margin:0 0 18px;padding:16px 16px 16px 18px;background:rgba(23,27,34,.9);border:1px solid rgba(145,160,184,.18);border-radius:16px;box-shadow:0 8px 24px rgba(0,0,0,.18);} .entry:before{content:'';position:absolute;left:-24px;top:22px;width:12px;height:12px;border-radius:999px;background:var(--accent);box-shadow:0 0 0 4px rgba(125,211,252,.15);} .meta{display:flex;gap:10px;flex-wrap:wrap;align-items:center;font-size:.85rem;color:var(--muted);margin-bottom:6px;} .badge{display:inline-block;padding:4px 10px;border-radius:999px;font-size:.72rem;font-weight:700;letter-spacing:.02em;text-transform:uppercase;} .item{background:rgba(52,211,153,.14);color:var(--item);} .quest{background:rgba(245,158,11,.14);color:var(--quest);} .skill{background:rgba(96,165,250,.14);color:var(--skill);} .diary{background:rgba(244,114,182,.14);color:var(--diary);} .name{font-size:1.05rem;font-weight:700;margin:0 0 4px;} .detail{margin:0;color:var(--text);line-height:1.45;} .empty{padding:28px;border:1px dashed rgba(145,160,184,.35);border-radius:16px;color:var(--muted);text-align:center;} </style></head><body><main>");
		html.append("<h1>Progression Timeline</h1>");
		html.append("<p class=\"sub\">Profile: ").append(escapeHtml(displayProfileLabel)).append("</p>");

		List<ProgressionTrackerEntry> entries = new ArrayList<>(journal.getEntries());
		if (entries.isEmpty())
		{
			html.append("<div class=\"empty\">No progression has been recorded yet.</div>");
		}
		else
		{
			html.append("<section class=\"timeline\">");
			for (ProgressionTrackerEntry entry : entries)
			{
				String badgeClass;
				switch (entry.getType())
				{
					case ITEM:
						badgeClass = "item";
						break;
					case QUEST:
						badgeClass = "quest";
						break;
					case SKILL:
						badgeClass = "skill";
						break;
						case DIARY:
							badgeClass = "diary";
							break;
					default:
						badgeClass = "item";
						break;
				}
				html.append("<article class=\"entry\">")
					.append("<div class=\"meta\"><span class=\"badge ").append(badgeClass).append("\">")
					.append(entry.getType().name().toLowerCase())
					.append("</span><span>")
					.append(escapeHtml(entry.getSource()))
					.append("</span><span>")
					.append(formatTimestamp(entry))
					.append("</span></div>")
					.append("<p class=\"name\">")
					.append(escapeHtml(entry.getName()))
					.append("</p>")
					.append("<p class=\"detail\">")
					.append(escapeHtml(entry.getDetail() == null ? "" : entry.getDetail()))
					.append("</p></article>");
			}
			html.append("</section>");
		}

		html.append("</main></body></html>");
		return html.toString();
	}

	static void exportRaw(File file, ProgressionTrackerJournal journal, Gson gson) throws IOException
	{
		Gson configuredGson = configuredGson(gson);
		ProgressionTrackerState state = new ProgressionTrackerState();
		state.entries = journal.getEntries();
		Files.writeString(file.toPath(), configuredGson.toJson(state), StandardCharsets.UTF_8);
	}

	static ProgressionTrackerJournal importRaw(File file, Gson gson) throws IOException
	{
		Gson configuredGson = configuredGson(gson);
		String json = Files.readString(file.toPath(), StandardCharsets.UTF_8);
		ProgressionTrackerState state = configuredGson.fromJson(json, ProgressionTrackerState.class);
		ProgressionTrackerJournal imported = new ProgressionTrackerJournal();
		if (state != null && state.entries != null)
		{
			imported.load(state.entries);
		}

		return imported;
	}

	private static String formatTimestamp(ProgressionTrackerEntry entry)
	{
		return TIMESTAMP_FORMAT.format(ZonedDateTime.ofInstant(entry.getObtainedAt(), ZoneId.systemDefault()));
	}

	private static String escapeHtml(String input)
	{
		return input
			.replace("&", "&amp;")
			.replace("<", "&lt;")
			.replace(">", "&gt;")
			.replace("\"", "&quot;")
			.replace("'", "&#39;");
	}

	private static File storageFile(String profileKey)
	{
		String safeProfileKey = profileKey == null ? "default" : profileKey.replaceAll("[^a-zA-Z0-9._-]", "_");
		return new File(new File(RuneLite.RUNELITE_DIR, "progression-tracker"), safeProfileKey + ".json");
	}

	private static Gson configuredGson(Gson gson)
	{
		return gson.newBuilder()
			.registerTypeAdapter(Instant.class, (JsonSerializer<Instant>) (src, typeOfSrc, context) -> new JsonPrimitive(src.toString()))
			.registerTypeAdapter(Instant.class, (JsonDeserializer<Instant>) (json, typeOfT, context) -> Instant.parse(json.getAsString()))
			.setPrettyPrinting()
			.create();
	}

	static final class ProgressionTrackerState
	{
		int version = 1;
		List<ProgressionTrackerEntry> entries = new ArrayList<>();
	}
}
