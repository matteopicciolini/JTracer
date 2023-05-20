#!/bin/bash
for angle in $(seq 0 359); do
    # Angle with three digits, e.g. angle="1" → angleNNN="001"
    angleNNN=$(printf "%03d" $angle)
    ./build/install/raytracing/bin/raytracing -d 3600 3600 $angle false img$angleNNN.pfm
done

# -r 25: Number of frames per second
ffmpeg -r 25 -f image2 -s 640x480 -i img%03d.png \
    -vcodec libx264 -pix_fmt yuv420p \
    spheres-perspective.mp4