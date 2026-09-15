package com.persiqa.testkit;

import static org.junit.jupiter.api.Assertions.*;

import com.persiqa.core.*;
import com.persiqa.model.Ckm.*;
import java.math.BigDecimal;
import java.util.Set;
import org.junit.jupiter.api.Test;

/** Executable regression suite for the electrical and Kubernetes reality tests. */
class RealityModelTest {
  @Test
  void electrical_rcbo_keeps_all_semantic_aspects() {
    var r = new RelationRegistry();
    var model = new CanonicalKnowledgeModel();
    var rcbo = new Entity("RCBO-01");
    model.add(r.create("product", "instanceOf", rcbo, new Concept("Acti9-iDPN-Vigi-C16")));
    model.add(r.create("breaker", "classifiedAs", rcbo, new Concept("CircuitBreaker")));
    model.add(r.create("rcd", "classifiedAs", rcbo, new Concept("ResidualCurrentDevice")));
    model.add(r.create("role", "playsRole", rcbo, new Concept("BathroomCircuitProtection")));
    model.add(
        r.create("capability", "hasCapability", rcbo, new Capability("ResidualCurrentProtection")));
    model.add(r.create("state", "hasState", rcbo, new State("Closed")));
    assertEquals(6, model.relations().size());
    assertEquals(1, model.view("role", x -> x.type().id().equals("playsRole")).relations().size());
  }

  @Test
  void electrical_refinement_preserves_explicit_and_derives_supply() {
    var r = new RelationRegistry();
    var m = new Entity("MCB");
    var j = new Entity("JunctionBox");
    var o = new Entity("Outlet");
    var first = r.create("s1", "supplies", m, j);
    var second = r.create("s2", "supplies", j, o);
    var derived = new KnowledgeBase().deriveSupply("s3", first, second);
    assertEquals(Set.of("s1", "s2"), derived.derivedFrom());
    assertEquals(KnowledgeKind.DERIVED, derived.knowledgeKind());
  }

  @Test
  void kubernetes_identity_survives_state_and_role_change() {
    var r = new RelationRegistry();
    var vm = new Entity("VM-01");
    var model = new CanonicalKnowledgeModel();
    model.add(r.create("node", "playsRole", vm, new Concept("KubernetesNode")));
    model.add(r.create("running", "hasState", vm, new State("Running")));
    model.add(r.create("runner", "playsRole", vm, new Concept("BuildRunner")));
    model.add(r.create("stopped", "hasState", vm, new State("Stopped")));
    assertEquals(1, model.nodes().stream().filter(x -> x.equals(vm)).count());
    assertEquals(4, model.relations().size());
  }

  @Test
  void kubernetes_workload_and_component_structure() {
    var r = new RelationRegistry();
    var model = new CanonicalKnowledgeModel();
    var pod = new Entity("Pod-01");
    model.add(r.create("placement", "hostedOn", pod, new Entity("VM-01")));
    model.add(r.create("component", "contains", pod, new Entity("Container-01")));
    assertEquals(
        1, model.view("placement", x -> x.type().id().equals("hostedOn")).relations().size());
    assertEquals(
        "partOf", r.create("p", "contains", pod, new Entity("Container-02")).type().inverse());
  }

  @Test
  void explicit_statement_is_preserved_separately_from_canonical_relation() {
    var model = new CanonicalKnowledgeModel();
    var writer = new StatementFirstWriter(model, new RelationRegistry());
    var source = new Entity("MCB-01");
    var target = new Entity("Outlet-01");
    var result =
        writer.assertRelation(
            "supply-mcb-outlet",
            "inspection-42",
            "supplies",
            source,
            target,
            new Context("inspection", new BigDecimal("0.9"), "T1"));
    assertEquals(KnowledgeKind.EXPLICIT, result.statement().knowledgeKind());
    assertEquals("inspection", result.statement().context().provenance());
    assertEquals("supply-mcb-outlet", result.relation().id());
    var derived =
        writer.deriveRelation(
            "supply-mcb-outlet",
            "topology-42",
            "supplies",
            source,
            target,
            Set.of("inspection-42"),
            new Context("supply-rule", new BigDecimal("0.9"), "T1"));
    assertEquals(result.relation(), derived.relation());
    assertEquals(2, model.statements().size());
    assertEquals(1, model.relations().size());
  }

  @Test
  void derived_relation_keeps_evidence_separate_from_explicit_assertions() {
    var model = new CanonicalKnowledgeModel();
    var writer = new StatementFirstWriter(model, new RelationRegistry());
    var result =
        writer.deriveRelation(
            "supply-mcb-outlet",
            "derived-1",
            "supplies",
            new Entity("MCB"),
            new Entity("Outlet"),
            Set.of("s1", "s2"),
            new Context("rule", null, "T1"));
    assertEquals(KnowledgeKind.DERIVED, result.statement().knowledgeKind());
    assertEquals(Set.of("s1", "s2"), result.statement().derivedFrom());
  }
}
