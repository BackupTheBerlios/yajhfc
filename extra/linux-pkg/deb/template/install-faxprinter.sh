#!/bin/sh
# "Installs" YajHFC in order to create a package

if [ -z "$1" ]; then
  echo "Usage: `basename $0` targetdir"
  exit 1
fi

PREFIX="$1"

cd cups

mkdir -p $PREFIX/etc/yajhfc
install -m 0644 settings.override $PREFIX/etc/yajhfc

mkdir -p $PREFIX/etc/xdg/autostart
install -m 0644 yajhfc-autostart.desktop $PREFIX/etc/xdg/autostart

mkdir -p $PREFIX/usr/share/yajhfc
install -m 0644 yajhfc.ppd $PREFIX/usr/share/yajhfc

mkdir -p $PREFIX/usr/lib/cups/backend
install -m 0755 yajhfc $PREFIX/usr/lib/cups/backend


