package virtualcity3d.models.hud.icons;

import framework.geometry.Point;
import framework.geometry.Rectangle;
import framework.models.models2D.Model2dBase;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.ReadableColor;
import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.Color;
import resources.AssetManager;
import resources.Assets2D;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created with IntelliJ IDEA.
 * User: Yan
 * Date: 29/09/13
 * Time: 18:48
 */
public class JunctionRoadIcon extends Model2dBase implements Icon {

    private static final double HALF_SIZE = 0.05;
    ColorSquare mColorSquare;
    double mSize;
    private IconClickListener mClickListener;

    public JunctionRoadIcon() {
        super(AssetManager.getAsset2D(Assets2D.ROAD_ICON_JUNCTION),
                new Rectangle(
                        new Point(-HALF_SIZE, -HALF_SIZE),
                        new Point(HALF_SIZE, HALF_SIZE)));

        mSize = HALF_SIZE;
        mColorSquare = new ColorSquare(null, HALF_SIZE * 2, HALF_SIZE * 2);
        mColorSquare.setPosition(mPosition);
    }

    public JunctionRoadIcon(JunctionRoadIcon cornerRoadIcon) {
        super(AssetManager.getAsset2D(Assets2D.ROAD_ICON_JUNCTION),
                new Rectangle(
                        new Point(-cornerRoadIcon.getSize(), -cornerRoadIcon.getSize()),
                        new Point(cornerRoadIcon.getSize(), cornerRoadIcon.getSize())));

        mSize = cornerRoadIcon.getSize();
        mColorSquare = new ColorSquare(cornerRoadIcon.getBackGroundColor(), mSize * 2, mSize * 2);
        setPosition(new Vector3f(cornerRoadIcon.getPosition()));
    }


    @Override
    public void render() {
        //draw background square
        disableRenderGLStates();
        mColorSquare.render();

        //draw the icon
        enableRenderGLStates();

        //store color
        glPushAttrib(GL_CURRENT_BIT);

        mTexture.bind();
        Color.white.bind();

        glPushMatrix();
        {

            //translate to position
            glTranslated(mPosition.x, mPosition.y, mPosition.z);

            GL11.glBegin(GL11.GL_QUADS);
            {
                GL11.glTexCoord2f(0, 1);
                GL11.glVertex2f((float) mRenderArea.getLeftBottom().getX(), (float) mRenderArea.getLeftBottom().getY());

                GL11.glTexCoord2f(0, 0);
                GL11.glVertex2f((float) mRenderArea.getLeftBottom().getX(), (float) mRenderArea.getRightTop().getY());

                GL11.glTexCoord2f(1, 0);
                GL11.glVertex2f((float) mRenderArea.getRightTop().getX(), (float) mRenderArea.getRightTop().getY());

                GL11.glTexCoord2f(1, 1);
                GL11.glVertex2f((float) mRenderArea.getRightTop().getX(), (float) mRenderArea.getLeftBottom().getY());
            }
            GL11.glEnd();
        }
        glPopMatrix();

        //restore color
        glPopAttrib();
    }

    @Override
    public void setPosition(Vector3f position) {
        super.setPosition(position);
        mColorSquare.setPosition(position);
    }

    @Override
    public void setBackGroundColor(ReadableColor color) {
        mColorSquare.setColor(color);
    }

    @Override
    public ReadableColor getBackGroundColor() {
        return mColorSquare.getColor();
    }

    @Override
    public Icon clone() {
        return new JunctionRoadIcon(this);
    }

    @Override
    public Rectangle getBoundingArea() {
        return mColorSquare.getRenderArea();
    }

    @Override
    public void onClick() {
        if (mClickListener != null)
            mClickListener.onIconClicked();
    }

    public double getSize() {
        return mSize;
    }

    @Override
    public double getX_Size() {
        return mColorSquare.getX_Size();
    }

    @Override
    public double getY_Size() {
        return mColorSquare.getY_Size();
    }

    public void setClickListener(IconClickListener clickListener) {
        mClickListener = clickListener;
    }

}
