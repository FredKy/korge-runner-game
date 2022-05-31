import com.soywiz.klock.DateTime
import com.soywiz.klock.TimeSpan
import com.soywiz.klock.milliseconds
import com.soywiz.klock.timesPerSecond
import com.soywiz.korau.sound.readMusic
import com.soywiz.korau.sound.readSound
import com.soywiz.korev.Key
import com.soywiz.korge.animate.animate
import com.soywiz.korge.input.onClick
import com.soywiz.korge.scene.Scene
import com.soywiz.korge.time.delay
import com.soywiz.korge.time.frameBlock
import com.soywiz.korge.view.*
import com.soywiz.korge.view.tiles.BaseTileMap
import com.soywiz.korge.view.tiles.TileSet
import com.soywiz.korge.view.tiles.tileMap
import com.soywiz.korge.view.tween.hide
import com.soywiz.korim.bitmap.Bitmap
import com.soywiz.korim.bitmap.Bitmap32
import com.soywiz.korim.bitmap.slice
import com.soywiz.korim.color.Colors
import com.soywiz.korim.format.readBitmap
import com.soywiz.korio.async.launch
import com.soywiz.korio.async.launchImmediately
import com.soywiz.korio.dynamic.KDynamic.Companion.toDouble
import com.soywiz.korio.file.std.resourcesVfs
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.round

class City : Scene() {

    override suspend fun Container.sceneInit() {

        // Play music.
        //val music = resourcesVfs["holiznapatreon_the_heat.mp3"].readSound()
        //val channel = music.play()
        //println(music.volume)

        // Main hero dogo alive or dead.
        var dogAlive: Boolean = true
        var canMove: Boolean = false
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
            //println(time.toString() + "   " + (now-start))
            if (gameTick == 100) {
                println(100)
            }
            if((now-start) > 15000.milliseconds) {
                canMove = false
                launch {
                    delay(TimeSpan(3000.0))
                    //channel.stop()
                    sceneContainer.changeTo<Industrial>()
                }
            }
        }

        // layer 1 - background
        val bg = image(bitmap("level_1_gradient_sky.png"))
        addChild(bg)

        // layer 2 - buildings back
        val tileset1 = TileSet(bitmap("level_1_buildings_back.png")
            .toBMP32()
            .scaleLinear(1.0, 1.0).slice(), 480, 270)
        val tilemap1 = tileMap(
            Bitmap32(1,1),
            repeatX = BaseTileMap.Repeat.REPEAT,
            tileset = tileset1)

        // layer 3 - buildings back
        val tileset2 = TileSet(bitmap("level_1_buildings_middle.png")
            .toBMP32()
            .scaleLinear(1.0, 1.0).slice(), 480, 270)
        val tilemap2 = tileMap(
            Bitmap32(1,1),
            repeatX = BaseTileMap.Repeat.REPEAT,
            tileset = tileset2)

        // layer 4 - buildings back
        val tileset3 = TileSet(bitmap("level_1_buildings_front.png")
            .toBMP32()
            .scaleLinear(1.0, 1.0).slice(), 480, 270)
        val tilemap3 = tileMap(
            Bitmap32(1,1),
            repeatX = BaseTileMap.Repeat.REPEAT,
            tileset = tileset3)

        // layer 5 - buildings back
        val tileset4 = TileSet(bitmap("level_1_road.png")
            .toBMP32()
            .scaleLinear(1.0, 1.0).slice(), 480, 270)
        val tilemap4 = tileMap(
            Bitmap32(1,1),
            repeatX = BaseTileMap.Repeat.REPEAT,
            tileset = tileset4)

        // layer 6 - buildings back
        val tileset5 = TileSet(bitmap("level_1_road_lights_bg.png")
            .toBMP32()
            .scaleLinear(1.0, 1.0).slice(), 480, 270)
        val tilemap5 = tileMap(
            Bitmap32(1,1),
            repeatX = BaseTileMap.Repeat.REPEAT,
            tileset = tileset5)

        var random_pos = (0..1).random()
        val barrel_hitbox = image(resourcesVfs["barrel_crop.png"].readBitmap()).xy(960,240-random_pos*33).scale(0.3)
        val barrel_fg = image(resourcesVfs["barrel_crop.png"].readBitmap()).xy(950,240-random_pos*33).scale(0.8)
        obstacles.add(barrel_hitbox)

        launchImmediately {
            frameBlock(144.timesPerSecond) {
                while (dogAlive) {
                    tilemap1.x -= 0.06
                    tilemap2.x -= 0.1
                    tilemap3.x -= 0.175
                    tilemap4.x -= 0.55
                    tilemap5.x -= 0.55
                    barrel_hitbox.x -= 0.55
                    barrel_fg.x -= 0.55
                    frame()
                }
            }
        }

        //val cube = solidRect(10.0, 10.0, Colors.GOLD).xy(300, 200)
        //obstacles.add(cube)
        //val cube2 = solidRect(48.0, 48.0, Colors.RED).xy(20, 200)
        //addChild(cube)
//        cube.addUpdater {
//            x -= 2
//        }


        val smDroneWalk: Bitmap = resourcesVfs["drone_forward.png"].readBitmap()
        val spriteMapRun: Bitmap = resourcesVfs["dog_run.png"].readBitmap()
        val spriteMapDeath: Bitmap = resourcesVfs["dog_death.png"].readBitmap()

        val runAnimation = SpriteAnimation(
            spriteMap = spriteMapRun,
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

        val orangeDroneWalk: Bitmap = resourcesVfs["orange_drone_forward_w82_h63.png"].readBitmap()
        val orangeDroneAnimation = SpriteAnimation(
            spriteMap = orangeDroneWalk,
            spriteWidth = 82,
            spriteHeight = 63,
            marginLeft = 0,
            marginTop = 0,
            columns = 4,
            rows = 1,
            offsetBetweenColumns = 0,
            offsetBetweenRows = 0
        )

        val skaterWalk: Bitmap = resourcesVfs["skater_forward_w32_h36_9.png"].readBitmap()
        val skaterAnimation = SpriteAnimation(
            spriteMap = skaterWalk,
            spriteWidth = 32,
            spriteHeight = 36,
            marginLeft = 0,
            marginTop = 0,
            columns = 9,
            rows = 1,
            offsetBetweenColumns = 0,
            offsetBetweenRows = 0
        )



        val drone: Sprite = sprite(droneAnimation).xy(900,200).scale(-1)
        obstacles.add(drone)
        drone.playAnimationLooped(spriteDisplayTime = 100.milliseconds)
        var shift = 0.0
        drone.addUpdater {
            x -= 2
            shift += 0.01
            y += cos(shift* PI)
        }



        val orangeDrone: Sprite = sprite(orangeDroneAnimation).xy(400,80).scale(1)
        obstacles.add(orangeDrone)
        orangeDrone.playAnimationLooped(spriteDisplayTime = 150.milliseconds)

        var orange_shift = 0.0
        var oSpeed = 1.0
        orangeDrone.addUpdater {
//			if (gameTick > 1) {
//				x -= 1
//				orange_shift += 0.01
//				y += cos(orange_shift*PI)
//			}
            if (gameTick > 1 && gameTick < 300) {
                //oSpeed-= 0.001
                x -= 1*oSpeed
                orange_shift += 0.001
                y += cos(orange_shift* PI) *0.1
            } else if (gameTick >= 300 ) {

            }

        }
        val skater: Sprite = sprite(skaterAnimation).xy(400,180).scale(1)
        obstacles.add(skater)
        skater.playAnimationLooped(spriteDisplayTime = 140.milliseconds)
        skater.addUpdater {
            if (dogAlive) {
                x -= 0.65
            } else {
                x += 0.45
            }


        }




        // Code for dogo.
        val dog: Sprite = sprite(runAnimation).xy(-50, 206)
        dog.playAnimationLooped(spriteDisplayTime = 80.milliseconds)
        launchImmediately {
            delay(3000.milliseconds)
            animate(completeOnCancel = false) { dog.moveTo(60.0, round(dog.y), time = 3000.milliseconds) }
            //delay(TimeSpan(2000.0))
            canMove = true
        }
        dog.addUpdater {
            var coll = 0

            if(collidesWith(obstacles)) {
                coll += 1
                println("Collision: " +coll)
                if (dogAlive) {
                    dogAlive = false
                    canMove = false
                    playAnimation(times = 0, deathAnimation, spriteDisplayTime = 200.milliseconds, startFrame = 0, endFrame = 3)
                    launchImmediately {
                        animate(completeOnCancel = false) { dog.moveTo(40.0, round(dog.y) + (-10..10).random(), time = 750.milliseconds) }
                    }

                    launch {
                        delay(TimeSpan(3000.0))
                        //channel.stop()
                        sceneContainer.changeTo<GameOver>()
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

        // layer 7 - buildings back
        val tileset6 = TileSet(bitmap("level_1_road_lights_fg.png")
            .toBMP32()
            .scaleLinear(1.0, 1.0).slice(), 480, 270)
        val tilemap6 = tileMap(
            Bitmap32(1,1),
            repeatX = BaseTileMap.Repeat.REPEAT,
            tileset = tileset6)

        launchImmediately {
            frameBlock(144.timesPerSecond) {
                while (dogAlive) {
                    tilemap6.x -= 0.55
                    frame()
                }
            }
        }

        val xDogCoords = text("0,0").xy(50,20).scale(1)
        val yDogCoords = text("0,0").xy(50,50).scale(1)
        val inGameTime = text("Time: ").xy(50,80).scale(1)

        inGameTime.addUpdater {
            text = "Time: ${DateTime.now()-start}"
        }
        //val inGameTime = text("0,0").xy(100,110).scale(1)
        xDogCoords.addUpdater {
            text = "X: ${round(dog.x)}"
        }
        yDogCoords.addUpdater {
            text = "Y: ${round(dog.y)}"
        }

        onClick {
            if (canMove) {
                if (round(dog.y) == 206.0) {
                    animate(completeOnCancel = false) { dog.moveTo(60, 239, time = 500.milliseconds) }
                }
            }
        }

        onClick {
            if (canMove) {
                if (round(dog.y) == 239.0) {
                    animate(completeOnCancel = false) { dog.moveTo(60, 206, time = 500.milliseconds) }
                }
            }
        }


        // Code for showing popup at start of stage.
        val startPopup = image(bitmap("stage_1.png")).alpha(0)
        addChild(startPopup)
        var startPopupShown = false
        startPopup.addUpdater { time ->
            var now = DateTime.now()
            print((now-start).toString() + "\n")
            if ((now-start) > 500.milliseconds && (!startPopupShown)) {
                startPopupShown = true
                launchImmediately {
                    animate(completeOnCancel = false) { parallel(time = 1000.milliseconds) {
                        startPopup.alpha(1)
                    } }
                    delay(2500.milliseconds)
                    animate(completeOnCancel = false) { parallel(time = 1000.milliseconds) {
                        startPopup.alpha(0)
                    } }

                }
            }
        }

        // Code for stage cleared popup.
        val clearedPopup = image(bitmap("stage_cleared.png")).alpha(0)
        addChild(clearedPopup)
        var clearedPopupShown = false
        clearedPopup.addUpdater { time ->
            var now = DateTime.now()
            print((now-start).toString() + "\n")
            if ((now-start) > 10000.milliseconds && (!clearedPopupShown)) {
                clearedPopupShown = true
                launchImmediately {
                    animate(completeOnCancel = false) { parallel(time = 1000.milliseconds) {
                        clearedPopup.alpha(1)
                    } }

                }
            }
        }

    }

}