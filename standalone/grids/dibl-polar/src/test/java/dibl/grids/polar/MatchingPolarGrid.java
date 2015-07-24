package dibl.grids.polar;

public class MatchingPolarGrid extends PolarGrid
{

    public MatchingPolarGrid(int angleOnFootside, int dotsPerCircle, double diameter, double minDiameter, String fillColor, double dotSize)
    {
        super(angleOnFootside, dotsPerCircle, diameter, minDiameter, fillColor, dotSize);
    }

    @Override
    double delta(double diameter, int angleOnFootside, double dotsPerCircle)
    {
        double angle = Math.toRadians(angleOnFootside);
        double correction = Math.PI / (4 * dotsPerCircle);
        correction *= (Math.tan(angle*0.93));
        return Math.tan(angle-correction) * (diameter * Math.PI / dotsPerCircle);
    }
}
