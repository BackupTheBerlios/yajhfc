#!/bin/sh
# Usage: makedistribution [outputdir]

set -e

EXTRADIR=~/java/yajhfc/extra
WORKSPACE=~/java/workspace

if [ $# -ge 1 ]; then
  OUTPUT="$1"
  OWNSUBDIR=0
else
  OUTPUT=~/java/yajhfc/temp/betas
  OWNSUBDIR=1
fi


build() {
ANT=ant
ISCC="wine \"C:\Program Files\Inno Setup 5\ISCC\""
ISSFILE=setup3.iss

read YAJVER YAJVERDOT FOPVER FOPVERDOT  <<EOF
$(perl -lne 'if ($_ =~/public static final String AppVersion = "(.*?)";/) { $Ver=$1; $Ver=~tr/./_/; printf("%s %s ", $Ver, $1);}' $WORKSPACE/yajhfc/src/yajhfc/Utils.java $WORKSPACE/FOPPlugin/src/yajhfc/faxcover/fop/EntryPoint.java )

EOF

echo "Building distribution for:"
echo "YajHFC Version: $YAJVERDOT (Tag: $YAJVER); FOP Plugin version: $FOPVERDOT (Tag: $FOPVER)"
echo

if [ $OWNSUBDIR -ne 0 ]; then
  OUTPUT=$OUTPUT/$YAJVERDOT
  mkdir -p $OUTPUT
fi

echo "Building YajHFC..."
cd $WORKSPACE/yajhfc
$ANT clean fulldist
echo "Building FOPPlugin..."
cd $WORKSPACE/FOPPlugin
$ANT clean fulldist
echo "Building yajhfc-console..."
cd $WORKSPACE/yajhfc-console
$ANT clean fulldist
echo "Building Windows Setup..."
cd $EXTRADIR/winsetup

echo "Converting README files to Windows format..."
READMES=""
for R in $WORKSPACE/yajhfc/README*.txt; do
  READMES="$READMES $R temp/`basename $R`"
done
windowsify -i utf-8 -o utf-8 $READMES 

echo "Building YajHFC setup..."
eval $ISCC /dVERSION=$YAJVERDOT /dFOPVersion=$FOPVERDOT $ISSFILE
echo "Building YajHFC with FOPPlugin setup..."
eval $ISCC /dVERSION=$YAJVERDOT /dFOPVersion=$FOPVERDOT /dWITHFOP $ISSFILE

echo "Copying files to output..."
cp $WORKSPACE/yajhfc/build/yajhfc.jar $OUTPUT/yajhfc-$YAJVER.jar
cp $WORKSPACE/yajhfc/build/yajhfc-src.zip $OUTPUT/yajhfc-$YAJVER-src.zip
cp $WORKSPACE/FOPPlugin/build/FOPPlugin.zip $OUTPUT/yajhfc-$YAJVER-FOPPlugin-$FOPVER.zip
cp $WORKSPACE/FOPPlugin/build/FOPPlugin-src.zip $OUTPUT/FOPPlugin-$FOPVER-src.zip
cp $WORKSPACE/yajhfc-console/build/yajhfc-console.zip $OUTPUT/yajhfc-console-$YAJVER.zip
cp $WORKSPACE/yajhfc-console/build/yajhfc-console-src.zip $OUTPUT/yajhfc-console-$YAJVER-src.zip

cp $EXTRADIR/winsetup/Output/setup.exe $OUTPUT/yajhfc-$YAJVER-setup.exe
cp $EXTRADIR/winsetup/Output/Setup-FOPPlugin.exe $OUTPUT/yajhfc-$YAJVER-FOPPlugin-$FOPVER-setup.exe

cat <<EOF > $OUTPUT/versioninfo.xml
<?xml version="1.0"?>
<yajhfcupdatefile>
	<currentVersion>$YAJVERDOT</currentVersion>
	<releaseDate>`date +%Y-%m-%d`</releaseDate>
	<infoURL>http://yajhfc.berlios.de/</infoURL>
</yajhfcupdatefile>
EOF

echo "Creating .deb package..."
cd $EXTRADIR/linux-pkg/deb
./makepackage.sh $YAJVERDOT "" "$OUTPUT"

echo "Creating MAC app..."
cd $EXTRADIR/mac-app
./make-macapp.sh $YAJVERDOT "$OUTPUT"

echo "Creating RPM package..."
cd $EXTRADIR/linux-pkg/rpm
./makepackage.sh $YAJVERDOT
./invoke-chroot.sh "$OUTPUT"

}

build 2>&1 | tee build.log
