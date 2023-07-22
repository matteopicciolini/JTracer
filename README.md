<img align="right" src="https://github.com/matteopicciolini/JTracer/assets/116730685/5fd1b57c-910c-461e-af29-7c2654a78bd3"  width="300">

<h1 align="center">  Ray Tracing Project </h1> <br>

![GitHub release (with filter)](https://img.shields.io/github/v/release/matteopicciolini/JTracer)
![GitHub top language](https://img.shields.io/github/languages/top/matteopicciolini/JTracer)
![GitHub contributors](https://img.shields.io/github/contributors/matteopicciolini/JTracer)
![GitHub Release Date - Published_At](https://img.shields.io/github/release-date/matteopicciolini/JTracer)
![GitHub](https://img.shields.io/github/license/matteopicciolini/JTracer)

This project was developed by [Matteo Picciolini][piccio] and [Francesco Villa][fravi] to generate photorealistic images by using Numerical Methods.
This project is developed for ***Numeric calculus for photorealistic images generation*** course, held by professor [Maurizio Tomasi][ziotom] at University of Milan, Physics Department.
The project has been written in `Java` and can be compiled using `Gradle`. Additionally, it is integrated with some bash scripts that allow generating animations using the open-source software `FFmpeg`. It is available for `Linux`, `Windows` and `macOS`.

## Table of Contents
- [Overview](#overview)
- [Prerequisites](#prerequisites)
- [Usage](#usage)
    - [Modes](#modes)
    - [Tutorials](#tutorials)
    - [Shapes](#shapes)
- [Animations](#animations)
- [Gallery](#gallery)
- [Issue Tracking](#issue-tracking)


## Overview

The primary objective of this library is to generate photorealistic images from input files that describe a specific scene.
The scene comprises geometric shapes (please refer to the list of available shapes), each defined by its coordinates and material.
Our code allows you to select between diffusive or reflective materials.
The code incorporates various backward ray tracing algorithms to simulate the propagation of light rays.
A camera (perspective or orthogonal), representing the observer, will view the world through a two-dimensional screen positioned in front of it. The camera is defined by its position, distance from the screen, and aspect ratio.

## Prerequisites
For this project, we are using [version 18.0.2][corretto] of `Amazon Corretto`, an `OpenJDK` distribution
provided by `Amazon Web Services`.
It is possible to download the correct version of `Java` for your operating system on the [`Oracle` website][correttoDownload].

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
- `Picocli` ([version 4.6.1][picocli]), used for creating a command-line interface;
- `JavaTuples` ([version 1.2][javatuples]), used to provide a series of tuple classes.

It is not necessary to install `Gradle`, as the project provides an executable [`./gradlew`][gradlew]
that can be used to avoid local installation of `Gradle`.



## Usage
In order to use and modify the code you can clone the repository with the command:
```
git clone git@github.com:matteopicciolini/JTracer.git
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

Below, and in more detail in [DOCUMENTATION file][documentation],  are the available execution modes and parameters using the `Gradle` `run` task, as it avoids the generation of the binary file. 
However, please note that what follows is also (and especially) valid for the binary file generated with the `installDist` task.

### Modes
This program can be executed in three different modes:
1. `convert` mode. With this mode, you can convert a PFM file to an LDR file.
2. `demo` mode. This mode allows the creation of photorealistic images by directly interacting with the source
   code of the `Tracer` class. If this mode is launched without modifying the code, a default image will be
   generated. Editing the source code is recommended for advanced users only.
3. `render` mode. That's the main feature of this program. With this mode, you can describe the scene inside
   a `.txt` file using some simple rules, and generate your photorealistic image without touching the source code.
4. `sum` mode. This technique allows to "combine" multiple images of the same scene generated with different seeds, in order to reduce the noise present in the image. It is typically used after generating many images in parallel.

All of these modes include the `-h` option which displays specifications for each one. For a detailed description of the commands and options available, please refer to the [documentation][].

<img align="right" img src="https://github.com/matteopicciolini/ray_tracing/assets/116730685/c40b165f-f7d5-41a1-910a-c10b1e454391"  width="280">

### Animations
This code also implements the ability to generate animations. 
Animations can be created by appropriately modifying and making 
executable the bash files `animation.sh` and/or `parallel-generate-image.sh`. 
Similarly, these files can be used to generate different images of the same scene in parallel, 
and then utilize the `sum` mode.


### Tutorials
For proper functionality, this code requires the user to be familiar with some syntax rules for describing the scene. To learn how to write the necessary `.txt` files for the program, you can refer to the documentation or use the tutorials provided in the [`Tutorials` folder][tutorials].




### Shapes
This raytracer implements the following shapes:

From version 1.0.0:
- Spheres 🌍
- Planes ✈️
- Boxes 📦
- Cylinders 🎩
- Cones 🍦
- Hyperboloids ⏳
- CSG Union ➕
- CSG Difference ➖
- CSG Intersection ⋂
  
From version 1.1.0:
- Triangles ▲
- Triangle Meshes 🦌

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

In this project, there is also a `Python` script that takes an input `.obj` file and generates an output `.txt` file with the preavious features.

## Gallery


<div align="center">
  <img src="https://github.com/matteopicciolini/ray_tracing/assets/58447570/6d180acb-2c20-4b9d-9f55-747dcbc1914a" height="250"  />
  <img src="https://github.com/matteopicciolini/ray_tracing/assets/58447570/5ff13031-f296-423c-aefa-cd786c5fb306" height="250"  />
</div>
<div align="center">
  <img src="https://github.com/matteopicciolini/ray_tracing/assets/58447570/8f553ba2-327a-404b-b4e0-5e54accba32b" height="250" />
  <img src="https://github.com/matteopicciolini/ray_tracing/assets/116730685/53b846d7-d331-4170-9fbc-4d174101661e" height="250" />
</div>
<p align="center"><em>Cornell Box rendered using JTracer with varying <br>
    the number of rays cast per pixel (antialiasing algorithm).</em></p>

    
<br>
<div align="center">
  <img src="https://github.com/matteopicciolini/ray_tracing/assets/58447570/30caf212-ac8e-4414-a389-573726ab675e" height="250" />
  <img src="https://github.com/matteopicciolini/ray_tracing/assets/58447570/6c4e73f1-3250-473d-b06e-21e7b353bd04" height="250" />
</div>
<p align="center"><em>Examples of pigments generated from existing pfm files.</em></p>



<br>
<div align="center">
  <img src="https://github.com/matteopicciolini/ray_tracing/assets/58447570/791445bd-5ae4-457d-aca5-6173ff56915e" height="200" />
  <img src="https://github.com/matteopicciolini/ray_tracing/assets/116730685/3fc5292b-b65c-4160-b08c-4e6a9f44b7f7" height="200" /> 
  <img src="https://github.com/matteopicciolini/ray_tracing/assets/58447570/364d3295-a575-4c23-b316-136c81beaa53" height="200" />
  <img src="https://github.com/matteopicciolini/ray_tracing/assets/58447570/cf0cd573-9f2b-4cf1-8feb-bcab256e8119" height="200" />
  <img src="https://github.com/matteopicciolini/ray_tracing/assets/58447570/5aab2522-2844-4e42-a44a-4f621176e6dc" height="200" />
  <img src="https://github.com/matteopicciolini/ray_tracing/assets/58447570/8c940470-39d0-452a-82b5-ec85b53e3423" height="200" />
</div>
<p align="center"><em>Some implementations of abstract scenes.</em></p>
<br>


  <div align="center">
  <img src="https://github.com/matteopicciolini/ray_tracing/assets/58447570/b1e58f2f-39f7-4e69-b3b9-46f6441082cb" height="200" />
  <img src="https://github.com/matteopicciolini/ray_tracing/assets/58447570/5d94964f-3046-47eb-a455-621440c069b1" height="200" />
  <img src="https://github.com/matteopicciolini/ray_tracing/assets/58447570/be40e663-6059-4ccf-997e-e5120c4d44cc" height="200" />
  <img src="https://github.com/matteopicciolini/ray_tracing/assets/58447570/d4d99cdc-bdf1-4231-b175-00c373608d56" height="200" />
  <img src="https://github.com/matteopicciolini/ray_tracing/assets/58447570/a4470de5-6317-4ce0-ab6c-dd5b13f8296d" height="200" />
</div>
<p align="center"><em>Milano Design Week. Picciolini Collection.</em></p>
  
  <br>
  <div align="center">
  <img src="https://github.com/matteopicciolini/JTracer/assets/116730685/0be349f4-1065-432c-8f7c-c6818b3767d5" height="200" />
  <img src="https://github.com/matteopicciolini/JTracer/assets/116730685/c32080d5-5d61-4aa0-8062-ca25eb35e182" height="200" />
  <img src="https://github.com/matteopicciolini/JTracer/assets/116730685/1ecc99c7-5658-40fb-8fe4-6d9c8b6eb54e" height="200" />
  <img src="https://github.com/matteopicciolini/JTracer/assets/116730685/395d57a8-9feb-4662-b2fe-2df412299edd" height="200" />
  <img src="https://github.com/matteopicciolini/JTracer/assets/116730685/06f25243-059d-42a2-9e50-8dc767b2b991" height="200" />
  <img src="https://github.com/matteopicciolini/JTracer/assets/116730685/8b8ed1e9-1ef6-4656-b799-7e07941a3b66" height="200" />
  <img src="https://github.com/matteopicciolini/JTracer/assets/116730685/a6240427-a372-41e4-a10f-6643ab62d1cd" height="200" />
</div>
<p align="center"><em> New York Zoo. Villa Collection.</em></p>


  
</p>




## Issue Tracking
![GitHub code size in bytes](https://img.shields.io/github/languages/code-size/matteopicciolini/JTracer)
![GitHub pull requests](https://img.shields.io/github/issues-pr/matteopicciolini/JTracer)
![GitHub closed pull requests](https://img.shields.io/github/issues-pr-closed/matteopicciolini/JTracer)
![GitHub issues](https://img.shields.io/github/issues/matteopicciolini/JTracer)
![GitHub closed issues](https://img.shields.io/github/issues-closed-raw/matteopicciolini/JTracer)

If you encounter any issues while using this project or find a bug in the code, please feel free to let us know! You can reach us at picciolinimatteo@gmail.com or fravilla30@gmail.com.



[fravi]: https://github.com/fravij99
[ziotom]: https://github.com/ziotom78
[piccio]: https://github.com/matteopicciolini
[build]: https://github.com/matteopicciolini/ray_tracing/blob/master/build.gradle
[picocli]: https://picocli.info/
[junit5]: https://junit.org/junit5/
[corretto]: https://aws.amazon.com/it/about-aws/whats-new/2022/03/amazon-corretto-18/
[correttoDownload]: https://www.oracle.com/java/technologies/javase/jdk18-archive-downloads.html
[gradlew]: https://github.com/matteopicciolini/ray_tracing/blob/master/gradlew
[javatuples]: https://www.javatuples.org/
[documentation]: https://github.com/matteopicciolini/ray_tracing/blob/master/DOCUMENTATION.md
[tutorials]: https://github.com/matteopicciolini/JTracer/tree/master/Tutorials
