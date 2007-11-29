#!/bin/bash
#
# $Id$
# Make this file executable:
# chmod 755 vekapu.sh

java -cp ./lib/vekapu.jar:./lib/mail.jar:./lib/activation.jar:./lib/log4j.jar:. net.vekapu.gui.VekapuGuiStarter $1 $2 $3 $4 $5
