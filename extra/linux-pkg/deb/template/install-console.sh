#!/bin/sh
# "Installs" YajHFC-console in order to create a package

if [ -z "$1" ]; then
  echo "Usage: `basename $0` targetdir"
  exit 1
fi

PREFIX="$1/usr"

mkdir -p $PREFIX/bin
ln -s yajhfc $PREFIX/bin/cyajhfc

mkdir -p $PREFIX/share/man/man1
install -m 0644 cyajhfc.1 $PREFIX/share/man/man1

mkdir -p $PREFIX/share/doc/yajhfc-console
for FILE in console/doc/*; do
  install -m 0644 $FILE $PREFIX/share/doc/yajhfc-console/
done

mkdir -p $PREFIX/share/yajhfc
install -m 0644 yajhfc-console.jar $PREFIX/share/yajhfc


