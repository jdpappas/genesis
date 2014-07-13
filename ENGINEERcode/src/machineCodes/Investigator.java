package machineCodes;

import java.util.ArrayList;

/* Investigator class is used to find elements in Lists with specific attributes
 * to achieve that:
 *  it iterates through an ArrayList
 *  it gets the class of the specific item in the list
 *  it gets the field specified with the name given
 *  it compares it with the value given 
 *  The find method is overloaded for each type of value
 *  The above described technique is called reflection 
 *  
 *  It is important to state that in getField method the specified field MUST be PUBLIC 
 */
public class Investigator { 
	// All top level classes are by definition static. 
	// So it is meaningless to declare it again.
	
	public static <Kind> Kind find (ArrayList<Kind> coll, String field, String value) {
		Kind answer = null;
		for (Kind obj: coll){
			try {
				if (obj.getClass().getField(field).get(obj).equals(value)) {
					answer = obj;
				} // close if
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchFieldException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		} // close loop
		return answer;
	} // close find method
	
	public static <Kind> Kind find (ArrayList<Kind> coll, String field, char value) {
		Kind answer = null;
		for (Kind obj: coll){
			String temp = null;
			try {
				temp = (String) obj.getClass().getField(field).get(obj);
			} catch (IllegalArgumentException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IllegalAccessException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (NoSuchFieldException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (SecurityException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
				if (temp.charAt(0)==value) {
					answer = obj;
				} // close if
			
		} // close loop
		return answer;
	} // close find method
	
	public static <Kind> Kind find (ArrayList<Kind> coll, String field, int value) {
		Kind answer = null;
		for (Kind obj: coll){
			int temp = 0;
			try {
				temp = obj.getClass().getField(field).getInt(obj);
			} catch (IllegalArgumentException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IllegalAccessException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (NoSuchFieldException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (SecurityException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			if (temp==value) {
				answer = obj;
			} // close if
			
		} // close loop
		return answer;
	} // close find method
	
} // close Investigator class