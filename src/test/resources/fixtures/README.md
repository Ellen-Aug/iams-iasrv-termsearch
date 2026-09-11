# Layer 1 fixtures — old EJB test programs → REST JSON

Requests are taken from the **active** (uncommented) lines of the 6.0.2 EJB `main()` tests
and health servlets. Responses are **status skeletons**: `recordsPending: true` until CMS
or DEV Oracle golden bodies are pasted.

| ID | Source | Operation | After SQL / validation |
|---|---|---|---|
| S1 | `TestGenericSrchByCode` | searchTermCode | `returnCode` 0, ≥1 record |
| S2 | `TestGenericSrchByKeyword` | searchTermCode | `returnCode` 0, ≥1 record |
| S3 | `TestGenericSrchById` | searchTermCode | `returnCode` 0, ids 1069/707/704 |
| S4 | `SrchTermCodeHealthTest` | searchTermCode | `returnCode` 0 (health also accepted 7/8) |
| S5 | empty `{}` | searchTermCode | `returnCode` **6** (missing compulsory; not null → not 5) |
| S6 | bogus ICPC2 `ZZZ999` | searchTermCode | `returnCode` **7**, empty list |
| D1 | `TestGetTermDescByCode` | getTermDesc | `returnCode` 0 |
| D2 | `TestGetTermDescById` | getTermDesc | ids win over codes; 1055 duplicated |
| D3 | `GetTermDescHealthTest` | getTermDesc | 3 `termDescs` |
| D4 | unknown ICD9Dx `999.99` | getTermDesc | `returnCode` **9**, HTTP 200, ALS stub logs 404 |
| D5 | empty `{}` | getTermDesc | `returnCode` **6** |

Ignore `timestamp` when comparing. Never assert HTTP 404 to CMS.

Enable `TermServiceApiHappyTest` after DAO SQL port.
Enable `TermServiceApiEdgeTest` after manager validation port.
