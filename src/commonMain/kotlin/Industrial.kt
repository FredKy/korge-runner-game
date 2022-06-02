import com.soywiz.klock.DateTime
import com.soywiz.klock.TimeSpan
import com.soywiz.klock.milliseconds
import com.soywiz.klock.timesPerSecond
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
import com.soywiz.korim.bitmap.Bitmap
import com.soywiz.korim.bitmap.Bitmap32
import com.soywiz.korim.bitmap.slice
import com.soywiz.korim.format.PNG.readImage
import com.soywiz.korim.format.readBitmap
import com.soywiz.korio.async.launch
import com.soywiz.korio.async.launchImmediately
import com.soywiz.korio.file.std.resourcesVfs
import com.soywiz.korma.geom.Anchor
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.round

class Industrial() : Scene() {
    override suspend fun Container.sceneInit() {

        // Play music.
        //val music = resourcesVfs["holiznapatreon_the_heat.mp3"].readSound()
        //val channel = music.play()

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
                    sceneContainer.changeTo<Winner>()
                }
            }
        }


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
        val s = (10/9)*0.55

        var randomPos = 0
        var randomPos2 = if (randomPos == 0) 1 else 0
        val barrelHitbox = image(resourcesVfs["barrel_crop.png"].readBitmap()).xy(960,240-randomPos2*33).scale(0.3)
        val barrelFg = image(resourcesVfs["barrel_crop.png"].readBitmap()).xy(950,240-randomPos2*33).scale(0.8)
        obstacles.add(barrelHitbox)
        val barrelHitboxB = image(resourcesVfs["barrel_crop.png"].readBitmap()).xy(1130,240-randomPos*33).scale(0.3)
        val barrelFgB = image(resourcesVfs["barrel_crop.png"].readBitmap()).xy(1120,240-randomPos*33).scale(0.8)
        obstacles.add(barrelHitboxB)
        val barrelHitbox2 = image(resourcesVfs["barrel_crop.png"].readBitmap()).xy(1360,240-randomPos2*33).scale(0.3)
        val barrelFg2 = image(resourcesVfs["barrel_crop.png"].readBitmap()).xy(1350,240-randomPos2*33).scale(0.8)
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
        val barrelHitbox7 = image(resourcesVfs["barrel_crop.png"].readBitmap()).xy(2360,240-randomPos2*33).scale(0.3)
        val barrelFg7 = image(resourcesVfs["barrel_crop.png"].readBitmap()).xy(2350,240-randomPos2*33).scale(0.8)
        obstacles.add(barrelHitbox7)
        val barrelHitbox8 = image(resourcesVfs["barrel_crop.png"].readBitmap()).xy(2460,240-randomPos*33).scale(0.3)
        val barrelFg8 = image(resourcesVfs["barrel_crop.png"].readBitmap()).xy(2450,240-randomPos*33).scale(0.8)
        obstacles.add(barrelHitbox8)


        launchImmediately {
            frameBlock(144.timesPerSecond) {
                while (stageMoving) {
                    tilemap2.x -= 0.1*s
                    tilemap3.x -= 0.175*s
                    tilemap4.x -= 0.35*s
                    tilemap5.x -= 0.6*s
                    tilemap6.x -= 0.9*s
                    barrelHitbox.x -= 0.9*s
                    barrelFg.x -= 0.9*s
                    barrelHitbox2.x -= 0.9*s
                    barrelFg2.x -= 0.9*s
                    barrelHitbox3.x -= 0.9*s
                    barrelFg3.x -= 0.9*s
                    barrelHitbox4.x -= 0.9*s
                    barrelFg4.x -= 0.9*s
                    barrelHitbox5.x -= 0.9*s
                    barrelFg5.x -= 0.9*s
                    barrelHitbox6.x -= 0.9*s
                    barrelFg6.x -= 0.9*s
                    barrelHitbox7.x -= 0.9*s
                    barrelFg7.x -= 0.9*s
                    barrelHitbox8.x -= 0.9*s
                    barrelFg8.x -= 0.9*s
                    barrelHitboxB.x -= 0.9*s
                    barrelFgB.x -= 0.9*s
                    frame()
                }
            }
        }


        //val smDroneWalk: Bitmap = resourcesVfs["drone_forward.png"].readBitmap()
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

        val orangeDrone: Sprite = sprite(orangeDroneAnimation).xy(840,80).scale(1)
        obstacles.add(orangeDrone)
        orangeDrone.playAnimationLooped(spriteDisplayTime = 150.milliseconds)

        var orange_shift = 0.0
        var oSpeed = 1.0
        orangeDrone.addUpdater {
            var now = DateTime.now()
            if ((now-start) > 0.milliseconds && (now-start) < 7000.milliseconds) {

                x -= 1.5*oSpeed

            } else if ((now-start) >= 7000.milliseconds && (now-start) < 10000.milliseconds) {
                oSpeed-= 0.002
                x -= 1.5*oSpeed
                orange_shift += 0.001
                y += cos(orange_shift* PI) *0.1
            } else if ((now-start) >= 10000.milliseconds && (now-start) < 10700.milliseconds) {
                oSpeed = 1.0
                y += 1.5*oSpeed
            } else if ((now-start) >= 10700.milliseconds && (now-start) < 12700.milliseconds) {

            } else if ((now-start) >= 12700.milliseconds && (now-start) < 13200.milliseconds) {
                oSpeed = 1.0
                y -= 1.5*oSpeed
            } else if ((now-start) >= 13200.milliseconds && (now-start) < 16300.milliseconds) {

            } else if ((now-start) >= 16300.milliseconds && (now-start) < 16900.milliseconds) {
                y += 1.5*oSpeed
            } else if ((now-start) >= 19000.milliseconds && (now-start) < 21000.milliseconds) {

            } else if ((now-start) >= 21000.milliseconds && (now-start) < 21500.milliseconds) {
                y -= 1.5*oSpeed
            } else if ((now-start) >= 21500.milliseconds && (now-start) < 26800.milliseconds) {
                x += oSpeed
            } else if ((now-start) >= 26800.milliseconds && (now-start) < 32800.milliseconds) {
                x -= 1.5*oSpeed
                y += 0.75*oSpeed
            }

        }


        // Code for dogo.
        val dog: Sprite = sprite(runAnimation).xy(-50, 206)
        dog.playAnimationLooped(spriteDisplayTime = 80.milliseconds)
        launchImmediately {
            delay(3000.milliseconds)
            animate(completeOnCancel = false) { dog.moveTo(60.0, round(dog.y), time = 3000.milliseconds) }
            canMove = true
        }
        dog.addUpdater {
            if(collidesWith(obstacles)) {
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

        // Code block for debugging that has been commented out.

        /*val xDogCoords = text("0,0").xy(50,20).scale(1)
        val yDogCoords = text("0,0").xy(50,50).scale(1)
        val inGameTime = text("Time: ").xy(50,80).scale(1)

        inGameTime.addUpdater {
            text = "Time: ${DateTime.now()-start}"
        }
        xDogCoords.addUpdater {
            text = "X: ${round(dog.x)}"
        }
        yDogCoords.addUpdater {
            text = "Y: ${round(dog.y)}"
        }*/

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
        val startPopup = image(bitmap("stage_3.png")).alpha(0)
        addChild(startPopup)
        var startPopupShown = false
        startPopup.addUpdater { time ->
            var now = DateTime.now()
            //print((now-start).toString() + "\n")
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
            //print((now-start).toString() + "\n")
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
