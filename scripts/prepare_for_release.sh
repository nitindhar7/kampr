#!/bin/sh

echo '\nLogging:             ' && egrep -rn 'Log\.' src/
echo '\nDebuggable:          ' && egrep -rn 'android:debuggable' AndroidManifest.xml
echo '\nVersion Code:        ' && egrep -rn 'android:versionCode' AndroidManifest.xml
echo '\nVersion Name:        ' && egrep -rn 'android:versionName' AndroidManifest.xml
echo '\nIcon:                ' && egrep -rn 'android:icon' AndroidManifest.xml
echo '\nApp Name:            ' && egrep -rn 'android:label' AndroidManifest.xml
echo '\nMinimum SDK Version: ' && egrep -rn 'android:minSdkVersion' AndroidManifest.xml
echo '\nTarget SDK Version:  ' && egrep -rn 'android:targetSdkVersion' AndroidManifest.xml
