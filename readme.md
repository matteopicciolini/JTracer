<img align="right" img src="https://github.com/matteopicciolini/ray_tracing/assets/116730685/f3f3a503-224d-4669-ae42-efb1931727b7" alt="Competizione tra cervo gatto e lupo"  width="300">
<h1 align="center">  Ray Tracing Project </h1> <br>

This project was developed by [Matteo Picciolini][piccio] and [Francesco Villa][fravi] to generate photorealistic images by solving the rendering equation using Numerical Methods.
This project is developed for ***Numeric calculus for photorealistic images generation*** course, held by professor [Maurizio Tomasi][ziotom] at University of Milan, Physics Department.
The project has been written in `Java` and can be compiled using `Gradle`. Additionally, it is integrated with some bash scripts that allow generating animations using the open-source software `FFmpeg`.

*Competition between deer, cat and wolf, realized with boxes, triangle meshes, sphere and plane*

## Table of Contents
- [Overview](#Overview)
- [Prerequisites](#Prerequisites)
- [Usage](#Usage)
    - [Examples](#Examples)
    - [Demo-mode](#Demo-mode)
    - [Shapes](#Shapes)
- [Documentation](#Documentation)
- [License](#License)
- [Gallery](#Gallery)
- [Issue-tracking](#Issue-tracking)


## Overview 

The purpose of the course is precisely the generation of photorealistic images using a Ray Tracer, a library provided on this website called J-Tracer, written in Java.
The algorithm used employs the backwards ray tracing method, where for each pixel of the desired image, a user-defined number of rays are launched, tracing the geometric path of the light rays back to the source. The presence of a camera behind the screen is simulated to form a proper image.

Our code allows for the implementation of various types of materials (we will provide the complete table in a `.txt` file later) as well as different types of shapes, such as spheres, planes, cubes, various types of conics, triangles, and triangle meshes (the use of triangle meshes will be thoroughly explained).
Once the desired shapes are defined and the scene is rendered using the methods described earlier, the resulting image is in HDR format (a `.pfm` file), and subsequently converted into an LDR file such as `.jpg` or .png based on the user's needs.

## Prerequisites
For this project, we are using [version 18.0.2][corretto] of `Amazon Corretto`, an `OpenJDK` distribution
provided by `Amazon Web Services` (`AWS`) designed to be a stable, secure, and free version of `Java`.
It is possible to download the correct version of `Java` for your operating system on the `Oracle` website at this [link][correttoDownload].
On a Debian-based system, you can install it by executing the following commands:
```
wget https://download.oracle.com/java/18/archive/jdk-18.0.2.1_linux-x64_bin.tar.gz
tar -xvf jdk-18.0.2.1_linux-x64_bin.tar.gz
```
Additionally, add the following line to the `.bashrc` file:
```
export JAVA_HOME=/path/to/.jdks/corretto-18.0.2
```
Please note that the provided path `/path/to/.jdks/corretto-18.0.2` assumes a specific directory structure. 
Make sure to adjust the path according to your actual system configuration.


For the project compilation, we relied on the `Gradle` build automation tool. 
`Gradle` allows specifying project dependencies in the [build file][build] so that it can automatically 
download the necessary external libraries during the compilation phase, ensuring that the required 
classes and methods are available for the proper execution of the program. 

For this project, the following external libraries have been imported:
- `JUnit Jupiter` ([version 5.9.2][junit5]), required for writing automated tests;
- `Apache Commons CLI` ([version 1.4][cli]), used for creating a command-line interface.

It is not necessary to install `Gradle`, as the project provides an executable [`./gradlew`][gradlew] 
that can be used to avoid local installation of `Gradle`.



## Usage
In order to use and modify the code you can clone the repository with the command:
```
git clone git@github.com:matteopicciolini/ray_tracing.git
```
Then, you can run the command `./gradlew test` to verify that the package functions correctly by executing the tests.
Project can be built from source code running the command `./gradlew build`.

To run the code, you can proceed in two ways:

1. By generating an executable using the command ```./gradlew installDist``` and you can launch it with the specified parameters below.
2. By using the file `./gradlew` and running it in `run` mode: `./gradlew run`.
With the `--args` flag, you can pass parameters from the command line. For example:
```
./gradlew run --args="render --input=image.txt"
```


The main feature of the program is the ***renderer mode***, which reads an externali file `.txt` containing the scene that will be renderized. It is necessary to use this command to activate the renderer mode

     gradlew run --args --all_the_arguments_needed

  now it follows the full list fo args and their default settings:
  
  - `--angle-deg`            float: Angle of view. Default: 0.
  - `--algorithm`            string: Algorithm of rendering. Default: pathTracer.
  - `--antialiasing`         bool: Use antialiasing algorithm. Default: false.
  - `--convertToPNG`         bool: At the end of the program execution, automatically convert the PFM file to PNG.
                               Default: true.
  - `--deletePFM`            bool: At the end of the program execution, keep only the LDR image, deleting the PFM.
                               Default: false.
  - `--factor`               float: Multiplicative factor. Default: 0.18.
  - `--flushFrequence`       int: Frequency of flush (expressed in number of processed pixels) of the progress bar.
                               Default: 100         
  - `--gamma`                float: Exponent for gamma-correction. Default: 2.2.
  - `--help`                 Show this help message and exit.
  - `--height`               int: Height of the image. Default: 480.
  - `--luminosity`           float: Luminosity of the image. 	 Default: It is calculated for the pathTracer; otherwise,
                               it is set to 0.5.
  - `--maxDepth`             int: Maximum recursion depth
  - `--numRays`              int: Number of rays per pixel
  - `--nThreads`             int: Number of threads to use for parallelization. Default: 4.
  - `--orthogonal`          bool: Use an orthogonal camera. Default: false.
  - `--output`       string: Path of the output ldr file. Default: img.pfm.
  - `--parallelAntialiasing` bool: Parallelize antialiasing algorithm. Default: false.
  - `--russianRouletteLimit` int: Russian roulette limit. Default: 3.
  - `--version`              Print version information and exit.
  - `--width`                int: Width of the image. Default: 480.

  BY the end of the execution, there's no need to specify the conversion from HDR image to LDR image cause of the default settings of `--convertToPNG=true`

 ### Examples
  Our code needs very specific sintax rules, so There are many examples on how to write a `.txt` file with correct instructions:
 - [Cornell Box]()
 - [Example-2]()


<img align="right" img src="https://github.com/matteopicciolini/ray_tracing/assets/116730685/c40b165f-f7d5-41a1-910a-c10b1e454391"  width="280">

 ### Demo mode
   This peculiar mode is usually called where there is some need tu build a quick scene without pass trough the `.txt` file.
   To strart running **demo mode**, the user should use the main command:

      gradlew run demo --args

  To obtain the full list of arguments the command is the following:

      gradlew run demo -h

### Shapes
   The complete list of available shapes are the following:
  - Sphere
  - Planes
  - Cubes
  - Quadratics shapes
  - Triangles
  - Triangle Meshes 💎

Concerning about the last shape, using triangle meshes is a bit complex than other shapes. In facts, in order to build a scene with some specific shapes (tetrahedron, icosahedron and all the possible shapes like deers or cats) it is necessary to include a particular file `.txt` with the following structure:

```
# vertices
v 0.00 0.00 0.00
v 1.00 0.00 0.00
v 0.00 1.00 0.00
v 0.00 0.00 1.00
 
# faces
f 1 3 2
f 1 2 4
f 1 3 4
f 2 3 4
```

In our project, there is also a simple script in python wicht accept a `.obj` file as input and generate a `.txt` file as output with the prefìvious features.

## Documentation

## Gallery

In this section there are part of the images we've generated:

 <p float="center">
  <img src="https://github.com/matteopicciolini/ray_tracing/assets/116730685/b65d86ed-dec6-400e-94a0-27c83ba3741c" height="200" />
  <img src="https://github.com/matteopicciolini/ray_tracing/assets/116730685/04ccd682-2dd5-4dcb-a958-99a90671ec32" height="200" /> 
  <img src="https://github.com/matteopicciolini/ray_tracing/assets/116730685/53b846d7-d331-4170-9fbc-4d174101661e" height="200" /> 
  <img src="https://github.com/matteopicciolini/ray_tracing/assets/116730685/c5def2d6-d87c-4358-bfb5-d5a4ce9842db" height="200" /> 
  <img src="https://github.com/matteopicciolini/ray_tracing/assets/116730685/3fc5292b-b65c-4160-b08c-4e6a9f44b7f7" height="200" /> 
  <img src="https://github.com/matteopicciolini/ray_tracing/assets/116730685/77a3f104-5e0c-42b1-9674-5bdc1823f874" height="200" />
  <img src="https://github.com/matteopicciolini/ray_tracing/assets/116730685/ef222fc3-8a45-4b92-a90c-172a93023777" height="200" />
  <img src="https://github.com/matteopicciolini/ray_tracing/assets/116730685/f9b36d44-0663-402b-ab25-75c4f1466532" height="200" />


  
</p>



## Issue-Tracking
If the user ecounters a bug or any type of issues related to the code we have written, please let us know. Contact us via e-mail of the following addresses:
- fravilla30@gmail.com
- picciolinimatteo@gmail.com



[fravi]: https://github.com/fravij99
[ziotom]: https://github.com/ziotom78
[piccio]: https://github.com/matteopicciolini
[build]: https://github.com/matteopicciolini/ray_tracing/blob/master/build.gradle
[cli]: https://commons.apache.org/proper/commons-cli/
[junit5]: https://junit.org/junit5/
[corretto]: https://aws.amazon.com/it/about-aws/whats-new/2022/03/amazon-corretto-18/
[correttoDownload]: https://www.oracle.com/java/technologies/javase/jdk18-archive-downloads.html
[gradlew]: https://github.com/matteopicciolini/ray_tracing/blob/master/gradlew
