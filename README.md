# AmeliorateLag
a·me·lio·rate - verb (formal)  
make (something bad or unsatisfactory) better.  
"the reform did much to ameliorate living standards"  

KISS plugin to stop entity lag by culling old mobs. Features state that 
survives across server restarts without any database.

# How It Works

AmeliorateLag uses a very simple algorithm.
Every 5 (configurable) minutes, it scans over all mobs in the world.
If the mob is not in the list of excluded entity types, is not tamed,
did not spawn from breeding (or wasn't bread itself),
and is not within 10 (configurable) blocks of the player,
the mob's GC count will be incremented by 1.
If the mob's GC count reaches its configured maximum, it'll be despawned.

In essence, it causes certain mobs to despawn after a configured time.
For example, Cod will despawn after about 10 minutes of spawning.

In addition, if there are more than 16 (configurable) domesticated mobs
(either tamed or have been bred), in a 16 block (configurable) radius,
the domesticated mobs' GC count will start to be incremented and will despawn
after about 4 hours (yet again, configurable).

# Configuring

AmeliorateLag uses a new paradigm of configuring.
Instead of an error-prone config.yml file, you simply edit the `Config.kt` class
and recompile.
The compiler will check for syntax errors and give helpful error messages,
especially in the case of type errors and missing values.

# Recommendations

After installing AmeliorateLag, there are a few tuning parameters you should
adjust in various spigot, bukkit, and minecraft configs to further reduce lag.

## Entity Tracking

Entity tracking over long distances has been recently optimized by mojang and may
no longer need adjustment. However, it can be changed in `spigot.yml` under
`entity-activation-range` and `entity-tracking-range`.

# Commands

All commands require op or the permission `amelioratelag.datacommands`.

## /entityplayers

For each player, displays a count of the entities nearest to that player.

## /entityheatmap

Gets a list of the chunks sorted by the number of entities in that chunk.
Coordinates displayed are block coordinates.

## /chunkloadheatmap

Gets a list of the chunks sorted by the number of times they have been loaded in the last 5 (configurable) minutes.
Coordinates displayed are block coordinates.