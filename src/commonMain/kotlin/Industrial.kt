import com.soywiz.klock.timesPerSecond
import com.soywiz.korge.scene.Scene
import com.soywiz.korge.time.frameBlock
import com.soywiz.korge.view.Container
import com.soywiz.korge.view.image
import com.soywiz.korge.view.tiles.BaseTileMap
import com.soywiz.korge.view.tiles.TileSet
import com.soywiz.korge.view.tiles.tileMap
import com.soywiz.korim.bitmap.Bitmap32
import com.soywiz.korim.bitmap.slice
import com.soywiz.korim.format.PNG.readImage
import com.soywiz.korio.async.launchImmediately
import com.soywiz.korma.geom.Anchor

class Industrial() : Scene() {
    override suspend fun Container.sceneInit() {
        // background
        val bg = image(bitmap("industry_1.png"))
        addChild(bg)

        // layer 2
        val tileset2 = TileSet(bitmap("industry_2.png")
            .toBMP32().scaled(480,270)
            .scaleLinear(1.0, 1.0).slice(), 480, 270)
        val tilemap2 = tileMap(
            Bitmap32(1,1),
            repeatX = BaseTileMap.Repeat.REPEAT,
            tileset = tileset2)

        // layer 3
        val tileset3 = TileSet(bitmap("industry_3.png")
            .toBMP32().scaled(480,270)
            .scaleLinear(1.0, 1.0).slice(), 480, 270)
        val tilemap3 = tileMap(
            Bitmap32(1,1),
            repeatX = BaseTileMap.Repeat.REPEAT,
            tileset = tileset3)

        // layer 4
        val tileset4 = TileSet(bitmap("industry_4.png")
            .toBMP32().scaled(480,270)
            .scaleLinear(1.0, 1.0).slice(), 480, 270)
        val tilemap4 = tileMap(
            Bitmap32(1,1),
            repeatX = BaseTileMap.Repeat.REPEAT,
            tileset = tileset4)

        // layer 5
        val tileset5 = TileSet(bitmap("industry_5.png")
            .toBMP32().scaled(480,270)
            .scaleLinear(1.0, 1.0).slice(), 480, 270)
        val tilemap5 = tileMap(
            Bitmap32(1,1),
            repeatX = BaseTileMap.Repeat.REPEAT,
            tileset = tileset5)

        // layer 5
        val tileset6 = TileSet(bitmap("level_3_road.png")
            .toBMP32().scaled(480,270)
            .scaleLinear(1.0, 1.0).slice(), 480, 270)
        val tilemap6 = tileMap(
            Bitmap32(1,1),
            repeatX = BaseTileMap.Repeat.REPEAT,
            tileset = tileset6)

        tilemap2.y -= 60
        tilemap3.y -= 60
        tilemap4.y -= 60
        tilemap5.y -= 60
        val s = (10/9)*0.5

        launchImmediately {
            frameBlock(144.timesPerSecond) {
                while (true) {
                    tilemap2.x -= 0.1*s
                    tilemap3.x -= 0.175*s
                    tilemap4.x -= 0.35*s
                    tilemap5.x -= 0.6*s
                    tilemap6.x -= 0.9*s
                    frame()
                }
            }
        }
    }
}
