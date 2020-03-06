List of things I've changed or fixed:

- **Nonbreaking changes:**
	- Tunnels will appear "ghosted" if they have no partners (unconnected to anything).
	- Tunnels can be color-coded.
	- Plexers default to NOT having an enable input.
	- Gates default to narrow with 2 inputs.
- **Breaking changes:**
	- No more asynchronous 0 clear on registers.
- **Mac-specific fixes:**
	- Exits when closing last window.
	- Confirms save on exit, as Logisim was using an old method of detecting that which breaks on newer versions of macOS/Java.
	- Fixed Cmd+K/Cmd+E shortcuts (they were being run twice due to a bug in Swing).