package pw.amel.amelioratelag

class AmeliorateLag: JavaPlugin() {
	companion object {
		private var instanceStorage: AmeliorateLag? = null

		internal val instance: AmeliorateLag
			get() = instanceStorage!!
	}
	
	override fun onEnable() {
		instanceStorage = this
	}
}
