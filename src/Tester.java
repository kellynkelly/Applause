/**
 * Simple class to maintain the testers full name and their experience score
 * For sorting, this class implements the Comparable interface.
 * 
 * @author Kelly Kelly
 *
 */
public class Tester implements Comparable<Tester>{
	private String name = null;
	private int experience = 0;
	
	public Tester(String name) {
		this.name = name;
	}
	
	public Tester(String firstName, String lastName) {
		this.name = firstName + " " + lastName;
	}
	
	public void addBug() {
		experience++;
	}
	
	@Override
	public String toString() {
		return this.name + "=> " + this.experience; 
	}

	@Override
	public int compareTo(Tester arg0) {
		
		if(this.experience < arg0.experience)
			return -1;
		else if(this.experience > arg0.experience)
			return 1;
		
		return 0;
	}
	
}
