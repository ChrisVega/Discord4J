/*
 * Discord4J - Unofficial wrapper for Discord API
 * Copyright (c) 2016
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package sx.blah.discord;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.DiscordException;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.IListener;
import sx.blah.discord.handle.impl.events.ReadyEvent;
import sx.blah.discord.modules.Configuration;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Main class. :D
 */
public class Discord4J {
	
	/**
	 * The name of the project
	 */
	public static String NAME;
	/**
	 * The version of the api
	 */
	public static String VERSION;
	/**
	 * The api's description
	 */
	public static String DESCRIPTION;
	/**
	 * The github repo for the api
	 */
	public static String URL;
	
	/**
	 * SLF4J Instance
	 */
	public static final Logger LOGGER = LoggerFactory.getLogger(Discord4J.class);
	
	//Dynamically getting various information from maven
	static {
		InputStream stream = Discord4J.class.getClassLoader().getResourceAsStream("app.properties");
		Properties properties = new Properties();
		try {
			properties.load(stream);
			stream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		NAME = properties.getProperty("application.name");
		VERSION = properties.getProperty("application.version");
		DESCRIPTION = properties.getProperty("application.description");
		URL = properties.getProperty("application.url");
		
		LOGGER.info("{} v{}", NAME, VERSION);
		LOGGER.info("{}", DESCRIPTION);
	}
	
	/**
	 * This is used to run Discord4J independent of any bot, making it module dependent.
	 * 
	 * @param args The args should be email, password IN THAT ORDER
	 */
	public static void main(String[] args) {
		//This functionality is dependent on these options being true
		if (!Configuration.AUTOMATICALLY_ENABLE_MODULES || !Configuration.LOAD_EXTERNAL_MODULES)
			throw new RuntimeException("Invalid configuration!");
		
		//There needs to be at least 2 args
		if (args.length < 2)
			throw new IllegalArgumentException("At least 2 arguments are required!");
		
		try {
			IDiscordClient client = new ClientBuilder().withLogin(args[0], args[1]).login();
			client.getDispatcher().registerListener((IListener<ReadyEvent>) (ReadyEvent e) -> {
				LOGGER.info("Logged in as {}", e.getClient().getOurUser().getName());
			});
			//The modules should handle the rest
		} catch (DiscordException e) {
			LOGGER.error("There was an error initializing the client", e);
		}
	}
}