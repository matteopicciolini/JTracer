package org.mirrors;

/**
 * A camera that creates perspective rays for rendering a scene.
 */
public class PerspectiveCamera extends Camera {
    public float distance;

    /**
     * Creates a new perspective camera with the specified distance and aspect ratio.
     * @param distance the distance from the camera to the image plane
     * @param aspectRatio the aspect ratio of the camera's view
     */
    public PerspectiveCamera(float distance, float aspectRatio){
        this.distance = distance;
        this.aspectRatio = aspectRatio;
        this.transformation = new Transformation();
    }

    /**
     * Creates a new perspective camera with the specified distance, aspect ratio, and transformation.
     * @param distance the distance from the camera to the image plane
     * @param aspectRatio the aspect ratio of the camera's view
     * @param transformation the transformation to apply to the camera
     */
    public PerspectiveCamera(float distance, float aspectRatio, Transformation transformation){
        this.distance = distance;
        this.aspectRatio = aspectRatio;
        this.transformation = transformation;
    }

    /**
     * Fires a perspective ray from the camera's position through the specified
     * pixel coordinates.
     * @param u the horizontal coordinate of the pixel, in the range [-1, 1]
     * @param v the vertical coordinate of the pixel, in the range [-1, 1]
     * @return the ray originating from the camera's position and passing through
     *         the specified pixel coordinates
     */
    public Ray fireRay(float u, float v){
        Point origin = new Point(-this.distance, 0.f, 0.f);
        Vec direction = new Vec(this.distance, (1.f - 2.f * u) * this.aspectRatio, 2.f * v - 1.f);
        return new Ray(origin, direction).transform(this.transformation);
    }
}
