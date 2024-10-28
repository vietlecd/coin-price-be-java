FROM tomcat:9.0.96-jdk8-corretto-al2
RUN rm -rf /usr/local/tomcat/webapps/*
COPY /target/dath_cnpm.war /usr/local/tomcat/webapps/ROOT.war
COPY src/main/resources/keystore.p12 /usr/local/tomcat/app/keystore.p12
ENV CATALINA_OPTS="-Djavax.net.ssl.keyStore=/usr/local/tomcat/app/keystore.p12 \
                   -Djavax.net.ssl.keyStorePassword=${KEYSTORE_PASSWORD} \
                   -Djavax.net.ssl.keyStoreType=PKCS12"
CMD ["catalina.sh", "run"]