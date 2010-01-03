#!/bin/bash
# Create DEB package

set -e

BUILDDIR=/tmp/yajhfc-deb
WORKSPACE=$HOME/java/workspace/yajhfc
DEBDIR=$HOME/java/yajhfc/temp
REPOSITORY=$HOME/java/yajhfc/temp/deb-repository

if [ $# -lt 1 ]; then
  echo "Usage: makepackage.sh VERSION [PACKAGEVERSION]"
  exit 1
fi

cd `dirname $0`

VERSION=$1
if [ $# -lt 2 ]; then
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
popd

cat - template/debian/changelog <<EOF >$TARGETDIR/debian/changelog
yajhfc ($PACKAGEVERSION) unstable; urgency=low

  * Package build for YajHFC $VERSION

 -- Jonas Wolz <jonas.wolz@freenet.de>  `date -R`

EOF

cp $TARGETDIR/debian/changelog template/debian/changelog.full

echo 'Building Debian packets...'
pushd $TARGETDIR
dpkg-buildpackage -rfakeroot

cd ..
cp *.deb $DEBDIR

echo 'Updating repository...'
cp *.deb $REPOSITORY/stable

cd $REPOSITORY
apt-ftparchive packages stable > stable/Packages
gzip -c9 stable/Packages > stable/Packages.gz
bzip2 -c stable/Packages > stable/Packages.bz2

apt-ftparchive -o 'APT::FTPArchive::Release::Suite=stable' release stable > stable/Release
gpg -u jonas.wolz@freenet.de --output stable/Release.gpg -ba stable/Release

