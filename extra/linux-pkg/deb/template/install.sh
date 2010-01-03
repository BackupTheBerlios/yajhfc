#!/bin/sh
# "Installs" YajHFC in order to create a package

if [ -z "$1" ]; then
  echo "Usage: `basename $0` targetdir"
  exit 1
fi

PREFIX="$1/usr"

mkdir -p $PREFIX/bin
install -m 0755 yajhfc $PREFIX/bin

mkdir -p $PREFIX/share/man/man1
install -m 0644 yajhfc.1 $PREFIX/share/man/man1

mkdir -p $PREFIX/share/doc/yajhfc
for FILE in doc/*; do
  install -m 0644 $FILE $PREFIX/share/doc/yajhfc
done

mkdir -p $PREFIX/share/yajhfc
install -m 0644 yajhfc.jar $PREFIX/share/yajhfc

mkdir -p $PREFIX/share/pixmaps
install -m 0644 yajhfc-48x48.png $PREFIX/share/pixmaps/yajhfc.png
install -m 0644 yajhfc.xpm $PREFIX/share/pixmaps/yajhfc.xpm

mkdir -p $PREFIX/share/applications
install -m 0644 yajhfc.desktop $PREFIX/share/applications
