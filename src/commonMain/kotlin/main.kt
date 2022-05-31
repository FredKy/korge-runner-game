import com.soywiz.korge.*
import com.soywiz.korge.scene.Module
import com.soywiz.korge.scene.Scene
import com.soywiz.korim.color.Colors
import com.soywiz.korim.format.*
import com.soywiz.korinject.AsyncInjector
import com.soywiz.korio.file.std.*
import com.soywiz.korma.geom.SizeInt
import kotlin.reflect.KClass

suspend fun main() = Korge(Korge.Config(module = ConfigModule))




object ConfigModule : Module() {
	override val bgcolor = Colors["#2b2b2b"]
	override val size = SizeInt(480, 270)
	override val windowSize = SizeInt(1200, 675)
	//override val fullscreen = true
	//override val mainScene : KClass<out Scene> = City::class
	override val mainScene : KClass<out Scene> = Forest::class

	override suspend fun AsyncInjector.configure() {
		mapPrototype { TitleScreen() }
		mapPrototype { City() }
		mapPrototype { Forest() }
		mapPrototype { Industrial() }
		mapPrototype { GameOver() }
		mapPrototype { Winner() }
	}

}





suspend fun bitmap(path: String) = resourcesVfs[path].readBitmap()

