package com.persiqa.persistence.json;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class TypedJsonValueTest {
  @Test
  void normalizes_numbers_to_big_decimal() {
    assertEquals(new BigDecimal("42"), TypedJsonValue.write(42));
    assertEquals(new BigDecimal("42"), TypedJsonValue.read(42));
    assertEquals("ok", TypedJsonValue.write("ok"));
    assertEquals(Boolean.TRUE, TypedJsonValue.read(true));
  }

  @Test
  void rejects_non_scalar_values() {
    assertThrows(IllegalArgumentException.class, () -> TypedJsonValue.write(java.util.Map.of()));
    assertThrows(IllegalStateException.class, () -> TypedJsonValue.read(java.util.Map.of()));
  }
}
