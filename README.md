# ChatGuard
![ChatGuard-Banner.png](assets/ChatGuard-Banner.png)

## What's ChatGuard?
**ChatGuard** is a Minecraft plugin designed for servers running version b1.7.3. It cancels player messages containing blocked terms or matching RegEx patterns, logs actions (via Discord webhooks, console, or local files), prevents chat spam, issues temporary mutes (requires [Essentials v2.5.8](https://github.com/AleksandarHaralanov/ChatGuard/raw/refs/heads/master/libs/Essentials.jar)), and enforces escalating penalties via a six strike tier system. The plugin is entirely configurable to the operator's liking.

---
### Download
Latest releases of **ChatGuard** can be found here on [GitHub](https://github.com/AleksandarHaralanov/ChatGuard/releases).<br>
Alternatively, you can also download through [Modrinth](https://modrinth.com/plugin/chatguard/versions).

The plugin is fully open-source and transparent.<br>
If you'd like additional peace of mind, you're welcome to scan the `.jar` file using [VirusTotal](https://www.virustotal.com/gui/home/upload).

---
### Requirements
Your b1.7.3 server must be running one of the following APIs: CB1060-CB1092, [Project Poseidon](https://github.com/retromcorg/Project-Poseidon) or [UberBukkit](https://github.com/Moresteck/Project-Poseidon-Uberbukkit).

You also need to be running **Essentials v2.5.8 or newer** for the temporary mutes to work.<br>You can download it from [here](https://github.com/AleksandarHaralanov/ChatGuard/raw/refs/heads/master/libs/Essentials.jar).

---
### Usage
By default, only OPs have permission.

Use PermissionsEx or similar plugins to grant groups the permission, enabling the commands.

#### Commands:
- `/cg` - View ChatGuard commands.
- `/cg about` - About ChatGuard.
- `/cg reload` - `chatguard.config` - Reload ChatGuard configuration.
- `/cg strikes <username>` - `chatguard.config` - View strikes of player.
- `/cg strikes <username> [0-5]` - `chatguard.config` - Set strikes of player.

#### Permissions:
##### Single permissions:
- `chatguard.bypass` - Allows player to bypass the ChatGuard protection.
- `chatguard.config` - Allows player to reload and modify the ChatGuard configuration.
##### Wildcard permissions:
- `chatguard.*` - Wildcard permission that grants all permissions.

---
### Configurations
Generates `config.yml` and `strikes.yml` located at `plugins/ChatGuard`.

#### Config
This is the default `config.yml` configuration file:
```yaml
spam-prevention:      # Configuration for chat spam prevention.
  enabled: true       # Toggles chat spam prevention feature.
  warn-player: true   # Sends a warning to the player when they trigger spam prevention.
  cooldown-ms:        # Specifies cooldown durations in milliseconds for different strike tiers (s0-s5).
    s0: 1000
    s1: 2000
    s2: 3000
    s3: 4000
    s4: 5000
    s5: 6000

filter:
  enabled: true       # Toggles the chat filtering feature.
  warn-player: true   # Sends a warning to the player when their message is blocked.
  log:                
    console: true     # Logs blocked messages to the server console.
    local-file: true  # Logs blocked messages to a local file.
    discord-webhook:  # Logs blocked messages through a Discord webhook by an embed.
      enabled: false  # Toggles Discord webhook logging.
      url: ""         # The URL of the Discord webhook.
  mute:               # Configuration for muting players who violate filter rules.
    enabled: true     # Toggles the muting feature.
    duration:         # Specifies mute durations for different strike tiers (s0-s5).
      s0: "30m"
      s1: "1h"
      s2: "2h"
      s3: "4h"
      s4: "8h"
      s5: "24h"
  rules:              # Configuration for blocking rules.
    regex: []         # List of regular expressions used for blocking messages.
    terms:            # Configuration for blocking specific terms.
      whitelist: []   # List of terms explicitly allowed in messages.
      blacklist: []   # List of terms that are not allowed in messages.
```

> [!CAUTION]
> 🔖 **v2.3.0**: If your server is not running Essentials v2.5.8 or newer, make sure to download and install it. Without it, the entire plugin will break, and in-game messages will fail to send properly. You can find the download [here](#requirements) in the requirements heading.

#### Strikes
The default `strikes.yml` configuration file is initially empty. When a player joins for the first time after ChatGuard is installed on the server, they are added to the configuration with 0 strikes. From there, the plugin manages their strikes, incrementing them up to a maximum of 5 as necessary. Read note below on how that works.

> [!NOTE]
> 🔖 **v2.3.0**: Strike tiers will increment only when the filter is enabled, and a disallowed term or matching regex pattern is detected in a message. Otherwise, all strike tiers will default to 0 unless manually modified in the configuration file or through the included command.