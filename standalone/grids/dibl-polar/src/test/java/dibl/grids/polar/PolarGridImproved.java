package dibl.grids.polar;

public class PolarGridImproved extends PolarGrid
{

    public PolarGridImproved(int angleOnFootside, int dotsPerCircle, double diameter, double minDiameter, String fillColor, double dotSize)
    {
        super(angleOnFootside, dotsPerCircle, diameter, minDiameter, fillColor, dotSize);
    }

    @Override
    double delta(double diameter, int angleOnFootside, double dotsPerCircle)
    {
        double angle = Math.toRadians(angleOnFootside);
        double alpha = Math.toRadians(360D / dotsPerCircle);
        double correction = alpha / (360.0 / angleOnFootside);
        return Math.tan(angle - correction) * (diameter * Math.PI / dotsPerCircle);
    }
}