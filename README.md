[Dominion 1.0a](https://dl.dropboxusercontent.com/u/22037628/CivDominion.jar)

# Quick Description:

Dominions are circular. Growing with every user, who has his bed inside the radius, and was atleast 30 minutes online that day. Users with beds inside the dominion, who haven't been online for 31 days make your dominion shrink again. (1:1 ratio).
Dominions now have to chose a path (war or economy) to reach certain upgrades. This can be, like ordering upgrades, only be done by a dominion admin or higher (User -> Moderator -> Admin -> Owner). A moderator can only add or remove users. An owner can do anything he pleases.

Dominions below a size of 15 do not have an inner exclusion zone at all.

Dominion stores its data upon reload / shutdown of the server. The data is written newly from memory everytime you reload. Watch the xmlpersistence folder for size, and delete some of the inner folders, if you have to. Be careful not to delete the newest one.

---
# Commands

## Operator commands: (ops are set in the config of dominion)

 * /forcecompletedominion:

  Forces instant completion of the predominion you are pointing at.

 alias: fcd

 * /dominionforceaddallupgrades:

 Forces a dominion to add all upgrades, regardless of existing ones, or the path of a certain upgrade.

 * /forcedominiongrowth

 Forces a dominion to add 5 to its boundaries.

 alias: dfg

 * /dominionclear:

 Forces the CivDominion Plugin to drop all data. WARNING THIS WILL DELETE EVERY DATA DOMINION HAS. DON'T DO THIS ON THE PRODUCTIVE SERVER.

 alias: dc

 * /dominionsetgrowthinterval <time>

 Sets the interval in which dominion will undergo the growth. (time is in seconds)

 alias:dsgi

 * /dominionforceglobalgrowth

 This will force the growthcycle to take place now, the moment you hit enter.

 alias:dfgg

 * /dominionstripupgrades

 This will remove all upgrades from a dominion.

 alias: dsu

 * /dominionsetradius <radius>

 Sets the radius of a dominion. This will override the old radius, regardless of what it was. It is not braced against negative values, or zero for that matter. so be careful.

## User Commands:

 * /dominiondrawboundaries

 Forces the boundaries of a dominion to be drawn. (Inner exclusion zone marked by emerald, radius marked by glowstone, outer exclusion zone marked by redstone)

 alias: ddb

 * /dominionadduser <name> <rank>

 Adds a user to your dominion with the specified rank. The rank can NOT be higher as your own, and you have to be atleast moderator in this dominion.

 alias: dau

 * /dominionremoveuser <name>:

 Removes a user from your dominion.

 alias: dru

 * /dominionmodifyuser <name> <rank>

 Assigns a new rank to the specified user. Same rules as with dominionadduser apply.

 alias: dmu

 * /dominionlistusers

 Lists all users with rank in the dominion.

 alias: dlm

 * /dominionaddcitadelgroup <name>

 Adds a complete citadelgroup to your dominion. Citadel moderators are translated to DominionModerators. Everyone else becomes a user.

 * /dominionaddupgrade <upgradename | upgradeid>

 Queues an upgrade for completion on your dominion. This upgrade has to be on the path you set on your dominion.

 alias: dua

 * /dominionremoveupgrade <upgradename | upgradeid>

 Removes an upgrade from your dominion. The price you paid to have it developed is not given back to you.

 alias: drup

 * /dominionlistupgrades

 Lists all upgrades a dominion has.

 alias: dlu

 * /dominionsetpath <name>

 Sets the development path of the dominion. Warning, setting a path may drop upgrades already developed.

 alias: dsp

 * /dominionsetname <name>

 Sets the name for your dominion. This name must be unique.

 alias: dsn

 * /dominionshowname

 Shows the name of your dominion.

 alias: dns

**Upgrade specific commands**:

 * /dominionxpinfo

 Shows the stored xp gathered through the "WheatLeech" Upgrade.
 
 alias: dxi

 * /displace <name>
 
 Teleports from one dominion to a random position in another dominion. These two dominions must be linked together by /link and you must be member of both of them.
 alias: dis

 * /link <name>:

 Links one dominion to another.

 alias: li
 * /unlink <name>:

 Unlinks one dominion from another.

 alias: uli

---

## Upgrades:
Upgrades have currently 3 categories. Basic (can be developed anyways), Economy (your dominion has to be in the economy path) and War (your dominion has to be set to the war path).

### BASIC

* NoInvisibility:
 
 Drinking an invisibility potion in this dominion will cancel the potion (also throwing an invis pot) and hurt the one consuming it (1heart dmg.)

* Fortification:
 
 One can no longer build within the exclusion zones of your dominion. (Block placement is forbidden there for any non member.)

* MobCull:

 Mobs targetting you from outside your dominion, while you are inside your dominion, are killed instantly.

* NoHostileMobs:
 
 Hostile mobs cannot spawn within your dominion.

* NoLava:
 
 Players cannot empty lava buckets within your dominion.

* WheatLeech:

 If wheat grows within your dominion, there is a 1% chance the growth is cancelled, and your dominion gains 1 XP. You can retrieve the xp by hitting the dominion block with a glas bottle in hand.

### WAR

* FootInTheGrave:

 Upon falling below 30% health, you gain RegenII for 10 seconds, Damage mitigation II for 5 seconds and Invisibility for 2 seconds. Has a 30 minute cooldown.

* ParasiticBond:
 
 Upon killing something inside your dominion, you regain 1 heart.

* DeathIntoLife:

 Even without a bed, you will respawn within 1k of a dominion you are member of, and which does have this upgrade.

* PotentCorruption:

 Upon hitting someone inside your dominion, you have a 1% chance to steal one of his potion effects. If you already have the effect, your effect is override by the enemy ones. (Steal RegenII for 5 secs from enemy, while having regenII for 8 minutes will leave you with only 5 secs of regenII.)

* FullOfLife:

 Upon taking damage inside your dominion, you gain regenI for 5 seconds. ICD: 10 seconds.

* DeathlyPerception:

 Upon falling below 50% health inside your dominion, you gain 1.2x the damage you would do without this upgrade, for 2 seconds. ICD: 30 seconds.

* PathOfMidnight:
 
 200% damage vs mobs at night, inside your dominion.

* WitheringStrikes:

 1% chance on hit, that your enemy will only do 0.8 damage for 3 seconds. ICD: 10 minutes.

### ECONOMY

* PrayerOfIdols:
 
 Mining ores in your dominion will always drop the normal drop + 1. So if a block dropped 2 diamonds, you would get 3. Mining Stone, cobble and dirt will drop +2.

* PrayerOfFertility:

 Reproducing animals within your dominion have a 20% chance to create an additional offspring.

* PrayerOfAsh:

 A growing wheat block has a 1% chance to make an adjecent block grow too. (Horizontally.)

* UnearthlyShackles:
 
 Someone new joining the server has a 1/numberofdominionswiththisupgrade chance to spawn within 1000 blocks of your.

* TesseractDisplacement:

 You can teleport with /displace between linked dominions with this upgrade.
