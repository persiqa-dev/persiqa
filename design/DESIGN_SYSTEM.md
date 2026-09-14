# Persiqa Visual Design System

**Status:** Canonical  
**Version:** 1.0  
**Scope:** Brand and product visual design

## 1. Design Principles

Persiqa uses a modern, clean, geometric, precise, structured, and knowledge-driven visual language.

The visual system should communicate:

- structure
- relationships
- progressive knowledge
- technical precision
- clarity
- confidence

Visual complexity should come from the information and relationships being represented, not from decorative styling.

The design system is based on a small number of coherent primitives and should remain consistent across brand, product UI, documentation, and visualization.

## 2. Brand Foundation

### 2.1 Logo

The approved Persiqa logo is the finalized **v15 geometric mark**.

The logo geometry is canonical and must not be redesigned or structurally altered.

Its visual construction is based on:

- regular hexagonal outer geometry
- central three-face flat cube
- upper-right node
- deliberate open segments
- internal geometric masking
- connection-oriented line structure

The open portion of the outer geometry intentionally represents unexplored or not-yet-modeled parts of a system.

The node represents a known or identified connection point.

### 2.2 Wordmark

The approved Persiqa wordmark is based on **Manrope SemiBold (600)**.

The `i` dot is replaced by a Persiqa node derived directly from the approved logo geometry.

This creates a deliberate relationship between the logo and the wordmark: the node is not a decorative symbol but part of the Persiqa visual language.

The canonical wordmark is maintained as vector artwork and does not require the Manrope font at runtime.

Canonical asset:

`brand/wordmark/persiqa_wordmark_v1.svg`

## 3. Color

### 3.1 Primary Brand Color

**Persiqa Amber**

```text
HEX  #C25F05
```

Persiqa Amber is the primary accent and brand color.

Use it for:

- primary brand marks
- key interaction accents
- selected states
- nodes and connection highlights
- important data points
- visual emphasis

Amber should be used deliberately rather than as a default fill for large areas.

### 3.2 Neutral Palette

The visual environment is primarily neutral and supports the Amber accent.

Core neutrals include:

- Black
- Near Black
- Charcoal
- Graphite
- Gray
- White
- Warm White

Neutral values should provide the majority of the visual surface area.

### 3.3 Semantic Colors

Product interfaces may introduce semantic colors for:

- success
- warning
- error
- information

Semantic colors must remain secondary to the Persiqa brand palette and must not compete with Persiqa Amber.

## 4. Typography

**Manrope** is the primary Persiqa typeface.

### 4.1 Primary Weights

| Weight | Typical use |
|---|---|
| Regular 400 | body text, descriptions |
| Medium 500 | labels, navigation, secondary hierarchy |
| SemiBold 600 | headings, prominent labels, wordmark |
| Bold 700 | strong emphasis and selected display use |

Typography should use hierarchy through:

- size
- weight
- spacing
- alignment
- contrast

Avoid unnecessary typographic decoration.

The Manrope source package and licensing information are maintained under:

`brand/typography/manrope/`

## 5. Geometry

Persiqa uses a geometric vocabulary derived from the logo and its structural meaning.

Primary geometric primitives:

- HEXAGON
- CUBE
- NODE
- CONNECTION
- GRID

These primitives should be used consistently across visual assets.

The logo geometry is the reference for the proportions and visual character of this system, but the primitives should not be mechanically copied into every design.

## 6. Iconography

Persiqa iconography is:

- geometric
- minimal
- precise
- consistent
- based on a 24×24 base grid

Icons should favor simple construction and clear silhouettes.

Avoid:

- excessive detail
- ornamental styling
- inconsistent corner treatment
- unrelated icon families
- visual effects that obscure structure

Icons should feel like part of the same system as the logo rather than an unrelated UI icon library.

## 7. Graphic Language

The Persiqa graphic language extends the core geometry into compositional elements.

### Core vocabulary

**HEXAGON**  
Represents structure, enclosure, and system boundaries.

**CUBE**  
Represents modeled objects, structure, and multidimensional infrastructure.

**NODE**  
Represents a known point, object, endpoint, or connection point.

**CONNECTION**  
Represents relationships, dependencies, and flow.

**GRID**  
Represents organization, topology, spatial structure, and system context.

### Usage principle

Graphic elements should reinforce meaning.

Do not use geometric motifs merely as decoration when they do not contribute to the represented concept.

## 8. Layout

Persiqa layouts should be structured, balanced, and information-oriented.

A consistent spacing scale should be used across product and brand applications.

Recommended base spacing scale:

```text
4
8
12
16
24
32
48
64
```

Layouts should favor:

- clear alignment
- predictable spacing
- strong grouping
- generous whitespace where appropriate
- dense information presentation where the domain requires it

The visual system should support both high-density technical views and calm explanatory views.

## 9. Product UI

Persiqa product interfaces should prioritize understanding relationships and system structure.

UI should feel:

- technical without being sterile
- structured without being rigid
- information-dense without being cluttered
- modern without being trend-driven

The graph and underlying model should remain visually central where relevant.

Views and projections should share a common visual language rather than appearing as unrelated screens.

## 10. Data Visualization

Data visualization should emphasize relationships, structure, and explainability.

Prefer:

- clear topology
- explicit connections
- restrained color use
- meaningful visual hierarchy
- direct labeling
- consistent node and edge semantics

Persiqa Amber should be reserved for important emphasis rather than applied indiscriminately.

Visualization should remain legible in both light and dark environments.

## 11. Imagery

Persiqa imagery should support an architectural and infrastructure-oriented character.

Preferred imagery themes include:

- infrastructure
- architecture
- technical details
- systems
- materials
- structured environments
- abstract geometric compositions

Avoid generic SaaS, generic AI, or overly decorative stock imagery.

## 12. Light and Dark Environments

The visual system supports both light and dark presentation.

### Light

Use:

- white or warm-white surfaces
- charcoal text
- restrained neutrals
- Persiqa Amber for emphasis

### Dark

Use:

- near-black or charcoal surfaces
- light neutral text
- graphite layers
- Persiqa Amber for emphasis

The logo's internal cube masking must invert with the background in negative applications.

## 13. Effects

Effects are secondary to geometry and typography.

The canonical identity should work without effects.

Blur, shadow, depth, gradients, or other treatments may be used only when they improve a specific application context and do not alter the underlying brand geometry.

The primary brand assets remain flat, clean, and vector-based.

## 14. Canonical Assets

The canonical visual system is maintained under:

```text
design/
├── DESIGN_SYSTEM.md
└── brand/
    ├── logo/
    │   └── geometry-reference.png
    ├── wordmark/
    │   ├── persiqa_wordmark_v1.svg
    │   └── README.md
    ├── color/
    │   └── palette.md
    └── typography/
        ├── typography.md
        └── manrope/
            ├── README.md
            └── upstream/
```

Canonical brand assets should be reused rather than recreated.

## 15. Non-Goals

The Persiqa visual system should not evolve toward:

- generic SaaS aesthetics
- excessive futuristic styling
- decorative geometric overload
- unnecessary gradients or effects
- inconsistent illustration styles
- visual complexity without semantic purpose

The goal is not to make every surface visually distinctive.

The goal is to make the system itself recognizable and coherent.
