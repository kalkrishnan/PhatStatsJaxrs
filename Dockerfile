FROM jetty:9
VOLUME /var/lib/jetty/webapps
COPY target/PhatStats-1.0.war /var/lib/jetty/webapps/
EXPOSE 8080
CMD ["jetty.sh", "run"]
