package ru.xpixel.xchat;

import dev.toma.configuration.Configuration;
import dev.toma.configuration.config.format.ConfigFormats;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.message.v1.ServerMessageEvents;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.xpixel.xchat.command.AnswerCommand;
import ru.xpixel.xchat.command.ExtraChatCommand;
import ru.xpixel.xchat.command.AskCommand;
import ru.xpixel.xchat.config.ExtraChatConfig;
import ru.xpixel.xchat.integration.FTBTeamsIntegration;

public class ExtraChatMod implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final String MOD_ID = "xchat";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static ExtraChatConfig config;

	@Override
	public void onInitialize() {
		Integrations.FTBTeams = FabricLoader.getInstance().isModLoaded("ftbteams");
		Integrations.LuckPerms = FabricLoader.getInstance().isModLoaded("luckperms");

		if (Integrations.FTBTeams) FTBTeamsIntegration.register();

		config = Configuration.registerConfig(ExtraChatConfig.class, ConfigFormats.json()).getConfigInstance();

		ServerMessageEvents.ALLOW_CHAT_MESSAGE.register(((Message::onChatMessage)));

		CommandRegistrationCallback.EVENT.register(((dispatcher, registryAccess, environment) -> {
			AskCommand.register(dispatcher);
			ExtraChatCommand.register(dispatcher);
			AnswerCommand.register(dispatcher);
		}));

		LOGGER.info("Have fun!");
	}

	public static class Integrations {

		public static boolean FTBTeams = false;
		public static boolean LuckPerms = false;
	}
}
