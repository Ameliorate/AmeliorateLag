package pw.amel.amelioratelag

import org.bukkit.plugin.java.JavaPlugin

class AmeliorateLag: JavaPlugin() {
	companion object {
		private var instanceStorage: AmeliorateLag? = null

		internal val instance: AmeliorateLag
			get() = instanceStorage!!
	}
	
	override fun onEnable() {
		instanceStorage = this
		initCollectionMap()
		server.pluginManager.registerEvents(MarkBredAnimalsAsDomesticated, this)

		GarbageCollector.runTaskTimer(this, GARBAGE_COLLECTION_INTERVAL, GARBAGE_COLLECTION_INTERVAL)
	}
}
