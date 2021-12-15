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

简单的传送请求插件，支持设置家。

## 依赖

- **[必须]** 插件本体基于 [Spigot-API](https://hub.spigotmc.org/stash/projects/SPIGOT) 、[BukkitAPI](http://bukkit.org/) 实现。
- **[推荐]** 变量部分基于 [PlaceholderAPI](https://www.spigotmc.org/resources/6245/) 实现。

详细依赖列表可见 [Dependencies](https://github.com/CarmJos/MoeTeleport/network/dependencies) 。

## 配置

### 插件配置文件 ([config.yml](src/main/resources/config.yml))

```yaml

defaultHome: 1 # 默认所有玩家可以设置一个家

permissions:
  # 以下命令全部为 MoeTeleport 的子节点
  # 如 "home.1" 的权限全拼就是 "MoeTeleport.home.1"
  "home.1": 1 # 有这个权限的玩家最多可以设置一个家
  "home.vip": 10 # 有这个权限的玩家最多可以设置10个家

```

### 玩家数据配置文件 (data/<UUID>.yml)

```yaml
# 玩家的家位置记录
# 格式为 world;x;y;z;yaw;pitch
homes:
  - "world;112.21;45;21.241245552;92.5512;-11"

```

## 支持与捐赠

若您觉得本插件做的不错，您可以捐赠支持我！

感谢您成为开源项目的支持者！

<img height=25% width=25% src="https://raw.githubusercontent.com/CarmJos/CarmJos/main/img/donate-code.jpg"  alt=""/>
