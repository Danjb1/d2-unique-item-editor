# D2Edit

A Unique Item Editor for Diablo II.

**[Video](https://www.youtube.com/watch?v=TLLkM-mlXDY)**

> **NOTE:** This is designed to work with text files for Diablo II LoD v1.11+.

## Pre-Requisites

### DTbl

[DTbl](https://d2mods.info/forum/viewtopic.php?t=47573) is required in order to edit the item names.

This should be extracted such that the executable is located at `dtbl/dtbl.exe`.

### Graphics

I can't legally distribute the graphics from Diablo II, so you will need to follow these steps to produce a *gfx* folder:

1) Use [WinMPQ](http://sfsrealm.hopto.org/downloads/WinMPQ.html) to extract all DC6 files prefixed with *inv* from **d2data.mpq**.

    > Note that WinMPQ requires *Visual Basic 4 runtime files* and *Runtime Files Pack 3*, available from the WinMPQ download page.

1) Use [DC6Con](https://d2mods.info/forum/downloadsystemcat?id=14) to convert all those files from DC6 to PCX.

1) Repeat for **d2exp.mpq** (replace old files when prompted).
