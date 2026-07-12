# Plugin Hub Submission Notes

This document helps prepare and submit the Progression Tracker plugin to RuneLite Plugin Hub.

## 1. Metadata

Edit:

- src/main/resources/runelite-plugin.properties

Check fields:

- displayName
- author
- description
- tags
- plugins
- support

Important:

- support should point to a real issue tracker URL you own.

## 2. Build and test

From repository root:

- gradle clean build

Expected:

- Compilation succeeds
- Unit tests pass

## 3. Functional verification

Verify in RuneLite developer mode:

- Plugin appears in plugin list
- Startup/status notifications behave as expected
- HTML export works
- Raw JSON export/import works between two profiles/installations

## 4. Migration scenario validation

Use this exact migration check before submission:

1. On machine/install A, export raw JSON.
2. On machine/install B, import raw JSON.
3. Confirm entry count and representative entries match.
4. Export HTML and verify timeline content.

## 5. Submission prep

- Keep README.md up to date.
- Include changelog notes in pull request description.
- Be ready to answer reviewer questions about:
  - Data storage location
  - External API/network usage (none for this plugin)
  - Import behavior (currently replace, not merge)

## 6. Suggested first release notes

- Initial release of Progression Tracker.
- Tracks quests, skill milestones, and curated item unlocks.
- Exports vertical timeline HTML.
- Supports raw JSON backup/restore for migration.
