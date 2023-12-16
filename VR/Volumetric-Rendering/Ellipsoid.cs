using System;


namespace rt
{
    public class Ellipsoid : Geometry
    {
        private Vector Center { get; }
        private Vector SemiAxesLength { get; }
        private double Radius { get; }


        public Ellipsoid(Vector center, Vector semiAxesLength, double radius, Material material, Color color) : base(material, color)
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
            double semiAxesXSquared = SemiAxesLength.X * SemiAxesLength.X;
            double semiAxesYSquared = SemiAxesLength.Y * SemiAxesLength.Y;
            double semiAxesZSquared = SemiAxesLength.Z * SemiAxesLength.Z;

            //double a = line.Dx * line.Dx;
            double a = line.Dx.X * line.Dx.X / semiAxesXSquared +
                       line.Dx.Y * line.Dx.Y / semiAxesYSquared +
                       line.Dx.Z * line.Dx.Z / semiAxesZSquared;

            //double b = 2 * ((line.X0 * line.Dx) - (line.Dx * Center));
            double b = 2 *
                ((line.Dx.X * (line.X0.X - Center.X)) / semiAxesXSquared +
                (line.Dx.Y * (line.X0.Y - Center.Y)) / semiAxesYSquared +
                (line.Dx.Z * (line.X0.Z - Center.Z)) / semiAxesZSquared);

            //double c = (line.X0 * line.X0 + Center * Center)  - Radius * Radius - (line.X0 * Center) * 2;
            double c = ((line.X0.X * line.X0.X) + (Center.X * Center.X)) / semiAxesXSquared +
                       ((line.X0.Y * line.X0.Y) + (Center.Y * Center.Y)) / semiAxesYSquared +
                       ((line.X0.Z * line.X0.Z) + (Center.Z * Center.Z)) / semiAxesZSquared +
                        -2 * (
                        line.X0.X * Center.X / semiAxesXSquared +
                        line.X0.Y * Center.Y / semiAxesYSquared +
                        line.X0.Z * Center.Z / semiAxesZSquared
                        )
                       - Radius * Radius;

            double delta = b * b - 4.0f * a * c;
            double epsilon = 0.0001;
            Vector intersectionPoint;

            Vector normal = new();

            if (delta <= epsilon)
            {
                normal = new Vector(0, 0, 0);
                return new Intersection(false, false, this, line, 0, null, Material, Color);
            }

            var (t1, t2) = ComputeSolutionsForSecondDegreeEquation(delta, a, b);

            bool isT1Valid = t1 >= minDist && t2 <= maxDist;
            bool isT2Valid = t2 >= minDist && t2 <= maxDist;


            if (isT1Valid == false && isT2Valid == false)
            {
                return new Intersection(false, false, this, line, 0, null, Material, Color);
            }

            else if (isT1Valid == true && isT2Valid == false)
            {
                intersectionPoint = line.X0 + line.Dx * t1;
                normal = Normal(intersectionPoint);
                return new Intersection(true, true, this, line, t1, normal, Material, Color);
            }
            else if (isT1Valid == false && isT2Valid == true)
            {
                intersectionPoint = line.X0 + line.Dx * t2;
                normal = Normal(intersectionPoint);
                return new Intersection(true, true, this, line, t2, normal, Material, Color);
            }

            double distanceFactorToShortestPoint = Math.Min(t1, t2);
            intersectionPoint = line.X0 + line.Dx * distanceFactorToShortestPoint;
            normal = Normal(intersectionPoint);
            return new Intersection(true, true, this, line, distanceFactorToShortestPoint, normal, Material, Color);
        }

        private Tuple<double, double> ComputeSolutionsForSecondDegreeEquation(double delta, double a, double b)
        {
            double t1 = (-b - Math.Sqrt(delta)) / ((double)2.0 * a);
            double t2 = (-b + Math.Sqrt(delta)) / ((double)2.0 * a);

            return new Tuple<double, double>(t1, t2);
        }

        public Vector Normal(Vector position)
        {
            return new Vector(2 * (position.X - Center.X) / (SemiAxesLength.X * SemiAxesLength.X),
               2 * (position.Y - Center.Y) / (SemiAxesLength.Y * SemiAxesLength.Y),
               2 * (position.Z - Center.Z) / (SemiAxesLength.Z * SemiAxesLength.Z)).Normalize();
        }

        public List<Intersection> GetAllIntersections(Line line)
        {
            List<Intersection> intersections = new();
            double semiAxesXSquared = SemiAxesLength.X * SemiAxesLength.X;
            double semiAxesYSquared = SemiAxesLength.Y * SemiAxesLength.Y;
            double semiAxesZSquared = SemiAxesLength.Z * SemiAxesLength.Z;

            //double a = line.Dx * line.Dx;
            double a = line.Dx.X * line.Dx.X / semiAxesXSquared +
                       line.Dx.Y * line.Dx.Y / semiAxesYSquared +
                       line.Dx.Z * line.Dx.Z / semiAxesZSquared;

            //double b = 2 * ((line.X0 * line.Dx) - (line.Dx * Center));
            double b = 2 *
                ((line.Dx.X * (line.X0.X - Center.X)) / semiAxesXSquared +
                (line.Dx.Y * (line.X0.Y - Center.Y)) / semiAxesYSquared +
                (line.Dx.Z * (line.X0.Z - Center.Z)) / semiAxesZSquared);

            //double c = (line.X0 * line.X0 + Center * Center)  - Radius * Radius - (line.X0 * Center) * 2;
            double c = ((line.X0.X * line.X0.X) + (Center.X * Center.X)) / semiAxesXSquared +
                       ((line.X0.Y * line.X0.Y) + (Center.Y * Center.Y)) / semiAxesYSquared +
                       ((line.X0.Z * line.X0.Z) + (Center.Z * Center.Z)) / semiAxesZSquared +
                        -2 * (
                        line.X0.X * Center.X / semiAxesXSquared +
                        line.X0.Y * Center.Y / semiAxesYSquared +
                        line.X0.Z * Center.Z / semiAxesZSquared
                        )
                       - Radius * Radius;

            double delta = b * b - 4.0f * a * c;
            double epsilon = 0.0001;
            Vector intersectionPoint;

            Vector normal = new();

            if (delta <= epsilon)
            {
                //intersections.Add(new Intersection(false, false, this, line, 0, null, Material, Color));
                return intersections;
            }

            var (t1, t2) = ComputeSolutionsForSecondDegreeEquation(delta, a, b);

            intersectionPoint = line.X0 + line.Dx * t1;
            normal = Normal(intersectionPoint);
            intersections.Add(new Intersection(true, true, this, line, t1, normal, Material, Color));

            intersectionPoint = line.X0 + line.Dx * t2;
            normal = Normal(intersectionPoint);

            intersections.Add(new Intersection(true, true, this, line, t2, normal, Material, Color));

            return intersections;
        }
    }
}
