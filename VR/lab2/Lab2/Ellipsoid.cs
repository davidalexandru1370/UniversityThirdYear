using Microsoft.VisualBasic;
using System;


namespace rt
{
    public class Ellipsoid : Geometry
    {
        private Vector Center { get; }
        private Vector SemiAxesLength { get; }
        private double Radius { get; }

        public Ellipsoid(Vector center, Vector semiAxesLength, double radius, Material material, Color color) : base(
            material, color)
        {
            Center = center;
            SemiAxesLength = semiAxesLength;
            Radius = radius;
        }

        public Ellipsoid(Vector center, Vector semiAxesLength, double radius, Color color) : base(color)
        {
            Center = center;
            SemiAxesLength = semiAxesLength;
            Radius = radius;
        }

        public override Intersection GetIntersection(Line line, double minDist, double maxDist)
        {
            // TODO: ADD CODE HERE

            //double a = line.Dx * line.Dx;
            double a = line.Dx.X * line.Dx.X / (SemiAxesLength.X * SemiAxesLength.X) +
                       line.Dx.Y * line.Dx.Y / (SemiAxesLength.Y * SemiAxesLength.X) +
                       line.Dx.Z * line.Dx.Z / (SemiAxesLength.Z * SemiAxesLength.Z);

            //double b = 2 * ((line.X0 * line.Dx) - (line.Dx * Center));
            double b = 
                2 * line.Dx.X * (line.X0.X - Center.X) / (SemiAxesLength.X * SemiAxesLength.X) +
                2 * line.Dx.Y * (line.X0.Y - Center.Y) / (SemiAxesLength.Y * SemiAxesLength.Y) +
                2 * line.Dx.Z * (line.X0.Z - Center.Z) / (SemiAxesLength.Z * SemiAxesLength.Z)
            ;

            //double c = (line.X0 * line.X0 + Center * Center)  - Radius * Radius - (line.X0 * Center) * 2;
            double c = (line.X0.X - Center.X) * (line.X0.X - Center.X) / (SemiAxesLength.X * SemiAxesLength.X) +
                       (line.X0.Y - Center.Y) * (line.X0.Y - Center.Y) / (SemiAxesLength.Y * SemiAxesLength.Y) +
                       (line.X0.Z - Center.Z) * (line.X0.Z - Center.Z) / (SemiAxesLength.Z * SemiAxesLength.Z) +
                       - Radius * Radius;

            double delta = b * b - 4.0f * a * c;
            double epsilon = 0.0001;

            Intersection intersection = new();
            Vector normal = new();

            if (delta <= epsilon)
            {
                normal = new Vector(0, 0, 0);
                return new Intersection(false, false, this, line, 0, normal);
            }

            var (t1, t2) = ComputeSolutionsForSecondDegreeEquation(delta, a, b);

            bool isT1Valid = t1 >= minDist && t2 <= maxDist;
            bool isT2Valid = t2 >= minDist && t2 <= maxDist;


            if (isT1Valid == false && isT2Valid == false)
            {
                return new Intersection(false, false, this, line, 0, new Vector());
            }

            else if (isT1Valid == true && isT2Valid == false)
            {
                intersection = new Intersection(true, true, this, line, t1);
                normal = ComputeNormalForEllipsoidIntersectionPoint(intersection.Position);
                intersection.Normal = normal;

                return intersection;
            }
            else if (isT1Valid == false && isT2Valid == true)
            {
                intersection = new Intersection(true, true, this, line, t2);
                normal = ComputeNormalForEllipsoidIntersectionPoint(intersection.Position);
                intersection.Normal = normal;

                return intersection;
            }

            double distanceFactorToShortestPoint = Math.Min(t1, t2);
            intersection = new Intersection(true, true, this, line, distanceFactorToShortestPoint);
            normal = ComputeNormalForEllipsoidIntersectionPoint(intersection.Position);
            intersection.Normal = normal;

            return intersection;
        }

        private Vector ComputeNormalForEllipsoidIntersectionPoint(Vector position)
        {
            return new Vector(2 * position.X / SemiAxesLength.X * SemiAxesLength.X,
                2 * position.Y / SemiAxesLength.Y * SemiAxesLength.Y,
                2 * position.Z / SemiAxesLength.Z * SemiAxesLength.Z);
        }

        private Tuple<double, double> ComputeSolutionsForSecondDegreeEquation(double delta, double a, double b)
        {
            double t1 = (-b - Math.Sqrt(delta)) / ((double)2.0 * a);
            double t2 = (-b + Math.Sqrt(delta)) / ((double)2.0 * a);

            return new Tuple<double, double>(t1, t2);
        }
    }
}