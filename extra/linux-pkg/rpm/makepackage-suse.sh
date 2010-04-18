#!/bin/sh
# Part of the build process to execute under SUSE
set -e

RPMDIR="/home/jonas/RPM"

cd `dirname $0`
SCRIPTDIR=`pwd`

OUTDIR=$SCRIPTDIR/files

cd $OUTDIR

cp yajhfc-*-rpmsrc.tgz $RPMDIR/SOURCES
cp yajhfc*.spec $RPMDIR/SPECS

cd $RPMDIR

rpmbuild -ba SPECS/yajhfc*.spec

cp RPMS/noarch/yajhfc*.rpm SRPMS/yajhfc*.src.rpm $OUTDIR
