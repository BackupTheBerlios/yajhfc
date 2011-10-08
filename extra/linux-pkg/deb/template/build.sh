#!/bin/sh
# "Builds" YajHFC in order to create a package

set -e

echo "Creating man pages..."
LANG=C java -jar yajhfc.jar --Xprint-manpage > yajhfc.1
LANG=C java -jar yajhfc-console.jar --Xprint-manpage > cyajhfc.1

