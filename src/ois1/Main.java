package ois1;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ois1.surface.CrossingException;
import ois1.surface.Surface;
import ois1.surface.impl.Ellipsoid;
import ois1.surface.impl.Plane;
import ois1.surface.impl.Sphere;

import java.util.Scanner;

public class Main extends Application {
    private static Ray ray;
    private static double n1, n2;
    private static Surface surface;
    private static String fxmlResource;
    public static final double ERR = 1.E-13;

    public static Ray getRay() {
        return ray;
    }

    public static Surface getSurface() {
        return surface;
    }

    public static void main(String[] args) {
        System.out.println("Выберите поверхность:\n 1 - плоскость;\n 2 - сфера\n 3 - элипсоид");
        Scanner scanner = new Scanner(System.in);
        int surfaceNumber = scanner.nextInt();
        switch (surfaceNumber) {
            case 1:
                System.out.println("Уравнение плоскости: (n, (r - r0)) = 0");
                System.out.println("Введите вектор нормали n");
                double[] n = new double[3];
                n[0] = scanner.nextDouble();
                n[1] = scanner.nextDouble();
                n[2] = scanner.nextDouble();
                System.out.println("Введите вектор r0");
                double[] r0 = new double[3];
                r0[0] = scanner.nextDouble();
                r0[1] = scanner.nextDouble();
                r0[2] = scanner.nextDouble();
                surface = new Plane(n, r0);
                fxmlResource = "planeView.fxml";
                break;
            case 2:
                System.out.println("Уравнение сферы: (p - p0, p - p0) = R^2");
                System.out.println("Введите радиус-вектор центра сферы p0");
                double[] p0 = new double[3];
                p0[0] = scanner.nextDouble();
                p0[1] = scanner.nextDouble();
                p0[2] = scanner.nextDouble();
                System.out.println("Введите радиус сферы R");
                double rad = scanner.nextDouble();
                surface = new Sphere(p0, rad);
                fxmlResource = "sphereView.fxml";
                break;
            case 3:
                System.out.println("Уравнение элипсоида: (x - px)/a^2 + (y - py)/b^2 + (z - pz)/c^2 = 1");
                System.out.println("Введите радиус-вектор центра элипсоида p0");
                double[] pe0 = new double[3];
                pe0[0] = scanner.nextDouble();
                pe0[1] = scanner.nextDouble();
                pe0[2] = scanner.nextDouble();
                System.out.println("Введите a");
                double a = scanner.nextDouble();
                System.out.println("Введите b");
                double b = scanner.nextDouble();
                System.out.println("Введите c");
                double c = scanner.nextDouble();
                surface = new Ellipsoid(pe0, a, b, c);
                fxmlResource = "ellipsoidView.fxml";
                break;
            default:
                return;
        }
        System.out.println("Уравнение луча: r = r0 + e*t");
        System.out.println("Введите вектор r0");
        double[] r0 = new double[3];
        r0[0] = scanner.nextDouble();
        r0[1] = scanner.nextDouble();
        r0[2] = scanner.nextDouble();
        System.out.println("Введите вектор e");
        double[] e = new double[3];
        e[0] = scanner.nextDouble();
        e[1] = scanner.nextDouble();
        e[2] = scanner.nextDouble();
        ray = new Ray(r0, e);

        System.out.println("Выберите задание:\n 1 - точка пересечения;\n 2 - отражение луча\n 3 - преломление луча\n 4 - отрисовка луча");
        int task = scanner.nextInt();
        switch (task) {
            case 1:
                try {
                    double[] dot = surface.crossing(ray);
                    System.out.printf("Точка пересечения: [%.4f, %.4f, %.4f]\nt = %.4f\n", dot[0], dot[1], dot[2], dot[3]);
                    if (dot.length > 4) {
                        System.out.printf("Точка пересечения: [%.4f, %.4f, %.4f]\nt = %.4f\n", dot[4], dot[5], dot[6], dot[7]);
                    }
                } catch (CrossingException e1) {
                    System.out.println(e1.getMessage());
                    return;
                }
                break;
            case 2:
                try {
                    System.out.println(surface.reflection(ray).toString());
                } catch (CrossingException e1) {
                    System.out.println(e1.getMessage());
                    return;
                }
                break;
            case 3:
                System.out.println("Введите коэффициент преломления n1");
                n1 = scanner.nextDouble();
                System.out.println("Введите коэффициент преломления n2");
                n2 = scanner.nextDouble();
                try {
                    System.out.println(surface.refraction(ray, n1, n2).toString());
                } catch (CrossingException e1) {
                    System.out.println(e1.getMessage());
                    return;
                }
                break;
            case 4:
                System.out.println("Введите коэффициент преломления n1");
                n1 = scanner.nextDouble();
                System.out.println("Введите коэффициент преломления n2");
                n2 = scanner.nextDouble();
                launch(args);
        }
    }

    public static double getN1() {
        return n1;
    }

    public static double getN2() {
        return n2;
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource(fxmlResource));
        primaryStage.setTitle("Lab1");
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.show();
    }

}
