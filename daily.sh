#!/bin/sh

cd FrontEnd/Debug

#Change max to set iterations
sessions=3
for i in `seq 1 $sessions`
do
  ./FrontEnd.exe $i
done

cd ../../DailyTransactions

cat * > MergedDailyTransactions.txt

cd ../BackEnd

javac Main.java
java Main

cd ..

cp availableItems.txt AvailableItemsPrintout/availableItems.txt
cp newAvailableItems.txt AvailableItemsPrintout/newAvailableItems.txt

rm userData.txt availableItems.txt
mv newUserData.txt userData.txt
mv newAvailableItems.txt availableItems.txt

cd DailyTransactions

rm *.txt