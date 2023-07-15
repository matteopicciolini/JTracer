# Guidelines for properly creating text file and generate photorealistic images

A quick way is to take inspiration from the files in the examples directory. Getting familiar with them is quite straightforward, however, we provide a detailed description of the syntax used in JTracer. Please note that although spaces and endline characters are ignored, they can still improve the file's readability. Additionally, comments can be helpful: `# This is a comment in a scene.`

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
  7. `difference(shape1, shape2, transformation)`;
  8. `union(shape1, shape2, transformation)`;
  9. `intersection(shape1, shape2, transformation)`.