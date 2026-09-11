# iams-iasvc-termsearch-svc

REST Cloud rewrite of **IAMS Terminology Search** (`iams-iasrv-termsearch` 6.0.2 EJB / WebLogic)
for OpenShift (`ha-app`). This `base` branch is the **scaffold**: Maven, Direct package tree,
Helm values, and DEV CI. Search SQL is not ported yet.

| | |
|---|---|
| Artifact | `hk.org.ha.iams:iams-iasvc-termsearch-svc:1.0.0-SNAPSHOT` |
| Runtime | Spring Boot 3.5.10 · Java 21 |
| HTTP | `POST /iams/api/termsearch/searchTermCode` · `POST /iams/api/termsearch/getTermDesc` |
| Consumer | CMS via Ingress · HTTP **200** + `TermReturnStatus` / `GetTermDescReturnStatus` in JSON |
| Old sources | https://github.com/Ellen-Aug/cms-iams-ha-iams-iasrv-termsearch-app |

## Layout

```
pom.xml                         Boot 3.5.10, UCP, EclipseLink, no Hikari / no Hibernate
Dockerfile  script/start.sh     ECP OpenJDK 21 · no summon-conjur
values-{DEV,SIT,UAT,LPT,PRD}.yaml
.github/workflows/development-workflow.yml
src/main/java/hk/org/ha/iams/
  tool/direct/                  @DirectApi @DirectManager @DirectDataAccess (copied markers)
  termsearch/controller|biz|dao|jpa
```

## Build

```
./mvnw -B verify
java -jar target/iams-iasvc-termsearch-svc.jar --spring.profiles.active=local
```

Local profile does **not** open Oracle. Actuator: `/actuator/health/liveness`.
OpenAPI: `/v3/api-docs`.

## Tests (Layer 0–2)

| Class | When it runs | What it proves |
|---|---|---|
| `TermServiceApiPlaceholderTest` | always | stub HTTP 200 + `returnCode` 7, malformed JSON → 400 |
| `TermServiceApiFixtureContractTest` | always | S1/S4/D1/D3 JSON binds (Jackson) and stays HTTP 200 |
| `TermServiceApiHappyTest` | `@Disabled` until SQL port | S1–S4, D1–D3 → `returnCode` 0 |
| `TermServiceApiEdgeTest` | `@Disabled` until validation + SQL | S5/D5 → **6**; S6 → 7 (real no-record); D4 → **9** + ALS 404 log |

Fixtures: `src/test/resources/fixtures/` (from old EJB `main()` tests / health servlets).
Paste CMS/DEV Oracle bodies over `recordsPending: true` in `*.expected.json`.

To run Happy/Edge locally (they **fail** on the stub — that is the TDD signal):

```
./mvnw -B test -Dtest=TermServiceApiHappyTest -Dsurefire.failIfNoSpecifiedTests=false
```

Remove `@Disabled` on those two classes after spec steps 6–7.

## CI / OCP (DEV)

Pipeline env: `APP_NAME=iams-iasvc-termsearch-svc` `MODULE=iams` `JAVA_VERSION=21`.

| Step | Target |
|---|---|
| Maven | `./mvnw -B verify` |
| Push image | `artifactrepo.server.ha.org.hk:55743/int-docker-dev-iams/iams-iasvc-termsearch-svc` |
| Helm pull | virtual `docker-dev-iams/iams-iasvc-termsearch-svc` |
| Namespace | `iams-dev-1` |
| Chart | ha-app, values-only, `fullnameOverride` + `ucp.enabled: true` |

GitHub Environment **DEV** secrets (not Git): `ORACLE_PASSWORD`, `ARTIFACTORY_USER`,
`ARTIFACTORY_PASSWORD`, `ECP_SA_TOKEN_DEV_C1`.

Replace `REPLACE_*_HOST` in `values-*.yaml` with DBA TNS hosts before deploy.

## Out of this drop

- Legacy search SQL (`TermServiceDataAccessPOJO`) — next drop
- Conjur / Summon, EHR `common-ucp`, ALS JARs, Snowstorm, JMS, SGR
- SIT→PRD promotion workflows
