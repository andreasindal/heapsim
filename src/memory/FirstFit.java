package memory;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * This memory model allocates memory cells based on the first-fit method. 
 * 
 * @author "Johan Holmberg, Malmö university"
 * @since 1.0
 */
public class FirstFit extends Memory {
    private LinkedList<Pointer> pointers;
	private HashMap<Pointer, Integer> pointerSize;
    private boolean compacting;

	/**
	 * Initializes an instance of a first fit-based memory.
	 * 
	 * @param size The number of cells.
     * @param compacting To compact(true) or not
	 */
	public FirstFit(int size, boolean compacting) {
		super(size);
        pointers = new LinkedList<>();
		pointerSize = new HashMap<>();
        this.compacting = compacting;
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

        for (Pointer p: pointers) {
            if ((p.pointsAt() - address) > size) {
                return addPointer(address, size);
            }
            address = p.pointsAt() + pointerSize.get(p);
        }

		if ((cells.length  - address +1) > size) {
			return addPointer(address, size);
		}

		return new Pointer(this);
	}

	private Pointer addPointer(int address, int size) {
		Pointer pointer = new Pointer(this);
		pointer.pointAt(address);
        pointers.add(pointer);
		pointerSize.put(pointer, size);
        sort();
		return pointer;
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
        if (compacting)
        compact();
	}

    /**
     * Method sorting the pointers by pointer address ascending.
     */
    private void sort() {
        pointers.sort((p1, p2) -> {
            return p1.pointsAt() - p2.pointsAt();
        });
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

	/**
	 * Compacts the memory space.
	 */
	public void compact() {
		int address = 0;

        for (Pointer p: pointers) {
            p.pointAt(address);
            address = address + pointerSize.get(p);
        }

	}

}
