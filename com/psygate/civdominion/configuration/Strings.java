package com.psygate.civdominion.configuration;

import static org.bukkit.ChatColor.*;

import org.bukkit.ChatColor;

import com.psygate.civdominion.types.uac.Rank;

public class Strings {
	public static final String closeby = encode("Another dominion is already nearby.", RED);
	public static final String predomcreated = encode("Your dominion is now pending. Votes needed: ", GREEN);
	public static final String doubleVote = encode("You already voted on this dominion.", RED);
	public static final String domcreate = encode("Your dominion has been approved. ", GREEN);
	public static final String onlyPlayerCommand = "Only players may use this command. ";
	public static final String generalCommandException = "An unknown exception occured. Check the server log.";
	public static final String opOnlyCommand = encode("This command is operators only.", RED);
	public static final String notPointingAtPreDominion = encode("You are not pointing at a pending dominion.", RED);
	public static final String forcedCompletion = encode("Completion forced.", GREEN);
	public static final String notPointingAtDominion = encode("You are not pointing at a dominion.", RED);
	public static final String drawbounds = encode("Drawing boundaries.", GREEN);
	public static final String notmember = encode("You are not part of that dominion.", RED);
	public static final String insufficientPermissions = encode("You do not have permission to do that.", RED);
	public static final String invalidArguments = encode("You did not provide enough arguments. ", RED);
	public static final String unknownrank = encode(getRanks(), RED);
	public static final String invalidrank = encode("The rank you provided is not valid.", RED);
	public static final String addnewuser = encode("New user added.", GREEN);
	public static final String alreadyMember = encode("User is already a member of this dominion.", GOLD);
	public static final String notamember = encode("Provided user is not a member of this dominion.", RED);
	public static final String removeselffail = encode("You cannot remove yourself from a dominion.", RED);
	public static final String removeduser = encode("Removed user.", GREEN);
	public static final String demotionfailed = encode("You cannot demote people above your rank.", RED);
	public static final String illegalUpgradeName = encode("The upgrade number / name you provided is not valid. ", RED);
	public static final String domdoesnthaveupgrade = encode("The dominion you selected doesn't have that upgrade you provided.", RED);
	public static final String upgradeaddforced = encode("Forcibly added all upgrades to dominion.", GREEN);
	public static final String wrongvotingmaterial = encode("The material in your hand is not a valid voting material.", RED);
	public static final String voteRegistered = encode("Your vote on this pending dominion was registered.", GREEN);
	public static final String upgradefinish = encode("Upgrade $d$ @$c$ finished.", GOLD);
	public static final String upgradealreadythere = encode("This dominion already has that upgrade.", GOLD);
	public static final String upgradeinthemaking = encode("There is already an upgrade queued on this dominion.", RED);
	public static final String upgradetooexpensive = encode("You do not have the required $d$ materials to order this upgrade.", RED);
	public static final String inexclnobuild = encode("You cannot build here. This dominion is fortified.", RED);
	public static final String nopotions = encode("You cannot drink this potion here.", RED);
	public static final String nopearlport = encode("You cannot pearl teleport here.", RED);
	public static final String notallowedhere = encode("You cannot place this here.", RED);
	public static final String notanumber = encode("The argument to this command has to be a number.", RED);
	public static final String generatedDominions = encode("Generated requested dominions.",GREEN);
	public static final String generatedxofy = encode("Generated ",GOLD);
	public static final String generatingDominions = encode("Generating $n$ dominions.",GREEN);
	public static final String generatingPreDominions = encode("Generating $n$ predominions.", GREEN);
	public static final String brokendominion = encode("Your dominion @$c$ was broken.", RED);
	public static final String brokenpredominion = encode("Your predominion @$c$ was broken.",RED);
	public static final String dominionscleared = encode("All dominions cleared.", GREEN);
	public static final String intervalset = encode("New dominion growth interval: $i$ms.", GREEN);
	public static final String forcedgrowth = encode("Forced dominion growth.", GREEN);
	public static final String upgradedisabled = encode("This upgrade is disabled.", RED);
	public static final String upgradesstripped = encode("Upgrades stripped.", GREEN);
	public static final String upgradequeued = encode("Upgrade $d$ queued. Finishing @$e$", GREEN);
	public static final String debugOn = encode("Debug is now on.", GREEN);
	public static final String debugOff = encode("Debug is now off.", RED);
	public static final String nocitadel = encode("Citadel is not enabled here.", RED);
	public static final String citadelgroupmissing = encode("Citadel group $g$ not found.", RED);
	public static final String mustbenumber = encode("Argument must be a number.", RED);
	public static final String xpinfo = encode("XP now: $x$", GOLD);
	public static final String newradius = encode("New dominion radius: $r$", GREEN);
	public static final String deathsbreath = encode("You feel Deaths Breath running across your neck.", BLUE);
	public static final String parasiticbond = encode("You draw strength from peril.", BLUE);
	public static final String deathintolife = encode("Your spirit is drawn by ethereal bonds to this location.", BLUE);
	public static final String potentcorrupted = encode("The ether seems to favor you. You have been granted $e$.", BLUE);
	public static final String potentcurroptionloss = encode("The ether is not favoring you. $e$ has been taken from you.", BLUE);
	public static final String fulloflife = encode("You feel so full of life.", BLUE);
	public static final String deathlyperception = encode("Your senses sharpen as death draws closer.", BLUE);
	public static final String pathofmidnight = encode("Your strength grows as midnight draws nigh.", BLUE);
	public static final String witheringprecision = encode("You feel your strength fade.", BLUE);
	public static final String withered = encode("You only land a glancing blow.", BLUE);
	public static final String wrongpath = encode("You cannot develop this plugin with the currently set path on your dominion.", RED);
	public static final String notavalidpath = encode("\"$p$\" is not a valid development path for your dominion.", RED);
	public static final String upgradedroppedpathinc = encode("Upgrade $u$ dropped. Incompatible with current path.", GOLD);
	public static final String dominionpathset = encode("Dominion path is now $p$", GREEN);
	public static final String shackles = encode("Unearthly shackles moved you here.", BLUE);
	public static final String dominionnameset = encode("New Dominion name: $n$ set.", GREEN);
	public static final String anothername = encode("Another dominion already has this name.",RED);
	public static final String invalidname = encode("The provided name is not valid.", RED);
	public static final String impossibledisplace = encode("Space time geometry is hard. And you don't seem to understand it.", RED);
	public static final String spacewarp = encode("A gravity well is blocking space from bending that way.", RED);
	public static final String tesseracted = encode("A bright light, and for a short moment earth shrunk in a weird way.", GREEN);
	public static final String mercyfail = encode("This place seems to not know mercy.", RED);
	public static final String linked = encode("Space folded in the right way. Four dimensional path created.", GREEN);
	public static final String beacon = encode("A bright light shows the path. The universe seems mercyful to you.", GREEN);
	public static final String nomoremercy = encode("There is no mercy left in this harsh universe.", RED);
	public static final String influence = encode("Your dominions current influence is $i$.", GREEN);
	
	private static String encode(String s, ChatColor col) {
		StringBuilder builder = new StringBuilder();
		builder.append(col);
		builder.append(s);
		return builder.toString();
	}
	
	private static String getRanks() {
		String out = "The provided rank is not a valid rank. (";
		Rank[] ra = Rank.values();
		
		for(int i = 0; i < ra.length; i++) {
			out += ra[i];
			if(i < ra.length - 1) {
				out+=", ";
			}
		}
		
		out+= ")";
		
		return out;
	}
}
