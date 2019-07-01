import java.util.ArrayList;

/*
 * This class is written to deal with the equation  
 * */

public class Formule {

	private String string;
	
	public Formule(String s) {
		this.string=s;
	}
	
	public String get() {
		string="$$"+string+"$$";
		return this.string;
	}
	
	public void build() {
		string=delate(string);
		 string = transformTerm(string, "\\\\", "");
         string = transformTerm(string, "rSub", "_");
         string = transformTerm(string, "cdot", "\\cdot");
         string = transformTerm(string, "times", "\\times");
         string = transformTerm(string, "\\*", "\\ast");
         string = transformTerm(string, "div", "\\div");
         string = transformTerm(string, "\\+\\-", "\\pm");
         string = transformTerm(string, "\\-\\+", "\\mp");
         string = transformTerm(string, "\\<\\>", "\\ne");
         string = transformTerm(string, "\\<\\<", "\\ll");
         string = transformTerm(string, "\\>\\>", "\\gg");
         string = transformTerm(string, "\\>\\=", "\\geq");
         string = transformTerm(string, "\\<\\=", "\\leq");
         string = transformTerm(string, "leslant", "\\legslant");
         string = transformTerm(string, "geslant", "\\geqslant");
         string = transformTerm(string, "approx", "\\approx");
         string = transformTerm(string, "sim", "\\sim");
         string = transformTerm(string, "simeq", "\\simeq");
         string = transformTerm(string, "equiv", "\\equiv");
         
         string = transformTerm(string, "ω", "\\omega ");
         string = transformTerm(string, "π", "\\pi ");
         string = transformTerm(string, "lInt", "\\oint ");
         string = transformTerm(string, "rSup", "^");
         string = transformTerm(string, "α", "\\alpha ");
         string = transformTerm(string, "γ", "\\gamma ");
         string = transformTerm(string, "β", "\\beta ");
         string = transformTerm(string, "δ", "\\delta ");
         string = transformTerm(string, "υ", "\\nu ");
         string = transformTerm(string, "ital", "");
         string = transformTerm(string, "\"", "");
         string = transformTerm(string, "Sum", "\\sum ");
         string = transformTerm(string, "cSub", "_");
         string = transformTerm(string, "left", "");
         string = transformTerm(string, "right", "");
         
         
         
         
         
         //Transformation de la mise en forme de la fraction
         string = switchFrac(string);
         string = transformTerm(string, "over", "\\frac");
         
         //Ajout de \ pour diverses fonctions
         string = transformTerm(string, "sqrt", "\\sqrt");
         string = transformTerm(string, "arcsin", "\\arccos");
         string = transformTerm(string, "arctan", "\\arctan");
         string = transformTerm(string, "coth", "\\coth");
         string = transformTerm(string, "sinh", "\\sinh");
         string = transformTerm(string, "cosh", "\\cosh");
         string = transformTerm(string, "tanh", "\\tanh");
         string = transformTerm(string, "cos", "\\cos");
         string = transformTerm(string, "sin", "\\sin");
         string = transformTerm(string, "tan", "\\tan");
         string = transformTerm(string, "cot", "\\cot");
         
         //Fonctions logarithmiques
         string = transformTerm(string, "ln", "\\ln");
         string = transformTerm(string, "log", "\\log");
         string = transformTerm(string, "exp", "\\exp");
         
         //Fonctions de sup閞iorit� et de limites
         string = transformTerm(string, "max", "\\max");
         string = transformTerm(string, "min", "\\min");
         string = transformTerm(string, "sup", "\\sup");
         string = transformTerm(string, "inf", "\\inf");
         string = transformTerm(string, "lim", "\\lim");
         string = transformTerm(string, "infinity", "infty");
         
         string = transformTerm(string, "int from", "\\int_");
         string = transformTerm(string, "prod from", "\\prod_");
         string = transformTerm(string, "lim from", "lim_");
         //la ligne suivante peut causer des erreurs si d'autres "to" sont pr閟ents dans les expressions ou dans des mots
         string = transformTerm(string, "to", "^");
         string = transformTerm(string, "\\-\\>", "\\to\\");
	}
	
	public String delate(String s) {
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
			out=delate(out);
		}			
		return out;	
	}
	
	 public String transformTerm(String s, String oldTerm,  String newTerm){
         String[] temporaire = s.split(oldTerm);
         s = temporaire[0];
         int i;
         for (i=1;i<temporaire.length;i++){
             s = s + newTerm + temporaire[i];
         }
         return s;
	 }
	 
	 public String switchFrac(String toTransform){
	        //Copie du terme entre {} pr閏閐ent \frac
	        String[] temp = toTransform.split("over");
	        String str;
	        int i;
	        //Pour chaque \frac rencontr�, il faut replacer le terme au bon endroit
	        for(i=0;i<temp.length-1;i++){
	            
	            str = updateFraction(temp[i],temp[i+1]);
	            temp[i+1] = str;
	        }
	        
	        return temp[temp.length-1];
	    }
	    
	    


	    public String updateFraction(String leftTerm, String rightTerm){
	        char[] tabLeft = leftTerm.toCharArray();
	        int i, j;
	        int count = 0;
	        char x1,x2;
	        String newString = "";
	        x1 = '}';
	        x2 = '{';
	        //quel symb鬺e pour les diviseurs/dividendes ?
	        for(i=tabLeft.length;i>=0;i--){
	            if(tabLeft[tabLeft.length - 1] == '}'){x1='}';x2='{';break;}
	            if(tabLeft[tabLeft.length - 1] == ')'){x1=')';x2='(';break;}
	        }
	        
	        //
	        for(i=tabLeft.length - 1; i>=0; i--){
	            if(tabLeft[i] == x1){
	                count = count + 1;
	            }
	            if(tabLeft[i] == x2){
	                count = count - 1;
	                //Si on trouve le bon i pour d閘imiter la partie � copier
	                if(count <= 0){
	                    //Alors on copie
	                    for(j=0;j<i;j++){
	                        newString = newString + tabLeft[j];
	                    }
	                    leftTerm = newString;
	                    newString = "";
	                    for(j=i;j<tabLeft.length;j++){
	                        newString = newString + tabLeft[j];
	                    }
	                    rightTerm = "over " + newString + rightTerm;
	                    break;
	                }
	            }
	        }
	        newString = leftTerm + rightTerm;
	        return newString;
	        //Le rightTerm contiendra la fraction reconstruite ainsi que la partie du leftTerm non modifi�
	    }
	 
	
	
}
