#!/bin/sh
# Creates a distribution of the YajHFC launcher

BUILDHTMLDIR="../../homepage/doc/"
BUILDHTMLPREFIX="yajhfc-launcher"

export LANG=C
export LC_ALL=C

for BUILDHTML in $BUILDHTMLDIR/$BUILDHTMLPREFIX*.html; do

	BUILDDATE=`date -r $BUILDHTML`

	SUFFIX=${BUILDHTML##*$BUILDHTMLPREFIX}

	sed -e 's~href="../default.css"~href="img/default.css"~' -e 's~winfaxprinter.html~http://yajhfc.berlios.de/doc/winfaxprinter.html~' -e "s~<!--#echo var=\"LAST_MODIFIED\" -->~$BUILDDATE~" $BUILDHTML > dist/install$SUFFIX

done

cd dist
rm -f ../yajhfc-launcher.zip
zip -r ../yajhfc-launcher.zip * -x 'CVS/*' -x '*/CVS/*'

