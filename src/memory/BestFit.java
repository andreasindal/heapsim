package memory;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * This memory model allocates memory cells based on the best-fit method.
 * 
 * @author "Johan Holmberg, Malmö university"
 * @since 1.0
 */
public class BestFit extends Memory {

	/**
	 * Initializes an instance of a best fit-based memory.
	 * 
	 * @param size The number of cells.
	 */
	public BestFit(int size) {
		super(size);
		pointers = new LinkedList<>();
		pointerSize = new HashMap<>();
	}

	/**
	 * Allocates a number of memory cells. 
	 * 
	 * @param size the number of cells to allocate.
	 * @return The address of the first cell.
	 */
	@Override
	public Pointer alloc(int size) {
		int address = 0;

		if (pointers.isEmpty()) {
			return addPointer(address, size);
		}

		System.out.println(getAvailable());

		for (Range range : this.getAvailable()) {
			if (size <= range.size()) {
				return addPointer(range.from, size);
			}
		}

		return new Pointer(this);
	}

	public List<Range> getAvailable() {
		int address = 0;
		List<Range> list = new LinkedList<>();
		for (Pointer p : pointers) {
			if (address < p.pointsAt()) {
				list.add(new Range(address, p.pointsAt() - 1));
			}
			address = p.pointsAt() + pointerSize.get(p);
		}

		if (address != cells.length) {
			list.add(new Range(address, cells.length - 1));
		}

		list.sort((r1, r2) -> r1.size() - r2.size());
		return list;
	}


	
	/**
	 * Releases a number of data cells
	 * 
	 * @param p The pointer to release.
	 */
	@Override
	public void release(Pointer p) {
		pointers.remove(p);
		pointerSize.remove(p);
		sort();
	}

	/**
	 * Prints a simple model of the memory.
	 *
	 * |    0 -  110 | Allocated
	 * |  111 -  150 | Free
	 * |  151 -  999 | Allocated
	 * | 1000 - 1024 | Free
	 */
	@Override
	public void printLayout() {
		int address = 0;
		System.out.println("------------------------------------------");
		for (Pointer p: pointers) {
			if (address < p.pointsAt()) {
				System.out.format("| %4d - %4d | Free %n", address, (p.pointsAt() - 1));
			}
			System.out.format("| %4d - %4d | Allocated  (pointerSize is %d)%n", p.pointsAt(), (p.pointsAt() + pointerSize.get(p) - 1), pointerSize.get(p));
			address = p.pointsAt() + pointerSize.get(p);
		}

		if(address < cells.length) {
			System.out.format("| %4d - %4d | Free %n", address, cells.length);
		}
		System.out.println("------------------------------------------");
	}

	private class Range {
		int from, to;

		Range(int from, int to) {
			this.from = from;
			this.to = to;
		}

		int size() {
			return to - from + 1;
		}

		@Override
		public String toString() {
			return String.format("%s - %s (%s)", from, to, size());
		}
	}
}
