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
    @Override
    public String toString(){
        String str =  "Matrix4x4: \n";
        for(int i = 0; i < 4; ++i){
            for (int j = 0; j < 4; ++j){
                str += "   " + this.getMatrixElement(j, i);
            }
            str += "\n";
        }
        return str;
    }
    public Matrix4x4(float[] matrix) throws InvalidMatrix {
        if(matrix.length != 16){
            throw new InvalidMatrix("Invalid 4x4 matrix.");
        }
        this.matrix = matrix;
    }

    public void setMatrixElement(int row, int col, float value) {
        this.matrix[col * 4 + row] = value;
    }
    public float getMatrixElement(int row, int col) {
        return this.matrix[col * 4 + row];
    }

    public Matrix4x4 cross(Matrix4x4 b){
        Matrix4x4 matrix_prod = new Matrix4x4(0.f);

        for (int i = 0; i < 4; ++i) {
            for (int j = 0; j < 4; ++j) {
                float sum = 0.0f;
                for (int k = 0; k < 4; ++k) {
                    sum += this.getMatrixElement(k, i) * b.getMatrixElement(j, k);
                }
                matrix_prod.setMatrixElement(j, i, sum);
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
