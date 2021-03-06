package com.github.mmonkey.Destinations.Commands;

import java.util.ArrayList;

import org.spongepowered.api.entity.player.Player;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.util.command.CommandException;
import org.spongepowered.api.util.command.CommandResult;
import org.spongepowered.api.util.command.CommandSource;
import org.spongepowered.api.util.command.args.CommandContext;
import org.spongepowered.api.util.command.spec.CommandExecutor;
import org.spongepowered.api.world.Location;

import com.github.mmonkey.Destinations.Home;
import com.github.mmonkey.Destinations.Destinations;
import com.github.mmonkey.Destinations.Utilities.FormatUtil;

public class HomeCommand implements CommandExecutor {
	
	private Destinations plugin;

	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		
		if (!(src instanceof Player)) {
			return CommandResult.success();
		}
		
		String name = (args.hasAny("name")) ? ((String) args.getOne("name").get()) : "";
		Player player = (Player) src;
		ArrayList<Home> homes = plugin.getHomeStorageService().getHomes(player);
		
		if (homes.isEmpty()) {	
			player.sendMessage(Texts.of(FormatUtil.ERROR, "No home has been set!").builder().build());
			return CommandResult.success();
		}
			
		Home home = (name.equals("")) ? getClosestHome(player, homes) : getNamedHome(player, homes, name);
		Location location = (home != null) ? home.getDestination().getLocation(plugin.getGame()) : null;
			
		if (location != null) {
			player.setRotation(home.getDestination().getRotation());
			player.setLocation(location);
		}
		
		// TODO add no home found if location == null
			
		return CommandResult.success();
	}
	
	/**
	 * Calculate the closest home to the player's current location
	 * 
	 * @param player Player
	 * @param homes ArrayList<Home>
	 * @return Home|null
	 */
	private Home getClosestHome(Player player, ArrayList<Home> homes) {
		
		Location playerLocation = player.getLocation();
		
		double min = -1;
		double tmp = 0;
		Home result = null;
		
		for (Home home: homes) {
			
			Location location = home.getDestination().getLocation(plugin.getGame());
			double x = Math.pow((playerLocation.getX() - location.getX()), 2);
			double y = Math.pow((playerLocation.getY() - location.getY()), 2);
			double z = Math.pow((playerLocation.getZ() - location.getZ()), 2);
			tmp = Math.sqrt(x + y + z);
			
			if (min == -1 || tmp < min) {
				min = tmp;
				result = home;
			}
		}
		
		return result;
		
	}
	
	/**
	 * Get the Home of the given name.
	 * 
	 * @param player Player
	 * @param homes ArrayList<Home>
	 * @param name String
	 * @return Home|null
	 */
	public Home getNamedHome(Player player, ArrayList<Home> homes, String name) {
		
		for(int i = 0; i < homes.size(); i++) {
			
			if (homes.get(i).getName().equals(name)) {
				return homes.get(i);
			}

		}
		
		return null;
		
	}
	
	public HomeCommand(Destinations plugin) {
		this.plugin = plugin;
	}

}
