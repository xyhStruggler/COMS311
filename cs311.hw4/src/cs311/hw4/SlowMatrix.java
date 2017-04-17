package cs311.hw4;

public class SlowMatrix implements IMatrix, IMeasurable{

	public int val[][];
    public int M;
    public int N;
    
    public SlowMatrix(int M, int N){
	this.M = M;
	this.N = N;
	val = new int[M][N];
    }
    
    public SlowMatrix(int[][] data) {
        M = data.length;
        N = data[0].length;
        this.val = new int[M][N];
        for (int i = 0; i < M; i++)
            for (int j = 0; j < N; j++)
                    this.val[i][j] = data[i][j];
    }
    
	@Override
	public IMatrix subMatrix(int upperLeftRow, int upperLeftCol, int lowerRightRow, int lowerRightCol)
			throws IllegalArgumentException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setElement(int row, int col, Number val) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		if(row >= M || col >= N)  throw new RuntimeException("Illegal matrix dimensions.");
		this.val[row][col] = (int) val;
	}

	@Override
	public Number getElement(int row, int col) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		if(row >= M || col >= N)  throw new RuntimeException("Illegal matrix dimensions.");
		return val[row][col];
	}

	@Override
	public SlowMatrix multiply(IMatrix mat) throws IllegalArgumentException {
		 SlowMatrix A = this;
		 SlowMatrix B = (SlowMatrix) mat;
	        if (A.N != B.M) throw new RuntimeException("Illegal matrix dimensions.");
	        SlowMatrix C = new SlowMatrix(A.M, B.N);
	        for (int i = 0; i < C.M; i++)
	            for (int j = 0; j < C.N; j++)
	                for (int k = 0; k < A.N; k++)
	                    C.val[i][j] += (A.val[i][k] * B.val[k][j]);
	        return C;
	}

	@Override
	public IMatrix add(IMatrix mat) throws IllegalArgumentException {
		SlowMatrix A = this;
		SlowMatrix B = (SlowMatrix) mat;
        if (B.M != A.M || B.N != A.N) throw new RuntimeException("Illegal matrix dimensions.");
        SlowMatrix C = new SlowMatrix(M, N);
        for (int i = 0; i < M; i++)
            for (int j = 0; j < N; j++)
                C.val[i][j] = A.val[i][j] + B.val[i][j];
        return C;
	}

	@Override
	public void execute() {
		// TODO Auto-generated method stub
		multiply(this);
	}

}
