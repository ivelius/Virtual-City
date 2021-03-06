package virtualcity3d.models.models3d;

import framework.models.models3D.Model3DBase;
import resources.AssetManager;
import resources.Assets3D;

/**
 * Created with IntelliJ IDEA.
 * User: Yan
 * Date: 25/09/13
 * Time: 11:35
 * To change this template use File | Settings | File Templates.
 */
public class TreeModel extends Model3DBase {

    //approximated size
    private static final int X_SIZE = 5;
    private static final int Y_SIZE = 5;
    private static final int Z_SIZE = 5;

    public TreeModel() {
        super(AssetManager.getAsset3D(Assets3D.TREE));
    }

    @Override
    public double getX_Size() {
        return X_SIZE;
    }

    @Override
    public double getY_Size() {
        return Y_SIZE;
    }

    @Override
    public double getZ_Size() {
        return Z_SIZE;
    }


}
