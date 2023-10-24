using System;

namespace rt
{
    public class Sphere : Ellipsoid
    {
        public Sphere(Vector center, double radius, Material material, Color color) : base(center, new Vector(1, 1, 1), radius, material, color)
        {
        }
        public Sphere(Vector center, double radius, Color color) : base(center, new Vector(1, 1, 1), radius, color)
        {
        }

        public override Vector Normal(Vector position)
        {
            var n = position - this.Center;
            n.Normalize();
            return n;
        }
    }
}