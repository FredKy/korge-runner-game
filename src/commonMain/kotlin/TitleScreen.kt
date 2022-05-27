import com.soywiz.klock.seconds
import com.soywiz.korau.sound.readMusic
import com.soywiz.korge.input.onClick
import com.soywiz.korge.scene.Scene
import com.soywiz.korge.tween.get
import com.soywiz.korge.tween.tween
import com.soywiz.korge.view.*
import com.soywiz.korim.format.readBitmap
import com.soywiz.korio.async.launchImmediately
import com.soywiz.korio.file.std.resourcesVfs
import com.soywiz.korma.geom.degrees
import com.soywiz.korma.interpolation.Easing
import kotlin.math.PI
import kotlin.math.cos

class TitleScreen: Scene() {
	override suspend fun Container.sceneInit() {
		// load music

		resourcesVfs["holiznapatreon_dog_days.mp3"].readMusic().playForever()

		// background
		val background = resourcesVfs["title_screen_background.png"].readBitmap()
		image(background)

		// logo
		val logo = resourcesVfs["title_screen_logo.png"].readBitmap()
		val logoImage = image(logo)

		// amazing

		val minDegrees = (-6).degrees
		val maxDegrees = (+6).degrees

		var title_text = image(resourcesVfs["title_screen_amazing_crop.png"].readBitmap()) {
			rotation = maxDegrees
			anchor(.5, .5)
			scale(1.0)
			position(240, 76)
		}


		launchImmediately { while (true) {
			title_text.tween(title_text::rotation[minDegrees], time = 1.25.seconds, easing = Easing.EASE_IN_OUT)
			title_text.tween(title_text::rotation[maxDegrees], time = 1.25.seconds, easing = Easing.EASE_IN_OUT)

			//delay(8.milliseconds)
		}
		}

		// shiba left
		val shibaLeft = resourcesVfs["title_screen_shiba_left.png"].readBitmap()
		val shibaLeftImage = image(shibaLeft)
		shibaLeftImage.x+= -60
		var shift = 0.0
		shibaLeftImage.addUpdater {
			shift += 0.007
			x += cos(shift* PI) *1.25
		}

		// shiba that speaks
		val shibaSugoi = resourcesVfs["title_screen_shiba_sugoi.png"].readBitmap()
		val shibaSugoiImage = image(shibaSugoi)
		var s = 0.0
		shibaSugoiImage.addUpdater {
			s += 0.01
			x += cos(shift* PI + PI) *0.6
		}

		// start game
		logoImage.onClick {
//			sceneContainer.changeTo<Industrial>()
			sceneContainer.changeTo<City>()
		}
	}
}