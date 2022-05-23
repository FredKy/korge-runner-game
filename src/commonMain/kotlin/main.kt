import com.soywiz.klock.DateTime
import com.soywiz.klock.TimeSpan
import com.soywiz.klock.milliseconds
import com.soywiz.klock.timesPerSecond
import com.soywiz.korev.Key
import com.soywiz.korge.*
import com.soywiz.korge.animate.animate
import com.soywiz.korge.input.onClick
import com.soywiz.korge.scene.Module
import com.soywiz.korge.scene.Scene
import com.soywiz.korge.time.delay
import com.soywiz.korge.time.frameBlock
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
	override val mainScene : KClass<out Scene> = Scene1::class

	override suspend fun AsyncInjector.configure() {
		mapPrototype { Scene1() }
		mapPrototype { Scene2() }
		mapPrototype { Scene3() }
		mapPrototype { Industrial() }
	}

}

class Scene1: Scene() {
	override suspend fun Container.sceneInit() {
		val bitmap = resourcesVfs["start.png"].readBitmap()
		val image = image(bitmap).scale(0.3).centerOnStage()

		image.onClick {
//			sceneContainer.changeTo<Industrial>()
			sceneContainer.changeTo<Scene2>()
		}
	}
}

class Scene2 : Scene() {

	override suspend fun Container.sceneInit() {
		// Main hero dogo alive or dead.
		var dogAlive: Boolean = true
		// List of obstacles.
		var obstacles: MutableList<View> = mutableListOf<View>()
		// Counts how many in game frame ticks have occured.
		var gameTick: Int = 0
		var timePassed = 0.milliseconds
		// Attach updater to this container.
		val start = DateTime.now()
		addUpdater() { time ->
			var now = DateTime.now()
			gameTick += 1
			//timePassed += 1000.milliseconds
			println(gameTick)
			println(now-start)
		}
		// tileable background
		val tileset = TileSet(bitmap("assembly.png")
			.toBMP32()
			.scaleLinear(1.0, 1.0).slice(), 480, 270)
		val tilemap = tileMap(
			Bitmap32(1,1),
			repeatX = BaseTileMap.Repeat.REPEAT,
			tileset = tileset)

		launchImmediately {
			frameBlock(144.timesPerSecond) {
				while (dogAlive) {
					tilemap.x -= 0.5
					frame()
				}
			}
		}

		val cube = solidRect(10.0, 10.0, Colors.GOLD).xy(300, 200)
		obstacles.add(cube)
		//val cube2 = solidRect(48.0, 48.0, Colors.RED).xy(20, 200)
		//addChild(cube)


		val smDroneWalk: Bitmap = resourcesVfs["drone_forward.png"].readBitmap()

		val spriteMapRun: Bitmap = resourcesVfs["dog_run.png"].readBitmap()
		val spriteMapDeath: Bitmap = resourcesVfs["dog_death.png"].readBitmap()

		val spriteMapRunExp: Bitmap = spriteMapRun.clone()

		val runAnimation = SpriteAnimation(
			spriteMap = spriteMapRun,
			spriteWidth = 48,
			spriteHeight = 48,
			marginLeft = 0,
			marginTop = 0,
			columns = 6,
			rows = 1,
			offsetBetweenColumns = 0,
			offsetBetweenRows = 0
		)

		val runAnimation2 = SpriteAnimation(
			spriteMap = spriteMapRunExp,
			spriteWidth = 48,
			spriteHeight = 25,
			marginLeft = 0,
			marginTop = 23,
			columns = 6,
			rows = 1,
			offsetBetweenColumns = 0,
			offsetBetweenRows = 0
		)
		val deathAnimation = SpriteAnimation(
			spriteMap = spriteMapDeath,
			spriteWidth = 48,
			spriteHeight = 25,
			marginLeft = 0,
			marginTop = 23,
			columns = 4,
			rows = 1,
			offsetBetweenColumns = 0,
			offsetBetweenRows = 0
		)


		val droneAnimation = SpriteAnimation(
			spriteMap = smDroneWalk,
			spriteWidth = 48,
			spriteHeight = 32,
			marginLeft = 0,
			marginTop = 16,
			columns = 4,
			rows = 1,
			offsetBetweenColumns = 0,
			offsetBetweenRows = 0
		)

		val drone: Sprite = sprite(droneAnimation).xy(600,200).scale(-1)
		obstacles.add(drone)
		drone.playAnimationLooped(spriteDisplayTime = 100.milliseconds)
		var shift = 0.0
		drone.addUpdater {
			x -= 2
			shift += 0.01
			y += cos(shift*PI)
		}

		cube.addUpdater {
			x -= 2
		}

		val dog: Sprite = sprite(runAnimation2).xy(60, 206)
		//dog.bounds(20.0,40.0,60.0,80.0)
		dog.playAnimationLooped(spriteDisplayTime = 80.milliseconds)
		dog.addUpdater {
			var coll = 0
			if(collidesWith(obstacles)) {
				coll += 1
				println("Collision: " +coll)
				if (dogAlive) {
					dogAlive = false
					playAnimation(times = 0, deathAnimation, spriteDisplayTime = 200.milliseconds, startFrame = 0, endFrame = 3)
					launchImmediately {
						animate(completeOnCancel = false) { dog.moveTo(40.0, round(dog.y) + (-10..10).random(), time = 750.milliseconds) }
					}

					launch {
						delay(TimeSpan(3000.0))
						sceneContainer.changeTo<Scene3>()
					}
				}
			}

			if (dogAlive) {
				if(views.input.keys[Key.UP] && dog.y > 207) {
					dog.y -= 3
				}
				if(views.input.keys[Key.DOWN] && dog.y < 238 ) {
					dog.y += 3
				}
			}
		}

		val xDogCoords = text("0,0").xy(100,50).scale(1)
		val yDogCoords = text("0,0").xy(100,80).scale(1)
		xDogCoords.addUpdater {
			text = "X: ${round(dog.x)}"
		}
		yDogCoords.addUpdater {
			text = "Y: ${round(dog.y)}"
		}

		onClick {
			if (dogAlive) {
				if (round(dog.y) == 206.0) {
						animate(completeOnCancel = false) { dog.moveTo(60, 239, time = 500.milliseconds) }
				}
			}
		}

		onClick {
			if (dogAlive) {
				if (round(dog.y) == 239.0) {
						animate(completeOnCancel = false) { dog.moveTo(60, 206, time = 500.milliseconds) }
				}
			}
		}

	}

}

suspend fun bitmap(path: String) = resourcesVfs[path].readBitmap()

class Scene3 : Scene() {
	override suspend fun Container.sceneInit() {
		val bitmap = resourcesVfs["game_over.png"].readBitmap()
		val image = image(bitmap).scale(0.3).centerOnStage()

		image.onClick {
			sceneContainer.changeTo<Scene2>()
		}
	}
}