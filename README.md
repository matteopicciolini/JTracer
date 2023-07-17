<!--<img align="right" img src="https://github.com/matteopicciolini/ray_tracing/assets/116730685/f3f3a503-224d-4669-ae42-efb1931727b7" alt="Competizione tra cervo gatto e lupo"  width="300">
-->
<img align="right" src="https://github.com/matteopicciolini/JTracer/assets/116730685/5fd1b57c-910c-461e-af29-7c2654a78bd3"  width="300">

<h1 align="center">  Ray Tracing Project </h1> <br>


This project was developed by [Matteo Picciolini][piccio] and [Francesco Villa][fravi] to generate photorealistic images by solving the rendering equation using Numerical Methods.
This project is developed for ***Numeric calculus for photorealistic images generation*** course, held by professor [Maurizio Tomasi][ziotom] at University of Milan, Physics Department.
The project has been written in `Java` and can be compiled using `Gradle`. Additionally, it is integrated with some bash scripts that allow generating animations using the open-source software `FFmpeg`. It is available for `Linux`, `Windows` and `macOS`.

<!--*Competition between deer, cat and wolf, realized with boxes, triangle meshes, sphere and plane*-->

## Table of Contents
- [Overview](#Overview)
- [Prerequisites](#Prerequisites)
- [Usage](#Usage)
    - [Modes](#Modes)
    - [Tutorials](#Tutorials)
    - [Shapes](#Shapes)
- [Animations](#Animations)
- [Gallery](#Gallery)
- [Issue Tracking](#Issue-Tracking)


## Overview

The main purpose of this library is to generate photorealistic images from input files that describe 
a specific scene. 
The scene consists of geometric shapes (please refer to the list of available shapes), 
each defined by its coordinates and material. 
Our code allows you to choose between diffusive, emissive, or reflective materials. 
The code incorporates different backward ray tracing algorithms to simulate the propagation of light rays. 
A camera (perspective or orthogonal), representing the observer, will view the world through a 
two-dimensional screen positioned in front of it. The camera is defined by its position, 
distance from the screen, and aspect ratio.
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
- `Picocli` ([version 4.6.1][picocli]), used for creating a command-line interface;
- `JavaTuples` ([version 1.2][javatuples]), used to provide a series of tuple classes.

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

Below are the available execution modes and parameters using the `Gradle` `run` task, as it avoids the generation of the binary file. 
However, please note that what follows is also (and especially) valid for the binary file generated with the `installDist` task.

### Modes
This program can be executed in three different modes:
1. `convert` mode. With this mode, you can convert a PFM file to an LDR file.
2. `demo` mode. This mode allows the creation of photorealistic images by directly interacting with the source
   code of the `Tracer` class. If this mode is launched without modifying the code, a default image will be
   generated. Editing the source code is recommended for advanced users only.
3. `render` mode. That's the main feature of this program. With this mode, you can describe the scene inside
   a .txt file using some simple rules, and generate your photorealistic image without touching the source code.
4. `sum` mode. This technique allows to "combine" multiple images of the same scene generated with different seeds, in order to reduce the noise present in the image. It is typically used after generating many images in parallel.

All of these modes include the `-h` option which displays specifications for each one.

The `demo` and `render` modes are the modes that actually allow for the creation of a photorealistic image. Therefore, it is important to focus on the fundamental parameter `--algorithm`, which is accepted by both modes. This parameter specifies the algorithm to use for rendering the image, and you can choose from the following options:
1. `onOff`: simply estimates if a ray hits the point;
2. `flat`: a bit more advanced than the previous, estimates the surface color;
3. `pathTracer`: the "real" ray tracer, uses the ray tracing equation.

Below is a summary of the specifications and usage for each mode.

#### `convert` mode

This mode can be executed by launching the following command from the command line.
```
./gradlew run --args="convert image.pfm"
```
Below we show the available options:
- `-f, --factor`            `float`: Multiplicative factor. Default: `0.18`.
- `-g, --gamma`             `float`: Exponent for gamma-correction. Default: `2.2`.
- `-o, --outputFileName`    `string`: Path of the output LDR file. Default: `<inputFileName>.png`.
- `-l, --luminosity`      `float`: Luminosity of the image. Default: If it is not specified, it is calculated; otherwise, it is set to `0.5`.


If the output LDR file extension is not specified using the `-o` option, 
a PNG file with the name of the input file will be generated.
#### `demo` mode
The command to run the program in demo mode is as follows:
```
./gradlew run --args="demo"
```
The method to modify to describe the scene is the `demo` method of the `Tracer` class. 
For options, please refer to the `render` mode, which implements the same ones.

#### `render` mode
The render mode is the preferred mode for describing the scene and generating the photorealistic image. 
To run the program in render mode, it is necessary to write a TXT file according to the rules described 
in the `documentation.md` file that describes the scene. Some sample tutorials are also available in this folder. 
To run the program in this mode, you can use the following command:
```
./gradlew run --args="render -i=inputFile.txt"
``` 
where `inputFile.txt` is the file that describes the scene.


Below are the available options for the render mode. Note that the options for the demo mode are very similar.
- `-a, --angle-deg`               `float`: Angle of view. Default: `0`.
- `--algorithm`                   `string`: Algorithm of rendering. Default: `pathTracer`.
- `--antialiasing`                `bool`: Use antialiasing algorithm. Default: `false`.
- `-c, --convertToPNG`            `bool`: At the end of the program execution, automatically convert the PFM file to PNG. Default: `true`.
- `-d, --deletePFM`               `bool`: At the end of the program execution, keep only the LDR image, deleting the PFM. Default: `false`.
- `-f, --factor`                  `float`: Multiplicative factor. Default: `0.18`.
- `--flushFrequence`              `int`: Frequency of flush (expressed in number of processed pixels) of the progress bar. Default: `100`.
- `-g, --gamma`                   `float`: Exponent for gamma-correction. Default: `2.2`.
- `--height`                      `int`: Height of the image. Default: `480`.
- `-i, --input`                   `string`: Path of the input TXT file. REQUIRED.
- `--initState`                   `int`: PCG starter parameter. Default: `42`.
- `--initSeq`                     `int`: PCG starter parameter. Default: `52`.
- `-l, --luminosity`              `float`: Luminosity of the image.     Default: It is calculated for the pathTracer; otherwise, it is set to `0.5`.
- `--maxDepth`                    `int`: Maximum recursion depth. Default: `2`.
- `-n, --numRays`                 `int`: Number of rays per pixel. Default: `10`.
- `--nThreads`                    `int`: Number of threads to use for parallelization. Default: `8`.
- `--output`                      `string`: Path of the output ldr file. Default: `img.pfm`.
- `--parallel`                  `bool`: Parallelize the code. Default: `true`.
- `--russianRouletteLimit`        `int`: Russian roulette limit. Default: `3`.
- `-s, --samplePerSide`           `int`: In antialiasing algorithm, the number of samples per side. Default: `4`.
- `-w, --width`                   `int:` Width of the image. Default: `480`.


By the end of the execution, there's no need to specify the conversion from HDR image to LDR image cause of the default settings of `--convertToPNG=true`.
#### `sum` mode
This last method allows for effective utilization of parallel programming. By using certain bash scripts, it is possible to run the same program in parallel on multiple cores with different `--initState` and `--initSeq` parameters for each core. Afterwards, this method can be used to "average" each pixel of the generated PFM images and reduce the noise.

The following options are available for this mode:
- `--firstImage` `string`: Path of the first pfm file.
- `--secondImage` `string`: Path of the second pfm file.
- `--imageNamePattern` `string`: Pattern of the pfm file.
- `--numOfImages` `int`: number of images.
- `--outputFileName` `string`: output file name (.pfm). Default: `outputSum.pfm`.
- `-f`, `--factor` `float`: Multiplicative factor. Default: `0.18`.
- `-g`, `--gamma` `float`: Exponent for gamma-correction. Default: `2.2`
- `-l`, `--luminosity` `float`: Luminosity of the image. Default: If it is not specified, it is calculated; otherwise, it is set to `0.5`.

This mode can be used in two different ways: by averaging between two images using the `--firstImage` and `--secondImage` parameters, or by averaging between multiple images with the same pattern using the `--imageNamePattern` parameter. For example, if `--numOfImages=20` images have been generated with the pattern `image`, the program will assume the existence of files `image01.pfm`, `image02.pfm`, ..., `image20.pfm`, and it will average all 20 images together.

### Animations
This code also implements the ability to generate animations. 
Animations can be created by appropriately modifying and making 
executable the bash files `animation.sh` and/or `parallel-generate-image.sh`. 
Similarly, these files can be used to generate different images of the same scene in parallel, 
and then utilize the `sum` mode.


### Tutorials
For proper functionality, this code requires the user to be familiar with some syntax rules for describing the scene. To learn how to write the necessary TXT files for the program, you can refer to the documentation or use the tutorials provided in the `Tutorials` folder.


<img align="right" img src="https://github.com/matteopicciolini/ray_tracing/assets/116730685/c40b165f-f7d5-41a1-910a-c10b1e454391"  width="280">

### Shapes
This raytracer implements the following shapes:
- Sphere
- Planes
- Boxes
- Cylinder
- Cones
- Hyperboloid
- CSG Union
- CSG Difference
- CSG Intersection
- Triangles
- Triangle Meshes

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

## Gallery

 <p float="center">
  <!--<img src="https://github.com/matteopicciolini/ray_tracing/assets/116730685/b65d86ed-dec6-400e-94a0-27c83ba3741c" height="200" />
  <img src="https://github.com/matteopicciolini/ray_tracing/assets/116730685/04ccd682-2dd5-4dcb-a958-99a90671ec32" height="200" /> 
  <img src="https://github.com/matteopicciolini/ray_tracing/assets/116730685/c5def2d6-d87c-4358-bfb5-d5a4ce9842db" height="200" /> -->
  
  <!--<img src="https://github.com/matteopicciolini/ray_tracing/assets/116730685/ef222fc3-8a45-4b92-a90c-172a93023777" height="200" />
  <img src="https://github.com/matteopicciolini/ray_tracing/assets/116730685/f9b36d44-0663-402b-ab25-75c4f1466532" height="200" /> -->

  <img src="https://github.com/matteopicciolini/ray_tracing/assets/58447570/cf0cd573-9f2b-4cf1-8feb-bcab256e8119" height="200" />
  <img src="https://github.com/matteopicciolini/ray_tracing/assets/58447570/985fc7f0-b165-4c62-a6a8-646f57a88afa" height="200" />
  <img src="https://github.com/matteopicciolini/ray_tracing/assets/58447570/6d180acb-2c20-4b9d-9f55-747dcbc1914a" height="200" />
  <img src="https://github.com/matteopicciolini/ray_tracing/assets/58447570/5ff13031-f296-423c-aefa-cd786c5fb306" height="200" />
  <img src="https://github.com/matteopicciolini/ray_tracing/assets/58447570/8f553ba2-327a-404b-b4e0-5e54accba32b" height="200" />
  <img src="https://github.com/matteopicciolini/ray_tracing/assets/116730685/53b846d7-d331-4170-9fbc-4d174101661e" height="200" /> 

  <img src="https://github.com/matteopicciolini/ray_tracing/assets/58447570/30caf212-ac8e-4414-a389-573726ab675e" height="200" />
  <img src="https://github.com/matteopicciolini/ray_tracing/assets/58447570/6c4e73f1-3250-473d-b06e-21e7b353bd04" height="200" />
  <img src="https://github.com/matteopicciolini/ray_tracing/assets/58447570/791445bd-5ae4-457d-aca5-6173ff56915e" height="200" />
  <img src="https://github.com/matteopicciolini/ray_tracing/assets/116730685/3fc5292b-b65c-4160-b08c-4e6a9f44b7f7" height="200" /> 
  <img src="https://github.com/matteopicciolini/ray_tracing/assets/58447570/364d3295-a575-4c23-b316-136c81beaa53" height="200" />
  <img src="https://github.com/matteopicciolini/ray_tracing/assets/58447570/94148ac8-3d0d-4f3b-9646-9fb49896b7b7" height="200" />
  <img src="https://github.com/matteopicciolini/ray_tracing/assets/58447570/5aab2522-2844-4e42-a44a-4f621176e6dc" height="200" />
  <img src="https://github.com/matteopicciolini/ray_tracing/assets/58447570/8c940470-39d0-452a-82b5-ec85b53e3423" height="200" />
  <img src="https://github.com/matteopicciolini/ray_tracing/assets/58447570/b1e58f2f-39f7-4e69-b3b9-46f6441082cb" height="200" />
  <img src="https://github.com/matteopicciolini/ray_tracing/assets/58447570/5d94964f-3046-47eb-a455-621440c069b1" height="200" />
  <img src="https://github.com/matteopicciolini/ray_tracing/assets/58447570/be40e663-6059-4ccf-997e-e5120c4d44cc" height="200" />
  <img src="https://github.com/matteopicciolini/ray_tracing/assets/58447570/d4d99cdc-bdf1-4231-b175-00c373608d56" height="200" />
  <img src="https://github.com/matteopicciolini/ray_tracing/assets/58447570/a4470de5-6317-4ce0-ab6c-dd5b13f8296d" height="200" />


  <img src="https://github.com/matteopicciolini/JTracer/assets/116730685/0be349f4-1065-432c-8f7c-c6818b3767d5" height="200" />

  <img src="https://github.com/matteopicciolini/JTracer/assets/116730685/c32080d5-5d61-4aa0-8062-ca25eb35e182" height="200" />

  <img src="https://github.com/matteopicciolini/JTracer/assets/116730685/1ecc99c7-5658-40fb-8fe4-6d9c8b6eb54e" height="200" />
  
  <img src="https://github.com/matteopicciolini/JTracer/assets/116730685/395d57a8-9feb-4662-b2fe-2df412299edd" height="200" />

  <img src="https://github.com/matteopicciolini/JTracer/assets/116730685/8b8ed1e9-1ef6-4656-b799-7e07941a3b66" height="200" />

  <img src="https://github.com/matteopicciolini/JTracer/assets/116730685/06f25243-059d-42a2-9e50-8dc767b2b991" height="200" />

  <img src="https://github.com/matteopicciolini/JTracer/assets/116730685/a6240427-a372-41e4-a10f-6643ab62d1cd" height="200" />

  

  
</p>




## Issue-Tracking
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