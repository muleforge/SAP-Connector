SAP_JCO_HOME=/home/makoto/java/sap/sapjco3
mvn install:install-file -DgroupId=com.sap.conn.jco \
 -DartifactId=sapjco -Dversion=3.0.1 -Dpackaging=jar \
 -Dfile=$SAP_JCO_HOME/sapjco3.jar

