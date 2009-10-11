#!/bin/sh
# Create ZIP archive for distribution

YAJHFCDIR=../../../workspace/yajhfc

rm -f coverpages.zip
zip -j coverpages.zip *.html *.txt *.png *.ps *.txt $YAJHFCDIR/src/yajhfc/faxcover/coverpage*.html
