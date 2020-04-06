#!/bin/bash

#This script works for all tests except logout 
#This is because windows terminates every line with ^M\n where unix is just \n
#The expected output terminates with \n 
nl = '\n'

for transaction in "Phase1/*"
do
    for test in $transaction/*
    do

    #Runs the test puts it in the same output folder as the comparison
    Debug/./FinalProject.exe userData.txt availableItems.txt <$test/input/input.inp >$test/output/actualOutput.atf   
    dos2unix $test/output/actualOutput.atf > /dev/null 2>&1 #Throws output of this 

    #checks if the files are different
    if diff $test/output/actualOutput.atf $test/output/output_terminal.etf 
    then
        echo $test succeeded 
    else
        echo $test failed
    fi
    echo "$nl" #new line
    done
done