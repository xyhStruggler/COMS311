package cs311.hw4;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MeasureTimeComplexity implements IMeasureTimeComplexity {
	ArrayList<result> arr = new ArrayList();
	public MeasureTimeComplexity(){
	    }
	@Override
	public int normalize(IMeasurable m, long timeInMilliseconds) {
		long start = System.currentTimeMillis();
		m.execute();
		long time = System.currentTimeMillis() - start;
		return  (int) (time/(timeInMilliseconds*0.001));
	}

	@Override
	public List<? extends IResult> measure(IMeasureFactory factory, int nmeasures, int startsize, int endsize,
			int stepsize) {
		for(int i = startsize; i< endsize; i++){
			result r = new result(i, normalize(factory.createRandom(i), nmeasures));
			arr.add(r);
		}
		return arr;
	}

}
