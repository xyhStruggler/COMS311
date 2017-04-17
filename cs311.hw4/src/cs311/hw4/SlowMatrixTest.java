package cs311.hw4;

import java.util.ArrayList;

public class SlowMatrixTest {
	public static void main(String[] arg){
		SlowMatrixFactory smf = new SlowMatrixFactory();
		SlowMatrix s = (SlowMatrix) smf.createRandom(2);
		MeasureTimeComplexity m = new MeasureTimeComplexity();
		System.out.println(m.normalize(s, 2));
		
		@SuppressWarnings("unchecked")
		ArrayList<result> arr = (ArrayList<result>) m.measure(smf, 2, 2, 101, 1);
		for(int i = 0; i < arr.size(); i++){
			System.out.println(arr.get(i).getSize() + " " + arr.get(i).getTime());
		}
	}
}
