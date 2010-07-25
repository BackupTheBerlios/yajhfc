#!/bin/sh
# Update APT repository in the current directory
set -e

apt-ftparchive packages stable > stable/Packages
gzip -c9 stable/Packages > stable/Packages.gz
bzip2 -c stable/Packages > stable/Packages.bz2

apt-ftparchive sources stable > stable/Sources
gzip -c9 stable/Sources > stable/Sources.gz
bzip2 -c stable/Sources > stable/Sources.bz2


apt-ftparchive -o 'APT::FTPArchive::Release::Suite=stable' release stable > stable/Release
rm -f stable/Release.gpg
gpg -u jonas.wolz@freenet.de --output stable/Release.gpg -ba stable/Release

