package com.vincenterc.starfishcollector

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.Event
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable

class LevelScreen : BaseScreen() {
    private lateinit var turtle: Turtle
    private var win = false
    private lateinit var starfishLabel: Label

    private lateinit var dialogBox: DialogBox

    override fun initialize() {
        val ocean = BaseActor(0f, 0f, mainStage)
        ocean.loadTexture("water-border.jpg")
        ocean.setSize(1200f, 900f)
        BaseActor.setWorldBounds(ocean)

        Starfish(400f, 400f, mainStage)
        Starfish(500f, 100f, mainStage)
        Starfish(100f, 450f, mainStage)
        Starfish(200f, 250f, mainStage)

        Rock(200f, 150f, mainStage)
        Rock(100f, 300f, mainStage)
        Rock(300f, 350f, mainStage)
        Rock(450f, 200f, mainStage)

        turtle = Turtle(20f ,20f, mainStage)

        val sign1 = Sign(20f, 400f, mainStage)
        sign1.text = "West Starfish Bay"

        val sign2 = Sign(600f, 300f, mainStage)
        sign2.text = "East Starfish Bay"

        // user interface code
        starfishLabel = Label("Starfish Left:", BaseGame.labelStyle)
        starfishLabel.color = Color.CYAN
        //starfishLabel.setPosition( 20, 520 );
        //uiStage.addActor(starfishLabel);

        val buttonStyle = ButtonStyle()

        val buttonTex = Texture(Gdx.files.internal("undo.png"))
        val buttonRegion = TextureRegion(buttonTex)
        buttonStyle.up = TextureRegionDrawable(buttonRegion)

        val restartButton = Button(buttonStyle)
        restartButton.color = Color.CYAN
        //restartButton.setPosition(720,520);
        //uiStage.addActor(restartButton);

        restartButton.addListener { e: Event ->
            val ie = e as InputEvent
            if (ie.type == InputEvent.Type.touchDown) StarfishGame.setActiveScreen(LevelScreen())
            false
        }

        uiTable.pad(10f)
        uiTable.add(starfishLabel).top()
        uiTable.add().expandX().expandY()
        uiTable.add(restartButton).top()

        dialogBox = DialogBox(0f, 0f, uiStage)
        dialogBox.setBackgroundColor(Color.TAN)
        dialogBox.setFontColor(Color.BROWN)
        dialogBox.setDialogSize(600f, 100f)
        dialogBox.setFontScale(0.80f)
        dialogBox.alignCenter()
        dialogBox.isVisible = false

        uiTable.row()
        uiTable.add(dialogBox).colspan(3)
    }

    override fun update(dt: Float) {
        for (rockActor in BaseActor.getList(mainStage, "com.vincenterc.starfishcollector.Rock"))
            turtle.preventOverlap(rockActor)

        for (starfishActor in BaseActor.getList(mainStage, "com.vincenterc.starfishcollector.Starfish")) {
            val starfish = starfishActor as Starfish
            if (turtle.overlaps(starfish) && !starfish.collected) {
                starfish.collected = true
                starfish.clearActions()
                starfish.addAction(Actions.fadeOut(1f))
                starfish.addAction(Actions.after(Actions.removeActor()))

                val whirl = Whirlpool(0f, 0f, mainStage)
                whirl.centerAtActor(starfish)
                whirl.setOpacity(0.25f)
            }
        }

        if (BaseActor.count(mainStage, "com.vincenterc.starfishcollector.Starfish") == 0 && !win) {
            win = true
            val youWinMessage = BaseActor(0f, 0f, uiStage)
            youWinMessage.loadTexture("you-win.png")
            youWinMessage.centerAtPosition(400f, 300f)
            youWinMessage.setOpacity(0f)
            youWinMessage.addAction(Actions.delay(1f))
            youWinMessage.addAction(Actions.after(Actions.fadeIn(1f)))
        }

        starfishLabel.setText("Starfish Left: " + BaseActor.count(mainStage, "com.vincenterc.starfishcollector.Starfish"))

        for (signActor in BaseActor.getList(mainStage, "com.vincenterc.starfishcollector.Sign")) {
            val sign = signActor as Sign

            turtle.preventOverlap(sign)
            val nearby = turtle.isWithinDistance(4f, sign)

            if (nearby && !sign.isViewing) {
                dialogBox.setText(sign.text)
                dialogBox.isVisible = true
                sign.isViewing = true
            }

            if (sign.isViewing && !nearby) {
                dialogBox.setText(" ")
                dialogBox.isVisible = false
                sign.isViewing = false
            }
        }
    }
}