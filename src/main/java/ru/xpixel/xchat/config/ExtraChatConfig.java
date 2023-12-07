package ru.xpixel.xchat.config;

import dev.toma.configuration.config.Config;
import dev.toma.configuration.config.Configurable;
import ru.xpixel.xchat.ExtraChatMod;

@Config(id = ExtraChatMod.MOD_ID)
public class ExtraChatConfig {
    @Configurable
    @Configurable.Comment({"Enable or disable mod"})
    public boolean enable = true;

    @Configurable
    public int local_chat_range = 100;

    @Configurable
    public boolean aloneMessage = true;
//
//    @Configurable
//    public MyEnum myConfigurableEnum = MyEnum.SOME_VALUE;

    @Configurable
    public Formats formats = new Formats();

    public void reload() {

    }

    public static class Formats {

        @Configurable
        public String local = "<yellow>[LOCAL]</yellow> <gray>%player:displayname%</gray>: %message%";

        @Configurable
        public String global = "<green>[GLOBAL]</green> <gray>%player:displayname%</gray>: %message%";

        @Configurable
        public String world = "<blue>[WORLD]</blue> <gray>%player:displayname%</gray>: %message%";

        @Configurable
        public String ooc = "<gold>[OOC]</gold> <gray>%player:displayname%</gray>: %message%";

        @Configurable
        public String admin = "<aqua>[ADMIN]</aqua> <gray>%player:displayname%</gray>: %message%";

        @Configurable
        public String ask = "<dark_green>[HELP]</dark_green> <gray><click:suggest_command:'/answer %answer_player_name% '>%player:displayname_visual%</click></gray>: %message%";

        @Configurable
        public String announce = "<red>[ANNOUNCE]</red> %message%";

        @Configurable
        public String answer = "<red>[ANSWER]</red> %message%";

        @Configurable
        public String spy = "<light_purple>[SPY]</light_purple> <gray>%player:displayname%</gray>: %message%";

        @Configurable
        public String ftb_team = "[TEAM] (%player:ftbteam%) <gray>%player:displayname%</gray>: %message%";
    }

    @Configurable
    public Messages messages = new Messages();

    public static class Messages {

        @Configurable
        public String alone = "No players nearby";

        @Configurable
        public String ask = "Your message has been sent to the server staff";

        @Configurable
        public String help = "xChat by Stark";

        @Configurable
        public String spy = "Spy mode toggled";

        @Configurable
        public String no_team = "You are not on the team";
    }
}
