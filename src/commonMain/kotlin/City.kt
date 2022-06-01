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

        // Stage lasts this amount of time.
        val stageEnd = 35000.milliseconds

        // Main hero dog alive or dead, the dog can move and the stage is moving.
        var stageMoving = true
        var dogAlive: Boolean = true
        var canMove: Boolean = false
        // List of obstacles.
        var obstacles: MutableList<View> = mutableListOf<View>()

        // Start timer.
        val start = DateTime.now()

        // Attach updater to this container.
        addUpdater() { time ->
            var now = DateTime.now()
            if((now-start)> stageEnd-600.milliseconds) {
                canMove = false
            }
            if((now-start) > stageEnd) {
                stageMoving = false
                launch {
                    delay(TimeSpan(6000.0))
                    //channel.stop()
                    sceneContainer.changeTo<Forest>()
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

        val skater: Sprite = sprite(skaterAnimation).xy(800,180).scale(1)
        obstacles.add(skater)
        skater.playAnimationLooped(spriteDisplayTime = 140.milliseconds)
        skater.addUpdater {
            if (dogAlive) {
                x -= 0.65
            } else {
                x += 0.45
            }


        }


        var randomPos = (0..1).random()
        var randomPos2 = if (randomPos == 0) 1 else 0
        val barrelHitbox = image(resourcesVfs["barrel_crop.png"].readBitmap()).xy(960,240-randomPos2*33).scale(0.3)
        val barrelFg = image(resourcesVfs["barrel_crop.png"].readBitmap()).xy(950,240-randomPos2*33).scale(0.8)
        obstacles.add(barrelHitbox)
        val barrelHitbox2 = image(resourcesVfs["barrel_crop.png"].readBitmap()).xy(1360,240-randomPos*33).scale(0.3)
        val barrelFg2 = image(resourcesVfs["barrel_crop.png"].readBitmap()).xy(1350,240-randomPos*33).scale(0.8)
        obstacles.add(barrelHitbox2)
        val barrelHitbox3 = image(resourcesVfs["barrel_crop.png"].readBitmap()).xy(1760,240-randomPos2*33).scale(0.3)
        val barrelFg3 = image(resourcesVfs["barrel_crop.png"].readBitmap()).xy(1750,240-randomPos2*33).scale(0.8)
        obstacles.add(barrelHitbox3)
        val barrelHitbox4 = image(resourcesVfs["barrel_crop.png"].readBitmap()).xy(1960,240-randomPos*33).scale(0.3)
        val barrelFg4 = image(resourcesVfs["barrel_crop.png"].readBitmap()).xy(1950,240-randomPos*33).scale(0.8)
        obstacles.add(barrelHitbox4)
        val barrelHitbox5 = image(resourcesVfs["barrel_crop.png"].readBitmap()).xy(2060,240-randomPos2*33).scale(0.3)
        val barrelFg5 = image(resourcesVfs["barrel_crop.png"].readBitmap()).xy(2050,240-randomPos2*33).scale(0.8)
        obstacles.add(barrelHitbox5)
        val barrelHitbox6 = image(resourcesVfs["barrel_crop.png"].readBitmap()).xy(2160,240-randomPos*33).scale(0.3)
        val barrelFg6 = image(resourcesVfs["barrel_crop.png"].readBitmap()).xy(2150,240-randomPos*33).scale(0.8)
        obstacles.add(barrelHitbox6)

        launchImmediately {
            frameBlock(144.timesPerSecond) {
                while (stageMoving) {
                    tilemap1.x -= 0.06
                    tilemap2.x -= 0.1
                    tilemap3.x -= 0.175
                    tilemap4.x -= 0.55
                    tilemap5.x -= 0.55
                    barrelHitbox.x -= 0.55
                    barrelFg.x -= 0.55
                    barrelHitbox2.x -= 0.55
                    barrelFg2.x -= 0.55
                    barrelHitbox3.x -= 0.55
                    barrelFg3.x -= 0.55
                    barrelHitbox4.x -= 0.55
                    barrelFg4.x -= 0.55
                    barrelHitbox5.x -= 0.55
                    barrelFg5.x -= 0.55
                    barrelHitbox6.x -= 0.55
                    barrelFg6.x -= 0.55
                    frame()
                }
            }
        }

        // Add sprite assets and animations
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





        val drone: Sprite = sprite(droneAnimation).xy(900,200).scale(-1)
        obstacles.add(drone)
        drone.playAnimationLooped(spriteDisplayTime = 100.milliseconds)
        var shift = 0.0
        drone.addUpdater {
            x -= 2
            shift += 0.01
            y += cos(shift* PI)
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
                    stageMoving = false
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

            if (dogAlive && (DateTime.now()-start) > stageEnd ) {
                x += 1.1

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


        // layer 7 - foreground poles
        val tileset6 = TileSet(bitmap("level_1_road_lights_fg.png")
            .toBMP32()
            .scaleLinear(1.0, 1.0).slice(), 480, 270)
        val tilemap6 = tileMap(
            Bitmap32(1,1),
            repeatX = BaseTileMap.Repeat.REPEAT,
            tileset = tileset6)

        launchImmediately {
            frameBlock(144.timesPerSecond) {
                while (stageMoving) {
                    tilemap6.x -= 0.55
                    frame()
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
            if ((now-start) > stageEnd && (!clearedPopupShown)) {
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