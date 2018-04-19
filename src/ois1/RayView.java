package ois1;

import javafx.event.ActionEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.*;
import ois1.surface.CrossingException;
import ois1.surface.Surface;
import ois1.surface.impl.Ellipsoid;
import ois1.surface.impl.Plane;
import ois1.surface.impl.Sphere;

public class RayView {
    public Line linePlane;
    public AnchorPane anchor;
    public Line lineRay1;
    public Line lineReflected;
    public Line lineRefracted;
    public Circle lineSphere;
    public Line lineRefracted2;
    public Ellipse lineEllipsoid;
    public Box BoxPlane;

    public void drawPlane() {
        Plane plane = (Plane) Main.getSurface();
        Ray ray1 = Main.getRay();
        double n1 = Main.getN1();
        double n2 = Main.getN2();
        /*BoxPlane.setVisible(true);
        BoxPlane.setLayoutX(anchor.getPrefWidth()/2);
        BoxPlane.setLayoutY(anchor.getPrefHeight()/2);
        BoxPlane.setRotate(90 - Math.acos(plane.getN()[0])*180/Math.PI);
        BoxPlane.setRotate(90 - Math.acos(plane.getN()[2])*180/Math.PI);*/
        try {
            linePlane.setVisible(true);
            linePlane.setLayoutX(anchor.getPrefWidth() / 2);
            linePlane.setLayoutY(anchor.getPrefHeight() / 2);
            linePlane.setRotate(90 - Math.acos(plane.getN()[0]) * 180 / Math.PI);
            Ray rayReflected = plane.reflection(ray1);
            Ray rayRefracted = plane.refraction(ray1, n1, n2);
            lineRay1.setLayoutX(anchor.getPrefWidth() / 2);
            lineRay1.setLayoutY(anchor.getPrefHeight() / 2);
            lineRay1.setStartX(-ray1.getE()[0] * 300);
            lineRay1.setStartY(ray1.getE()[1] * 300);
            lineRay1.setEndX(0);
            lineRay1.setEndY(0);
            lineRay1.setVisible(true);
            lineReflected.setLayoutX(anchor.getPrefWidth() / 2);
            lineReflected.setLayoutY(anchor.getPrefHeight() / 2);
            lineReflected.setStartX(rayReflected.getE()[0] * 300);
            lineReflected.setStartY(-rayReflected.getE()[1] * 300);
            lineReflected.setEndX(0);
            lineReflected.setEndY(0);
            lineReflected.setVisible(true);
            lineRefracted.setLayoutX(anchor.getPrefWidth() / 2);
            lineRefracted.setLayoutY(anchor.getPrefHeight() / 2);
            lineRefracted.setStartX(rayRefracted.getE()[0] * 300);
            lineRefracted.setStartY(-rayRefracted.getE()[1] * 300);
            lineRefracted.setEndX(0);
            lineRefracted.setEndY(0);
            lineRefracted.setVisible(true);
        } catch (CrossingException e ){
            System.out.println(e.getMessage());
        }
    }

    private void drawTracing(Surface surface, double leftBorder, double bottomBorder, double viewC, double n1, double n2){

        Ray ray = Main.getRay();
        try {
            double[] crossDot = surface.crossing(ray);
            double xPix = (crossDot[0] - leftBorder) * viewC;
            double yPix = anchor.getPrefHeight() - (crossDot[1] - bottomBorder) * viewC;
            lineRay1.setLayoutX(xPix);
            lineRay1.setLayoutY(yPix);
            lineRay1.setEndX(0);
            lineRay1.setEndY(0);
            lineRay1.setStartX(-ray.getE()[0] * crossDot[3] * viewC);
            lineRay1.setStartY(ray.getE()[1] * crossDot[3] * viewC);
            lineRay1.setVisible(true);
            Ray reflectedRay = surface.reflection(ray);
            double t = 300 / viewC;
            try {
                double crossDot2[] = surface.crossing(reflectedRay);
                if (crossDot2[3] < Main.ERR && crossDot2.length > 4)
                    t = crossDot2[7];
                if (crossDot2[3] > Main.ERR && crossDot2.length <= 4)
                    t = crossDot2[3];
            } catch (CrossingException e) {
                //ignored;
            }
            lineReflected.setLayoutX(xPix);
            lineReflected.setLayoutY(yPix);
            lineReflected.setEndX(0);
            lineReflected.setEndY(0);
            lineReflected.setStartX(reflectedRay.getE()[0] * t * viewC);
            lineReflected.setStartY(-reflectedRay.getE()[1] * t * viewC);
            lineReflected.setVisible(true);
            Ray refractedRay1 = surface.refraction(ray, n1, n2);
            try {
                double crossDot2[] = surface.crossing(refractedRay1);
                if (crossDot2[3] < Main.ERR && crossDot2.length > 4) {
                    crossDot2[0] = crossDot2[4];
                    crossDot2[1] = crossDot2[5];
                    crossDot2[2] = crossDot2[6];
                    crossDot2[3] = crossDot2[7];
                } else if (crossDot2[3] < Main.ERR) throw new CrossingException("не пересекает");
                t = crossDot2[3];
                double xPix2 = (crossDot2[0] - leftBorder) * viewC;
                double yPix2 = anchor.getPrefHeight() - (crossDot2[1] - bottomBorder) * viewC;
                Ray refractedRay2 = surface.refraction(refractedRay1, n2, n1);
                lineRefracted2.setLayoutX(xPix2);
                lineRefracted2.setLayoutY(yPix2);
                lineRefracted2.setEndX(0);
                lineRefracted2.setEndY(0);
                lineRefracted2.setStartX(refractedRay2.getE()[0] * 300);
                lineRefracted2.setStartY(-refractedRay2.getE()[1] * 300);
                lineRefracted2.setVisible(true);
            } catch (CrossingException e) {//ignored
                t = 300 / viewC;
            } finally {
                lineRefracted.setLayoutX(xPix);
                lineRefracted.setLayoutY(yPix);
                lineRefracted.setEndX(0);
                lineRefracted.setEndY(0);
                lineRefracted.setStartX(refractedRay1.getE()[0] * t * viewC);
                lineRefracted.setStartY(-refractedRay1.getE()[1] * t * viewC);
                lineRefracted.setVisible(true);
            }
        } catch (CrossingException e) {
            System.out.println(e.getMessage());
        }
    }

    public void drawEllipsoid(ActionEvent actionEvent) {
        Ellipsoid ellipsoid = (Ellipsoid) Main.getSurface();
        double n1 = Main.getN1();
        double n2 = Main.getN2();
        lineEllipsoid.setCenterX(anchor.getPrefWidth()/2);
        lineEllipsoid.setCenterY(anchor.getPrefHeight()/2);
        lineEllipsoid.setLayoutX(0);
        lineEllipsoid.setLayoutY(0);
        lineEllipsoid.setVisible(true);
        double viewC;
        if(ellipsoid.getA() > ellipsoid.getB()) {
            viewC = anchor.getPrefHeight()/(ellipsoid.getA()*4);
        } else {
            viewC = anchor.getPrefWidth()/(ellipsoid.getB()*4);
        }
        double leftBorder = ellipsoid.getP0()[0] - anchor.getPrefWidth()/(viewC*2);
        double bottomBorder = ellipsoid.getP0()[1] - anchor.getPrefHeight()/(viewC*2);
        lineEllipsoid.setRadiusX(ellipsoid.getA()*viewC);
        lineEllipsoid.setRadiusY(ellipsoid.getB()*viewC);
        drawTracing(ellipsoid, leftBorder, bottomBorder, viewC, n1, n2);
    }

    public void drawSphere(ActionEvent actionEvent) {
        Sphere sphere = (Sphere) Main.getSurface();
        double n1 = Main.getN1();
        double n2 = Main.getN2();
        lineSphere.setCenterX(anchor.getPrefWidth()/2);
        lineSphere.setCenterY(anchor.getPrefHeight()/2);
        lineSphere.setLayoutX(0);
        lineSphere.setLayoutY(0);
        lineSphere.setRadius(anchor.getPrefHeight()/4);
        lineSphere.setVisible(true);
        double viewC = anchor.getPrefHeight()/(sphere.getRad()*4);
        double leftBorder = sphere.getP0()[0] - 3*sphere.getRad();
        double bottomBorder = sphere.getP0()[1] - 2*sphere.getRad();
        drawTracing(sphere, leftBorder, bottomBorder, viewC, n1, n2);
    }
}
