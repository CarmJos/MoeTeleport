```text
 __  __         _______   _                       _   
|  \/  |       |__   __| | |                     | |  
| \  / | ___   ___| | ___| | ___ _ __   ___  _ __| |_ 
| |\/| |/ _ \ / _ \ |/ _ \ |/ _ \ '_ \ / _ \| '__| __|
| |  | | (_) |  __/ |  __/ |  __/ |_) | (_) | |  | |_ 
|_|  |_|\___/ \___|_|\___|_|\___| .__/ \___/|_|   \__|
                                | |                   
                                |_|                   
```

# MoeTeleport 喵喵传送

简单的传送请求插件，支持设置家与返回上一个地点。

本插件由 [璎珞服务器](https://www.yingluo.world/) 请求本人开发，经过授权后开源。

## [依赖](https://github.com/CarmJos/MoeTeleport/network/dependencies)

- **[必须]** 插件本体基于 [Spigot-API](https://hub.spigotmc.org/stash/projects/SPIGOT) 、[BukkitAPI](http://bukkit.org/) 实现。
- **[推荐]** 变量部分基于 [PlaceholderAPI](https://www.spigotmc.org/resources/6245/) 实现。

详细依赖列表可见 [Dependencies](https://github.com/CarmJos/MoeTeleport/network/dependencies) 。

## [指令](src/main/resources/plugin.yml)

- 必须参数 `<参数>`
- 可选参数 `[参数]`

```text
# /back
- 返回上一个传送地点

# /tpa <玩家> 
- 请求传送到一个玩家身边。
# /tpaHere <玩家>
- 请求一个玩家传送到自己身边。
# /tpAccept [玩家]
- 同意一个请求，可以限定某个玩家。
# /tpDeny [玩家]
- 拒绝一个请求，可以限定某个玩家。

# /home [name]
- 返回设定的家
- 不填name会返回第一个设置的家
- 若存在名为“home”的家则优先返回“home”。
# /listHome
- 列出所有家的位置
# /setHome [name]
- 设置家 (不填name默认为home)
# /delHome <name>
- 删除家
```

## 配置

### 插件配置文件 ([config.yml](src/main/resources/config.yml))

详见源文件。

### 消息配置文件 ([messages.yml](src/main/resources/messages.yml))

详见源文件。

### 玩家数据配置文件 (data/\<UUID\>.yml)

本插件采用 `YAML格式` 存储玩家数据。

```yaml
# 玩家的家位置记录
# 格式为 world;x;y;z;yaw;pitch
homes:
  "name": "world;112.21;45;21.241245552;92.5512;-11"

```

## 支持与捐赠

若您觉得本插件做的不错，您可以捐赠支持我！

感谢您成为开源项目的支持者！

<img height=25% width=25% src="https://raw.githubusercontent.com/CarmJos/CarmJos/main/img/donate-code.jpg"  alt=""/>

## 开源协议

本项目源码采用 [GNU General Public License v3.0](https://opensource.org/licenses/GPL-3.0) 开源协议。
> ### 关于 GPL 协议
> GNU General Public Licence (GPL) 有可能是开源界最常用的许可模式。GPL 保证了所有开发者的权利，同时为使用者提供了足够的复制，分发，修改的权利：
>
> #### 可自由复制
> 你可以将软件复制到你的电脑，你客户的电脑，或者任何地方。复制份数没有任何限制。
> #### 可自由分发
> 在你的网站提供下载，拷贝到U盘送人，或者将源代码打印出来从窗户扔出去（环保起见，请别这样做）。
> #### 可以用来盈利
> 你可以在分发软件的时候收费，但你必须在收费前向你的客户提供该软件的 GNU GPL 许可协议，以便让他们知道，他们可以从别的渠道免费得到这份软件，以及你收费的理由。
> #### 可自由修改
> 如果你想添加或删除某个功能，没问题，如果你想在别的项目中使用部分代码，也没问题，唯一的要求是，使用了这段代码的项目也必须使用 GPL 协议。
>
> 需要注意的是，分发的时候，需要明确提供源代码和二进制文件，另外，用于某些程序的某些协议有一些问题和限制，你可以看一下 @PierreJoye 写的 Practical Guide to GPL Compliance 一文。使用 GPL 协议，你必须在源代码代码中包含相应信息，以及协议本身。
>
> *以上文字来自 [五种开源协议GPL,LGPL,BSD,MIT,Apache](https://www.oschina.net/question/54100_9455) 。*
