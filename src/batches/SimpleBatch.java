package batches;

import memory.FirstFit;
import memory.Memory;
import memory.Pointer;

public class SimpleBatch {

	public static void main(String[] args) {
		SimpleBatch batch = new SimpleBatch();
		batch.run();
	}
	
	public int[] range(int start, int stop) {
		int[] range = new int[stop - start + 1];
		
		for (int i = 0; i < range.length; i++) {
			range[i] = start + i;
		}
		
		return range;
	}

	public void run() {
		Memory m = new FirstFit(100, true); // size, boolean for compacting(true) or not
		Pointer p1, p2, p3, p4, p5, p6;
		
		p1 = m.alloc(20);
		p1.write(range(1, 20));
		p2 = m.alloc(5);
		p2.write(range(38, 42));
		p3 = m.alloc(30);
		p3.write(range(100, 129));
		p4 = m.alloc(15);
		p4.write(range(101, 115));
		m.release(p3);
		m.release(p1);
		p5 = m.alloc(10);
		p5.write(range(70, 79));
		p6 = m.alloc(15);
		p6.write(range(1, 15));
		
		m.printLayout();

//		m.compact();
//		System.out.println("After compact()");
//		m.printLayout();
	}
}
