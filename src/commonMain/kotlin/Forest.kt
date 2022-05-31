import com.soywiz.korge.scene.Scene
import com.soywiz.korge.view.Container
import com.soywiz.klock.DateTime
import com.soywiz.klock.TimeSpan
import com.soywiz.klock.milliseconds
import com.soywiz.klock.timesPerSecond
import com.soywiz.korau.sound.readMusic
import com.soywiz.korev.Key
import com.soywiz.korge.animate.animate
import com.soywiz.korge.input.onClick
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
import com.soywiz.korim.format.readBitmap
import com.soywiz.korio.async.launch
import com.soywiz.korio.async.launchImmediately
import com.soywiz.korio.file.std.resourcesVfs
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.round

class Forest : Scene() {

    override suspend fun Container.sceneInit() {

        // Play music.
        //val city_music = resourcesVfs["holiznapatreon_the_heat.mp3"].readMusic()
        //city_music.playForever()
        //println(music.volume)

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
            //println(gameTick)
            //println(now-start)
            if (gameTick == 100) {
                println(100)
            }
        }

        // layer 1 - background
        /*val bg = image(bitmap("Star sky.png"))
        addChild(bg) */

        // layer 1 - star sky
        val tileset1 = TileSet(bitmap("star_sky.png")
            .toBMP32()
            .scaleLinear(1.0, 1.0).slice(), 1440, 270)
        val tilemap1 = tileMap(
            Bitmap32(1,1),
            repeatX = BaseTileMap.Repeat.REPEAT,
            tileset = tileset1)

        tilemap1.x += 1000


        // layer 2 - mountains
        val tileset2 = TileSet(bitmap("mountains.png")
            .toBMP32()
            .scaleLinear(1.0, 1.0).slice(), 1440, 270)
        val tilemap2 = tileMap(
            Bitmap32(1,1),
            repeatX = BaseTileMap.Repeat.REPEAT,
            tileset = tileset2)

        // layer 3 - forest back
        val tileset3 = TileSet(bitmap("forest_back_2.png")
            .toBMP32()
            .scaleLinear(1.0, 1.0).slice(), 1440, 270)
        val tilemap3 = tileMap(
            Bitmap32(1,1),
            repeatX = BaseTileMap.Repeat.REPEAT,
            tileset = tileset3)

        // layer 4 - forest front
        val tileset4 = TileSet(bitmap("forest_front.png")
            .toBMP32()
            .scaleLinear(1.0, 1.0).slice(), 1440, 270)
        val tilemap4 = tileMap(
            Bitmap32(1,1),
            repeatX = BaseTileMap.Repeat.REPEAT,
            tileset = tileset4)

        // layer 6 - buildings back
        val tileset5 = TileSet(bitmap("ground_2.png")
            .toBMP32()
            .scaleLinear(1.0, 1.0).slice(), 480, 270)
        val tilemap5 = tileMap(
            Bitmap32(1,1),
            repeatX = BaseTileMap.Repeat.REPEAT,
            tileset = tileset5)

        var random_pos = (0..1).random()
        val barrel_hitbox = image(resourcesVfs["barrel_crop.png"].readBitmap()).xy(360,240-random_pos*33).scale(0.3)
        val barrel_fg = image(resourcesVfs["barrel_crop.png"].readBitmap()).xy(350,240-random_pos*33).scale(0.8)
        obstacles.add(barrel_hitbox)

        launchImmediately {
            frameBlock(144.timesPerSecond) {
                while (dogAlive) {
                    //tilemap1.x -= 0.06
                    tilemap2.x -= 0.1
                    tilemap3.x -= 0.175
                    tilemap4.x -= 0.3
                    tilemap5.x -= 0.55
                    //barrel_hitbox.x -= 0.55
                    //barrel_fg.x -= 0.55
                    frame()
                }
            }
        }



    }

}