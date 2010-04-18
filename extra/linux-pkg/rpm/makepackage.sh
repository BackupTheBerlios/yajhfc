#!/bin/bash
# Create DEB package

set -e

BUILDDIR=/tmp/yajhfc-rpm
WORKSPACE=$HOME/java/workspace/yajhfc

if [ $# -lt 1 ]; then
  echo "Usage: makepackage.sh VERSION [PACKAGEVERSION]"
  exit 1
fi

cd `dirname $0`
SCRIPTDIR="$PWD"
OUTPUTDIR=$SCRIPTDIR/files

VERSION=$1
if [ $# -lt 2 ]; then
  PACKAGEVERSION=${VERSION}
else
  PACKAGEVERSION=${2}
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
  sed -e "s/§VERSION§/$VERSION/g" -e "s/§PACKAGEVERSION§/$PACKAGEVERSION/g" $SPEC > $OUTPUTDIR/$SPEC
done


