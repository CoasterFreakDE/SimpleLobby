# SimpleLobby
 Simple Lobby System for Minecraft PurPur/Paper 1.18.1



## Requirements and Installation

You need to have a minecraft server running paper/purpur 1.18.1 or later. <br>

- [Paper 1.18.1](https://papermc.io/downloads#Paper-1.18)
- [PurPur 1.18.1](https://purpurmc.org/downloads?v=1.18.1)

Download the release and move it into your plugins folder.

» Download [SimpleLobby für 1.18.1](/releases/SimpleLobby.jar)


## Optional Dependencies

For further customization you can install the following dependencies:

- [LuckPerms](https://www.spigotmc.org/resources/luckperms.28140/) for Prefix and Suffix support


## Usage

### Commands
There are only three simple commands:

    - /build
    - /spawn
    - /setspawn

##### /build

With this command you can toggle your build mode. <br>
This will switch into creative mode and saves your current inventory.

Permission: `simplelobby.build` or `OP`


##### /spawn

This command will teleport you to the spawn. <br>
It will also be executed when you join the server. <br>
You need to set a spawn first with `/setspawn`.

Permission: `simplelobby.spawn` or `DEFAULT`


##### /setspawn

With this command you can set the spawn at your current location. <br>

Permission: `simplelobby.setspawn` or `OP`


### Configuration

After enabling the plugin you can configure it in the `config.yml` located in the `config` folder in your root directory. <br>
Every message in here supports [`RGB Hex Colors`](https://github.com/CoasterFreakDE/minecraft-spigot-rgb-chat-support) <br>
You can configure the following settings:

##### Prefix

You can change the prefix to your liking. <br>

##### Welcome message

The welcome message is shown when you join the server. <br>
You can use the following placeholders:

- `%player%`: The player name


##### Server menu

Now comes the fun part. <br>
You can fully customize the server selection menu. <br>
It is seperated into two sections:

- Static Slots
- Server Slots

Static Slots are neverchanging slots. <br>
Here you can be creative and change the look of your menu. <br>
<br>
Server Slots are the slots where users can select a server. <br>
You can configure the following settings:

- server-name: The name of the server
- max-players: The maximum amount of players
- item: An itemstack to display in the slot. Placeholders for lore are `%playerCount%` and `%maxPlayers%`