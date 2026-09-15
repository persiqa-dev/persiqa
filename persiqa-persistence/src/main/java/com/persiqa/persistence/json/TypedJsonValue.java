package com.persiqa.persistence.json;

import java.math.BigDecimal;

/**
 * Normalizes scalar Statement/State typed values for JSON persistence.
 *
 * <p>Accepted domain values are {@link String}, {@link Boolean}, and {@link Number}. Numbers are
 * stored and recovered as {@link BigDecimal} so JSON number decoding remains stable across drivers.
 */
public final class TypedJsonValue {
  private TypedJsonValue() {}

  /** Validates and normalizes a value before it is written to a JSON column. */
  public static Object write(Object value) {
    return normalize(value, true);
  }

  /** Normalizes a value read from a JSON column back into the CKM scalar vocabulary. */
  public static Object read(Object value) {
    return normalize(value, false);
  }

  private static Object normalize(Object value, boolean writing) {
    if (value == null) {
      return null;
    }
    if (value instanceof String || value instanceof Boolean) {
      return value;
    }
    if (value instanceof Number number) {
      return new BigDecimal(number.toString());
    }
    var message = "typed JSON values must be String, Number, or Boolean";
    throw writing
        ? new IllegalArgumentException(message)
        : new IllegalStateException(message + ": " + value.getClass().getName());
  }
}
