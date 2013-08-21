package com.psygate.civdominion.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.psygate.civdominion.events.SendChatEvent;

public class ChatListener implements Listener {
	@EventHandler
	public void sendChat(SendChatEvent ev) {
		ev.execute();
	}
}
