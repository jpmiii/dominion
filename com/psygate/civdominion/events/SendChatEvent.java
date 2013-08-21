package com.psygate.civdominion.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class SendChatEvent extends ADominionEvent {
	private String message;
	private Player player;
	
	public SendChatEvent(String message, Player to) {
		this.message = message;
		this.player = to;
	}
	
	public void execute() {
		player.sendMessage(message);
	}
}
