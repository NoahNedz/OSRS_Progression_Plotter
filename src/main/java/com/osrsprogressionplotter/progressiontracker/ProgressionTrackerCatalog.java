/*
 * Copyright (c) 2026
 * All rights reserved.
 */
package com.osrsprogressionplotter.progressiontracker;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

final class ProgressionTrackerCatalog
{
	private static final Set<String> ITEMS = Set.of(
		"Abyssal tentacle",
		"Abyssal whip",
		"Alchemist's amulet",
		"Amulet of glory",
		"Amulet of rancour",
		"Graceful hood",
		"Graceful top",
		"Graceful legs",
		"Graceful gloves",
		"Graceful boots",
		"Graceful cape",
		"Amulet of strength",
		"Amulet of torture",
		"Ancestral hat",
		"Ancestral robe top",
		"Ancestral robe bottom",
		"Ancient icon",
		"Ancient staff",
		"Arclight",
		"Arkan blade",
		"Ash sanctifier",
		"Fighter torso",
		"Dragon defender",
		"Barrows gloves",
		"Ava's accumulator",
		"Ava's assembler",
		"Avernic treads",
		"Bandos godsword",
		"Berserker ring (i)",
		"Black mask (i)",
		"Bloodbark body",
		"Bloodbark helm",
		"Bloodbark legs",
		"Bonecrusher",
		"Book of the dead",
		"Burning claws",
		"Climbing boots",
		"Confliction gauntlets",
		"Crystal halberd",
		"Desert amulet 4",
		"Fire cape",
		"Slayer helmet",
		"Slayer helmet (i)",
		"Dragon boots",
		"Dragon claws",
		"Dragon dagger",
		"Dragon pickaxe",
		"Dragon warhammer",
		"Elder maul",
		"Elidinis' ward",
		"Elite void robe",
		"Elite void top",
		"Emberlight",
		"Eternal boots",
		"Explorer's ring 4",
		"Eye of ayak",
		"Ferocious gloves",
		"Gem bag",
		"Ghommal's hilt 2",
		"Ghommal's hilt 4",
		"Granite body",
		"Hallowed crystal shard",
		"Herb sack",
		"Imbued zamorak cape",
		"Infernal cape",
		"Infinity boots",
		"Karamja gloves 3",
		"Karamja gloves 4",
		"Lightbearer",
		"Mage's book",
		"Magus ring",
		"Helm of neitiznot",
		"Dragon scimitar",
		"Magic shortbow (i)",
		"God d'hide body",
		"Ranger boots",
		"Mixed hide boots",
		"Mixed hide cape",
		"Necklace of anguish",
		"Oathplate chest",
		"Oathplate helm",
		"Oathplate legs",
		"Berserker ring",
		"Zombie axe",
		"Bow of faerdhinen (c)",
		"Crystal helm",
		"Crystal body",
		"Crystal legs",
		"Pharaoh's sceptre",
		"Prescription goggles",
		"Primordial boots",
		"Rada's blessing 4",
		"Red chinchompa",
		"Ring of suffering (i)",
		"Rite of vile transference",
		"Rune pouch",
		"Salve amulet(ei)",
		"Saturated heart",
		"Scorching bow",
		"Scythe of vitur",
		"Thread of elidinis",
		"Tormented bracelet",
		"Toxic blowpipe",
		"Tumeken's shadow",
		"Twisted bow",
		"Ultor ring",
		"Void knight gloves",
		"Void knight robe",
		"Void knight top",
		"Void ranger helm",
		"Voidwaker",
		"Warped sceptre",
		"Wrath rune",
		"Zamorakian hasta",
		"Bandos chestplate",
		"Bandos tassets",
		"Osmumten's fang",
		"Avernic defender",
		"Torva platebody",
		"Torva platelegs",
		"Masori body (f)",
		"Masori chaps (f)",
		"Masori mask (f)",
		"Zaryte crossbow",
		"Zaryte vambraces",
		"Dizana's quiver",
		"Occult necklace",
		"Trident of the swamp",
		"Iban's staff (u)"
	);

	private static final Map<String, String> ITEM_SOURCE = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

	static
	{
		ITEM_SOURCE.put("Graceful hood", "Skilling");
		ITEM_SOURCE.put("Graceful top", "Skilling");
		ITEM_SOURCE.put("Graceful legs", "Skilling");
		ITEM_SOURCE.put("Graceful gloves", "Skilling");
		ITEM_SOURCE.put("Graceful boots", "Skilling");
		ITEM_SOURCE.put("Graceful cape", "Skilling");
		ITEM_SOURCE.put("Fighter torso", "Equipment");
		ITEM_SOURCE.put("Dragon defender", "Equipment");
		ITEM_SOURCE.put("Barrows gloves", "Quest");
		ITEM_SOURCE.put("Ava's accumulator", "Quest");
		ITEM_SOURCE.put("Ava's assembler", "Quest");
		ITEM_SOURCE.put("Fire cape", "Equipment");
		ITEM_SOURCE.put("Slayer helmet", "Equipment");
		ITEM_SOURCE.put("Slayer helmet (i)", "Equipment");
		ITEM_SOURCE.put("Helm of neitiznot", "Equipment");
		ITEM_SOURCE.put("Dragon scimitar", "Equipment");
		ITEM_SOURCE.put("Magic shortbow (i)", "Equipment");
		ITEM_SOURCE.put("God d'hide body", "Equipment");
		ITEM_SOURCE.put("Ranger boots", "Equipment");
		ITEM_SOURCE.put("Amulet of glory", "Equipment");
		ITEM_SOURCE.put("Salve amulet(ei)", "Equipment");
		ITEM_SOURCE.put("Oathplate chest", "Equipment");
		ITEM_SOURCE.put("Oathplate helm", "Equipment");
		ITEM_SOURCE.put("Oathplate legs", "Equipment");
		ITEM_SOURCE.put("Berserker ring", "Equipment");
		ITEM_SOURCE.put("Zombie axe", "Equipment");
		ITEM_SOURCE.put("Bow of faerdhinen (c)", "Equipment");
		ITEM_SOURCE.put("Crystal helm", "Equipment");
		ITEM_SOURCE.put("Crystal body", "Equipment");
		ITEM_SOURCE.put("Crystal legs", "Equipment");
		ITEM_SOURCE.put("Bandos chestplate", "Equipment");
		ITEM_SOURCE.put("Bandos tassets", "Equipment");
		ITEM_SOURCE.put("Osmumten's fang", "Equipment");
		ITEM_SOURCE.put("Avernic defender", "Equipment");
		ITEM_SOURCE.put("Torva platebody", "Equipment");
		ITEM_SOURCE.put("Torva platelegs", "Equipment");
		ITEM_SOURCE.put("Masori body (f)", "Equipment");
		ITEM_SOURCE.put("Masori chaps (f)", "Equipment");
		ITEM_SOURCE.put("Zaryte crossbow", "Equipment");
		ITEM_SOURCE.put("Dizana's quiver", "Equipment");
		ITEM_SOURCE.put("Ancestral robe top", "Equipment");
		ITEM_SOURCE.put("Ancestral robe bottom", "Equipment");
		ITEM_SOURCE.put("Occult necklace", "Equipment");
		ITEM_SOURCE.put("Trident of the swamp", "Equipment");
		ITEM_SOURCE.put("Iban's staff (u)", "Equipment");

		// Ensure every tracked item has a source; default combat-oriented progression label.
		ITEMS.forEach(item -> ITEM_SOURCE.putIfAbsent(item, "Equipment"));
	}

	private ProgressionTrackerCatalog()
	{
	}

	static boolean isTrackedItem(String itemName)
	{
		return ITEMS.contains(itemName);
	}

	static String itemSource(String itemName)
	{
		return ITEM_SOURCE.getOrDefault(itemName, "Item");
	}
}


