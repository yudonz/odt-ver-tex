import java.util.ArrayList;

/*
 * This class is just to test the function equation
 * */
public class Test_equation {

	public static void main(String[] args) {
//		String in="size 12{x \\( t \\) =s \\( f rSub { size 8{¦Ø} }  \\( t \\)  \\) } {}";
		String in=" size 12{ lInt rSub { size 8{C} }  {F \\( z \\)  ital \"dz\"=2¦Ðj Sum cSub { size 8{k} }  {\"Re\"s left [F \\( z \\) ,p rSub { size 8{k} }  right ]} } } {}";
//		String in=" size 12{f rSub { size 8{¦Ø} }  \\( t \\) = {  {1}  over  {2¦Ðj} }  lInt rSub { size 8{C} }  { {  {v rSup { size 8{ - 1k} } d¦Ô}  over  { \\( 1 - ¦Âv rSup { size 8{ - 1} }  \\)  \\( v rSup { size 8{ - 1} }  - ¦Â \\) } } } } {}";

//		String out1=get1(in);
//		System.out.println(out1);

		
		Equation e1=new Equation(in);
		e1.equation();
		System.out.println(e1.get());
		
		Formule e2=new Formule(in);
		e2.build();
		System.out.println(e2.get());
		

	}
	

	
	public static String get1(String s) {
		String out = "";
//		System.out.println(s);
		if (s.indexOf("size")!=-1) {
			int p_size=s.indexOf("size");
//			System.out.println("the first  is "+p_size);
			ArrayList<Character> string=new ArrayList<Character>();
			int j = 0,p_left = -1,p_right = s.length()+1;
			for(int i=0;i<s.length();i++) {
				string.add(s.charAt(i));
				if(s.charAt(i)=='{'&&i>p_size) {
					j++;
					if(j==1&&p_left==-1) {
						p_left=i;
//						System.out.println("the first { is "+p_left);
					}						
				}
				else if(s.charAt(i)=='}'&&i>p_size) {
					j--;
					if(j==0&&p_right==s.length()+1) {						
						p_right=i;
//						System.out.println("the first } is "+p_right);											
					}
				}
			}
			string.remove(p_right);
			for(int i=p_left;i>=p_size;i--)
				string.remove(i);
			for(Character c:string)
				out+=c;		
		}
		if(out.indexOf("size")!=-1) {
//			System.out.println();
			out=get1(out);
		}			
		return out;	
	}
	


}
