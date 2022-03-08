import java.util.Arrays;
public class JohnTrotter {
	static int[] ary;
	static boolean[] mov;
	static int max = 0, count = 1, n = 0;
	static Queue<String> q = new Queue<String>();
	static String str = "";
	/*
	 * The main method will check for inputs and run the permutations. The array permutation shifts are dependant upon the boolean array, which are used to decide the direction of the arrows of each corrosponding index of the numerical array, as well as which index eligable to move. A queue is used instead of straight string concatonation for the printing because I have found that it is just faster; probably due to the garbage collector.
	 * The while loop the index to be shifted is -1; The index of the element to be moved is given by the movable(int a) method, and the 'arrows' are checked by the flips(int num) method. hold and h are used for swapping.
	 * show is used to check if the program should even bother enqueueing the permutations. If not then just skip.
	 * in mov[], true == left and false == right
	 */
	public static void main(String[] args){
		count = 1;
		boolean show = false;
		try{
			n = Integer.parseInt(args[0]);
			show = Boolean.parseBoolean(args[1]);
		} catch(Exception e){
			System.err.println("Error:\nUse case =>java JT <n> <true/false>");
			System.exit(0);
		}
		if(n <= 0){
			System.out.printf("\n%s array: No possible permutations.\n\n", n==0 ? "Empty" : "Negative");
			System.out.println("The program generated 0 permutations.");
			System.exit(0);
		}
		mov = new boolean[n];
		ary = new int[n];
		long start, stop, overhead;
		start = System.nanoTime();
		stop = System.nanoTime();
		overhead = stop-start;
		for(int i = 0; i < n; i++){
			mov[i] = true;
			ary[i] = i+1;
		}
		max = ary[n-1];
		int i = movable(n-1), hold = 0;
		boolean h = true;
		System.out.print("    ");
		start = System.nanoTime();
		if(show){
			print();
			//testingPrint(i);
		}
		while(i != -1){
			if(mov[i]){
				hold = ary[i];
				ary[i] = ary[i-1];
				ary[i-1] = hold;
				h = mov[i];
				mov[i] = mov[i-1];
				mov[i-1] = h;
				i-=1;
			} else{
				hold = ary[i];
				ary[i] = ary[i+1];
				ary[i+1] = hold;
				h = mov[i];
				mov[i] = mov[i+1];
				mov[i+1] = h;
				i+=1;
			}
			flips(ary[i]);
			i = movable(i);
			if(show){
				print();
				//testingPrint(i);
			}
			count+=1;
		}
		stop = System.nanoTime();
		start = stop - start - overhead;
		System.out.printf("\nThe program generated %d permutation(s).\n\n", count);
		while(!q.isEmpty())  System.out.print(q.dequeue());
		System.out.printf("\n\nTime(ns): %d", start);
		//return start;
	}

	/*
	 * flips will filp the arrows for any element bigger than num (not an index). If the number passed is the max then just return, as there is no element bigger than the max. a is assigned to the number passed.
	 * The condition a!=max in the for loops are used to decide when to exit the loop. If the number passed is 5, and the max is 7, there will only be 2 flips required, as only 6 and 7 are bigger.
	 * So when 6 is compared to 5, 'a' will iterate to 6; when 7 is compared to 5, a will iterate to 7; now the condition a!=max is met, performing the 2 flips required. There is no reason to compare any numbers, so the loop exits.
	 * s is used to cut time iterating through the array looking to find those bigger numbers, by making the comparisons start near the bigger numbers. Starting at the beginning or the end of the array.
	 * Because the arrows are pointing to the left, as well as the array being sorted from least to greatest, the largset number at n-1, will move towards the left, stopping at the beginning, so when the number moving isn't the biggest number, the method will be able to check for flips, which will start at the beginning where the biggest would be at, once fliped the condition to decide where to start (s) will then be !s, changing the start to the end, as the next time a number will be able to check for flips is when the biggest number is back at the end of the array, hence why it starts there now. The start will "flip flop" along with the biggest number "bouncing" from end to end predictablly.
	 */
	static boolean s = true;
	private static void flips(int num){
		if(num == max)
			return;
		int a = num;
		if(s){
			for(int i = 0; a != max; i++)
				if(ary[i] > num){
					mov[i] = !mov[i];
					a+=1;
				}
		} else{
			for(int j = n-1; a!=max; j--)
				if(ary[j] > num){
					mov[j] = !mov[j];
					a+=1;
				}
		}
		s=!s;
	}


	/*
	 * movable will return the index of the element to be shifted. movable has a parameter to prevent any unessesary array iteration and comparisons.
	 * The number passed is only used to check if the number to be moved is the max, I saw this as usefull as the number that moves the most is the max. The condition will check if it's the max, then, by ternary operators, check if the max is NOT at the front or the end of the array, if so, then return true, as the arrows will not effect the moblility as it is the biggest number. If the max IS at the front or end of the array, see if it's either the front or the back, then check the arrows to see if it's eligible to move according to which end it's at, return true or false. If true, then return back the index of the max as there is no need to check any numbers. Else process with comparisons.
	 * If the number passed is not the biggest then process with the comparisons. As the main() while loop will stop when the index is -1, ind and m are initialized to -1. Any number eligible to move will change it. This method will return -1, as no number changes it, indicating that no numbers are eligible to move.
	 * The comparisons will check the ends (when encountered) and it's arrows, skipping them if the arrows themselves prevents them to move, regardless of their number.
	 * When not the ends, comparisons will, again, use ternary operators, comparing indexes depending on the arrows, either returning the bigger index, or re-returning ind. m is used to keep track of the biggest found index.
	 * After all is done, return m. Added parenthesis for (slightly) better readablility.
	 */
	private static int movable(int a){
		if(ary[a] == max && ((a!=0 && a!=n-1) ? true : (a==0 ? !mov[a] : mov[a])))
			return a;
		int ind = -1, m = -1;
		for(int i = 0; i < n; i++){
			if(i == 0 && mov[i])
				continue;
			if(i == n-1 && !mov[i])
				continue;
			ind = mov[i] ? ((ary[i] > ary[i-1]) ? i : ind) : ((ary[i] > ary[i+1]) ? i : ind);
			if(m != -1)
				m = (ary[m] > ary[ind]) ? m : ind;
			else
				m = ind;
		}
		return m;
	}

	/*
	 * print() stores permutations and formats the strings. Dequeued and printed after timing.
	 */
	private static void prnt(){
		if(count%7==0) q.enqueue("\n    ");// str += "\n    ";
		for(int i : ary)
			q.enqueue(String.format("%-2d ",i));
		q.enqueue("   ");
	}
	
	private static void print(){
		if(count%7==0) str += ("\n    ");// str += "\n    ";
		for(int i : ary)
			str += (String.format("%-2d ",i));
		System.out.printf("%s   ", str);
		str = "";
	}

	//Used for testing; Shows the current array and the direction of all elements' arrows
	private static void testingPrint(int i){
		System.out.printf("\nArray:    %s\nArrows:   %s\n\n", s,Arrays.toString(ary), Arrays.toString(mov));
	}

}
