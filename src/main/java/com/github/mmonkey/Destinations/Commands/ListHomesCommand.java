package com.github.mmonkey.Destinations.Commands;

import java.util.List;

import org.spongepowered.api.entity.player.Player;
import org.spongepowered.api.text.TextBuilder;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.util.command.CommandException;
import org.spongepowered.api.util.command.CommandResult;
import org.spongepowered.api.util.command.CommandSource;
import org.spongepowered.api.util.command.args.CommandContext;
import org.spongepowered.api.util.command.spec.CommandExecutor;

import com.github.mmonkey.Destinations.Destinations;
import com.github.mmonkey.Destinations.Utilities.FormatUtil;
import com.github.mmonkey.Destinations.Utilities.HomeUtil;
import com.github.mmonkey.Destinations.Utilities.PaginatedList;

public class ListHomesCommand implements CommandExecutor {
	
	private Destinations plugin;

	public ListHomesCommand(Destinations plugin) {
		this.plugin = plugin;
	}
	
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {

		// Only allow players to use this command
		if (!(src instanceof Player)) {
			return CommandResult.empty();
		}
		Player player = (Player) src;
		
		// Get the page number from arguments, defaults to 1
		int currentPage = (args.hasAny("page")) ? (Integer) args.getOne("page").get() : 1;
		
		// Get this players list of homes
		List<String> list = plugin.getHomeStorageService().getHomeList(player);
		
		// If this player doesn't have any homes, return with message
		if (list.isEmpty()) {
			player.sendMessage(Texts.of(FormatUtil.ERROR, "No home has been set!"));
			return CommandResult.success();
		}
		
		// Get utility classes and new PaginatedList
		FormatUtil format = new FormatUtil();
		HomeUtil homeUtil = new HomeUtil();
		TextBuilder message = Texts.builder();
		TextBuilder header = Texts.builder();
		PaginatedList paginatedList = new PaginatedList("/listhomes");
		
		// Fill paginatedList with items
		for (String name: list) {
			TextBuilder item = Texts.builder();
			item.append(homeUtil.getHomeLink(name), Texts.of(" - "));
			item.append(homeUtil.getDeleteHomeLink(name, "delete"));
			
			paginatedList.add(item.build());
		}
		
		// Created header for paginatedList
		header.append(Texts.of(FormatUtil.HEADLINE, format.getFill(12, '-')));
		header.append(Texts.of(FormatUtil.HEADLINE, " Showing homes page " + currentPage + " of " + paginatedList.getTotalPages() + " "));
		header.append(Texts.of(FormatUtil.HEADLINE, format.getFill(12, '-')));
		
		// Add header to paginatedList
		paginatedList.setHeader(header.build());
		
		// Clear the chat window
		message.append(format.empty());
		
		// Add the paginated list to the message
		message.append(paginatedList.getPage(currentPage));
		
		// Send message to this player
		player.sendMessage(message.build());
		
		return CommandResult.success();

	}

}
