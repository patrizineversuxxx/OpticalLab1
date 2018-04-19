package ois1.surface.impl;

import ois1.Main;
import ois1.Ray;
import ois1.VectUtil;
import ois1.surface.CrossingException;
import ois1.surface.Surface;

public class Ellipsoid implements Surface {
    private double[] p0;
    private double a, b, c;

    public Ellipsoid(double[] p0, double a, double b, double c) {
        this.p0 = p0;
        this.a = a;
        this.b = b;
        this.c = c;
    }

    public double[] getP0() {
        return p0;
    }

    public double getA() {
        return a;
    }

    public double getB() {
        return b;
    }

    public double getC() {
        return c;
    }

    @Override
    public double[] crossing(Ray ray) throws CrossingException {
        double[] Mabc = new double[3];
        Mabc[0] = b*c;
        Mabc[1] = a*c;
        Mabc[2] = a*b;
        double[] MabcET = new double[3];
        for (int i = 0; i < 3; i ++) {
            MabcET[i] = ray.getE()[i]*Mabc[i];
        }
        double[] MabcR0P0T = new double[3];
        for (int i = 0; i < 3; i ++) {
            MabcR0P0T[i] = Mabc[i]*(ray.getR0()[i] - p0[i]);
        }

        double MabcET2 = 0;
        for (int i = 0; i < 3; i++) {
            MabcET2 += Math.pow(MabcET[i], 2.0);
        }

        double MabcR0P0T2 = 0;
        for (int i = 0; i < 3; i++) {
            MabcR0P0T2 += Math.pow(MabcR0P0T[i], 2.0);
        }

        double comp1 = 0;
        for (int i = 0; i < 3; i++) {
            comp1 += MabcET[i]*MabcR0P0T[i];
        }

        double comp2 = Math.pow(comp1, 2.0) - MabcET2*(MabcR0P0T2 - Math.pow(a*b*c, 2.0));

        if(comp2 < 0) {
            throw new CrossingException("Нет точек пересечения");
        } else if(comp2 == 0) {
            System.out.println("Луч касается");
            return ray.getDot(-comp1/MabcET2);
        } else {
            double t1 = (-comp1 + Math.sqrt(comp2))/MabcET2;
            double t2 = (-comp1 - Math.sqrt(comp2))/MabcET2;
            if (t1 < 0 && t2 < 0) {
                throw new CrossingException("Луч не пересекает элипсоид");
            } else if (t1 < 0 || t2 < 0) {
                System.out.println("Луч выходит изнутри элипсоид");
                return ray.getDot(Math.max(t1, t2));
            } else {
                System.out.println("Луч пересекает элипсоид в 2 точках");
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
        n[0] = (crossDot[0] - p0[0])*2/Math.pow(a, 2.0);
        n[1] = (crossDot[1] - p0[1])*2/Math.pow(b, 2.0);
        n[2] = (crossDot[2] - p0[2])*2/Math.pow(c, 2.0);
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
        n[0] = (crossDot[0] - p0[0])*2/Math.pow(a, 2.0);
        n[1] = (crossDot[1] - p0[1])*2/Math.pow(b, 2.0);
        n[2] = (crossDot[2] - p0[2])*2/Math.pow(c, 2.0);
        VectUtil.norm(n);
        double[] newE = VectUtil.refractionE(ray.getE(), n, n1, n2);
        return new Ray(crossDot, newE);
    }
}
