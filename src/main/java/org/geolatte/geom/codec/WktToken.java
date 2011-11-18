/*
 * This file is part of the GeoLatte project.
 *
 *     GeoLatte is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Lesser General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     GeoLatte is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Lesser General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public License
 *     along with GeoLatte.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright (C) 2010 - 2011 and Ownership of code is shared by:
 * Qmino bvba - Romeinsestraat 18 - 3001 Heverlee  (http://www.qmino.com)
 * Geovise bvba - Generaal Eisenhowerlei 9 - 2140 Antwerpen (http://www.geovise.com)
 */

package org.geolatte.geom.codec;

import org.geolatte.geom.DimensionalFlag;
import org.geolatte.geom.GeometryType;

/**
 * A token in a WKT representation.
 *
 * @author Karel Maesen, Geovise BVBA, 2011
 */
abstract class WktToken {

    private static final WktToken END = new End();
    private static final WktToken START_LIST = new StartList();
    private static final WktToken END_LIST = new EndList();
    private static final WktToken EMPTY = new Empty();
    private static final WktToken ELEMENT_SEP = new ElementSeparator();

    /**
     * Returns the WKT token that identifies the specified geometry type.
     *
     * @param type
     * @param measured
     * @return
     */
    static WktToken geometryTag(GeometryType type, boolean measured) {
        return new Geometry(type, measured);
    }

    static WktToken startList() {
        return START_LIST;
    }

    static WktToken endList() {
        return END_LIST;
    }

    static WktToken pointSquence(org.geolatte.geom.PointSequence sequence) {
        return new PointSequence(sequence);
    }

    static WktToken dimensionMarker(DimensionalFlag flag) {
        return new DimensionMarker(flag);
    }

    /**
     * Returns the token that ends the WKT representation.
     *
     * @return
     */
    public static WktToken end() {
        return END;
    }

    /**
     * Returns the token that separates elements in a WKT representation.
     * @return
     */
    public static WktToken elementSeparator() {
        return ELEMENT_SEP;
    }

    /**
     * Returns the token taht identifies that the geometry is empty.
     *
     * @return
     */
    public static WktToken empty() {
        return EMPTY;
    }

    static class Geometry extends WktToken {
        private GeometryType geometryType;
        private boolean isMeasured;

        Geometry(GeometryType tag, boolean measured) {
            this.geometryType = tag;
            this.isMeasured = measured;
        }

        GeometryType getType() {
            return this.geometryType;
        }

        boolean isMeasured() {
            return this.isMeasured;
        }

    }

    static class End extends WktToken {
    }

    static class StartList extends WktToken {
    }

    static class EndList extends WktToken {
    }

    static class Empty extends WktToken {
    }

    static class PointSequence extends WktToken {
        private org.geolatte.geom.PointSequence points;

        PointSequence(org.geolatte.geom.PointSequence points) {
            this.points = points;
        }

        org.geolatte.geom.PointSequence getPoints() {
            return this.points;
        }
    }

    static class DimensionMarker extends WktToken {
        private DimensionalFlag dimensionalFlag;

        DimensionMarker(DimensionalFlag flag) {
            this.dimensionalFlag = flag;
        }

        public boolean isMeasured() {
            return this.dimensionalFlag.isMeasured();
        }

        public boolean is3D() {
            return this.dimensionalFlag.is3D();
        }
    }

    static class ElementSeparator extends WktToken {
    }

    static class TextToken extends WktToken {
        private String text;

        TextToken(String text){
            super();
            this.text = text;
        }

        public String getText(){
            return this.text;
        }
    }

    static class NumberToken extends WktToken {
        private double number;

        public NumberToken(double number) {
            super();
            this.number = number;
        }

        public double getNumber(){
            return this.number;
        }
    }
}

