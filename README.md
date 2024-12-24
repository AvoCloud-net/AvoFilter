# AvoFilter

Minecraft integration for AvoCloud's [SafeText](https://github.com/AvoCloud-net/SafeText) chat filtering server.


## Default config

```yaml
# The address of your chatfilter server, default server is AvoCloud's and will work, recommended to use your own server as delays are avoided
address: "https://safetext.avocloud.net/chatfilter"
key: "savetext-publickey" # The key to use for the chatfilter server

punish:
  # The type of punishment to be executed
  # kick: Just kick the player from the server Pro: No trouble with signatures Con: Player can rejoin
  # command: Execute a command on the player Pro: You can handle it with your own plugins Con: Command has to be on proxy
  # cancel: Cancel the message Pro: No traces Con: Lots of trouble with signatures so you should use this only if you know what you are doing
  type: "kick"
  # Content of what should be done.
  # %message% will be replaced with the message the player sent
  # %player% will be replaced with the name of the player
  # %player_uuid% will be replaced with the uuid of the player
  # %rating% will be replaced with the rating of the message
  content: "You have been kicked for using inappropriate language. Message: '%message%'."

debug: false # Enable debug messages
config-version: 1 # DO NOT TOUCH THIS
```


[Modrinth](https://modrinth.com/project/avofilter)  
[Discord](https://avocloud.net/discord/)
