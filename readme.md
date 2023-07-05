<img align="right" img src="https://github.com/matteopicciolini/ray_tracing/assets/116730685/f64d7b32-25f2-4a6d-a465-07ba183f2b0b" alt="cervo e gatto specchiati"  width="250">

 <h1 align="center">  Ray Tracing Project </h1> <br>


*Specular view of deer and cat, realized with two triangle mashes*

This is a project developed by Matteo Picciolini and [Francesco Villa][1] to reproduce some photorealistic images by using Numerical Methods. This came from a course we attended in Unimi, called *Numeric calculus for photorealistic images generation*, directed by our professor [Maurizio Tomasi][2].
Our library is written in java, a very powerful lenguage we learned in this course. 


## Table of Contents
- [Overview](#Overview)
- [Prerequisites](#Prerequisites)
- [Usage](#Usage)
- [Render-mode](#Render-mode)
- [How-to-create-input-files](#How-to-create-input-files)
- [Demo-mode](#Demo-mode)
- [Convert-mode](#Convert-mode)
- [Documentation](#Documentation)
- [License](#License)
- [Gallery](#Gallery)
- [Issue-tracking](#Issue-tracking)
- [Overview](#Overview)

## Overview 

The purpose of the course is precisely the generation of photorealistic images using a Ray Tracer, a library provided on this website called J-Tracer, written in Java.
The algorithm used employs the backwards ray tracing method, where for each pixel of the desired image, a user-defined number of rays are launched, tracing the geometric path of the light rays back to the source. The presence of a camera behind the screen is simulated to form a proper image.

Our code allows for the implementation of various types of materials (we will provide the complete table in a `.txt` file later) as well as different types of shapes, such as spheres, planes, cubes, various types of conics, triangles, and triangle meshes (the use of triangle meshes will be thoroughly explained).
Once the desired shapes are defined and the scene is rendered using the methods described earlier, the resulting image is in HDR format (a `.pfm` file), and subsequently converted into an LDR file such as `.jpg` or .png based on the user's needs.

## Prerequisites


Using the Java language and the Gradle compiler, our luck was that we didn't need to install external libraries. In fact, it was enough to include the necessary libraries for converting from HDR to LDR images and for running the tests in the Gradle.build file.
Having Java 18 correttoy installed and Gradle v7.6.1 are the only prerequisites to use this program.
- [Java-corretto-v18](https://www.oracle.com/java/technologies/javase/jdk18-archive-downloads.html)
- [Gradle-compiler](https://gradle.org/releases/)

## Usage

To use this library, you need to clone the repository into a local folder using the command:

    git clone git@github.com:matteopicciolini/ray_tracing.git

Then, you can run the command "gradlew test" to verify that the package functions correctly by executing the tests.
Afterwards, you can run the code in different modes to meet the user's specific needs:
- Demo-mode
  This mode is desighed to do fast projects in order to design the true scene that will be implemented inthe following points.
  To strart running **demo mode**, the user should use the main command:

      gradlew run demo

  now it follows the full list fo args and their default settings:
  
  -     --angle-deg            float: Angle of view. Default: 0.
  -     --algorithm            string: Algorithm of rendering. Default: pathTracer.
  -     --antialiasing         bool: Use antialiasing algorithm. Default: false.
  -     --convertToPNG         bool: At the end of the program execution, automatically convert the PFM file to PNG.
                               Default: true.
  -     --deletePFM            bool: At the end of the program execution, keep only the LDR image, deleting the PFM.
                               Default: false.
  -     --factor               float: Multiplicative factor. Default: 0.18.
  -     --flushFrequence       int: Frequency of flush (expressed in number of processed pixels) of the progress bar.
                               Default: 100         
  -     --gamma                float: Exponent for gamma-correction. Default: 2.2.
  -     --help                 Show this help message and exit.
  -     --height               int: Height of the image. Default: 480.
  -     --luminosity           float: Luminosity of the image. 	 Default: It is calculated for the pathTracer; otherwise,
                               it is set to 0.5.
  -     --maxDepth             int: Maximum recursion depth
  -     --numRays              int: Number of rays per pixel
  -     --nThreads             int: Number of threads to use for parallelization. Default: 4.
  -     --orthogonal           bool: Use an orthogonal camera. Default: false.
  -     --output       string: Path of the output ldr file. Default: img.pfm.
  -     --parallelAntialiasing bool: Parallelize antialiasing algorithm. Default: false.
  -     --russianRouletteLimit int: Russian roulette limit. Default: 3.
  -     --version              Print version information and exit.
  -     --width                int: Width of the image. Default: 480.



[1]: https://github.com/fravij99
[2]: https://github.com/ziotom78
