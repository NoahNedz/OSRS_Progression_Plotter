# OSRS Progression Tracker (Plugin Hub Project)

This repository contains a standalone external RuneLite plugin project for Progression Tracker, organized for Plugin Hub submission.

## What it does

- Tracks curated progression milestones:
  - Quest completions
  - Skill level milestones
  - Achievement Diary completions (Easy/Medium/Hard/Elite)
  - Important progression items
- Captures from supported containers:
  - Inventory
  - Equipment
  - Bank
  - Seed Vault
  - Group Storage
- Exports a vertical HTML timeline
- Exports and imports raw JSON progression backups for migration across installs

## Project structure

- src/main/java/com/osrsprogressionplotter/progressiontracker
  - Plugin source code
- src/main/resources/runelite-plugin.properties
  - Plugin Hub metadata
- src/test/java/com/osrsprogressionplotter/progressiontracker
  - Unit tests

## Build

Requirements:

- Java 11+ (Java 21 recommended locally)
- Gradle

Build command:

- gradle build

## Local testing options

- Sideload into RuneLite developer mode (recommended during development)
- Package and run in your custom RuneLite workflow

## Plugin Hub submission checklist

1. Verify metadata in src/main/resources/runelite-plugin.properties.
2. Replace support URL placeholder with your real issue tracker URL.
3. Confirm description/tags are final.
4. Ensure all tests pass.
5. Follow Plugin Hub review requirements.

See docs/PLUGIN_HUB_SUBMISSION.md for a practical submission flow.
