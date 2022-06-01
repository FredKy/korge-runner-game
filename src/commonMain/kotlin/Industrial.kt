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

        val orangeDrone: Sprite = sprite(orangeDroneAnimation).xy(400,80).scale(1)
        obstacles.add(orangeDrone)
        orangeDrone.playAnimationLooped(spriteDisplayTime = 150.milliseconds)

        var orange_shift = 0.0
        var oSpeed = 1.0
        orangeDrone.addUpdater {
            var now = DateTime.now()
//			if (gameTick > 1) {
//				x -= 1
//				orange_shift += 0.01
//				y += cos(orange_shift*PI)
//			}
            if ((now-start) > 0.milliseconds && (now-start) < 5000.milliseconds) {
                //oSpeed-= 0.001
                x -= 1*oSpeed
                orange_shift += 0.001
                y += cos(orange_shift* PI) *0.1
            } else if ((now-start) >= 5000.milliseconds ) {

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
        val startPopup = image(bitmap("stage_3.png")).alpha(0)
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
