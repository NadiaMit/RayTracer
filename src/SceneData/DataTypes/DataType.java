package SceneData.DataTypes;

public abstract class DataType {
    protected float x;
    protected float y;
    protected float z;

    public DataType(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public DataType() {
        this.x = 0.0f;
        this.y = 0.0f;
        this.z = 0.0f;
    }
}
