import com.soywiz.klock.seconds
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

class GameOver : Scene() {
    override suspend fun Container.sceneInit() {
        // background
        val background = resourcesVfs["game_over_screen_background.png"].readBitmap()
        image(background)

        // shiba
        val shiba = resourcesVfs["game_over_screen_shiba.png"].readBitmap()
        val shibaImage = image(shiba)
        var shift = 0.0
        var s = 0.0
        shibaImage.addUpdater {
            shift += 0.05
            s += 0.01
            y += cos(shift * PI + PI) * 0.1
        }

        // stone
        val minDegrees = (-1).degrees
        val maxDegrees = (+1).degrees
        val stone = image(resourcesVfs["game_over_screen_stone.png"].readBitmap()) {
            rotation = maxDegrees
            anchor(.5, .5)
            scale(1.0)
            position(240, 120)
        }


        launchImmediately {
            while (true) {
                stone.tween(stone::rotation[minDegrees], time = 1.seconds, easing = Easing.EASE_IN_OUT)
                stone.tween(stone::rotation[maxDegrees], time = 1.seconds, easing = Easing.EASE_IN_OUT)

                //delay(8.milliseconds)
            }
        }

        // restart game
        stone.onClick {
            //sceneContainer.changeTo<Industrial>()
            //channel.stop()
            sceneContainer.changeTo<City>()
        }
    }
}