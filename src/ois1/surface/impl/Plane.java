package ois1.surface.impl;

import ois1.Ray;
import ois1.VectUtil;
import ois1.surface.CrossingException;
import ois1.surface.Surface;

public class Plane implements Surface {
    private double[] n;
    private double[] r0;

    public Plane(double[] n, double[] r0) {
        this.n = n;
        VectUtil.norm(n);
        this.r0 = r0;
    }

    public double[] getN() {
        return n;
    }

    @Override
    public double[] crossing(Ray ray) throws CrossingException {
        double numerator = VectUtil.dotProduct(VectUtil.substrProduct(r0, ray.getR0()), n);
        double denumerator = VectUtil.dotProduct(n, ray.getE());
        /*for (int i = 0; i < 3; i++) {
            numerator += (r0[i] - ray.getR0()[i])*n[i];
            denumerator += n[i]*ray.getE()[i];
        }*/

        if (denumerator == 0 && numerator == 0) {
            throw new CrossingException("Луч находится в плоскости");
        } else if (denumerator == 0) {
            throw new CrossingException("Луч параллелен");
        }
        double t = numerator/denumerator;
        if (t < 0) {
            throw new CrossingException("Луч не пересекает плоскость");
        }
        double[] ansDot = ray.getDot(t);
        return ansDot;
    }

    @Override
    public Ray reflection(Ray ray) throws CrossingException {
        double[] crossDot = crossing(ray);
        double[] newE = VectUtil.reflectionE(ray.getE(), n);
        return new Ray(crossDot, newE);
    }

    @Override
    public Ray refraction(Ray ray, double n1, double n2) throws CrossingException {
        double[] crossDot = crossing(ray);
        double[] newE = VectUtil.refractionE(ray.getE(), n, n1, n2);
        return new Ray(crossDot, newE);
    }
}
