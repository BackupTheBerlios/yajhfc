#!/bin/bash
# Create RPM package

set -e
shopt -s extglob

BUILDDIR=/tmp/yajhfc-rpm
WORKSPACE=$HOME/java/workspace/yajhfc

if [ $# -lt 1 ]; then
  echo "Usage: makepackage.sh VERSION [PACKAGEVERSION]"
  exit 1
fi

cd `dirname $0`
SCRIPTDIR="$PWD"
OUTPUTDIR=$SCRIPTDIR/files

ORIGINALVERSION=$1
if [ $# -lt 2 ]; then
  LASTCOMP=${ORIGINALVERSION##*.}
  APPENDIX=${LASTCOMP##*([0-9])}
  
  if [ -n "$APPENDIX" ]; then
   NUMVER=${LASTCOMP%%+([A-Za-z])*([0-9])}
   (( NUMVER=NUMVER-1 ))
   case $APPENDIX in
   beta*)
     EXTRANUM=90
   ;;
   alpha*)
     EXTRANUM=80
   ;;
   rc*)
     EXTRANUM=99
   ;;
   *)
     EXTRANUM=77
   ;;
   esac
   VERSION=${ORIGINALVERSION%.*}.$NUMVER.$EXTRANUM$APPENDIX
  else
   VERSION=${ORIGINALVERSION}
  fi
else
  VERSION=${2}
fi

echo 'Copying template...'

TARGETDIR=$BUILDDIR/yajhfc-$VERSION
if [ -e $TARGETDIR ]; then
  rm -rf $TARGETDIR
fi

if [ ! -f $BUILDDIR ]; then
  mkdir -p $BUILDDIR
fi

cp -a template $TARGETDIR
cd $WORKSPACE
cp build/yajhfc.jar $TARGETDIR
cp doc/faq.html doc/faq.css doc/footnote.png doc/faq.pdf README.txt COPYING $TARGETDIR/doc

cd $BUILDDIR

tar czvf $OUTPUTDIR/yajhfc-$VERSION-rpmsrc.tgz yajhfc-$VERSION

cd $SCRIPTDIR

for SPEC in yajhfc*.spec ; do
  sed -e "s/§VERSION§/$VERSION/g" -e "s/§ORIGINALVERSION§/$ORIGINALVERSION/g" $SPEC > $OUTPUTDIR/$SPEC
done


