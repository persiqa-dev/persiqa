# Coding Standards

## Equal quality boundary

Production code and test code SHALL follow the same engineering-quality rules. A test is executable documentation and SHALL NOT use a lower-quality implementation style as a convenience.

In particular, persistence SQL in both production and test sources SHALL:

- use Spring JDBC named parameters rather than positional parameter indexes;
- remain parameterized and SHALL NOT interpolate values into SQL text;
- execute through the project transaction and persistence conventions where applicable; and
- comply with the configured Checkstyle and editor settings.

Exceptions require an explicit, documented technical reason and a narrowly scoped suppression.
