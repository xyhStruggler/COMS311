package cs311.hw4;

public class SlowMatrixFactory implements IMeasureFactory {
	public SlowMatrixFactory(){
	    }
	@Override
	public IMeasurable createRandom(int size) {
		SlowMatrix SM = new SlowMatrix(size,size);
		return SM;
	}

}
