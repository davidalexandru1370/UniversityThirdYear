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
            var u = n * viewPlaneSize / imgSize;
            u -= viewPlaneSize / 2;
            return u;
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
            // ADD CODE HERE: Detect whether the given point has a clear line of sight to the given light

            Line lineStartingFromLightPoint = new Line(light.Position, point);
            Intersection intersection = FindFirstIntersection(lineStartingFromLightPoint, 0, 1000000);

            double epsilon = 0.0001;

            return intersection.T > (light.Position - point).Length() - epsilon;

        }

        public void Render(Camera camera, int width, int height, string filename)
        {
            var background = new Color();
            var viewParallel = (camera.Up ^ camera.Direction).Normalize();

            var image = new Image(width, height);

            var vecW = camera.Direction * camera.ViewPlaneDistance;
            for (var i = 0; i < width; i++)
            {
                for (var j = 0; j < height; j++)
                {
                    // ADD CODE HERE: Implement pixel color calculation
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
                                var normal = (intersection.Geometry as Sphere).Normal(intersection.Position);
                                var cameraVector = (camera.Position - intersection.Position).Normalize();
                                var vectorFromLight = (light.Position - intersection.Position).Normalize();
                                var reflectionVector = (normal * (normal * vectorFromLight) * 2 - vectorFromLight).Normalize();

                                if(normal * vectorFromLight > 0)
                                {
                                    lightningColor += intersection.Geometry.Material.Diffuse * light.Diffuse * (normal * vectorFromLight);
                                }

                                if(cameraVector * reflectionVector > 0)
                                {
                                    lightningColor += intersection.Geometry.Material.Specular * light.Specular *
                                                      Math.Pow(cameraVector * reflectionVector, intersection.Geometry.Material.Shininess);
                                }

                                lightningColor *= light.Intensity;
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