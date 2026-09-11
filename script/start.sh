#!/bin/sh
# Locked start sequence for iams-iasvc-termsearch-svc.
# Do NOT call summon-conjur. Missing Conjur vars must not fail the process.

set -eu

if [ -f /usr/local/cloud/bin/jdk_baseline_config.sh ]; then
  # shellcheck disable=SC1091
  . /usr/local/cloud/bin/jdk_baseline_config.sh
fi

if [ "${ENABLE_APM_AGENT:-false}" = "true" ] && [ -f /usr/local/cloud/bin/apm.sh ]; then
  # shellcheck disable=SC1091
  . /usr/local/cloud/bin/apm.sh
fi

EXTRA_OPTS=""
if [ -f /eap/config/eap_application.properties ]; then
  EXTRA_OPTS="--spring.config.additional-location=optional:file:/eap/config/eap_application.properties"
fi

exec java ${JAVA_OPTS:-} -jar /webapps/application.jar ${EXTRA_OPTS}
