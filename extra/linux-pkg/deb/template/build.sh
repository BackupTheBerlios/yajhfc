#!/bin/sh
# "Builds" YajHFC in order to create a package

set -e

echo "Creating man page..."
LANG=C java -jar yajhfc.jar --Xprint-manpage > yajhfc.1

