#!/bin/bash
# Builds po files
# Usage: 
# - With no parameters: build all files
# - With a single parameter: Build the po file for the requested language

if [ -z `which javac` ]; then
	echo 'javac not found. Please make sure javac can be found in your PATH.'
	exit 1 ;
fi

if [ -z `which msgfmt` ]; then
	echo 'msgfmt not found. Please make sure you have GNU gettext installed and that it can be found in your PATH.'
	exit 1 ;
fi


# Make sure we compile for JDK 1.5
export JAVAC="javac -target 1.5"

INNAME=Messages
RESOURCE=yajhfc.console.i18n.$INNAME
DESTDIR=bin

# Usage compile FILE PROPERTYNAME
catprops() {
	PO=$1
  	LANG=${PO##*_}
  	LANG=${LANG%.po}
        PROPDEST="$DESTDIR/yajhfc/console/i18n"
	if [ $LANG != ${PO%.po} ]; then
		PROPOUT="$PROPDEST/$2_$LANG.properties"
  		echo -n "Using $PO for language $LANG: "
	else
		PROPOUT="$PROPDEST/$2.properties"
  		echo -n "Using $PO for default language: "
	fi
	
	# Check if recompilation is necessary
	if [ $PROPOUT -nt $PO ]; then
		echo "Output is up to date." ;
	else
		echo "Creating .properties..."
		# Convert to properties and strip comments
		msgcat --properties-output $PO | ( echo '# This file is auto-generated. Do not edit' ; grep -v '^#\|^$' ) > $PROPOUT
	fi ;
}

if [ -z "$1" ]; then
  echo 'Compiling language files ...'
  
  for PO in ${INNAME}_*.po ; do
  	LANG=${PO##*${INNAME}_}
  	LANG=${LANG%.po}
	CLASSOUT="$DESTDIR/yajhfc/console/i18n/${INNAME}_$LANG.class"
	CLASSOUT1="$DESTDIR/yajhfc/console/i18n/${INNAME}_$LANG\$1.class"

  	echo -n "Using $PO for language $LANG: "
	
	# Check if recompilation is necessary
	if [ $CLASSOUT -nt $PO -a $CLASSOUT1 -nt $PO ]; then
		echo "Output is up to date." ;
	else
		echo "Compiling..."
	  	msgfmt --java2 -d$DESTDIR --resource=$RESOURCE --locale=$LANG $PO ;
	fi ;
  done ; 

  # Create .properties as this gives smaller files here
  for PO in CommandLineOpts*.po ; do
	catprops $PO CommandLineOpts
  done ;
else 
  LANGFILE="${INNAME}_$1.po"
  if [ -f $LANGFILE ]; then
    echo "Compiling $LANGFILE..." 
    # Always compile if the language is explicitely given
    msgfmt --java2 -d$DESTDIR --resource=$RESOURCE  --locale=$1 $LANGFILE ;
  else
    echo "No PO file for language $1 found." ;
  fi ;
fi

