package SceneData.Surface;

import SceneData.DataTypes.MyColor;
import SceneData.DataTypes.Intersection;
import SceneData.DataTypes.MyMatrix;
import SceneData.Surface.Material.Material;
import SceneData.DataTypes.Ray;

public abstract class Surface {
    protected Material material;

    public Surface(Material material) {
        this.material = material;
    }


    public abstract MyColor getColor(float x, float y, float z);

    public Material getMaterial() {
        return material;
    }

    public Intersection intersect(Ray ray) {
        return intersect(ray, 0.0f);
    }

    public abstract Intersection intersect(Ray ray, float epsilon);

    @Override
    public String toString() {
        return "\n\t\tmaterial: " + material;
    }
}
