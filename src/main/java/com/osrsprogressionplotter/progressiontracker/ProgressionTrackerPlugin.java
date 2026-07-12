/*
 * Copyright (c) 2026
 * All rights reserved.
 */
package com.osrsprogressionplotter.progressiontracker;

import com.google.inject.Provides;
import java.awt.Desktop;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.Set;
import javax.inject.Inject;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.Item;
import net.runelite.api.ItemContainer;
import net.runelite.api.ItemComposition;
import net.runelite.api.InventoryID;
import net.runelite.api.Player;
import net.runelite.api.Quest;
import net.runelite.api.QuestState;
import net.runelite.api.Skill;
import net.runelite.api.Varbits;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.ItemContainerChanged;
import net.runelite.api.events.StatChanged;
import net.runelite.api.ChatMessageType;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.game.ItemManager;
import net.runelite.client.chat.ChatMessageBuilder;
import net.runelite.client.chat.ChatMessageManager;
import net.runelite.client.chat.ChatColorType;
import net.runelite.client.chat.QueuedMessage;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.ClientToolbar;
import net.runelite.client.ui.NavigationButton;
import net.runelite.client.util.ImageUtil;

@PluginDescriptor(
	name = "Progression Tracker",
	description = "Track curated account progression and export a vertical HTML timeline",
	tags = {"progression", "timeline", "export", "items", "quests", "skills"}
)
@Slf4j
public class ProgressionTrackerPlugin extends Plugin
{
	private static final int INVENTORY_CONTAINER_ID = InventoryID.INVENTORY.getId();
	private static final int EQUIPMENT_CONTAINER_ID = InventoryID.EQUIPMENT.getId();
	private static final int BANK_CONTAINER_ID = InventoryID.BANK.getId();
	private static final int SEED_VAULT_CONTAINER_ID = InventoryID.SEED_VAULT.getId();
	private static final int GROUP_STORAGE_CONTAINER_ID = InventoryID.GROUP_STORAGE.getId();
	private static final int GROUP_STORAGE_INV_CONTAINER_ID = InventoryID.GROUP_STORAGE_INV.getId();

	private static final Set<Integer> TRACKED_CONTAINER_IDS = Set.of(
		INVENTORY_CONTAINER_ID,
		EQUIPMENT_CONTAINER_ID,
		BANK_CONTAINER_ID,
		SEED_VAULT_CONTAINER_ID,
		GROUP_STORAGE_CONTAINER_ID,
		GROUP_STORAGE_INV_CONTAINER_ID
	);

	private static final List<DiaryMilestone> DIARY_MILESTONES = List.of(
		new DiaryMilestone("Ardougne", "Easy", Varbits.DIARY_ARDOUGNE_EASY),
		new DiaryMilestone("Ardougne", "Medium", Varbits.DIARY_ARDOUGNE_MEDIUM),
		new DiaryMilestone("Ardougne", "Hard", Varbits.DIARY_ARDOUGNE_HARD),
		new DiaryMilestone("Ardougne", "Elite", Varbits.DIARY_ARDOUGNE_ELITE),
		new DiaryMilestone("Desert", "Easy", Varbits.DIARY_DESERT_EASY),
		new DiaryMilestone("Desert", "Medium", Varbits.DIARY_DESERT_MEDIUM),
		new DiaryMilestone("Desert", "Hard", Varbits.DIARY_DESERT_HARD),
		new DiaryMilestone("Desert", "Elite", Varbits.DIARY_DESERT_ELITE),
		new DiaryMilestone("Falador", "Easy", Varbits.DIARY_FALADOR_EASY),
		new DiaryMilestone("Falador", "Medium", Varbits.DIARY_FALADOR_MEDIUM),
		new DiaryMilestone("Falador", "Hard", Varbits.DIARY_FALADOR_HARD),
		new DiaryMilestone("Falador", "Elite", Varbits.DIARY_FALADOR_ELITE),
		new DiaryMilestone("Fremennik", "Easy", Varbits.DIARY_FREMENNIK_EASY),
		new DiaryMilestone("Fremennik", "Medium", Varbits.DIARY_FREMENNIK_MEDIUM),
		new DiaryMilestone("Fremennik", "Hard", Varbits.DIARY_FREMENNIK_HARD),
		new DiaryMilestone("Fremennik", "Elite", Varbits.DIARY_FREMENNIK_ELITE),
		new DiaryMilestone("Kandarin", "Easy", Varbits.DIARY_KANDARIN_EASY),
		new DiaryMilestone("Kandarin", "Medium", Varbits.DIARY_KANDARIN_MEDIUM),
		new DiaryMilestone("Kandarin", "Hard", Varbits.DIARY_KANDARIN_HARD),
		new DiaryMilestone("Kandarin", "Elite", Varbits.DIARY_KANDARIN_ELITE),
		new DiaryMilestone("Karamja", "Easy", Varbits.DIARY_KARAMJA_EASY),
		new DiaryMilestone("Karamja", "Medium", Varbits.DIARY_KARAMJA_MEDIUM),
		new DiaryMilestone("Karamja", "Hard", Varbits.DIARY_KARAMJA_HARD),
		new DiaryMilestone("Karamja", "Elite", Varbits.DIARY_KARAMJA_ELITE),
		new DiaryMilestone("Kourend & Kebos", "Easy", Varbits.DIARY_KOUREND_EASY),
		new DiaryMilestone("Kourend & Kebos", "Medium", Varbits.DIARY_KOUREND_MEDIUM),
		new DiaryMilestone("Kourend & Kebos", "Hard", Varbits.DIARY_KOUREND_HARD),
		new DiaryMilestone("Kourend & Kebos", "Elite", Varbits.DIARY_KOUREND_ELITE),
		new DiaryMilestone("Lumbridge & Draynor", "Easy", Varbits.DIARY_LUMBRIDGE_EASY),
		new DiaryMilestone("Lumbridge & Draynor", "Medium", Varbits.DIARY_LUMBRIDGE_MEDIUM),
		new DiaryMilestone("Lumbridge & Draynor", "Hard", Varbits.DIARY_LUMBRIDGE_HARD),
		new DiaryMilestone("Lumbridge & Draynor", "Elite", Varbits.DIARY_LUMBRIDGE_ELITE),
		new DiaryMilestone("Morytania", "Easy", Varbits.DIARY_MORYTANIA_EASY),
		new DiaryMilestone("Morytania", "Medium", Varbits.DIARY_MORYTANIA_MEDIUM),
		new DiaryMilestone("Morytania", "Hard", Varbits.DIARY_MORYTANIA_HARD),
		new DiaryMilestone("Morytania", "Elite", Varbits.DIARY_MORYTANIA_ELITE),
		new DiaryMilestone("Varrock", "Easy", Varbits.DIARY_VARROCK_EASY),
		new DiaryMilestone("Varrock", "Medium", Varbits.DIARY_VARROCK_MEDIUM),
		new DiaryMilestone("Varrock", "Hard", Varbits.DIARY_VARROCK_HARD),
		new DiaryMilestone("Varrock", "Elite", Varbits.DIARY_VARROCK_ELITE),
		new DiaryMilestone("Western Provinces", "Easy", Varbits.DIARY_WESTERN_EASY),
		new DiaryMilestone("Western Provinces", "Medium", Varbits.DIARY_WESTERN_MEDIUM),
		new DiaryMilestone("Western Provinces", "Hard", Varbits.DIARY_WESTERN_HARD),
		new DiaryMilestone("Western Provinces", "Elite", Varbits.DIARY_WESTERN_ELITE),
		new DiaryMilestone("Wilderness", "Easy", Varbits.DIARY_WILDERNESS_EASY),
		new DiaryMilestone("Wilderness", "Medium", Varbits.DIARY_WILDERNESS_MEDIUM),
		new DiaryMilestone("Wilderness", "Hard", Varbits.DIARY_WILDERNESS_HARD),
		new DiaryMilestone("Wilderness", "Elite", Varbits.DIARY_WILDERNESS_ELITE)
	);

	@Inject
	private Client client;

	@Inject
	private ClientToolbar clientToolbar;

	@Inject
	private ConfigManager configManager;

	@Inject
	private ItemManager itemManager;

	@Inject
	private ProgressionTrackerConfig config;

	@Inject
	private ChatMessageManager chatMessageManager;

	private NavigationButton navButton;
	private ProgressionTrackerPanel panel;
	private ProgressionTrackerJournal journal = new ProgressionTrackerJournal();
	private String activeProfileKey;
	private boolean bootstrapPending;
	private boolean dirty;

	@Provides
	ProgressionTrackerConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(ProgressionTrackerConfig.class);
	}

	@Override
	protected void startUp() throws Exception
	{
		panel = new ProgressionTrackerPanel(this);
		BufferedImage icon = ImageUtil.loadImageResource(getClass(), "/skill_icons/overall.png");
		navButton = NavigationButton.builder()
			.tooltip("Progression Tracker")
			.icon(icon)
			.priority(6)
			.panel(panel)
			.build();
		clientToolbar.addNavigation(navButton);
		if (config.openPanelOnStartup())
		{
			clientToolbar.openPanel(navButton);
		}
		announce("Progression Tracker loaded. Use the Progression Tracker side panel to export HTML.");
		bootstrapIfLoggedIn();
	}

	@Override
	protected void shutDown() throws Exception
	{
		saveIfDirty();
		clientToolbar.removeNavigation(navButton);
		panel = null;
		navButton = null;
		journal = new ProgressionTrackerJournal();
		activeProfileKey = null;
		bootstrapPending = false;
		dirty = false;
	}

	@Subscribe
	public void onGameStateChanged(GameStateChanged event)
	{
		if (event.getGameState() == GameState.LOGGED_IN)
		{
			bootstrapIfLoggedIn();
		}
		else if (event.getGameState() == GameState.LOGIN_SCREEN)
		{
			saveIfDirty();
		}
	}

	@Subscribe
	public void onGameTick(GameTick event)
	{
		if (bootstrapPending)
		{
			bootstrapPending = false;
			captureBaseline();
		}

		if (client.getGameState() != GameState.LOGGED_IN)
		{
			return;
		}

		captureQuests();
		captureDiaries();
		saveIfDirty();
	}

	@Subscribe
	public void onStatChanged(StatChanged event)
	{
		if (client.getGameState() != GameState.LOGGED_IN)
		{
			return;
		}

		String key = "skill:" + event.getSkill().name() + ":" + event.getLevel();
		if (journal.recordSkill(key, event.getSkill().getName(), Instant.now(), "Reached level " + event.getLevel()))
		{
			dirty = true;
			refreshPanel();
			announce("Tracked skill milestone: " + event.getSkill().getName() + " level " + event.getLevel() + ".");
		}
	}

	@Subscribe
	public void onItemContainerChanged(ItemContainerChanged event)
	{
		if (client.getGameState() != GameState.LOGGED_IN || !TRACKED_CONTAINER_IDS.contains(event.getContainerId()))
		{
			return;
		}

		captureItems();
		saveIfDirty();
	}

	void exportHtml(File file)
	{
		if (activeProfileKey == null)
		{
			activeProfileKey = currentProfileKey();
		}

		String html = ProgressionTrackerStore.exportHtml(currentProfileLabel(), journal);
		try
		{
			java.nio.file.Files.writeString(file.toPath(), html, java.nio.charset.StandardCharsets.UTF_8);
			if (Desktop.isDesktopSupported())
			{
				Desktop.getDesktop().open(file);
			}
		}
		catch (IOException ex)
		{
			SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(null, "Unable to export progression timeline."));
			log.warn("error exporting progression html", ex);
		}
	}

	void exportRaw(File file)
	{
		try
		{
			ProgressionTrackerStore.exportRaw(file, journal);
			announce("Exported raw progression backup with " + journal.size() + " entries.");
		}
		catch (IOException ex)
		{
			SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(null, "Unable to export raw progression backup."));
			log.warn("error exporting raw progression", ex);
		}
	}

	void importRaw(File file)
	{
		if (activeProfileKey == null)
		{
			activeProfileKey = currentProfileKey();
		}

		if (activeProfileKey == null)
		{
			SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(null, "Log in first so imported progression can be saved to your profile."));
			return;
		}

		try
		{
			journal = ProgressionTrackerStore.importRaw(file);
			dirty = true;
			refreshPanel();
			saveIfDirty();
			announce("Imported raw progression backup with " + journal.size() + " entries.");
		}
		catch (IOException | RuntimeException ex)
		{
			SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(null, "Unable to import raw progression backup."));
			log.warn("error importing raw progression", ex);
		}
	}

	private void bootstrapIfLoggedIn()
	{
		if (client.getGameState() != GameState.LOGGED_IN)
		{
			return;
		}

		String profileKey = currentProfileKey();
		if (profileKey == null)
		{
			return;
		}

		if (!profileKey.equals(activeProfileKey))
		{
			saveIfDirty();
			activeProfileKey = profileKey;
			journal = ProgressionTrackerStore.load(profileKey);
			bootstrapPending = true;
			dirty = false;
			refreshPanel();
		}
	}

	private void captureBaseline()
	{
		int before = journal.size();
		captureQuests();
		captureDiaries();
		captureSkills();
		captureItems();
		int captured = journal.size() - before;
		if (captured > 0)
		{
			announce("Progression baseline captured " + captured + " entries.");
		}
		dirty = true;
		refreshPanel();
		saveIfDirty();
	}

	private void captureSkills()
	{
		for (Skill skill : Skill.values())
		{
			String key = "skill:" + skill.name() + ":" + client.getRealSkillLevel(skill);
			if (journal.recordSkill(key, skill.getName(), Instant.now(), "Reached level " + client.getRealSkillLevel(skill)))
			{
				dirty = true;
			}
		}
	}

	private void captureQuests()
	{
		int captured = 0;
		for (Quest quest : Quest.values())
		{
			QuestState state = quest.getState(client);
			if (state != QuestState.FINISHED)
			{
				continue;
			}

			String key = "quest:" + quest.name();
			if (journal.recordQuest(key, quest.getName(), Instant.now(), "Quest completed"))
			{
				dirty = true;
				captured++;
			}
		}

		if (captured > 0)
		{
			announce("Tracked " + captured + " completed quest" + (captured == 1 ? "" : "s") + ".");
		}
	}

	private void captureItems()
	{
		int captured = 0;
		for (int containerId : TRACKED_CONTAINER_IDS)
		{
			ItemContainer container = client.getItemContainer(containerId);
			if (container == null)
			{
				continue;
			}

			for (Item item : container.getItems())
			{
				if (item == null || item.getId() <= 0 || item.getQuantity() <= 0)
				{
					continue;
				}

				int canonicalId = itemManager.canonicalize(item.getId());
				ItemComposition itemComposition = itemManager.getItemComposition(canonicalId);
				String itemName = itemComposition.getName();
				if (!ProgressionTrackerCatalog.isTrackedItem(itemName))
				{
					continue;
				}

				String key = "item:" + canonicalId;
				String source = ProgressionTrackerCatalog.itemSource(itemName);
				String detail = source + " unlock found in " + containerLabel(containerId);
				if (journal.recordItem(key, itemName, source, Instant.now(), detail))
				{
					dirty = true;
					captured++;
				}
			}
		}

		if (captured > 0)
		{
			announce("Tracked " + captured + " new progression item" + (captured == 1 ? "" : "s") + ".");
		}

		refreshPanel();
	}

	private void captureDiaries()
	{
		int captured = 0;
		for (DiaryMilestone milestone : DIARY_MILESTONES)
		{
			if (client.getVarbitValue(milestone.varbitId) <= 0)
			{
				continue;
			}

			String key = "diary:" + milestone.area + ":" + milestone.tier;
			String name = milestone.area + " Diary (" + milestone.tier + ")";
			String detail = milestone.area + " " + milestone.tier + " diary completed";
			if (journal.recordDiary(key, name, Instant.now(), detail))
			{
				dirty = true;
				captured++;
			}
		}

		if (captured > 0)
		{
			announce("Tracked " + captured + " Achievement Diary completion" + (captured == 1 ? "" : "s") + ".");
		}
	}

	private void announce(String message)
	{
		if (!config.chatNotifications())
		{
			return;
		}

		final String chatMessage = new ChatMessageBuilder()
			.append(ChatColorType.HIGHLIGHT)
			.append("[Progression] ")
			.append(ChatColorType.NORMAL)
			.append(message)
			.build();

		chatMessageManager.queue(QueuedMessage.builder()
			.type(ChatMessageType.CONSOLE)
			.runeLiteFormattedMessage(chatMessage)
			.build());
	}

	private void refreshPanel()
	{
		if (panel != null)
		{
			panel.refresh(journal);
		}
	}

	private void saveIfDirty()
	{
		if (!dirty || activeProfileKey == null)
		{
			return;
		}

		ProgressionTrackerStore.save(activeProfileKey, journal);
		dirty = false;
	}

	private String currentProfileKey()
	{
		String profileKey = configManager.getRSProfileKey();
		if (profileKey != null)
		{
			return profileKey;
		}

		long accountHash = client.getAccountHash();
		return accountHash == 0 ? null : Long.toString(accountHash);
	}

	private String containerLabel(int containerId)
	{
		if (containerId == INVENTORY_CONTAINER_ID)
		{
			return "inventory";
		}

		if (containerId == EQUIPMENT_CONTAINER_ID)
		{
			return "equipment";
		}

		if (containerId == BANK_CONTAINER_ID)
		{
			return "bank";
		}

		if (containerId == SEED_VAULT_CONTAINER_ID)
		{
			return "seed vault";
		}

		if (containerId == GROUP_STORAGE_CONTAINER_ID)
		{
			return "group storage";
		}

		if (containerId == GROUP_STORAGE_INV_CONTAINER_ID)
		{
			return "group storage inventory";
		}

		return "container";
	}

	private String currentProfileLabel()
	{
		Player localPlayer = client.getLocalPlayer();
		if (localPlayer != null)
		{
			String name = localPlayer.getName();
			if (name != null && !name.isEmpty())
			{
				return name;
			}
		}

		return activeProfileKey == null ? "default" : activeProfileKey;
	}

	private static final class DiaryMilestone
	{
		private final String area;
		private final String tier;
		private final int varbitId;

		private DiaryMilestone(String area, String tier, int varbitId)
		{
			this.area = area;
			this.tier = tier;
			this.varbitId = varbitId;
		}
	}
}
