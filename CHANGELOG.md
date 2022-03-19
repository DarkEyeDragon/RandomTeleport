# Random Teleport Changelog

## Versions

#### version 3.0.0 (latest)

- Fixed various bugs related to timing
- Fixed various bugs related to eco
- Changed to a tick based searcher to not choke chunk thread on heavy load
- Config settings have changed to tick based instead of "5m" "10m" to keep it consistent
- Updated to Minecraft 1.18.2

#### version 1.2

- Add queue_init delay to config
- Fixed LocationSearcher (again) to now take offsets into account properly. Before it would not convert location offsets
  to chunk offsets resulting in offsets being 16x too big. Now rtp actually works as intended.
- Fix 0 argument commands failing to execute
- Added option to disable the queue population messages
- Added depleted queue message to config

#### version 1.1.1

#### version 1.1
- Added a queue system, now a location is always ready when needed. 
- Vastly improved location searching
- When reloading the config you the queue gets repopulated with the new parameters
- Reformatted the config (it is highly recommended to generate a new one!)
- Added Java 8 support (previously only Java 9+ was supported)
- Added Griefprevention support