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

class Winner : Scene() {
    override suspend fun Container.sceneInit() {
        // background
        val background = resourcesVfs["game_over_screen_background.png"].readBitmap()
        image(background)

        // shiba
        val shiba = resourcesVfs["winner_screen_shiba.png"].readBitmap()
        val shibaImage = image(shiba)
        shibaImage.y+= -60
        var shift = 0.0
        var s = 0.0
        shibaImage.addUpdater {
            shift += 0.009
            s += 0.01
            y += cos(shift * PI) * .9
        }

        val playAgain = resourcesVfs["winner_screen_play_again.png"].readBitmap()
        val playAgainImage = image(playAgain)

        // winner text
        val minDegrees = (-1).degrees
        val maxDegrees = (+1).degrees
        val winnerText = image(resourcesVfs["winner_screen_text.png"].readBitmap()) {
            rotation = maxDegrees
            anchor(.8, .5)
            scale(1.0)
            position(385, 140)
        }


        launchImmediately {
            while (true) {
                winnerText.tween(winnerText::rotation[minDegrees], time = 1.seconds, easing = Easing.EASE_IN_OUT)
                winnerText.tween(winnerText::rotation[maxDegrees], time = 1.seconds, easing = Easing.EASE_IN_OUT)

                //delay(8.milliseconds)
            }
        }

        // chicken
        val chicken = image(resourcesVfs["winner_screen_chicken.png"].readBitmap())

        chicken.addUpdater {
            shift += 0.0001
            x += cos(shift * PI + PI) * 0.2
        }

        // restart game
        playAgainImage.onClick {
            sceneContainer.changeTo<City>()
        }
    }
}