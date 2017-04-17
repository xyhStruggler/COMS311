package cs311.hw4;

public class result implements IResult{
public int size;
public long time;

public result(int size, long time){
	this.size = size;
	this.time = time;
}
	@Override
	public int getSize() {
		// TODO Auto-generated method stub
		return size;
	}

	@Override
	public long getTime() {
		// TODO Auto-generated method stub
		return time;
	}

}
