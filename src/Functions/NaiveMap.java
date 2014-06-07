package Functions;

import java.util.ArrayList;

public class NaiveMap {
	public static ArrayList<Integer> NMap(char race, char gender){
//		For semantic features, we have 12 leaves: {wf,bf,af,if,mf,of,wm,bm,am,im,mm,om}, where the first character represents the race value and the second represents the gender value. The semantic intepretation should be:
//		(1) map each record without unknown values in race and gender to one of the values in {wf,bf,af,if,mf,of,wm,bm,am,im,mm,om};
//		(2) map each record whose values in race and gender are both unknown to all of the values in {wf,bf,af,if,mf,of,wm,bm,am,im,mm,om};
//		(3) map the other records with partial unknown values (either in race or in gender) as follows:
		ArrayList<Integer> Nmap =new ArrayList<>();
		for(int i=0;i<12;i++){
			Nmap.add(0);
		}
		if(race=='u' && gender=='u'){
			for(int i=0;i<12;i++){
				Nmap.set(i, 1);
			}
			return Nmap;
		}
		if(race=='u'){
			switch(gender){
				case 'f': for(int i=0;i<6;i++){
					Nmap.set(i, 1);
				}
				break;
				case 'm': for(int i=6;i<12;i++){
					Nmap.set(i, 1);
				}
				break;
			
			}
			return Nmap;				
		}
		if(gender=='u'){
			switch(race){
				case 'w': Nmap.set(0,1);Nmap.set(6,1);
				break;
				case 'b': Nmap.set(1,1);Nmap.set(7,1);
				break;
				case 'a': Nmap.set(2,1);Nmap.set(8,1);
				break;
				case 'i': Nmap.set(3,1);Nmap.set(9,1);
				break;
				case 'm': Nmap.set(4,1);Nmap.set(10,1);
				break;
				case 'o': Nmap.set(5,1);Nmap.set(11,1);
				break;
			}
			return Nmap;	
		}
		if(gender=='f'){
			switch(race){
				case 'w': Nmap.set(0,1);
				break;
				case 'b': Nmap.set(1,1);
				break;
				case 'a': Nmap.set(2,1);
				break;
				case 'i': Nmap.set(3,1);
				break;
				case 'm': Nmap.set(4,1);
				break;
				case 'o': Nmap.set(5,1);
				break;
			}
			return Nmap;
		}
		else{
			switch(race){
				case 'w': Nmap.set(6,1);
				break;
				case 'b': Nmap.set(7,1);
				break;
				case 'a': Nmap.set(8,1);
				break;
				case 'i': Nmap.set(9,1);
				break;
				case 'm': Nmap.set(10,1);
				break;
				case 'o': Nmap.set(11,1);
				break;
			}
			return Nmap;
		}
	}

}
