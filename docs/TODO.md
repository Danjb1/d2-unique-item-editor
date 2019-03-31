# To Do

## Code

 - Use d2tbledit instead of dtbl?
    - Ensure "always insert" is used in the case of duplicate keys?
        - Just don't keep inserting the same strings over and over again!!
    - Export to text, edit text, and import again?

 - Use data from MPQ files instead of supplying hardcoded data files?
    
 - "Bone" sound appears in "Standard Sounds" and "Unused Sounds"
 - Include essence inv files from v1.13c
 - Ability to delete items
 - Backup UniqueItems.txt and patchstring.tbl before saving
 - What do we do if we fail to add to the string TBL? We should give a warning
 - "Help" button that becomes available for some properties (see help.txt)
 - Tooltips for fields like "Rarity"
 - Lock X and Y to '1' for boolean effects
 - Have a way of sorting items by category or the order they appear in the text file (the latter, at least, should be easy)
 - Read data directly from the game's text files instead of a manual database? This would require being able to parse many other files
 - Filter out expansion items when expansion = false? Again, this would require being able to parse many other files
 - Write an install script to produce the required "gfx" folder
 - Rune items, set items, other editors

## Properties / Help Notes

 - Unrecognised property: randclassskill
 - Go through all help notes and tidy up
 - Colour shifts depend on the item and so won't always work
 - See http://d2mods.info/forum/viewtopic.php?f=4&t=10179 for useful notes (possibly copy their categories?)
 - Some properties are not written correctly (e.g. increased chance of blocking shouldn't start with a '+')
 - [Not Shown] vs [Not Displayed] ?
 - Difference between swing1/swing2/swing3 (also move2, cast2, hit2, block2, etc.)?
 - Skill IDs (add more, not just class skills)
 - Skill tree IDs
 - Class IDs?
 - Crushing Blow / Deadly Strike / Open Wounds / Hit Blinds Target
