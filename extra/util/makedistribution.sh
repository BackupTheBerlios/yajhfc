#!/bin/sh

EXTRADIR=~/java/yajhfc/extra
WORKSPACE=~/java/workspace
OUTPUT=~/java/yajhfc/temp

ANT=ant
ISCC="wine \"C:\Program Files\Inno Setup 5\ISCC\""

read YAJVER YAJVERDOT FOPVER FOPVERDOT  <<EOF
$(perl -lne 'if ($_ =~/public static final String AppVersion = "(.*?)";/) { $Ver=$1; $Ver=~tr/./_/; printf("%s %s ", $Ver, $1);}' $WORKSPACE/yajhfc/src/yajhfc/Utils.java $WORKSPACE/FOPPlugin/src/yajhfc/faxcover/fop/EntryPoint.java )

EOF

echo "Building distribution for:"
echo "YajHFC Version: $YAJVERDOT (Tag: $YAJVER); FOP Plugin version: $FOPVERDOT (Tag: $FOPVER)"
echo

echo "Building YajHFC..."
cd $WORKSPACE/yajhfc
$ANT clean fulldist
echo "Building FOPPlugin..."
cd $WORKSPACE/FOPPlugin
$ANT clean fulldist
echo "Building Windows Setup..."
cd $EXTRADIR/winsetup

READMES=""
for R in $WORKSPACE/yajhfc/README*.txt; do
  READMES="$READMES $R temp/`basename $R`"
done
windowsify -i utf-8 -o utf-8 $READMES 

eval $ISCC /dVERSION=$YAJVERDOT /dFOPVersion=$FOPVERDOT setup2.iss
eval $ISCC /dVERSION=$YAJVERDOT /dFOPVersion=$FOPVERDOT /dWITHFOP setup2.iss

echo "Copying files to output..."
cp $WORKSPACE/yajhfc/build/yajhfc.jar $OUTPUT/yajhfc-$YAJVER.jar
cp $WORKSPACE/yajhfc/build/yajhfc-src.zip $OUTPUT/yajhfc-$YAJVER-src.zip
cp $WORKSPACE/FOPPlugin/build/FOPPlugin.zip $OUTPUT/yajhfc-$YAJVER-FOPPlugin-$FOPVER.zip
cp $WORKSPACE/FOPPlugin/build/FOPPlugin-src.zip $OUTPUT/FOPPlugin-$FOPVER-src.zip

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

