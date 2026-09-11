# iams-iasvc-termsearch-svc

REST Cloud rewrite of **IAMS Terminology Search** (`iams-iasrv-termsearch` 6.0.2 EJB / WebLogic)
for OpenShift (`ha-app`). Layer 3 is on `base`: manager validation + JDBC search SQL
(legacy `TermServiceDataAccessPOJO`). Local profile still does **not** open Oracle.

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

## Tests (Layer 0–3)

| Class | When it runs | What it proves |
|---|---|---|
| `TermServiceApiPlaceholderTest` | always | empty `{}` → **6**, malformed JSON → 400 |
| `TermServiceApiFixtureContractTest` | always | S1/S4/D1/D3 JSON binds; HTTP 200 |
| `TermServiceApiEdgeTest` | always | S5/D5 → **6**; S6 → **7**; D4 → **9** + ALS 404 log |
| `TermServiceApiHappyTest` | `@Disabled` until DEV Oracle | S1–S4, D1–D3 → `returnCode` **0** |

Local profile does **not** open Oracle (`termsearch.datasource.enabled=false`). Valid searches then return 7 (empty DAO). Point `ORACLE_URL` / profile `dev` at DEV TNS, then remove `@Disabled` on HappyTest.

Lookup SQL (when UCP is on): `iams_lookup_value` + `iams_lookup_list`.

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

- Conjur / Summon, EHR `common-ucp`, ALS JARs, Snowstorm, JMS, SGR
- SIT→PRD promotion workflows
- HappyTest against live DEV Oracle (remove `@Disabled` after TNS is set)
