#!/bin/bash

if [ "$1" == "" ]; then
    echo "Usage: $(basename $0) ANGLE"
    exit 1
fi

readonly angle="$1"
readonly angleNNN=$(printf "%03d" $angle)
readonly pfmfile=image$angleNNN.pfm
readonly pngfile=image$angleNNN.png

time ./build/install/raytracing/bin/raytracing -d 460 460 $angle false $pfmfile flat

#parallel -j NUM_OF_CORES ./generate-image.sh '{}' ::: $(seq 0 359)