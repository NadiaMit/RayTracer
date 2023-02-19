package SceneData.Surface.Material;

import SceneData.DataTypes.MyColor;

public abstract class Material {
    protected Phong phong;
    protected float reflectance;
    protected float transmittance;
    protected float refraction;

    public Material(Phong phong, float reflectance, float transmittance, float refraction) {
        this.phong = phong;
        this.reflectance = reflectance;
        this.transmittance = transmittance;
        this.refraction = refraction;
    }

    public Material(){
        this.phong = new Phong();
        this.reflectance = 0.0f;
        this.transmittance = 0.0f;
        this.refraction = 0.0f;
    }

    public abstract MyColor getColor(float u, float v);

    public Phong getPhong() {
        return phong;
    }

    public float getReflactance() { return reflectance; }

    public float getTransmittance() {
        return transmittance;
    }

    public float getRefractionIndex() { return refraction; }

    @Override
    public String toString() {
        return "\n\t\t\tphong: " + phong +
                ",\n\t\t\treflectance: " + reflectance +
                ",\n\t\t\ttransmittance: " + transmittance +
                ",\n\t\t\trefraction: " + refraction;
    }
}
