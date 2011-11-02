Seam Spring Module
==================

The goal of the Seam Spring module is to provide a bridge betwee the CDI programming model and the one
provided by the Spring Framework.

Building
========

  mvn clean package


In-container testing
====================

  export JBOSS_HOME=<jboss-as-7-location>
  mvn clean test -Dincontainer
