#!/bin/sh

if [ -z $1 ]; then
	echo "Usage: $0 lang"
	exit 1 ;
fi

WORKSPACE=~/java/workspace

makepack() {
  OUT=/tmp/yajhfc_$1.zip
  
  rm -f $OUT
  cd $WORKSPACE
  
  # file locations:
  LOCREADME=yajhfc/README_$1.txt
  README=yajhfc/README.txt 

  LOCFAQ=yajhfc/doc/faq-src/faq_$1.tex 
  FAQ=yajhfc/doc/faq-src/faq.tex

  FOPMSG=FOPPlugin/i18n/FOPMessages.po
  LOCFOPMSG=FOPPlugin/i18n/FOPMessages_$1.po

  FILES="yajhfc/src/yajhfc/i18n/messages_$1.po yajhfc/src/yajhfc/i18n/CommandLineOpts_$1.po"

  if [ -f $LOCREADME -a $README -nt $LOCREADME ]; then
     FILES="$FILES $README $LOCREADME"
  fi

  if [ -f $LOCFOPMSG -a $FOPMSG -nt $LOCFOPMSG ]; then
     FILES="$FILES $FOPMSG $LOCFOPMSG"
  fi

  if [ -f $LOCFAQ -a $FAQ -nt $LOCFAQ ]; then
     FILES="$FILES $FAQ $LOCFAQ yajhfc/doc/faq-src/faq.pdf yajhfc/doc/faq-src/faq_$1.pdf"
  fi

  zip -j $OUT $FILES

  echo "Output has been written to $OUT"
}

if [ $1 = all ]; then
  for PO in $WORKSPACE/yajhfc/src/yajhfc/i18n/messages_*.po ; do
        LANG=${PO##*messages_}
        LANG=${LANG%.po}
	
	echo "Creating Pack for language $LANG..."
	makepack $LANG
  done
else 
  makepack $1
fi

