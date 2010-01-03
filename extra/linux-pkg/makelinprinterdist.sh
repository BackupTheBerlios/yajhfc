#!/bin/sh
# Creates a distribution of the YajHFC launcher

BUILDHTMLDIR="../../homepage/doc/"
BUILDHTMLPREFIX="cupsfaxprinter"

export LANG=C
export LC_ALL=C

for BUILDHTML in $BUILDHTMLDIR/$BUILDHTMLPREFIX*.shtml; do

	BUILDDATE=`date -r $BUILDHTML`

	SUFFIX=${BUILDHTML##*$BUILDHTMLPREFIX}
	SUFFIX=${SUFFIX%shtml}html

	sed -e 's~href="../default.css"~href="img/default.css"~' -e "s|<!--#echo var=\"LAST_MODIFIED\" -->|$BUILDDATE|" -e 's|href="files/|href="|g' $BUILDHTML > printerport-cups/install$SUFFIX

done

rm -f printerport-cups.tar.gz
tar -czvf printerport-cups.tar.gz printerport-cups --exclude='*/CVS/*' --exclude='*.old'

