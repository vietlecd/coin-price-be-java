FROM tomcat:9.0.95
RUN rm -rf /usr/local/tomcat/webapps/*
COPY /target/spring-boot-1.0.war /usr/local/tomcat/webapps/ROOT.war
CMD ["catalina.sh", "run"]