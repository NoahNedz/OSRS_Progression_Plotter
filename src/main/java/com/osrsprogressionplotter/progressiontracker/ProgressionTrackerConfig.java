/*
 * Copyright (c) 2026
 * All rights reserved.
 */
package com.osrsprogressionplotter.progressiontracker;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("progressiontracker")
public interface ProgressionTrackerConfig extends Config
{
	@ConfigItem(
		keyName = "openPanelOnStartup",
		name = "Open panel on startup",
		description = "Open the Progression Tracker side panel when the plugin starts"
	)
	default boolean openPanelOnStartup()
	{
		return true;
	}

	@ConfigItem(
		keyName = "chatNotifications",
		name = "Chat notifications",
		description = "Show chat messages when progression entries are captured"
	)
	default boolean chatNotifications()
	{
		return true;
	}
}
