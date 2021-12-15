package cc.carm.plugin.moeteleport.configuration;

import cc.carm.plugin.moeteleport.configuration.message.ConfigMessage;
import cc.carm.plugin.moeteleport.configuration.message.ConfigMessageList;

public class PluginMessages {

	public static final ConfigMessageList NO_LAST_LOCATION = new ConfigMessageList("no-last-location");

	public static final ConfigMessageList TPA = new ConfigMessageList("tpa");
	public static final ConfigMessageList TPA_HERE = new ConfigMessageList("tpahere");
	public static final ConfigMessageList TPA_ACCEPT = new ConfigMessageList("tpaccept");
	public static final ConfigMessageList TPA_DENY = new ConfigMessageList("tpadeny");

	public static final ConfigMessageList ACCEPTED = new ConfigMessageList("accepted");
	public static final ConfigMessageList DENIED = new ConfigMessageList("denied");

	public static final ConfigMessageList TELEPORTING = new ConfigMessageList("teleporting");
	public static final ConfigMessageList DANGEROUS = new ConfigMessageList("dangerous");

	public static class Request {
		public static final ConfigMessageList MULTI = new ConfigMessageList("multi-requests");
		public static final ConfigMessageList TIMEOUT = new ConfigMessageList("request-timeout");
		public static final ConfigMessageList NOT_FOUND = new ConfigMessageList("no-request");
		public static final ConfigMessageList NOT_FOUND_PLAYER = new ConfigMessageList("no-request-player");
	}

	public static class Home {
		public static final ConfigMessageList HEADER = new ConfigMessageList("home-list-header");
		public static final ConfigMessage LIST_OBJECT = new ConfigMessage("home-list-object");
	}


}
