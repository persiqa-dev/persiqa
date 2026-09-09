# Persiqa Wordmark

The Persiqa wordmark is the typographic expression of the Persiqa brand identity and is designed to work together with the approved Persiqa geometric logo.

## Approved Wordmark

- Asset: `persiqa_wordmark_v1.svg`
- Typeface basis: Manrope SemiBold (600)
- The wordmark is converted entirely to vector paths.
- The `i` dot is replaced by a Persiqa node derived from the approved v15 logo geometry.
- The wordmark and logo therefore share the same visual node language.

## Relationship to the Logo

The wordmark is a distinct brand asset, while the Persiqa logo remains the primary geometric symbol.

The node used in the `i` is intentionally derived from the logo node. This creates a direct visual relationship between the symbol and the name without altering the approved logo geometry.

The approved logo geometry itself must remain unchanged.

## Vector Format

The canonical wordmark is maintained as an SVG vector asset.

The SVG:

- contains vector paths rather than live text;
- does not require the Manrope font at runtime;
- is resolution-independent;
- is suitable for digital and print applications where an SVG asset is supported.

The source Manrope font is retained separately under:

`../typography/manrope/`

for design reproducibility and future typography work.

## Usage

Use the canonical `persiqa_wordmark_v1.svg` asset rather than recreating the wordmark from the Manrope font.

Do not:

- replace the custom `i` node with a standard typographic dot;
- alter the approved logo geometry;
- stretch or distort the wordmark;
- recreate the wordmark using a different typeface;
- apply effects that change the core geometric character.

Additional lockups or application-specific arrangements may be defined separately without modifying this canonical wordmark asset.
