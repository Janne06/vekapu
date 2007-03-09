#!/bin/bash
#
# $Id$
# Make this file executable:
# chmod 755 vekapu.sh

#cd /to/you/vekapu/vekapu-n.n.n/

# Full path to java if using kcron
#/where/is/java
java -cp ./lib/vekapu.jar:./lib/mail.jar:./lib/activation.jar:./lib/log4j.jar:./lib/jmdi.jar:.  net.vekapu.gui3.VekapuMdiGui $1 $2 $3 $4 $5


