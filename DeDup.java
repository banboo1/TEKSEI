import java.util.Arrays;
import java.util.Set;
import java.util.HashSet;
import java.util.LinkedHashSet;

public class DeDup {

    public int[] randomIntegers = {1,2,34,34,25,1,45,3,26,85,4,34,86,25,43,2,1,10000,11,16,19,1,18,4,9,3,
                                   20,17,8,15,6,2,5,10,14,12,13,7,8,9,1,2,15,12,18,10,14,20,17,16,3,6,19,
                                   13,5,11,4,7,19,16,5,9,12,3,20,7,15,17,10,6,1,8,18,4,14,13,2,11};   
    
    /**
     * remove duplicate number from int array. 
     * runtime complexity O(N)
     * @param array contains duplicates  
     * @param a set that is used for keeping unqiue value
     * @return int array without duplicates 
     */       
    private int[] removeDuplicatesInternal(final int[] duplicates, final Set<Integer> unique) {
    	if ( duplicates == null || duplicates.length <= 1 )
    		return duplicates;
    	
        for (int element : duplicates) {
            unique.add(element);
        }
        
        return unique.stream().mapToInt(Integer::intValue).toArray();           
    }

    /**
     * remove duplicate number from int array. original order may not be kept
     * use built in HashSet
     * runtime complexity O(N)
     * @param array of int with duplicates
     * @return int array without duplicates 
     */    
    public int[] removeDuplicates(final int[] duplicates, final boolean keepOrder) {
        return removeDuplicatesInternal( duplicates, keepOrder ? new LinkedHashSet<>() : new HashSet<>());
    }

    /**
     * remove duplicate number from int array. original order is kept
     * use LinkedHashSet to keep the original order 
     * runtime complexity O(N)
     * @param array of int with duplicates
     * @return int array without duplicates 
     */        
    public int[] removeDuplicateKeepOrder(final int[] duplicates) {
    	if ( duplicates == null || duplicates.length <= 1 )
    		return duplicates;
        int[] temp = new int[duplicates.length];
        int i = 0;
        Set<Integer> unique = new HashSet<>();
        for (int element : duplicates) {
            if ( unique.add(element ) ) {
                temp[i++] = element;
            }
        }

        return Arrays.copyOf(temp, i);
    }
    
    static class Entry {
    	int key;
    	Entry next;    	
    }
    
    /*
     * SimpleSet for primitive to avoid boxing / unboxing
     */
    static class SimpleHashSet{
    	Entry[] table;
    	SimpleHashSet( int capcity ) {
    		table = new Entry[capcity];
    	}
    	
    	boolean add(int value) {
            int index = (value & 0x7FFFFFFF) % table.length;
         
            if ( table[index] == null ) {
            	table[index] = new Entry();
            	table[index].key = value;
    	     	return true;	            	             	
            }
	        for ( Entry e = table[index]; e != null ; e = e.next) {
	             if ( e.key != value && e.next == null ) {
	     	        e.next = new Entry();
	    	        e.next.key = value;
	    	     	return true;	            	 
	             }
	        }
	        return false;
    	}
    }

    /**
     * remove duplicate number from int array. original order is kept
     * use customized code to implement kind of set so NO boxing/unboxing cost   
     * runtime complexity O(N)
     * @param array of int with duplicates
     * @return int array without duplicates 
     */           
    public int[] removeDuplicates(final int[] duplicates) {
    	if ( duplicates == null || duplicates.length <= 1 )
    		return duplicates;
        int[] temp = new int[duplicates.length];
        int i = 0;
        SimpleHashSet unique = new SimpleHashSet(duplicates.length);
        for (int element : duplicates) {
            if ( unique.add(element ) ) {
                temp[i++] = element;
            }
        }

        return Arrays.copyOf(temp, i);
    }
    
    public static void main(String [] args) {
    	
    	/*
    	 * All three methods implemented has runtime of O(N). this is the best we can do
    	 * since we need to iterate all elements. Implementations that needs at least O(Nlog(N)) are not shown.
    	 * Set is used to keep the unique value and both HashSet and LinkedHasSet has O(1) for insertion.
    	 * LinkedHashSet is used to keep the order. The  removeDuplicateKeepOrder implementation is more efficient 
    	 * then removeDuplicates( int[], boolean ) since it does much less boxing/unboxing operation. But it still
    	 * uses Java built in Set collection.So it still has the cost of boxing/unboxing.  the overloaded method
    	 * of removeDuplicates( int[] ) uses a customized implementation of Simple Set behaviour to avoid boxing/unboxing 
    	 * cost
    	 * 
    	 */
    	DeDup test = new DeDup();
    	
    	int[] unique1 = test.removeDuplicates( test.randomIntegers, true );     	
    	int[] unique2 = test.removeDuplicateKeepOrder( test.randomIntegers );
    	int[] unique3 = test.removeDuplicates( test.randomIntegers, false );
    	int[] unique4 = test.removeDuplicates( test.randomIntegers );
    	
    	System.out.println( Arrays.equals( unique1, unique2 ) ); // expected true, order kept
    	System.out.println( Arrays.equals( unique3, unique2 ) ); // expected false, order not kept
    	System.out.println( Arrays.equals( unique1, unique4 ) ); // expected true, order kept
    }
}

