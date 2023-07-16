#!/bin/bash

if [ "$1" == "" ]; then
    echo "Usage: $(basename $0) ANGLE"
    exit 1
fi

readonly angle="$1"
readonly angleNNN=$(printf "%03d" $angle)
readonly pfmfile=image$angleNNN.pfm
readonly pngfile=cornell_$angleNNN.png

#time ./gradlew run --args="-d 500 500 $angle false $pfmfile pathTracer true false 1"
time ./gradlew run --args="render -a=0 --output=$pfmfile --input=firstImage.txt -n=10 --antialiasing=true -w=500 --height=500 --initSeq=$angle --initState=$angle"
#parallel -j NUM_OF_CORES ./parallel-generate-image.sh '{}' ::: $(seq 0 359)