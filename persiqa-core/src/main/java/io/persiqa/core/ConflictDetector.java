package io.persiqa.core;
import io.persiqa.model.Ckm.Statement;
/** Detects only declared state conflicts; it never removes source assertions. */
public final class ConflictDetector {
  public boolean conflicts(Statement left, Statement right) {
    return left.predicate().equals("hasState") && right.predicate().equals("hasState")
      && left.subject().equals(right.subject()) && !left.object().equals(right.object())
      && java.util.Objects.equals(left.context().validAt(), right.context().validAt());
  }
}
