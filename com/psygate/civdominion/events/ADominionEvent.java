package com.psygate.civdominion.events;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ADominionEvent extends Event implements Cancellable {

	private static final HandlerList handlers = new HandlerList();

	private boolean iscancelled = false;

	@Override
	public boolean isCancelled() {

		return iscancelled;

	}

	@Override
	public void setCancelled(boolean cancel) {

		this.iscancelled = cancel;

	}

	@Override
	public HandlerList getHandlers() {

		return handlers;

	}

	public static HandlerList getHandlerList() {

		return handlers;

	}

}
