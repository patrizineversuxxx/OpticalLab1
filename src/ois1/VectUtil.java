package ois1;

public class VectUtil {
    private VectUtil(){};

    public static void norm(double[] vect) {
        double len = 0;
        for (int i = 0; i < 3; i++) {
            len += vect[i]*vect[i];
        }
        len = Math.sqrt(len);
        for (int i = 0; i < 3; i++) {
            vect[i] = vect[i]/len;
        }
    }

    public static double dotProduct(double[] vect1, double[] vect2){
        double prd = 0;
        for (int i = 0; i < 3; i++) {
            prd += vect1[i]*vect2[i];
        }
        return prd;
    }

    public static double[] numberMult(double[] vect1, double a) {
        double[] prd = new double[3];
        for (int i = 0; i < 3; i++) {
            prd[i] = vect1[i]*a;
        }
        return prd;
    }

    public static double[] substrProduct(double[] vect1, double[] vect2) {
        double[] prd = new double[3];
        for (int i = 0; i < 3; i++) {
            prd[i] = vect1[i]-vect2[i];
        }
        return prd;
    }

    public static double[] reflectionE(double[] oldE, double[] n) {
        double[] newE = new double[3];
        double eDotN = dotProduct(oldE, n);
        for (int i = 0; i < 3; i ++) {
            newE[i] = oldE[i] - 2*eDotN*n[i];
        }
        return newE;
    }

    public static double[] refractionE(double[] oldE, double[] n, double n1, double n2) { //не как в методичке
        double[] newE = new double[3];
        double nn1, nn2;
        if (n1 > n2) {
            nn1 = n2;
            nn2 = n1;
        } else {
            nn1 = n1;
            nn2 = n2;
        }
        for (int i = 0; i < 3; i++) {
            //newE[i] = (nn1*oldE[i] - Math.signum(n2 - n1)*Math.signum(dotProduct(oldE, n))*n[i]*(nn1*Math.abs(dotProduct(oldE, n)) - nn2 * Math.sqrt(1 - (nn1*nn1/(nn2*nn2))*(1 - Math.pow(dotProduct(oldE, n), 2.0)))))/nn2;
            newE[i] = (nn1*oldE[i] - Math.signum(n2 - n1)*n[i]*nn1*dotProduct(oldE, n)*(1 - Math.sqrt((nn2*nn2 - nn1*nn1)/(Math.pow(dotProduct(oldE, n), 2.0)*nn1*nn1) + 1)))/nn2;
        }
        return newE;
    }
}
