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
        // Attach updater to this container.
        val start = DateTime.now()

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
                    sceneContainer.changeTo<Industrial>()
                }
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
        val tileset2 = TileSet(bitmap("mountains_3.png")
            .toBMP32()
            .scaleLinear(1.0, 1.0).slice(), 1440, 270)
        val tilemap2 = tileMap(
            Bitmap32(1,1),
            repeatX = BaseTileMap.Repeat.REPEAT,
            tileset = tileset2)

        // layer 3 - forest back
        val tileset3 = TileSet(bitmap("forest_back_3.png")
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

        //var randomPos = (0..1).random()
        var randomPos = 0
        val barrelHitbox = image(resourcesVfs["forest_barrel_crop.png"].readBitmap()).xy(1260,240-randomPos*33).scale(0.3)
        val barrelFg = image(resourcesVfs["forest_barrel_crop.png"].readBitmap()).xy(1250,240-randomPos*33).scale(0.8)
        obstacles.add(barrelHitbox)
        val carHitbox = image(resourcesVfs["forest_car_crop_hood_left.png"].readBitmap()).xy(710,245).scale(0.6)
        val car = image(resourcesVfs["forest_car_crop_hood_left.png"].readBitmap()).xy(700,240)
        obstacles.add(carHitbox)
        val carHitbox2 = image(resourcesVfs["forest_car_crop_hood_right.png"].readBitmap()).xy(1010,215).scale(0.6)
        val car2 = image(resourcesVfs["forest_car_crop_hood_right.png"].readBitmap()).xy(1000,210)
        obstacles.add(carHitbox2)
        val carHitbox3 = image(resourcesVfs["forest_car_crop_hood_left.png"].readBitmap()).xy(1410,215).scale(0.6)
        val car3 = image(resourcesVfs["forest_car_crop_hood_left.png"].readBitmap()).xy(1400,210)
        obstacles.add(carHitbox3)
        val carHitbox4 = image(resourcesVfs["forest_car_crop_hood_left.png"].readBitmap()).xy(2310,215).scale(0.6)
        val car4 = image(resourcesVfs["forest_car_crop_hood_left.png"].readBitmap()).xy(2300,210)
        obstacles.add(carHitbox4)
        val carHitbox5 = image(resourcesVfs["forest_car_crop_hood_left.png"].readBitmap()).xy(2610,245).scale(0.6)
        val car5 = image(resourcesVfs["forest_car_crop_hood_left.png"].readBitmap()).xy(2600,240)
        obstacles.add(carHitbox5)

        launchImmediately {
            frameBlock(144.timesPerSecond) {
                while (stageMoving) {
                    //tilemap1.x -= 0.06
                    tilemap2.x -= 0.1
                    tilemap3.x -= 0.175
                    tilemap4.x -= 0.3
                    tilemap5.x -= 0.55
                    carHitbox.x -= 0.55
                    car.x -= 0.55
                    carHitbox2.x -= 0.55
                    car2.x -= 0.55
                    carHitbox3.x -= 0.55
                    car3.x -= 0.55
                    carHitbox4.x -= 0.55
                    car4.x -= 0.55
                    carHitbox5.x -= 0.55
                    car5.x -= 0.55
                    barrelHitbox.x -= 0.55
                    barrelFg.x -= 0.55
                    frame()
                }
            }
        }




        //val smDroneWalk: Bitmap = resourcesVfs["drone_forward.png"].readBitmap()
        val spriteMapRun: Bitmap = resourcesVfs["forest_shiba.png"].readBitmap()
        val spriteMapDeath: Bitmap = resourcesVfs["forest_shiba_death.png"].readBitmap()

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

        val catForward: Bitmap = resourcesVfs["forest_yellow_cat_forward.png"].readBitmap()
        val catAnimation = SpriteAnimation(
            spriteMap = catForward,
            spriteWidth = 48,
            spriteHeight = 26,
            marginLeft = 0,
            marginTop = 22,
            columns = 6,
            rows = 1,
            offsetBetweenColumns = 0,
            offsetBetweenRows = 0
        )

        val ratForward: Bitmap = resourcesVfs["forest_rat_forward_crop.png"].readBitmap()
        val ratAnimation = SpriteAnimation(
            spriteMap = ratForward,
            spriteWidth = 26,
            spriteHeight = 11,
            marginLeft = 0,
            marginTop = 0,
            columns = 4,
            rows = 1,
            offsetBetweenColumns = 0,
            offsetBetweenRows = 0
        )

        val cat: Sprite = sprite(catAnimation).xy(2500,206).scale(-1,1)
        obstacles.add(cat)
        cat.playAnimationLooped(spriteDisplayTime = 100.milliseconds)
        cat.addUpdater {
            x -= 2
        }
        val cat2: Sprite = sprite(catAnimation).xy(2700,236).scale(-1,1)
        obstacles.add(cat2)
        cat2.playAnimationLooped(spriteDisplayTime = 100.milliseconds)
        cat2.addUpdater {
            x -= 2
        }
        val cat3: Sprite = sprite(catAnimation).xy(2950,206).scale(-1,1)
        obstacles.add(cat3)
        cat3.playAnimationLooped(spriteDisplayTime = 100.milliseconds)
        cat3.addUpdater {
            x -= 2
        }
        val rat: Sprite = sprite(ratAnimation).xy(3200,236).scale(-1.5,1.5)
        obstacles.add(rat)
        rat.playAnimationLooped(spriteDisplayTime = 100.milliseconds)
        rat.addUpdater {
            x -= 2
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
        val startPopup = image(bitmap("stage_2.png")).alpha(0)
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