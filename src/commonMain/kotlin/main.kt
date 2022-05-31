import com.soywiz.klock.*
import com.soywiz.korau.sound.readMusic
import com.soywiz.korev.Key
import com.soywiz.korge.*
import com.soywiz.korge.animate.animate
import com.soywiz.korge.input.onClick
import com.soywiz.korge.scene.Module
import com.soywiz.korge.scene.Scene
import com.soywiz.korge.time.delay
import com.soywiz.korge.time.frameBlock
import com.soywiz.korge.tween.get
import com.soywiz.korge.tween.tween
import com.soywiz.korge.view.*
import com.soywiz.korge.view.tiles.BaseTileMap
import com.soywiz.korge.view.tiles.TileSet
import com.soywiz.korge.view.tiles.tileMap
import com.soywiz.korim.bitmap.Bitmap
import com.soywiz.korim.bitmap.Bitmap32
import com.soywiz.korim.bitmap.slice
import com.soywiz.korim.color.Colors
import com.soywiz.korim.format.*
import com.soywiz.korinject.AsyncInjector
import com.soywiz.korio.async.launch
import com.soywiz.korio.async.launchImmediately
import com.soywiz.korio.file.std.*
import com.soywiz.korma.geom.SizeInt
import com.soywiz.korma.geom.degrees
import com.soywiz.korma.interpolation.Easing
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.round
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
	}

}





suspend fun bitmap(path: String) = resourcesVfs[path].readBitmap()

