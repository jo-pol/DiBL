package dibl.grids.polar;

public class PolarGridInscribedPolygon extends PolarGrid
{

    public PolarGridInscribedPolygon(int angleOnFootside, int dotsPerCircle, double diameter, double minDiameter, String fillColor, double dotSize)
    {
        super(angleOnFootside, dotsPerCircle, diameter, minDiameter, fillColor, dotSize);
    }

    @Override
    double delta(double diameter, int angleOnFootside, double dotsPerCircle)
    {

        double cos = Math.cos(Math.toRadians(360.0 / (2.0 * dotsPerCircle)));
        double tan = Math.tan(Math.toRadians(angleOnFootside));
        double triHeight = Math.sqrt(2.0 - 2.0 * Math.cos(Math.toRadians(360.0 / dotsPerCircle))) / 2.0;
        double tri = diameter * triHeight;
        return diameter * (cos - 1.0) + tri * tan;
    }
}
