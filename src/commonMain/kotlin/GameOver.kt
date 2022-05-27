import com.soywiz.korge.input.onClick
import com.soywiz.korge.scene.Scene
import com.soywiz.korge.view.Container
import com.soywiz.korge.view.centerOnStage
import com.soywiz.korge.view.image
import com.soywiz.korge.view.scale
import com.soywiz.korim.format.readBitmap
import com.soywiz.korio.file.std.resourcesVfs

class GameOver : Scene() {
    override suspend fun Container.sceneInit() {
        val bitmap = resourcesVfs["game_over.png"].readBitmap()
        val image = image(bitmap).scale(0.3).centerOnStage()

        image.onClick {
            sceneContainer.changeTo<City>()
        }
    }
}