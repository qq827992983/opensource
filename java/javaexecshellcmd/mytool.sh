#!/usr/bin/env bash
arg_count=$#
echo "arg_count=$arg_count"

while [ $# -gt 0 ];
do
        echo $1
        shift
done
