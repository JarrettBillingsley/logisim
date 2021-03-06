List of things I've changed or fixed:

- **Backwards-compatible changes:**
	- Tunnels will appear "ghosted" if they have no partners (unconnected to anything).
	- Tunnels can be color-coded.
	- Plexers default to NOT having an enable input.
	- Multi-bit wires with unknown values now display blue, instead of black.
	- Duplicate/paste put the copied components at the mouse cursor, where they can be placed wherever the user wants.
	- Displays Logisim icon in taskbar on modern OSes.
	- Now clearer when you are viewing an embedded subcircuit's state vs. the subcircuit's prototype.
	- Added an icon in the toolbar that shows whether simulation is enabled (green triangle) or not (red square).
- **Non-backwards-compatible changes:**
	- Gates default to narrow with 2 inputs. (Sometimes this breaks old circuits)
	- No more asynchronous 0 clear on registers.
- **Bugfixes**:
	- When duplicated, tunnels no longer stack up on themselves (they are offset like everything else).
	- Using arrow keys/home/end when editing labels no longer scrolls the circuit view.
- **Mac-specific bugfixes:**
	- Exits when closing last window.
	- Confirms save on exit, as Logisim was using an old method of detecting that which breaks on newer versions of macOS/Java.
	- Fixed Cmd+K/Cmd+E shortcuts (they were being run twice due to a bug in Swing).
	- Title now displays as "Logisim" instead of "Main".