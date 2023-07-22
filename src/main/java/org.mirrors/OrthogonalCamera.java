package org.mirrors;

/**
 * A camera that creates orthogonal rays for rendering a scene.
 */
public class OrthogonalCamera extends Camera {
    /**
     * Creates a new orthogonal camera with the specified aspect ratio.
     *
     * @param aspectRatio the aspect ratio of the camera's view
     */
    public OrthogonalCamera(float aspectRatio) {
        this.aspectRatio = aspectRatio;
        this.transformation = new Transformation();
    }

    /**
     * Creates a new orthogonal camera with the specified aspect ratio and transformation.
     *
     * @param aspectRatio    the aspect ratio of the camera's view
     * @param transformation the transformation to apply to the camera
     */
    public OrthogonalCamera(float aspectRatio, Transformation transformation) {
        this.aspectRatio = aspectRatio;
        this.transformation = transformation;
    }

    /**
     * Fires an orthogonal ray from the camera's position through the specified
     * pixel coordinates.
     *
     * @param u the horizontal coordinate of the pixel, in the range [-1, 1]
     * @param v the vertical coordinate of the pixel, in the range [-1, 1]
     * @return the ray originating from the camera's position and passing through
     * the specified pixel coordinates
     */
    public Ray fireRay(float u, float v) {
        Point origin = new Point(-1.f, (1.f - 2.f * u) * this.aspectRatio, 2.f * v - 1.f);
        return new Ray(origin, Global.VecX).transform(this.transformation);
    }
}