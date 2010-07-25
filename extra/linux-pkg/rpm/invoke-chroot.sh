#!/bin/sh

CHROOTDIR=/datengrab/jonas/suse11.2
CHROOTRPMDIR=/home/jonas/RPM

if [ $# -lt 1 ]; then
  TARGETDIR=/home/jonas/java/yajhfc/temp/betas
else
  TARGETDIR="$1"
fi

####

RPMDIR=$CHROOTDIR/$CHROOTRPMDIR

cd `dirname $0`
SCRIPTDIR="$PWD"
OUTPUTDIR=$SCRIPTDIR/files

rm $RPMDIR/SOURCES/yajhfc* $RPMDIR/SPECS/yajhfc*

cd $OUTPUTDIR

cp yajhfc-*-rpmsrc.tgz $RPMDIR/SOURCES
cp yajhfc*.spec $RPMDIR/SPECS

cd $RPMDIR

echo 'Invoking chrooted SUSE rpmbuild...'
su - -c "$CHROOTDIR/chroot-suse.sh \"cd $CHROOTRPMDIR && chgrp users SOURCES/yajhfc* SPECS/yajhfc* && rpmbuild -ba SPECS/yajhfc*.spec\""

#cp RPMS/noarch/yajhfc*.rpm SRPMS/yajhfc*.src.rpm $TARGETDIR
cp `find RPMS SRPMS -name 'yajhfc*.rpm' -a -mmin -15` $TARGETDIR

