#!/bin/sh
# Creates a distribution of the YajHFC launcher

BUILDHTML="../../homepage/doc/yajhfc-launcher.html"

export LANG=C
export LC_ALL=C

BUILDDATE=`date -r $BUILDHTML`

sed -e 's~href="../default.css"~href="img/default.css"~' -e 's~winfaxprinter.html~http://yajhfc.berlios.de/doc/winfaxprinter.html~' -e "s~<!--#echo var=\"LAST_MODIFIED\" -->~$BUILDDATE~" $BUILDHTML > dist/install.html

cd dist
rm -f ../yajhfc-launcher.zip
zip -r ../yajhfc-launcher.zip *

