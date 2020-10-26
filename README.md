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

## Adjust Mob Packing

Mob packing can help with high numbers of collision events from tightly
packed animal farms and players AFK at grinders.

The vanilla default is 24 mobs in one block. I personally would recommend about 3
to combat packed mob farms, or 5 to combat grinders. Packed mob farms tend to be
much more of a lag issue than grinders.

In the future, AmeliorateLag might cull animals in tightly-packed farms over
long-time spans, avoiding the need for restrictively low mob packing.

Mob packing can be adjusted using `/gamerule maxEntityCramming [NUMBER]`