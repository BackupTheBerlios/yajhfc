#!/bin/sh

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

  CMDLINEOPTS=yajhfc/src/yajhfc/i18n/CommandLineOpts.po
  LOCCMDLINEOPTS=yajhfc/src/yajhfc/i18n/CommandLineOpts_$1.po

  FILES="yajhfc/src/yajhfc/i18n/messages_$1.po"

  if [ "$INCLCMDLINEOPTS" != "n" -a -f $LOCCMDLINEOPTS ]; then
     FILES="$FILES $LOCCMDLINEOPTS"
  fi

  if [ "$INCLREADME" != "n" -a  -f $LOCREADME ]; then
     FILES="$FILES $README $LOCREADME"
  fi

  if [ "$INCLFOPMSG" != "n" -a -f $LOCFOPMSG ]; then
     FILES="$FILES $LOCFOPMSG"
  fi

  if [ "$INCLFAQ"!="n" -a -f $LOCFAQ ]; then
     FILES="$FILES $FAQ $LOCFAQ yajhfc/doc/faq-src/faq.pdf yajhfc/doc/faq-src/faq_$1.pdf"
  fi

  zip -j $OUT $FILES

  echo "Output has been written to $OUT"
}

read -p 'Include FAQ? [y/n]' INCLFAQ
read -p 'Include CommandLineOpts? [y/n]' INCLCMDLINEOPTS
read -p 'Include README? [y/n]' INCLREADME
read -p 'Include FOP Msg? [y/n]' INCLFOPMSG


for PO in $WORKSPACE/yajhfc/src/yajhfc/i18n/messages_*.po ; do
        LANG=${PO##*messages_}
        LANG=${LANG%.po}
	
	echo "Creating Pack for language $LANG..."
	makepack $LANG
done


