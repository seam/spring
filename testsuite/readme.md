Seam Spring Module Testsuite
============================

The test suite for the Seam Spring Module

Prerequisites
=============

Build and install the parent project

Testing Standalone
==================

  mvn clean verify

Testing In Container
====================

  mvn clean verify -Darquillian=jbossas-managed-7
