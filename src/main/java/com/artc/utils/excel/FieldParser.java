package com.artc.utils.excel;

@FunctionalInterface
public interface FieldParser<R> {

    R parse(String value);

    class Default implements FieldParser<String> {
        @Override
        public String parse(String value) {
            return value;
        }
    }

    class IntegerParser implements FieldParser<Integer> {
        @Override
        public Integer parse(String value) {
            if (value == null || "".equals(value)) {
                return null;
            }
            return Integer.parseInt(value);
        }
    }

    class LongParser implements FieldParser<Long> {
        @Override
        public Long parse(String value) {
            if (value == null || "".equals(value)) {
                return null;
            }
            return Long.parseLong(value);
        }
    }

    class DoubleParser implements FieldParser<Double> {
        @Override
        public Double parse(String value) {
            if (value == null || "".equals(value)) {
                return null;
            }
            return Double.parseDouble(value);
        }
    }

    class FloatParser implements FieldParser<Float> {
        @Override
        public Float parse(String value) {
            if (value == null || "".equals(value)) {
                return null;
            }
            return Float.parseFloat(value);
        }
    }

    class ShotParser implements FieldParser<Short> {
        @Override
        public Short parse(String value) {
            if (value == null || "".equals(value)) {
                return null;
            }
            return Short.parseShort(value);
        }
    }

    class ByteParser implements FieldParser<Byte> {
        @Override
        public Byte parse(String value) {
            if (value == null || "".equals(value)) {
                return null;
            }
            return Byte.parseByte(value);
        }
    }

    class CharacterParser implements FieldParser<Character> {
        @Override
        public Character parse(String value) {
            if (value == null || "".equals(value)) {
                return null;
            }
            if (value.length() == 1) {
                return value.charAt(0);
            }
            throw new RuntimeException("can not cast to character! value : " + value);
        }
    }

}
