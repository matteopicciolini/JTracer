package org.mirrors;

/**
 * This class represents a 4x4 matrix and provides methods for setting and getting its elements,
 * computing the matrix product with another 4x4 matrix, and checking for approximate equality
 * with another 4x4 matrix.
 */
public class Matrix4x4 {


    public float[] matrix = new float[16];

    /**
     * Constructs a new Matrix4x4 object initialized to the identity matrix.
     */
    public Matrix4x4(){
        this.matrix = Global.IdentityMatrix;
    }

    /**
     * Constructs a new Matrix4x4 object with all elements set to the specified value.
     *
     * @param a The value to set all elements to.
     */
    public Matrix4x4(float a){
        for (int i = 0; i < 16; ++i){
            this.matrix[i] = a;
        }
    }

    /**
     * Constructs a new Matrix4x4 object with the specified element values.
     *
     * @param matrix An array containing the 16 element values in row-major order.
     * @throws InvalidMatrixException if the provided array does not contain exactly 16 elements.
     */
    public Matrix4x4(float[] matrix) throws InvalidMatrixException {
        if(matrix.length != 16){
            throw new InvalidMatrixException("Invalid 4x4 matrix.");
        }
        this.matrix = matrix;
    }

    /**
     * Returns a string representation of this Matrix4x4 object.
     *
     * @return A string representation of this Matrix4x4 object.
     */
    @Override
    public String toString(){
        StringBuilder str = new StringBuilder("Matrix4x4: \n");
        for(int i = 0; i < 4; ++i){
            for (int j = 0; j < 4; ++j){
                str.append("   ").append(this.getMatrixElement(i, j));
            }
            str.append("\n");
        }
        return str.toString();
    }

    /**
     * Sets the value of a specific element in this matrix.
     *
     * @param row The row index of the element to set.
     * @param col The column index of the element to set.
     * @param value The value to set the element to.
     */
    public void setMatrixElement(int row, int col, float value) {
        this.matrix[row * 4 + col] = value;
    }

    /**
     * Returns the value of a specific element in this matrix.
     *
     * @param row The row index of the element to retrieve.
     * @param col The column index of the element to retrieve.
     * @return The value of the specified element.
     */
    public float getMatrixElement(int row, int col) {
        return this.matrix[row * 4 + col];
    }

    /**
     * Computes the matrix product of this matrix and another 4x4 matrix.
     * Remember that the result will be the product B*A instead of A*B
     *
     * @param b The matrix to multiply this matrix by.
     * @return The matrix product of this matrix and b.
     */

    public Matrix4x4 cross(Matrix4x4 b){
        Matrix4x4 matrix_prod = new Matrix4x4(0.f);

        for (int i = 0; i < 4; ++i) {
            for (int j = 0; j < 4; ++j) {
                float sum = 0.0f;
                for (int k = 0; k < 4; ++k) {
                    sum += this.getMatrixElement(i, k) * b.getMatrixElement(k, j);
                }
                matrix_prod.setMatrixElement(i, j, sum);
            }
        }
        return matrix_prod;
    }

    /**
     * Checks if this matrix is approximately equal to another 4x4 matrix.
     *
     * @param b The matrix to compare this matrix to.
     * @return True if this matrix is approximately equal to b, false otherwise.
     */
    public boolean isClose(Matrix4x4 b){
        float epsilon = 1e-4f;
        for(int i = 0; i < 16; ++i){
            if(this.matrix[i] - b.matrix[i] > epsilon){
                return false;
            }
        }
        return true;
    }
}
