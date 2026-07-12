/*
 * Copyright (c) 2026
 * All rights reserved.
 */
package com.osrsprogressionplotter.progressiontracker;

import java.awt.BorderLayout;
import java.io.File;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.FontManager;
import net.runelite.client.ui.PluginPanel;
import net.runelite.client.util.SwingUtil;

class ProgressionTrackerPanel extends PluginPanel
{
	private final ProgressionTrackerPlugin plugin;
	private final JLabel summary = new JLabel("No progression captured yet");

	ProgressionTrackerPanel(ProgressionTrackerPlugin plugin)
	{
		super();
		this.plugin = plugin;

		setBackground(ColorScheme.DARK_GRAY_COLOR);
		setLayout(new BorderLayout());

		JPanel content = new JPanel();
		content.setBackground(ColorScheme.DARK_GRAY_COLOR);
		content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

		summary.setFont(FontManager.getRunescapeSmallFont());
		summary.setForeground(ColorScheme.LIGHT_GRAY_COLOR);
		content.add(summary);

		JButton export = new JButton("Export HTML");
		SwingUtil.removeButtonDecorations(export);
		export.setFont(FontManager.getRunescapeSmallFont());
		export.addActionListener(ev -> exportHtml());
		content.add(export);

		JButton exportRaw = new JButton("Export Raw JSON");
		SwingUtil.removeButtonDecorations(exportRaw);
		exportRaw.setFont(FontManager.getRunescapeSmallFont());
		exportRaw.addActionListener(ev -> exportRaw());
		content.add(exportRaw);

		JButton importRaw = new JButton("Import Raw JSON");
		SwingUtil.removeButtonDecorations(importRaw);
		importRaw.setFont(FontManager.getRunescapeSmallFont());
		importRaw.addActionListener(ev -> importRaw());
		content.add(importRaw);

		add(content, BorderLayout.NORTH);
	}

	void refresh(ProgressionTrackerJournal journal)
	{
		summary.setText("Recorded entries: " + journal.size());
	}

	private void exportHtml()
	{
		JFileChooser chooser = new JFileChooser();
		chooser.setDialogTitle("Export progression timeline");
		chooser.addChoosableFileFilter(new FileNameExtensionFilter("HTML files", "html", "htm"));
		chooser.setAcceptAllFileFilterUsed(true);
		chooser.setSelectedFile(new File("progression-timeline.html"));

		if (chooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION)
		{
			return;
		}

		File file = chooser.getSelectedFile();
		if (!file.getName().toLowerCase().endsWith(".html") && !file.getName().toLowerCase().endsWith(".htm"))
		{
			file = new File(file.getParentFile(), file.getName() + ".html");
		}

		plugin.exportHtml(file);
	}

	private void exportRaw()
	{
		JFileChooser chooser = new JFileChooser();
		chooser.setDialogTitle("Export raw progression backup");
		chooser.addChoosableFileFilter(new FileNameExtensionFilter("JSON files", "json"));
		chooser.setAcceptAllFileFilterUsed(true);
		chooser.setSelectedFile(new File("progression-raw.json"));

		if (chooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION)
		{
			return;
		}

		File file = chooser.getSelectedFile();
		if (!file.getName().toLowerCase().endsWith(".json"))
		{
			file = new File(file.getParentFile(), file.getName() + ".json");
		}

		plugin.exportRaw(file);
	}

	private void importRaw()
	{
		JFileChooser chooser = new JFileChooser();
		chooser.setDialogTitle("Import raw progression backup");
		chooser.addChoosableFileFilter(new FileNameExtensionFilter("JSON files", "json"));
		chooser.setAcceptAllFileFilterUsed(true);

		if (chooser.showOpenDialog(this) != JFileChooser.APPROVE_OPTION)
		{
			return;
		}

		plugin.importRaw(chooser.getSelectedFile());
	}
}
