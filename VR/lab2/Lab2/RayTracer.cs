using System;
using System.Runtime.InteropServices;

namespace rt
{
    class RayTracer
    {
        private Geometry[] _geometries;
        private Light[] _lights;

        public RayTracer(Geometry[] geometries, Light[] lights)
        {
            this._geometries = geometries;
            this._lights = lights;
        }

        private double ImageToViewPlane(int n, int imgSize, double viewPlaneSize)
        {
            return -n * viewPlaneSize / imgSize + viewPlaneSize / 2;
        }

        private Intersection FindFirstIntersection(Line ray, double minDist, double maxDist)
        {
            var intersection = new Intersection();

            foreach (var geometry in _geometries)
            {
                var intr = geometry.GetIntersection(ray, minDist, maxDist);

                if (!intr.Valid || !intr.Visible) continue;

                if (!intersection.Valid || !intersection.Visible)
                {
                    intersection = intr;
                }
                else if (intr.T < intersection.T)
                {
                    intersection = intr;
                }
            }

            return intersection;
        }

        private bool IsLit(Vector point, Light light)
        {
            // TODO: ADD CODE HERE

            Line lineStartingFromLightPoint = new Line(light.Position, point);
            Intersection intersection = FindFirstIntersection(lineStartingFromLightPoint, 0, 1000000);
            
            return intersection.T > (light.Position - point).Length();
        }

        public void Render(Camera camera, int width, int height, string filename)
        {
            var background = new Color(0.2, 0.2, 0.2, 1.0);

            var image = new Image(width, height);

            for (var i = 0; i < width; i++)
            {
                for (var j = 0; j < height; j++)
                {
                    // TODO: ADD CODE HERE
                    var pointOnView = camera.Position + camera.Direction * camera.ViewPlaneDistance +
                                     (camera.Up ^ camera.Direction) * ImageToViewPlane(i, width, camera.ViewPlaneWidth) +
                                     camera.Up * ImageToViewPlane(j, height, camera.ViewPlaneHeight);
                    var ray = new Line(camera.Position, pointOnView);
                    var intersection = FindFirstIntersection(ray, camera.FrontPlaneDistance, camera.BackPlaneDistance);
                    if (intersection.Valid && intersection.Visible)
                    {
                        var color = new Color();
                        var lightningColor = new Color();
                        foreach (var light in _lights)
                        {
                            lightningColor += intersection.Geometry.Material.Ambient * light.Ambient;

                            if (IsLit(intersection.Position, light))
                            {

                            }
                        }

                        color += lightningColor;
                        image.SetPixel(i, j, color);
                    }
                    else
                    {
                        image.SetPixel(i, j, background);
                    }
                }
            }

            image.Store(filename);
        }
    }
}