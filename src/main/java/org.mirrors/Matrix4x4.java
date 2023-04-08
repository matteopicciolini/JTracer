package org.mirrors;

public class Matrix4x4 {
    public float[] matrix = new float[16];

    public Matrix4x4(){
        this.matrix = Global.IdentityMatrix;
    }
    public Matrix4x4(float a){
        for (int i = 0; i < 16; ++i){
            this.matrix[i] = a;
        }
    }
    public Matrix4x4(float[] matrix) throws InvalidMatrix {
        if(matrix.length != 16){
            throw new InvalidMatrix("Invalid 4x4 matrix.");
        }
        this.matrix = matrix;
    }

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

    public void setMatrixElement(int row, int col, float value) {
        this.matrix[row * 4 + col] = value;
    }
    public float getMatrixElement(int row, int col) {
        return this.matrix[row * 4 + col];
    }

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

    public boolean isClose(Matrix4x4 b){
        float epsilon = 1e-5f;
        for(int i = 0; i < 16; ++i){
            if(this.matrix[i] - b.matrix[i] > epsilon){
                return false;
            }
        }
        return true;
    }
}
