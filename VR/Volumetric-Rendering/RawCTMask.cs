using System.Text.RegularExpressions;

namespace rt;

public class RawCtMask : Geometry
{
    private readonly Vector _position;
    private readonly double _scale;
    private readonly ColorMap _colorMap;
    private readonly byte[] _data;

    private readonly int[] _resolution = new int[3];
    private readonly double[] _thickness = new double[3];
    //cubul este definit de v0 v1
    private readonly Vector _v0;
    private readonly Vector _v1;

    public RawCtMask(string datFile, string rawFile, Vector position, double scale, ColorMap colorMap) : base(Color.NONE)
    {
        _position = position;
        _scale = scale;
        _colorMap = colorMap;

        var lines = File.ReadLines(datFile);
        foreach (var line in lines)
        {
            var kv = Regex.Replace(line, "[:\\t ]+", ":").Split(":");
            if (kv[0] == "Resolution")
            {
                _resolution[0] = Convert.ToInt32(kv[1]);
                _resolution[1] = Convert.ToInt32(kv[2]);
                _resolution[2] = Convert.ToInt32(kv[3]);
            }
            else if (kv[0] == "SliceThickness")
            {
                _thickness[0] = Convert.ToDouble(kv[1]);
                _thickness[1] = Convert.ToDouble(kv[2]);
                _thickness[2] = Convert.ToDouble(kv[3]);
            }
        }

        _v0 = position;
        _v1 = position + new Vector(_resolution[0] * _thickness[0] * scale, _resolution[1] * _thickness[1] * scale, _resolution[2] * _thickness[2] * scale);

        var len = _resolution[0] * _resolution[1] * _resolution[2];
        _data = new byte[len];
        using FileStream f = new FileStream(rawFile, FileMode.Open, FileAccess.Read);
        if (f.Read(_data, 0, len) != len)
        {
            throw new InvalidDataException($"Failed to read the {len}-byte raw data");
        }
    }

    private ushort Value(int x, int y, int z)
    {
        if (x < 0 || y < 0 || z < 0 || x >= _resolution[0] || y >= _resolution[1] || z >= _resolution[2])
        {
            return 0;
        }

        return _data[z * _resolution[1] * _resolution[0] + y * _resolution[0] + x];
    }

    public override Intersection GetIntersection(Line line, double minDist, double maxDist)
    {
        // ADD CODE HERE
        //double maxSamplingDistance = 1000;
        double step = _thickness[0] * _scale;
        double currentFactor = minDist;

        double transparency = 1.0f;

        var facesIntersected = GetBoundingBox(line);
        
        if(facesIntersected.All(i => i.Visible == false))
        {
            return Intersection.NONE;
        }

        var entryBoundingBox = facesIntersected.Where(i => i.Visible == true).Min(i => i.T);
        var exitBoundingBox = facesIntersected.Where(i => i.Visible == true).Max(i => i.T);

        var start = entryBoundingBox;
        var stop = exitBoundingBox;

        while (start < stop)
        {
            var position = line.CoordinateToPosition(stop);
            var indexes = GetIndexes(position);
            var value = Value(indexes[0], indexes[1], indexes[2]);

            if (value > 0)
            {
                var color = GetColor(position);
                color += color * transparency * color.Alpha;
                return new Intersection(true, true, this, line, currentFactor, GetNormal(position), Material, color);
            }

            start += step;
        }

        return Intersection.NONE;
    }

    private List<Intersection> GetBoundingBox(Line line)
    {
        List<Intersection> intersections = new();

        var t0x = (_v0.X - line.X0.X) / (line.Dx.X);
        var t1x = (_v1.X - line.X0.X) / (line.Dx.X);
        var t0y = (_v0.Y - line.X0.Y) / (line.Dx.Y);
        var t1y = (_v1.Y - line.X0.Y) / (line.Dx.Y);
        var t0z = (_v0.Z - line.X0.Z) / (line.Dx.Z);
        var t1z = (_v1.Z - line.X0.Z) / (line.Dx.Z);

        var tmin = t0x > t0y ? t0x : t0y;
        var tmax = t1x < t1y ? t1x : t1y;

        if (t0x > t1y)
        {
            intersections.Add(Intersection.NONE);
            intersections.Add(Intersection.NONE);
        }
        else
        {
            intersections.Add(new Intersection(true, true, this, line, t0x, GetNormal(line.CoordinateToPosition(t0x)), Material, Color.NONE));
            intersections.Add(new Intersection(true, true, this, line, t1y, GetNormal(line.CoordinateToPosition(t1y)), Material, Color.NONE));
        }

        if (t0y > t1x)
        {
            intersections.Add(Intersection.NONE);
            intersections.Add(Intersection.NONE);
        }
        else
        {
            intersections.Add(new Intersection(true, true, this, line, t0x, GetNormal(line.CoordinateToPosition(t0y)), Material, Color.NONE));
            intersections.Add(new Intersection(true, true, this, line, t1y, GetNormal(line.CoordinateToPosition(t1x)), Material, Color.NONE));
        }

        if (tmin > t1z)
        {
            intersections.Add(Intersection.NONE);
            intersections.Add(Intersection.NONE);
        }
        else
        {
            intersections.Add(new Intersection(true, true, this, line, t1y, GetNormal(line.CoordinateToPosition(t1z)), Material, Color.NONE));
        }

        if (t0z > tmax)
        {
            intersections.Add(Intersection.NONE);
            intersections.Add(Intersection.NONE);
        }
        else
        {
            intersections.Add(new Intersection(true, true, this, line, t0z, GetNormal(line.CoordinateToPosition(t0z)), Material, Color.NONE));
        }

        return intersections;
    }

    private int[] GetIndexes(Vector v)
    {
        return new[]{
            (int)Math.Floor((v.X - _position.X) / _thickness[0] / _scale),
            (int)Math.Floor((v.Y - _position.Y) / _thickness[1] / _scale),
            (int)Math.Floor((v.Z - _position.Z) / _thickness[2] / _scale)};
    }

    private Color GetColor(Vector v)
    {
        int[] idx = GetIndexes(v);

        ushort value = Value(idx[0], idx[1], idx[2]);
        return _colorMap.GetColor(value);
    }

    private Vector GetNormal(Vector v)
    {
        int[] idx = GetIndexes(v);
        double x0 = Value(idx[0] - 1, idx[1], idx[2]);
        double x1 = Value(idx[0] + 1, idx[1], idx[2]);
        double y0 = Value(idx[0], idx[1] - 1, idx[2]);
        double y1 = Value(idx[0], idx[1] + 1, idx[2]);
        double z0 = Value(idx[0], idx[1], idx[2] - 1);
        double z1 = Value(idx[0], idx[1], idx[2] + 1);

        return new Vector(x0 - x1, y0 - y1, z0 - z1).Normalize();
    }
}