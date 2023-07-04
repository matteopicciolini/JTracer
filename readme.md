

 <h1 align="center">  Ray Tracing Progect </h1> <br>


<img align="left" img src="https://github.com/matteopicciolini/ray_tracing/assets/116730685/f64d7b32-25f2-4a6d-a465-07ba183f2b0b" alt="cervo e gatto specchiati"  width="300">

This is a project developed by Matteo Picciolini and Francesco Villa to reproduce some photorealistic images by using Numerical Methods. This came from a course we attended in Unimi, called *Numeric calculus for photorealistic images generation*, directed by our professor Maurizio Tomasi.
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
Our code allows for the implementation of various types of materials (we will provide the complete table in a .txt file later) as well as different types of shapes, such as spheres, planes, cubes, various types of conics, triangles, and triangle meshes (the use of triangle meshes will be thoroughly explained).
Once the desired shapes are defined and the scene is rendered using the methods described earlier, the resulting image is in HDR format (a .pfm file), and subsequently converted into an LDR file such as .jpg or .png based on the user's needs.
