FROM tomcat:9.0.95-jdk8-corretto-al2
RUN rm -rf /usr/local/tomcat/webapps/*
COPY /target/dath_cnpm.war /usr/local/tomcat/webapps/ROOT.war
CMD ["catalina.sh", "run"]