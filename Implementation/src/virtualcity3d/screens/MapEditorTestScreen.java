package virtualcity3d.screens;

import framework.core.architecture.BaseScreen;
import framework.core.architecture.Program;
import framework.core.camera.Camera2D;
import framework.core.input.mouse.MouseInputProcessor;
import framework.core.input.mouse.MouseInputProcessorListener;
import framework.text.TextRenderer;
import framework.utills.SimpleShapesRenderer;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.ReadableColor;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import virtualcity3d.listeners.MapEditorMouseListener;
import virtualcity3d.models.hud.ColorSquare;
import virtualcity3d.models.hud.HouseIcon;
import virtualcity3d.models.hud.Icon;

import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;

/**
 * Created with IntelliJ IDEA.
 * User: Yan
 * Date: 05/09/13
 * Time: 18:50
 * To change this template use File | Settings | File Templates.
 */
public class MapEditorTestScreen extends BaseScreen {

    Camera2D mCamera;
    private ColorSquare mEditorAreaSquare;
    private Icon mCurrentlySelectedIcon;
    private Vector2f mCursorWorldCoords;
    private ArrayList<Icon> mMapDrawnIcons = new ArrayList<Icon>();
    private ArrayList<Icon> mSidePanelIcons = new ArrayList<Icon>();
    private TextRenderer mTextRenderer = new TextRenderer();
    private MouseInputProcessorListener mInputProcessorListener;
    private String mRenderedText = "Select Icon";

    public MapEditorTestScreen(Program program) {
        super(program);
        mInputProcessorListener = new MapEditorMouseListener(this);
    }

    @Override
    public void init() {

        //init camera
        mCamera = new Camera2D();
        mCamera.initializePerspective();

        //init editor square
        double visibleAreaWidth = mCamera.getVisibleArea().getWidth();
        double visibleAreaHeight = mCamera.getVisibleArea().getHeight();
        double editorAreaWidth = 0.95 * visibleAreaWidth;
        double editorAreaHeight = 0.95 * visibleAreaHeight;
        mEditorAreaSquare = new ColorSquare(ReadableColor.GREY, editorAreaWidth, editorAreaHeight);
        mEditorAreaSquare.setPosition(
                new Vector3f((float) (visibleAreaWidth - editorAreaWidth),
                        -(float) (visibleAreaHeight - editorAreaHeight), 0f));

        //init side panel icons
        initSidePanelIcons((float) editorAreaWidth, (float) editorAreaHeight);

        //set listeners
        MouseInputProcessor.setMouseInputProcessorListener(mInputProcessorListener);
    }

    private void initSidePanelIcons(float editorAreaWidth, float editorAreaHeight) {

        float padding = 0.01f;
        float initX = -editorAreaWidth / 2 + padding;
        float initY = editorAreaHeight / 2 - 0.1f - padding;


        //small house green icon
        final HouseIcon smallHouseIconGreen = new HouseIcon(0.05);
        smallHouseIconGreen.setBackGroundColor(ReadableColor.GREEN);

        smallHouseIconGreen.setPosition(new Vector3f(initX, initY, 0f));
        smallHouseIconGreen.setClickListener(new Icon.IconClickListener() {
            @Override
            public void onIconClicked() {
                setRenderedText("Small House Icon Selected");
                setCurrentlySelectedIcon(smallHouseIconGreen.clone());
            }
        });
        mSidePanelIcons.add(smallHouseIconGreen);

        //small house blue icon
        final HouseIcon bigHouseIconBlue = new HouseIcon(0.05);
        bigHouseIconBlue.setBackGroundColor(ReadableColor.BLUE);
        bigHouseIconBlue.setPosition(new Vector3f(initX, (float) (initY - smallHouseIconGreen.getY_Size() - padding), 0f));
        bigHouseIconBlue.setClickListener(new Icon.IconClickListener() {
            @Override
            public void onIconClicked() {
                setRenderedText("Big House Icon Selected");
                setCurrentlySelectedIcon(bigHouseIconBlue.clone());
            }
        });
        mSidePanelIcons.add(bigHouseIconBlue);
    }

    @Override
    public void onDraw() {

        // clear depth buffer and color
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        glClearColor(0, 0, 0, 1.0f);
        glLoadIdentity();

        //render the map
        mEditorAreaSquare.render();


        //enable texture rendering states
        glEnable(GL_TEXTURE_2D);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        {

            drawSidePanel();

            //draw all icons that were marked already
            drawMarkedIcons();

            //follow the cursor
            if (mCurrentlySelectedIcon != null) {
                mCurrentlySelectedIcon.setPosition(new Vector3f(mCursorWorldCoords.x, mCursorWorldCoords.y, 0));
                mCurrentlySelectedIcon.render();
            }
        }

        //disable texture rendering states
        glDisable(GL_BLEND);
        glDisable(GL_TEXTURE_2D);

        //draws a red dot
        drawCursorPointer();
    }

    //draw all side panel icons
    private void drawSidePanel() {

        //draw icons
        for (Icon sidePanelIcon : mSidePanelIcons) {
            sidePanelIcon.render();
        }

        //draw description text
        mTextRenderer.renderText(
                0,
                mCamera.getVisibleArea().getRightTop().getY(),
                mRenderedText);

    }

    private void drawMarkedIcons() {
        //draw all marked icons
        for (Icon mapDrawenIcon : mMapDrawnIcons) {
            mapDrawenIcon.render();
        }
    }

    private void drawCursorPointer() {
        //draw simple cursor
        SimpleShapesRenderer.drawCursor(mCursorWorldCoords);
    }

    @Override
    public void onUpdate(long delta) {
        //poll cursor coordinates and translate to world coordinates
        mCursorWorldCoords = mCamera.screenToWorld(new Vector2f(Mouse.getX(), Mouse.getY()));
    }

    public ArrayList<Icon> getMapDrawnIcons() {
        return mMapDrawnIcons;
    }

    public ArrayList<Icon> getSidePanelIcons() {
        return mSidePanelIcons;
    }

    public Icon getCurrentlySelectedIcon() {
        return mCurrentlySelectedIcon;
    }

    public Vector2f getCursorWorldCoords() {
        return mCursorWorldCoords;
    }

    public void setRenderedText(String renderedText) {
        mRenderedText = renderedText;
    }

    public ColorSquare getEditorAreaSquare() {
        return mEditorAreaSquare;
    }

    public void setCurrentlySelectedIcon(Icon currentlySelectedIcon) {
        mCurrentlySelectedIcon = currentlySelectedIcon;
    }
}
