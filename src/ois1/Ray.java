package ois1;

import java.util.Arrays;

public class Ray {
    private double[] r0, e;

    public Ray(double[] r0, double[] e) {
        this.r0 = r0;
        this.e = e;
        VectUtil.norm(e);
    }

    public double[] getR0() {
        return r0;
    }

    public double[] getE() {
        return e;
    }

    public double[] getDot(double t) {
        double[] dot = new double[4];
        for (int i = 0; i < 3; i ++){
            dot[i] = r0[i] + e[i]*t;
        }
        dot[3] = t;
        return dot;
    }

    @Override
    public String toString() {
        String ans = "Луч{\n" +
                "r0=[" + r0[0] + ", " + r0[1] + ", " + r0[2] + "]" + ",\nt=" + r0[3];
        if(r0.length > 4) {
            ans +=",\nr0=[" + r0[4] + ", " + r0[5] + ", " + r0[6] + "]" + ",\nt=" + r0[7];
        }
        ans += ",\ne=" + Arrays.toString(e) +
                "\n}";
        return ans;
    }
}
