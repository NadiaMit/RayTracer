package SceneData.DataTypes;

/**
 * save the result of the quadratic equation, needed for the intersection test of a {@link SceneData.Surface.Sphere Sphere}
 */
public class QuadraticSolution {

    private float t0;
    private float t1;
    private boolean hasSolution;

    public QuadraticSolution(float t0, float t1, boolean hasSolution) {
        this.t0 = t0;
        this.t1 = t1;
        this.hasSolution = hasSolution;
    }

    public float getT0() {
        return t0;
    }

    public float getT1() {
        return t1;
    }

    public boolean hasSolution() {
        return hasSolution;
    }
}
