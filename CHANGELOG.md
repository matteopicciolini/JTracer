# Changelog
All notable changes to this project will be documented in this file.


## v0.3.0
#### New Features
- Implementation of the PCG random number generator [#6](https://github.com/matteopicciolini/ray_tracing/pull/6).
- Material characterization of `Shape` through the `Material` and `Pigment` classes is available [#6](https://github.com/matteopicciolini/ray_tracing/pull/6).
- Two additional renders are available: `flat` and `pathTracer` [#8](https://github.com/matteopicciolini/ray_tracing/pull/8). The latter is the core of the program. 
- Implementation of the antialiasing algorithm for `pathTracer` [#10](https://github.com/matteopicciolini/ray_tracing/pull/10).
- Implement parallelization for antialiasing [#10](https://github.com/matteopicciolini/ray_tracing/pull/10).
- New Shape: `Plane` [#11](https://github.com/matteopicciolini/ray_tracing/pull/6).
- New Shape: `Box` [#11](https://github.com/matteopicciolini/ray_tracing/pull/11).

#### Fixed
- Fixed bug in PCG [#9](https://github.com/matteopicciolini/ray_tracing/issues/9).
- Fixed bug in ScatterRay [#12](https://github.com/matteopicciolini/ray_tracing/issues/12).

## v0.2.0
#### New Features
- Define mathematical operators (`Vector`, `Normal`, `Point`, `ransformation`) and opteration [#1](https://github.com/matteopicciolini/ray_tracing/pull/1).
- Conversion of the build system from IntelliJ to Gradle in order to take advantage of CI Builds [#2](https://github.com/matteopicciolini/ray_tracing/pull/2).
- Define Camera class that allows specifying the position and orientation of the observer [#3](https://github.com/matteopicciolini/ray_tracing/pull/3).
- First demo [#5](https://github.com/matteopicciolini/ray_tracing/pull/5).
- Tests for code self-check.

#### Fixed
- Computation of pixel position onto the screen [#4](https://github.com/matteopicciolini/ray_tracing/pull/4).

## v0.1.0
#### New Features
- Implement tone mapping and gamma correction for conversion of PFM images to PNG images.
- Tests for code self-check.
- Add CHANGELOG.md and GNU GPL License.


