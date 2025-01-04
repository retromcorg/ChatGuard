# ChatGuard
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
### Configuration
Generates `config.yml` and `strikes.yml` located at `plugins/ChatGuard`.