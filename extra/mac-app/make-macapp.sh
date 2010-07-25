#!/bin/bash
# Create MAC application as ZIP file
# Usage: make-macapp.sh VERSION [outputdir]

set -e

BUILDDIR=/tmp/yajhfc-macapp
WORKSPACE=$HOME/java/workspace/yajhfc
if [ $# -ge 2 ]; then
  TARGETDIR="$2"
else
  TARGETDIR=$HOME/java/yajhfc/temp/betas
fi

if [ $# -lt 1 ]; then
  echo "Usage: make-macapp.sh VERSION"
  exit 1
fi

cd `dirname $0`

SCRIPTDIR="$PWD"

VERSION=$1

echo 'Copying template...'

if [ ! -f $BUILDDIR ]; then
  mkdir -p $BUILDDIR
fi

mkdir -p $BUILDDIR/yajhfc.app/Contents
mkdir -p $BUILDDIR/yajhfc.app/Contents/Resources/Java/

sed -e "s/§VERSION§/$VERSION/g" Info.plist.template > $BUILDDIR/yajhfc.app/Contents/Info.plist

cp $WORKSPACE/build/yajhfc.jar $BUILDDIR/yajhfc.app/Contents/Resources/Java/

ZIPFILE=$TARGETDIR/yajhfc-macapp-$VERSION.zip
cp yajhfc-template.zip $ZIPFILE

cd $BUILDDIR
echo "Creating $ZIPFILE..."
zip $ZIPFILE yajhfc.app/Contents/Info.plist yajhfc.app/Contents/Resources/Java/yajhfc.jar

