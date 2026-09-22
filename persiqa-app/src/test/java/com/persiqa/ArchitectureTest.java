package com.persiqa;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static com.tngtech.archunit.library.dependencies.SlicesRuleDefinition.slices;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

/** Guards the module boundaries that keep CKM semantics independent from JPA details. */
@AnalyzeClasses(
    packages = "com.persiqa",
    importOptions = ImportOption.DoNotIncludeTests.class)
class ArchitectureTest {
  @ArchTest
  static final ArchRule applicationDoesNotDependOnPersistence =
      noClasses()
          .that()
          .resideInAPackage("..application..")
          .should()
          .dependOnClassesThat()
          .resideInAPackage("..persistence..");

  @ArchTest
  static final ArchRule webDoesNotDependOnPersistence =
      noClasses()
          .that()
          .resideInAPackage("..web..")
          .should()
          .dependOnClassesThat()
          .resideInAPackage("..persistence..");

  @ArchTest
  static final ArchRule applicationPackagesHaveNoCycles =
      slices()
          .matching("com.persiqa.(*)..")
          .should()
          .beFreeOfCycles();
}
