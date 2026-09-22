package com.persiqa.testkit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.persiqa.core.CanonicalKnowledgeModel;
import com.persiqa.core.ConflictDetector;
import com.persiqa.core.KnowledgeBase;
import com.persiqa.core.RelationRegistry;
import com.persiqa.model.Ckm.Capability;
import com.persiqa.model.Ckm.Concept;
import com.persiqa.model.Ckm.Context;
import com.persiqa.model.Ckm.Entity;
import com.persiqa.model.Ckm.Kind;
import com.persiqa.model.Ckm.KnowledgeKind;
import com.persiqa.model.Ckm.State;
import com.persiqa.model.Ckm.Statement;
import java.math.BigDecimal;
import java.util.Set;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class CkmConformanceTest {
  @Test
  void e001_incomplete_entity_is_valid() {
    assertEquals(Kind.ENTITY, new Entity("Outlet-17").kind());
  }

  @Test
  void e002_semantic_aspects_have_distinct_contracts() {
    var r = new RelationRegistry();
    var e = new Entity("RCBO-01");
    assertEquals("instanceOf", r.create("a", "instanceOf", e, new Concept("Acti9")).type().id());
    assertEquals(
        "hasCapability",
        r.create("b", "hasCapability", e, new Capability("OvercurrentProtection")).type().id());
  }

  @Test
  void n001_invalid_state_endpoint_is_rejected() {
    var r = new RelationRegistry();
    assertThrows(
        IllegalArgumentException.class,
        () -> r.create("x", "hasState", new Entity("Pump"), new Entity("Cable")));
  }

  @Test
  void n002_unknown_type_is_rejected() {
    assertThrows(
        IllegalArgumentException.class,
        () ->
            new RelationRegistry().create("x", "magic", new Entity("Pump"), new Entity("Outlet")));
  }

  @Test
  void k001_hosted_on_validates() {
    var r = new RelationRegistry();
    assertEquals(
        "hosts", r.create("p", "hostedOn", new Entity("Pod"), new Entity("VM")).type().inverse());
  }

  @Test
  void e004_supply_derivation_is_explicit_and_traceable() {
    var r = new RelationRegistry();
    var m = new Entity("MCB");
    var j = new Entity("JB");
    var o = new Entity("Outlet");
    var result =
        new KnowledgeBase()
            .deriveSupply(
                "s103", r.create("s101", "supplies", m, j), r.create("s102", "supplies", j, o));
    assertEquals(KnowledgeKind.DERIVED, result.knowledgeKind());
    assertEquals(Set.of("s101", "s102"), result.derivedFrom());
  }

  @Test
  void e005_connectivity_does_not_compose_as_supply() {
    var r = new RelationRegistry();
    var a = r.create("a", "connectedTo", new Entity("MCB"), new Entity("JB"));
    var b = r.create("b", "connectedTo", new Entity("JB"), new Entity("Outlet"));
    assertThrows(IllegalArgumentException.class, () -> new KnowledgeBase().deriveSupply("x", a, b));
  }

  @Test
  void n003_same_endpoints_do_not_force_same_relation_identity() {
    var r = new RelationRegistry();
    var cable = new Entity("Cable");
    var box = new Entity("Box");
    Assertions.assertNotEquals(
        r.create("A", "connectedTo", cable, box), r.create("B", "connectedTo", cable, box));
  }

  @Test
  void n004_derived_statement_requires_provenance() {
    assertThrows(
        IllegalArgumentException.class,
        () ->
            new Statement(
                "x",
                KnowledgeKind.DERIVED,
                "supplies",
                new Entity("A"),
                new Entity("B"),
                Set.of()));
  }

  @Test
  void e006_competing_evidence_is_preserved() {
    var a =
        new Statement(
            "a",
            KnowledgeKind.EXPLICIT,
            "supplies",
            new Entity("Breaker"),
            new Entity("Outlet-1"),
            Set.of(),
            new Context("inspection-A", new BigDecimal("0.8"), "T1"));
    var b =
        new Statement(
            "b",
            KnowledgeKind.EXPLICIT,
            "supplies",
            new Entity("Breaker"),
            new Entity("Outlet-2"),
            Set.of(),
            new Context("inspection-B", new BigDecimal("0.7"), "T1"));
    Assertions.assertNotEquals(a.id(), b.id());
    assertEquals("inspection-A", a.context().provenance());
  }

  @Test
  void n004_state_conflict_requires_overlapping_context() {
    var entity = new Entity("Breaker");
    var closed =
        new Statement(
            "c",
            KnowledgeKind.EXPLICIT,
            "hasState",
            entity,
            new State("Closed"),
            Set.of(),
            new Context("sensor", null, "T1"));
    var openLater =
        new Statement(
            "o",
            KnowledgeKind.EXPLICIT,
            "hasState",
            entity,
            new State("Open"),
            Set.of(),
            new Context("sensor", null, "T2"));
    var openSameTime =
        new Statement(
            "o2",
            KnowledgeKind.EXPLICIT,
            "hasState",
            entity,
            new State("Open"),
            Set.of(),
            new Context("sensor", null, "T1"));
    var d = new ConflictDetector();
    Assertions.assertFalse(d.conflicts(closed, openLater));
    Assertions.assertTrue(d.conflicts(closed, openSameTime));
  }

  @Test
  void k003_views_project_one_canonical_model() {
    var r = new RelationRegistry();
    var model = new CanonicalKnowledgeModel();
    var vm = new Entity("VM");
    model.add(r.create("placement", "hostedOn", new Entity("Pod"), vm));
    model.add(r.create("network", "connectedTo", new Entity("Service"), new Entity("Pod")));
    var infrastructure = model.view("infrastructure", x -> x.type().id().equals("hostedOn"));
    var communication = model.view("communication", x -> x.type().id().equals("connectedTo"));
    assertEquals(1, infrastructure.relations().size());
    assertEquals(1, communication.relations().size());
    assertEquals(3, model.nodes().size());
  }

  @Test
  void canonical_ids_cannot_be_overwritten() {
    var model = new CanonicalKnowledgeModel();
    model.add(new Entity("Pump"));
    assertThrows(IllegalArgumentException.class, () -> model.add(new Entity("Pump")));
  }

  @Test
  void fixture_profile_builds_canonical_model() {
    var m = new PdsFixtureParser().parse("entity MCB\nentity Outlet\nMCB supplies Outlet");
    assertEquals(2, m.nodes().size());
    assertEquals("supplies", m.relations().iterator().next().type().id());
  }
}
