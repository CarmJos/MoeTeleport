package cc.carm.plugin.moeteleport.conf;


import cc.carm.lib.configuration.core.ConfigurationRoot;
import cc.carm.lib.configuration.core.annotation.HeaderComment;
import cc.carm.lib.configuration.core.util.MapFactory;
import cc.carm.lib.configuration.core.value.ConfigValue;
import cc.carm.lib.configuration.core.value.type.ConfiguredList;
import cc.carm.lib.configuration.core.value.type.ConfiguredMap;
import cc.carm.lib.configuration.core.value.type.ConfiguredValue;

public class PluginConfig extends ConfigurationRoot {

    public static final ConfigValue<Boolean> DEBUG = ConfiguredValue.of(Boolean.class, false);

    @HeaderComment({
            "统计数据设定",
            "该选项用于帮助开发者统计插件版本与使用情况，且绝不会影响性能与使用体验。",
            "当然，您也可以选择在这里关闭，或在plugins/bStats下的配置文件中关闭。"
    })
    public static final ConfigValue<Boolean> METRICS = ConfiguredValue.of(Boolean.class, true);

    @HeaderComment({
            "检查更新设定",
            "该选项用于插件判断是否要检查更新，若您不希望插件检查更新并提示您，可以选择关闭。",
            "检查更新为异步操作，绝不会影响性能与使用体验。"
    })
    public static final ConfigValue<Boolean> CHECK_UPDATE = ConfiguredValue.of(Boolean.class, true);

    @HeaderComment({"存储相关配置", "注意：存储配置不会通过重载指令生效，如有修改请重新启动服务器。"})
    public static final class STORAGE extends ConfigurationRoot {

        @HeaderComment("存储方式，可选 [ yaml | json | mysql | Essential(须安装ess插件) | CMI(须安装CMI插件) | custom(自定义，须重写功能) ]")
        public static final ConfigValue<String> METHOD = ConfiguredValue.of(String.class, "YAML");

    }

    @HeaderComment("传送的相关配置")
    public static final class TELEPORTATION extends ConfigurationRoot {

        @HeaderComment("危险的方块类型，将判断目的地脚下的方块的类型是否在这个列表中")
        public static final ConfiguredList<String> DANGEROUS_TYPES = ConfiguredList.builder(String.class)
                .fromString()
                .defaults("LAVA", "AIR")
                .build();

        @HeaderComment("传送的引导时间，单位为秒")
        public static final ConfigValue<Integer> WAIT_TIME = ConfiguredValue.of(Integer.class, 3);

        @HeaderComment("打断传送引导的方式")
        public static final class INTERRUPT {

            @HeaderComment("在传送引导时是否会因为移动打断传送")
            public static final ConfigValue<Boolean> MOVE = ConfiguredValue.of(Boolean.class, true);

            @HeaderComment("在传送引导时是否会因为攻击/被攻击打断传送")
            public static final ConfigValue<Boolean> ATTACK = ConfiguredValue.of(Boolean.class, true);

            @HeaderComment("在传送引导时是否会因为下蹲打断传送")
            public static final ConfigValue<Boolean> SNAKE = ConfiguredValue.of(Boolean.class, false);

        }

        @HeaderComment("传送引导特效与音效")
        public static final ConfigValue<Boolean> EFFECTS = ConfiguredValue.of(Boolean.class, true);

    }


    @HeaderComment("传送请求的相关配置")
    public static final class REQUEST extends ConfigurationRoot {

        @HeaderComment("请求的过期时间，单位为秒")
        public static final ConfigValue<Integer> EXPIRE_TIME = ConfiguredValue.of(Integer.class, 30);

        @HeaderComment("一个玩家同时能发出的最大请求数量")
        public static final ConfigValue<Integer> MAX = ConfiguredValue.of(Integer.class, 3);

    }

    @HeaderComment("返回上一传送点的相关配置")
    public static final class BACK extends ConfigurationRoot {

        @HeaderComment({"返回死亡点", "开启后将允许玩家输入 /back 返回死亡地点。"})
        public static final ConfigValue<Boolean> DEATH = ConfiguredValue.of(Boolean.class, true);

    }

    @HeaderComment("“家”功能相关配置")
    public static final class HOMES extends ConfigurationRoot {

        @HeaderComment("是否启用家功能")
        public static final ConfigValue<Boolean> ENABLE = ConfiguredValue.of(Boolean.class, false);

        @HeaderComment("普通玩家可设置多少家")
        public static final ConfigValue<Integer> DEFAULTS = ConfiguredValue.of(Integer.class, 2);

        @HeaderComment("设定权限对应的可设置家的数量。 (数量: 权限)")
        public static final ConfiguredMap<Integer, String> PERMISSIONS = ConfiguredMap
                .builder(Integer.class, String.class)
                .fromString()
                .parseKey(Integer::parseInt).serializeKey(Object::toString)
                .defaults(MapFactory.linkedMap(10, "MoeTeleport.vip").build())
                .build();

    }

    @HeaderComment("“地标”功能相关配置")
    public static final class WARPS extends ConfigurationRoot {

        @HeaderComment("是否启用地标功能")
        public static final ConfigValue<Boolean> ENABLE = ConfiguredValue.of(Boolean.class, false);

        @HeaderComment("普通玩家可设置多少地标")
        public static final ConfigValue<Integer> DEFAULTS = ConfiguredValue.of(Integer.class, 0);

        @HeaderComment("设定权限对应的可设置地标的数量。 (数量: 权限)")
        public static final ConfiguredMap<Integer, String> PERMISSIONS = ConfiguredMap
                .builder(Integer.class, String.class)
                .fromString()
                .parseKey(Integer::parseInt).serializeKey(Object::toString)
                .defaults(MapFactory.linkedMap(2, "MoeTeleport.vip").build())
                .build();
    }


}
