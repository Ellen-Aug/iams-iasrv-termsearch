FROM artifactrepo.server.ha.org.hk:55743/int_eap_docker_release/ecpimage/openjdk21:ecp-v26.08-openjdk21-21.0.12.el8.x86_64-conjur-13.0
USER root
COPY script/start.sh /usr/local/cloud/bin/start.sh
RUN chmod a+rx /usr/local/cloud/bin/start.sh
COPY /target/*.jar /webapps/application.jar
USER jboss
CMD ["bin/start.sh"]
