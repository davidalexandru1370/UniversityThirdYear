using System;

namespace rt
{
    public class Sphere : Geometry
    {
        private Vector Center { get; set; }
        private double Radius { get; set; }

        public Sphere(Vector center, double radius, Material material, Color color) : base(material, color)
        {
            Center = center;
            Radius = radius;
        }

        public override Intersection GetIntersection(Line line, double minDist, double maxDist)
        {
            // ADD CODE HERE: Calculate the intersection between the given line and this sphere

            double a = line.Dx * line.Dx;
            double b = 2 * (line.X0 * line.Dx) - (line.Dx * Center) * 2;
            double c = line.X0 * line.X0 + Center * Center - Radius * Radius - (line.X0 * Center) * 2;
            double delta = b * b - 4 * a * c;
            double epsilon = 0.0001;

            if (delta <= epsilon)
            {
                return new Intersection(false, false, this, line, 0);
            }

            var (t1, t2) = ComputeSolutionsForSecondDegreeEquation(delta, a, b);

            bool isT1Valid = t1 >= minDist && t2 <= maxDist;
            bool isT2Valid = t2 >= minDist && t2 <= maxDist;

            if(isT1Valid == false && isT2Valid == false)
            {
                return new Intersection(false, false, this, line, 0);
            }

            else if(isT1Valid == true && isT2Valid == false)
            {
                return new Intersection(true, true, this, line, t1);
            }

            else if(isT1Valid == false && isT2Valid == true)
            {
                return new Intersection(true, true, this, line, t2);
            }

            double t = Math.Min(t1, t2);

            return new Intersection(true, true, this, line, t);
        }

        private Tuple<double, double> ComputeSolutionsForSecondDegreeEquation(double delta, double a, double b)
        {
            double t1 = (-b - Math.Sqrt(delta)) / ((double)2.0 * a);
            double t2 = (-b + Math.Sqrt(delta)) / ((double)2.0 * a);

            return new Tuple<double, double>(t1, t2);

        }

        public override Vector Normal(Vector v)
        {
            var n = v - Center;
            n.Normalize();
            return n;
        }
    }
}