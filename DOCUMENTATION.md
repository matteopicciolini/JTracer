# Documentation
⚠️ This documentation does not claim to be exhaustive but to provide general guidelines with which the user can start interfacing with `JTracer`. For the installation of prerequisites and usage, please refer to the [`README.md` file][readme].

## Table of Contents
- [JTracer Commands and Options](#jtracer-commands-and-options)
- [Guidelines for properly creating text file and generate photorealistic images](#guidelines-for-properly-creating-text-file-and-generate-photorealistic-images)

## JTracer Commands and Options
This program can be executed in three different modes:
1. `convert` mode. With this mode, you can convert a PFM file to an LDR file.
2. `demo` mode. This mode allows the creation of photorealistic images by directly interacting with the source
   code of the `Tracer` class. If this mode is launched without modifying the code, a default image will be
   generated. Editing the source code is recommended for advanced users only.
3. `render` mode. That's the main feature of this program. With this mode, you can describe the scene inside
   a `.txt` file using some simple rules, and generate your photorealistic image without touching the source code.
4. `sum` mode. This technique allows to "combine" multiple images of the same scene generated with different seeds, in order to reduce the noise present in the image. It is typically used after generating many images in parallel.

All of these modes include the `-h` option which displays specifications for each one.

The `demo` and `render` modes are the modes that actually allow for the creation of a photorealistic image. Therefore, it is important to focus on the fundamental parameter `--algorithm`, which is accepted by both modes. This parameter specifies the algorithm to use for rendering the image, and you can choose from the following options:
1. `onOff`: simply estimates if a ray hits the point;
2. `flat`: a bit more advanced than the previous, estimates the surface color;
3. `pathTracer`: the "real" ray tracer, uses the ray tracing equation.

Below is a summary of the specifications and usage for each mode.

### `convert` mode

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
### `demo` mode
The command to run the program in demo mode is as follows:
```
./gradlew run --args="demo"
```
The method to modify to describe the scene is the `demo` method of the `Tracer` class. 
For options, please refer to the `render` mode, which implements the same ones.

### `render` mode
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
- `--flushFrequency`              `int`: Frequency of flush (expressed in number of processed pixels) of the progress bar. Default: `100`.
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
### `sum` mode
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


## Guidelines for properly creating text file and generate photorealistic images

A quick way to learn how to write a text file describing a scene is to take inspiration from the files contained in the `Tutorial` folder.
Getting familiar with them is quite straightforward, however, we provide a detailed description of the syntax used in JTracer. 
Please note that although spaces and endline characters are ignored, 
they can still improve the file's readability. Additionally, comments can be helpful: `# This is a comment in a scene.`

Let's start with numerical types:
- **Declaration of numerical variables:** `float newVariable(value)`, where the variable name must start with an alphanumeric character or underscore, and the value must be a number.
- **Colors:** `<r, g, b>`, where r, g, and b are three non-negative numbers representing red, green, and blue respectively.
- **Vectors:** `[x, y, z]`, where x points inside the monitor, y points to the left, and z points upwards. Each component is a real number.

Now we can move forward and begin describing a scene. Before shapes can come to life in the scene, it's crucial to understand the observer's position and how to position the objects.
- **Camera:** `camera(typeOfCamera, transformation, aspectRatio, distance)`. There are two supported camera types: orthogonal and perspective. Transformations are explained below and can be combined using the "*" operator, applied from right to left. The aspect ratio is typically the image width divided by its height, while the distance represents the observer's distance from the screen (note that it can be altered by the transformation).
- **Transformations:** Desired transformations can be achieved by utilizing the following commands:
  1. `identity`: useful when the object or observer doesn't need to be moved.
  2. `translation([x, y, z])`: translates the object by a vector described in the aforementioned syntax.
  3. `rotationX(angle)`: rotates the object along the x-axis by the specified angle, which can be a numerical value or the name of a previously declared variable (as mentioned earlier). The angle should be expressed in degrees.
  4. `rotationY(angle)`: similar to the previous command, but rotates the object along the y-axis.
  5. `rotationZ(angle)`: similar to the previous command, but rotates the object along the z-axis.
  6. `scaling([x, y, z])`: scales the object's components along the corresponding axes by the respective vector components. For instance, to double the object's height while keeping the other dimensions unchanged, you would apply `scaling([1.0, 1.0, 2.0])`.

Finally, let's discuss shapes. The syntax is straightforward, but it's essential to learn how to define the materials that make up the objects.
- **Pigment:** This is the initial step in assigning a color to a shape. Currently, there are three types of pigments:
  1. `uniform(<r, g, b>)`: represents a monochromatic pigment of any RGB color.
  2. `checkered(<r1, g1, b1>, <r2, g2, b2>, steps)`: creates a checkered pattern with two colors. The higher the number of steps (a positive integer), the denser the alternation.
  3. `image("file.pfm")`: covers the surface with a PFM image.

- **Materials:** `material name(typeOfBRDF(pigment), pigment)`. Materials require a name and consist of two parts: the first part defines how the surface responds to external light (BRDF), while the second part represents the intrinsic emitting radiance. Currently, the "typeOfBRDF" can be one of the following:
  1. `diffuse`: for objects that scatter incoming rays uniformly.
  2. `specular`: for surfaces that reflect light like a mirror.

- **Shapes:** The syntax for shapes depends on the type of `Shape` you want to use. We refer to the tutorials for the correct usage.
  In general, the available shapes are as follows:
  1. `sphere(material, transformation)`;
  2. `plane(material, transformation)`;
  3. `box(vertex1, vertex2, material, transformation)`;
  4. `cylinder(material, transformation)`
  5. `hyperboloid(zMin, zMax, material, transformation)`;
  6. `cone(radius, height, material, transformation)`;
  7. `triangle(vertex1, vertex2, vertex3, material, transformation)`;
  8. `fileshape(material, transformation, path)`;
  9. `difference(shape1, shape2, transformation)`;
  10. `union(shape1, shape2, transformation)`;
  11. `intersection(shape1, shape2, transformation)`.


[readme]: https://github.com/matteopicciolini/ray_tracing/blob/master/README.md
