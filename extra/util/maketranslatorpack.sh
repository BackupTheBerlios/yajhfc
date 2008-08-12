#!/bin/sh

if [ -z $1 ]; then
	echo "Usage: $0 lang"
	exit 1 ;
fi

OUT=/tmp/yajhfc_$1.zip
WORKSPACE=~/java/workspace

rm -f $OUT
cd $WORKSPACE

zip -j $OUT yajhfc/build/yajhfc.jar yajhfc/README.txt yajhfc/README_$1.txt yajhfc/doc/faq-src/faq.tex  yajhfc/doc/faq-src/faq_$1.tex  yajhfc/doc/faq-src/faq.pdf  yajhfc/doc/faq-src/faq_$1.pdf  yajhfc/yajhfc/i18n/messages_$1.po FOPPlugin/i18n/FOPMessages_$1.po

echo "Output has been written to $OUT"

