package calendar.graphics;

import java.util.Vector;


public class Bounds {

	public Vector<Integer> CalendarDayElementSize;
		
	public Bounds(){
		CalendarDayElementSize = new Vector<Integer>(); /*....*/add(CalendarDayElementSize,144,72);
		
	}
	
	private void add(Vector<Integer> vec,int x, int y){
		vec.addElement(x);
		vec.add(y);
	}
}
