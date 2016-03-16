package memory;

import java.util.HashMap;
import java.util.Map;

/**
 * This memory model allocates memory cells based on the first-fit method. 
 * 
 * @author "Johan Holmberg, Malmö university"
 * @since 1.0
 */
public class FirstFit extends Memory {

	private HashMap<Pointer, Integer> pointerSize;
	/**
	 * Initializes an instance of a first fit-based memory.
	 * 
	 * @param size The number of cells.
	 */
	public FirstFit(int size) {
		super(size);
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

        for (Map.Entry<Pointer, Integer> entry: pointerSize.entrySet()) {
            Pointer p = entry.getKey();
            if(p.pointsAt() - address > size) {
                return addPointer(address,size);
            }
            address = p.pointsAt() + entry.getValue();
        }
		if (cells.length  - address > size) {
			return addPointer(address, size);
		}
		return null;
	}

	private Pointer addPointer(int address, int size) {
		Pointer pointer = new Pointer(this);
		pointer.pointAt(address);
		pointerSize.put(pointer, size);
		return pointer;
	}

	/**
	 * Releases a number of data cells
	 * 
	 * @param p The pointer to release.
	 */
	@Override
	public void release(Pointer p) {
        pointerSize.remove(p);
	}
	
	/**
	 * Prints a simple model of the memory. Example:
	 * 
	 * |    0 -  110 | Allocated
	 * |  111 -  150 | Free
	 * |  151 -  999 | Allocated
	 * | 1000 - 1024 | Free
	 */
	@Override
	public void printLayout() {
        int counter = 0;

        for (Map.Entry<Pointer, Integer> entry: pointerSize.entrySet()) {
            Pointer p = entry.getKey();
            if (counter < p.pointsAt()) {
                System.out.println("" + counter + " - " + (p.pointsAt() - 1) + " Free");
            }
            System.out.println("" + p.pointsAt() + " - " + (p.pointsAt() + entry.getValue() - 1) + " Allocated  (pointerSize is " + entry.getValue() + ")");
            counter = p.pointsAt() + entry.getValue();
        }
        if(counter < cells.length) {
            System.out.println("" + counter + " - " + cells.length + " Free");
        }
    }

	/**
	 * Compacts the memory space.
	 */
	public void compact() {
		// TODO Implement this!
	}
}
