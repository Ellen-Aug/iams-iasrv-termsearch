# iams-iasvc-termsearch-svc

REST Cloud rewrite of **IAMS Terminology Search** (`iams-iasrv-termsearch` 6.0.2 EJB / WebLogic)
for OpenShift (`ha-app`). Layer 4 is on `base`: `@DirectApi` impl
`TermServiceApiImpl` + lookup tables `IAMS_VALUE` / `IAMS_VALUE_LIST`.
Local profile still does **not** open Oracle.

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
  tool/direct/                  @DirectApi @DirectManager @DirectDataAccess
  termsearch/controller|biz|dao|jpa
    controller/impl/TermServiceApiImpl.java
    biz/impl/TermServiceManagerImpl.java
    dao/impl/TermServiceDataAccessPOJO.java
```

## Build

```
./mvnw -B verify
java -jar target/iams-iasvc-termsearch-svc.jar --spring.profiles.active=local
```

Local profile does **not** open Oracle. Actuator: `/actuator/health/liveness`.
OpenAPI: `/v3/api-docs` (tag `termsearch`).

## Tests (Layer 0–4)

| Class | When it runs | What it proves |
|---|---|---|
| `TermServiceApiValidationTest` | always | empty `{}` → **6**, malformed JSON → 400 |
| `TermServiceApiFixtureContractTest` | always | S1/S4/D1/D3 JSON binds; HTTP 200 |
| `TermServiceApiEdgeTest` | always | S5/D5 → **6**; S6 → **7**; D4 → **9** + ALS 404 log |
| `TermServiceApiHappyTest` | only if `ORACLE_PASSWORD` is set | S1–S4, D1–D2 → **0**; D3 → **9** (ICD9Px `100` not in DEV) |

`./mvnw -B verify` without a password **skips** Happy (CI / laptop green).

## DEV Oracle on your PC

VPN to HA network required. **Password is env-only — never commit it, never paste it in chat.**

Defaults in `application-dev.yaml`:

- URL: `jdbc:oracle:thin:@//cdcdev33:19000/iamhaod1.cdcoradb11.server.ha.org.hk`
- User: `iams`

```powershell
git pull origin base

$env:ORACLE_PASSWORD = "your-password"
# optional overrides:
# $env:ORACLE_URL = "jdbc:oracle:thin:@//cdcdev33:19000/iamhaod1.cdcoradb11.server.ha.org.hk"
# $env:ORACLE_USERNAME = "iams"

"url=$env:ORACLE_URL user=$env:ORACLE_USERNAME pwLen=$($env:ORACLE_PASSWORD.Length)"

./mvnw -B test "-Dtest=TermServiceApiHappyTest" "-Dsurefire.failIfNoSpecifiedTests=false"
```

Do **not** use `DisabledCondition` deactivate. Happy enables itself when the password env is non-empty and uses `@ActiveProfiles("dev")`.

Run the JAR against Oracle:

```powershell
java -jar target/iams-iasvc-termsearch-svc.jar --spring.profiles.active=dev
```

Lookup SQL (when UCP is on): `IAMS_VALUE` + `IAMS_VALUE_SET` + `IAMS_VALUE_LIST`.
`IAMS_VALUE` has no `LIST_KEY`; list membership is the set table. Status text is then `A` not `15`.

Helm `values-DEV.yaml` uses OCP user **`iams`** (same as PC). Password still from secret `iams-iasvc-termsearch-svc-db` / `ORACLE_PASSWORD`. JDBC host is `REPLACE_DEV_HOST` until the DEV SCAN is filled.

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

## Out of this drop

- Conjur / Summon, EHR `common-ucp`, ALS JARs, Snowstorm, JMS, SGR
- SIT→PRD promotion workflows
