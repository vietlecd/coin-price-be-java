FROM tomcat:9.0.96-jdk8-corretto-al2
RUN rm -rf /usr/local/tomcat/webapps/*
COPY /target/dath_cnpm.war /usr/local/tomcat/webapps/ROOT.war
COPY keystore.p12 keystore.p12
CMD ["catalina.sh", "run"]