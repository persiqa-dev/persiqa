package com.persiqa.persistence.json;

/**
 * JSON shape of {@code relation_type.inference_policy}.
 *
 * <p>Only the fields currently used by the CKM Relation Type contract are modeled. Additional
 * policy keys can be added without changing the surrounding persistence boundary.
 */
public record InferencePolicy(boolean composable) {
  /** Policy with composition disabled. */
  public static InferencePolicy nonComposable() {
    return new InferencePolicy(false);
  }
}
