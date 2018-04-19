package ois1.surface;

import ois1.Ray;

public interface Surface {
    double[] crossing(Ray ray) throws CrossingException;
    Ray reflection(Ray ray) throws CrossingException;
    Ray refraction(Ray ray, double n1, double n2) throws CrossingException;
}
