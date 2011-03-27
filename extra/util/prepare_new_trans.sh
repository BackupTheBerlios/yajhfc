#!/bin/sh

if [ -z $1 ]; then
	echo "Usage: $0 lang"
	exit 1 ;
fi

WORKSPACE=~/java/workspace
OUTLANG=$1
OUTDIR=/tmp/yajhfc-$OUTLANG

 
  rm -rf $OUTDIR
  mkdir -p $OUTDIR
  mkdir $OUTDIR/optional

  cd $WORKSPACE
  
  cp yajhfc/README.txt            $OUTDIR/optional/README_$OUTLANG.txt
  cp yajhfc/doc/faq-src/faq.tex   $OUTDIR/optional/faq_$OUTLANG.tex
  
  cp yajhfc/src/yajhfc/i18n/messages.po $OUTDIR/messages_$OUTLANG.po
  cp FOPPlugin/i18n/FOPMessages.po      $OUTDIR/FOPMessages_$OUTLANG.po

  cd yajhfc/src/yajhfc/i18n
  perl maketemplate.pl < CommandLineOpts.po > $OUTDIR/CommandLineOpts_$OUTLANG.po

  echo "Output has been written to $OUTDIR"


