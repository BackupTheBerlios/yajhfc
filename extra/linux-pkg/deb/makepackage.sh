#!/bin/bash
# Create DEB package
# Usage: Usage: makepackage.sh VERSION [PACKAGEVERSION] [outputdir]"

if [ $# -lt 1 ]; then
  echo "Usage: makepackage.sh VERSION [PACKAGEVERSION] [outputdir]"
  exit 1
fi

set -e

BUILDDIR=/tmp/yajhfc-deb
WORKSPACE=$HOME/java/workspace/yajhfc

if [ $# -ge 3 ]; then
  DEBDIR="$3"
else
  DEBDIR=$HOME/java/yajhfc/temp/betas
fi
REPOSITORY=$DEBDIR/deb-repository

cd `dirname $0`

SCRIPTDIR="$PWD"

VERSION=$1
if [ $# -lt 2 -o -z "$2" ]; then
  PACKAGEVERSION=${VERSION}-1
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
pushd $WORKSPACE
cp build/yajhfc.jar $TARGETDIR
cp doc/faq.html doc/faq.css doc/footnote.png doc/faq.pdf README.txt $TARGETDIR/doc
cd ../FOPPlugin
cp build/FOPPlugin.jar $TARGETDIR/fopplugin
cp dist/readme.pdf $TARGETDIR/fopplugin
cp -R dist/examples $TARGETDIR/fopplugin/examples
popd

if grep -c "yajhfc ($PACKAGEVERSION) unstable" template/debian/changelog > /dev/null; then
  true # Do nothing 
else
  cat - template/debian/changelog <<EOF >$TARGETDIR/debian/changelog
yajhfc ($PACKAGEVERSION) unstable; urgency=low

  * Package build for YajHFC $VERSION

 -- Jonas Wolz <jonas.wolz@freenet.de>  `date -R`

EOF

  cp $TARGETDIR/debian/changelog template/debian/changelog.full
fi;

echo 'Building Debian packets...'
cd $TARGETDIR
dpkg-buildpackage -rfakeroot

cd ..
#cp *.deb $DEBDIR

for DIR in $REPOSITORY/stable/deb-all $REPOSITORY/stable/sources ; do
   if [ ! -d $DIR ]; then
     mkdir -p $DIR
   fi
done

echo 'Updating repository...'
cp *.deb    $REPOSITORY/stable/deb-all
cp *.dsc    $REPOSITORY/stable/sources
cp *.tar.gz $REPOSITORY/stable/sources

for F in *.deb ; do
  ln -f $REPOSITORY/stable/deb-all/$F $DEBDIR/$F
done
for F in *.dsc *.tar.gz ; do
  ln -f $REPOSITORY/stable/sources/$F $DEBDIR/$F
done

cd $REPOSITORY
$SCRIPTDIR/updaterepository.sh

