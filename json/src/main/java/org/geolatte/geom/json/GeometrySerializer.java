package org.geolatte.geom.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.geolatte.geom.Geometry;
import org.geolatte.geom.GeometryType;
import org.geolatte.geom.Position;
import org.geolatte.geom.PositionSequence;
import org.geolatte.geom.crs.CoordinateReferenceSystem;

import java.io.IOException;

import static org.geolatte.geom.GeometryType.LINESTRING;
import static org.geolatte.geom.GeometryType.POINT;

/**
 * Created by Karel Maesen, Geovise BVBA on 09/09/17.
 */
public class GeometrySerializer<P extends Position> extends JsonSerializer<Geometry<P>> {

    final private Context<P> context;
    public GeometrySerializer(Context<P> context) {
        this.context = context;
    }

    /**
     * Method that can be called to ask implementation to serialize
     * values of type this serializer handles.
     *
     * @param geometry    Geometry value to serialize; can <b>not</b> be null.
     * @param gen         Generator used to output resulting Json content
     * @param serializers Provider that can be used to get serializers for
     */
    @Override
    public void serialize(Geometry<P> geometry, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartObject();
        gen.writeStringField("type", geometry.getGeometryType().getCamelCased());
        writeCrs(gen, geometry.getCoordinateReferenceSystem());
        writeCoords(gen, geometry.getGeometryType(), geometry.getPositions());
        gen.writeEndObject();

    }

    private void writeCoords(JsonGenerator gen, GeometryType type, PositionSequence<P> positions) throws IOException {
        gen.writeFieldName("coordinates");
        double[] buf = new double[positions.getCoordinateDimension()];
        if (positions.isEmpty()) {
            gen.writeStartArray();
            gen.writeEndArray();
            return;
        }
        if (type == POINT) {
            writePosition(gen, positions.getPositionN(0), buf);
        }
        if (type == LINESTRING) {
            gen.writeStartArray();

            for (P pos : positions) {
                writePosition(gen, pos, buf);
            }
            gen.writeEndArray();
        }
    }


    private void writePosition(JsonGenerator gen, P position, double[] buf) throws IOException {
        gen.writeArray(position.toArray(buf), 0, buf.length);
    }

    private void writeCrs(JsonGenerator gen, CoordinateReferenceSystem<P> crs) throws IOException {
        if (context.isFeatureSet(Feature.SUPPRESS_CRS_SERIALIZATION)) {
            return;
        }
        gen.writeFieldName("crs");
        writeNamedCrs(gen, crs);
    }

    private void writeNamedCrs(JsonGenerator gen, CoordinateReferenceSystem<P> crs) throws IOException {
        gen.writeStartObject();
        gen.writeStringField("type", "name");
        gen.writeFieldName("properties");
        writeCrsName(gen, crs.getCrsId().toString());
        gen.writeEndObject();
    }

    private void writeCrsName(JsonGenerator gen, String epsgString) throws IOException {
        gen.writeStartObject();
        gen.writeStringField("name", epsgString);
        gen.writeEndObject();

    }

}