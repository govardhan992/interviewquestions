#!/bin/bash

echo"enter number of fibonacci series"
read num
a=0
b=1
count=2
echo"the fibanacci series are"

echo "$a"
echo "$b"
while [ count -le $num]
do
fib=`expr $a + $b`
a=$b
b=$fib
echo "$fib"
count=`expr $count +1`
done
