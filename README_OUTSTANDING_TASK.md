# Outstanding development tasks

HEAD of `base`: `a988975` (Ellen-Aug/iams-iasrv-termsearch).

**Already done (not in this list):** Layers 0–4 on laptop — `TermServiceApiImpl`, manager validation, JDBC SQL port, lookup via `IAMS_VALUE` + `IAMS_VALUE_SET` + `IAMS_VALUE_LIST`, Happy 7/7 against DEV Oracle when `ORACLE_PASSWORD` is set. `./mvnw -B verify` is green without a password (Happy skipped).

---

## Service / application

| ID | Task name | Description |
|---|---|---|
| S1 | Replace ALS stub | `AlsNotFoundLog` only writes `ALS_STUB HTTP 404`. Wire the real ALS adapter/JAR (or confirm v1 stays log-only). |
| S2 | Use or drop unused JPA | `iamsPersistenceUnit` + empty `jpa/entity` package. All data access is JDBC. Either add EclipseLink entities or remove unused JPA config. |
| S3 | Implement regenerate-code | `tools/regenerate-code/regenerate.js` is a no-op stub (O9). Scan `@DirectApi` / `@DirectManager` / `@DirectDataAccess` and generate impls, or remove the stub. |
| S4 | CMS golden response fixtures | Fixture expected JSON is status-only (`recordsPending: true`). Capture real CMS request/response pairs and assert record fields, not only `returnCode` / `minCount`. |
| S5 | D3 ICD9Px `100` data gap | DEV has no ICD9Px code `100`; Happy D3 expects returnCode **9**. Confirm with data owners whether to seed the code, change the fixture, or keep 9 as the DEV contract. |
| S6 | Port EJB audit / request-key logging | Legacy request-key and audit logging from the EJB manager is not in the Cloud service. |
| S7 | Align `application.yaml` default DB user | Cloud profile default is still `iams_app_cld_rw_user`. DEV Helm already sets `ORACLE_USERNAME=iams`. Align the default or document per-env override. |
| S8 | Remaining `queryForRowSet` TIMESTAMP risk | SNOMED `code_version_date` uses a RowMapper. Other date columns (`create_dtm`, `update_dtm`) still go through `CachedRowSet` and can fail if CAST is removed. |

## OCP DEV (this phase)

| ID | Task name | Description |
|---|---|---|
| O1 | Fill DEV JDBC SCAN host | `values-DEV.yaml` still has `HOST=REPLACE_DEV_HOST`. Need the DEV SCAN hostname, **or** move `ORACLE_URL` to GitHub Environment DEV secrets. |
| O2 | Wire Oracle credentials for the cluster | Helm uses K8s `secretKeyRef` `iams-iasvc-termsearch-svc-db` / `ORACLE_PASSWORD`. CI/CD PDF and screenshots use GitHub **DEV environment** secrets `ORACLE_URL`, `ORACLE_USERNAME`, `ORACLE_PASSWORD` injected at Helm time. Pick one path and implement it. Secret is not in Git. |
| O3 | Complete ha-app DEV values | Current `values-DEV.yaml` is minimal. Sample ha-app values include APM URL, `nodeSelector`, ALS configmap, imagePullSecrets, Kubernetes fieldRefs. Add what platform requires for `iams-dev-1`. |
| O4 | First live DEV deploy verify | No pod/route yet. After Helm: liveness/readiness, `/v3/api-docs`, `searchTermCode` / `getTermDesc` through Ingress `iams-iasvc-termsearch-svc.tstcld91.server.ha.org.hk`. |

## CI/CD (DEV Docker)

| ID | Task name | Description |
|---|---|---|
| C1 | Real DEV GitHub Actions workflow | `.github/workflows/development-workflow.yml` is a stub: `ubuntu-latest`, skip image push without secrets, Helm/`oc` only `echo`. Implement build → scan → push `int-docker-dev-iams` → Helm to `iams-dev-1`. |
| C2 | Align secret and variable names with IAMS PDF | Workflow uses `ARTIFACTORY_USER`, `ARTIFACTORY_PASSWORD`, `OCP_API`, `HELM_CHART_REPO`. PDF requires `ARTIFACTORY_USER_DEV`, `ARTIFACTORY_PASSWORD_DEV`, `CLUSTER_URL_DEV_C1`, `HELM_REPO_DEV`, runner label `iams` (`vars.RUNS_ON`). |
| C3 | Add scanner jobs | No SonarQube, Nexus IQ, or Fortify steps. PDF: `CODE_ANALYSER_*`, `OSS_ANALYSER_*_DEV`; Fortify (`SAST_TOKEN_PRD`) only if enabled. |
| C4 | Move or mirror repo to IAMS GHE | Deploy runner `iams` and org secrets exist on `hagithub.home` / `IAMS/iams-iasvc-termsearch-svc`, not on public `Ellen-Aug/iams-iasrv-termsearch`. |
| C5 | Create GitHub Environment DEV + org/repo vars | Organization variables (`PRODUCT`, `NAMESPACE_DEV`, `HELM_REPO_DEV`, `CLUSTER_URL_DEV_C1`, `JAVA_VERSION`, …), org secrets (`iams-cdra-dev`, `ECP_SA_TOKEN_DEV_C1`, CA), repo variables (`APP_NAME`, `MODULE`). UI only — not Git. |
| C6 | Drive APP_NAME / MODULE / JAVA_VERSION from repo vars | Workflow hardcodes `APP_NAME=iams-iasvc-termsearch-svc`, `MODULE=iams`, `JAVA_VERSION=21`. PDF: repository variables, do not derive APP_NAME from the Git slug. |

## Out of this drop (tracked, not in DEV scope)

| ID | Task name | Description |
|---|---|---|
| X1 | Conjur / Summon | Explicitly excluded. Do not add secret fetching to Dockerfile or Helm. |
| X2 | EHR `common-ucp` JAR | Service uses Boot UCP properties, not the EHR shared library. |
| X3 | Snowstorm | Out of TermSearch Cloud v1. |
| X4 | JMS / SGR | Not ported from EJB. |
| X5 | SIT → UAT → LPT → PRD promotion | No promotion workflows. `values-{SIT,UAT,LPT,PRD}.yaml` still have `REPLACE_*_HOST` and user `iams_app_cld_rw_user`. |

---

Highest-impact next work:

1. **C1 + C2 + C5** — real DEV pipeline and GitHub secrets/vars on IAMS GHE.
2. **O1 + O2** — Oracle URL/password on the cluster.
3. **O4** — first Helm deploy and health check on `iams-dev-1`.
