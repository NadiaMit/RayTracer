package SceneData.Surface.Material;

public class Phong {
    private float ka;
    private float kd;
    private float ks;
    private float exponent;

    public Phong(float ka, float kd, float ks, float exponent) {
        this.ka = ka;
        this.kd = kd;
        this.ks = ks;
        this.exponent = exponent;
    }

    public Phong(){
        this.ka = 0.0f;
        this.kd = 0.0f;
        this.ks = 0.0f;
        this.exponent = 0.0f;
    }

    public float getKa() {
        return ka;
    }

    public float getKd() {
        return kd;
    }

    public float getKs() {
        return ks;
    }

    public float getExponent() {
        return exponent;
    }

    @Override
    public String toString() {
        return "Phong {" +
                "ka: " + ka +
                ", kd: " + kd +
                ", ks: " + ks +
                ", exponent: " + exponent +
                '}';
    }
}
