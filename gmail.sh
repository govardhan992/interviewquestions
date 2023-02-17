#!/bin/bash


filename=$1

while read -r line; do
        if [[ $line == "@gmail.com" ]]; then
                echo "$line"
        fi
done < $filename
