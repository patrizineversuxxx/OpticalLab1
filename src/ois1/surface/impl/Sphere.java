package ois1.surface.impl;

import ois1.Main;
import ois1.Ray;
import ois1.VectUtil;
import ois1.surface.CrossingException;
import ois1.surface.Surface;

public class Sphere implements Surface {
    private double[] p0;
    private double rad;

    public Sphere(double[] p0, double rad) {
        this.p0 = p0;
        this.rad = rad;
    }

    @Override
    public double[] crossing(Ray ray) throws CrossingException {
        double comp1 = VectUtil.dotProduct(VectUtil.substrProduct(ray.getR0(), p0), ray.getE());
        double comp2;
        comp2 = Math.pow(comp1, 2.0) + Math.pow(rad, 2.0) - VectUtil.dotProduct(VectUtil.substrProduct(ray.getR0(), p0), VectUtil.substrProduct(ray.getR0(), p0));
        if(comp2 < 0) {
            throw new CrossingException("Нет точек пересечения");
        } else if(comp2 == 0) {
            System.out.println("Луч касается");
            return ray.getDot(-comp1);
        } else {
            double t1 = -comp1 + Math.sqrt(comp2);
            double t2 = -comp1 - Math.sqrt(comp2);
            if (t1 < 0 && t2 < 0) {
                throw new CrossingException("Луч не пересекает сферу");
            } else if (t1 < 0 || t2 < 0) {
                System.out.println("Луч выходит изнутри сферы");
                return ray.getDot(Math.max(t1, t2));
            } else {
                System.out.println("Луч пересекает сферу в 2 точках");
                double[] ansDot = new double[8];
                double[] dot1 = ray.getDot(Math.min(t1, t2));
                double[] dot2 = ray.getDot(Math.max(t1, t2));
                for (int i = 0; i < 4; i ++) {
                    ansDot[i] = dot1[i];
                }
                for (int i = 0; i < 4; i ++) {
                    ansDot[i+4] = dot2[i];
                }
                return ansDot;
            }
        }
    }

    @Override
    public Ray reflection(Ray ray) throws CrossingException {
        double[] crossDot = crossing(ray);
        double[] n = new double[3];
        n[0] = (crossDot[0] - p0[0])*2;
        n[1] = (crossDot[1] - p0[1])*2;
        n[2] = (crossDot[2] - p0[2])*2;
        VectUtil.norm(n);
        double[] newE = VectUtil.reflectionE(ray.getE(), n);
        return new Ray(crossDot, newE);
    }

    @Override
    public Ray refraction(Ray ray, double n1, double n2) throws CrossingException {
        double[] crossDot = crossing(ray);
        double[] n = new double[3];
        if (crossDot[3] < Main.ERR && crossDot.length > 4) {
            crossDot[0] = crossDot[4];
            crossDot[1] = crossDot[5];
            crossDot[2] = crossDot[6];
            crossDot[3] = crossDot[7];
        }
        n[0] = (crossDot[0] - p0[0]) * 2;
        n[1] = (crossDot[1] - p0[1]) * 2;
        n[2] = (crossDot[2] - p0[2]) * 2;

        VectUtil.norm(n);
        double[] newE = VectUtil.refractionE(ray.getE(), n, n1, n2);
        return new Ray(crossDot, newE);
    }


    public double[] getP0() {
        return p0;
    }

    public double getRad() {
        return rad;
    }
}
