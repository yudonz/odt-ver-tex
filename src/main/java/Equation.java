/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * This calss is written by Paul, but it has some problems, so i write another class Formule 
 */
public class Equation {
    
    private String in;
    
    Equation(String s){
        this.in=s;
    }
    
    public String get(){
    	in = "$$" + in + "$$";
        return this.in;
    }


//1er equation = "int from{0} to{+ infinity } t^{x - 1} e^{-t} dt "   
//--->   \int_{0}^{+\infty} t^{x-1} e{-t} dt
    
//2eme = "= lim from{n -> + infinity} {{n!n^z} over {z(z+1)...(z+n)}}"   
//--->  = \lim_{n\to\+\infty} \frac {n!n^z} {z(z+1)...(z+n)}

//3eme = "= {1} over {z} prod from{k=1} to{+infinity}{{(1+ {1} over {k})^z} over {1 + {z} over {k} }}"  
//--->   = \frac {1} {z} \prod_{k=1}^{+\infty} \frac {(1 + \frac {1}{k})^z} {1 + \frac {z}{k}}
    
    
    
//Remove bold and size x things from the odt equation
        //Then remove the useless \
        //Then change the " rSub " to "_"
        public void equation(){   
  
            //First we remove the bold and size terms with their respective { and }
            String[] tempo = in.split("[{]");
            int nbBoldSize = 0;
            String newString = "";
            int i;
            for(i = 0; i<tempo.length;i++){
                //System.out.println(tempo[i]);
                if(tempo[i].contains("bold") || (tempo[i].contains("size")) || (tempo[i].contains("ital")) || (tempo[i].contains("font"))){
                    nbBoldSize = nbBoldSize +1;
                }
                else{
                    if("".equals(newString)){newString = tempo[i];}
                    else{newString = newString + '{' +tempo[i];}
                }
            }
            char[] tab = newString.toCharArray();
            int tabtempo[] = {0,0,0,0,0,0,0};
            int j = 0;
            int count = 0;
            newString ="";
            for(i=0; i<tab.length;i++){
                if(tab[i] == '{'){count = count +1;
                }
                if(tab[i] == '}'){
                    count = count - 1;
                    if(count <= -1){
                        tabtempo[j] = i;
                        count = count +1;
                        j++;
                    }
                }
            }
            boolean aAjouter;
            for(i=0;i<tab.length;i++){
                aAjouter = true;
                for(j=0;j<7;j++){
                    if((i == tabtempo[j]) && i !=0){
                        aAjouter = false;
                    }
                }
                if(aAjouter){newString = newString + tab[i];}
            }
            

            System.out.println(newString);
            //Transformation de divers op閞ateurs unaires/binaires
            newString = transformTerm(newString, "\\\\", "");
            newString = transformTerm(newString, "rSub", "_");
            newString = transformTerm(newString, "cdot", "\\cdot");
            newString = transformTerm(newString, "times", "\\times");
            newString = transformTerm(newString, "\\*", "\\ast");
            newString = transformTerm(newString, "div", "\\div");
            newString = transformTerm(newString, "\\+\\-", "\\pm");
            newString = transformTerm(newString, "\\-\\+", "\\mp");
            newString = transformTerm(newString, "\\<\\>", "\\ne");
            newString = transformTerm(newString, "\\<\\<", "\\ll");
            newString = transformTerm(newString, "\\>\\>", "\\gg");
            newString = transformTerm(newString, "\\>\\=", "\\geq");
            newString = transformTerm(newString, "\\<\\=", "\\leq");
            newString = transformTerm(newString, "leslant", "\\legslant");
            newString = transformTerm(newString, "geslant", "\\geqslant");
            newString = transformTerm(newString, "approx", "\\approx");
            newString = transformTerm(newString, "sim", "\\sim");
            newString = transformTerm(newString, "simeq", "\\simeq");
            newString = transformTerm(newString, "equiv", "\\equiv");
            newString = transformTerm(newString, "ω", "\\omega ");
            newString = transformTerm(newString, "π", "\\pi ");
            newString = transformTerm(newString, "lInt", "\\oint");
            newString = transformTerm(newString, "rSup", "^");
            newString = transformTerm(newString, "α", "\\alpha ");
            newString = transformTerm(newString, "γ", "\\gamma ");
            newString = transformTerm(newString, "β", "\\beta ");
            newString = transformTerm(newString, "δ", "\\delta ");
            newString = transformTerm(newString, "υ", "\\nu ");
            
            
            
            //Transformation de la mise en forme de la fraction
            newString = switchFrac(newString);
            newString = transformTerm(newString, "over", "\\frac");
            
            //Ajout de \ pour diverses fonctions
            newString = transformTerm(newString, "sqrt", "\\sqrt");
            newString = transformTerm(newString, "arcsin", "\\arccos");
            newString = transformTerm(newString, "arctan", "\\arctan");
            newString = transformTerm(newString, "coth", "\\coth");
            newString = transformTerm(newString, "sinh", "\\sinh");
            newString = transformTerm(newString, "cosh", "\\cosh");
            newString = transformTerm(newString, "tanh", "\\tanh");
            newString = transformTerm(newString, "cos", "\\cos");
            newString = transformTerm(newString, "sin", "\\sin");
            newString = transformTerm(newString, "tan", "\\tan");
            newString = transformTerm(newString, "cot", "\\cot");
            
            //Fonctions logarithmiques
            newString = transformTerm(newString, "ln", "\\ln");
            newString = transformTerm(newString, "log", "\\log");
            newString = transformTerm(newString, "exp", "\\exp");
            
            //Fonctions de sup閞iorit� et de limites
            newString = transformTerm(newString, "max", "\\max");
            newString = transformTerm(newString, "min", "\\min");
            newString = transformTerm(newString, "sup", "\\sup");
            newString = transformTerm(newString, "inf", "\\inf");
            newString = transformTerm(newString, "lim", "\\lim");
            newString = transformTerm(newString, "infinity", "infty");
            
            newString = transformTerm(newString, "int from", "\\int_");
            newString = transformTerm(newString, "prod from", "\\prod_");
            newString = transformTerm(newString, "lim from", "lim_");
            //la ligne suivante peut causer des erreurs si d'autres "to" sont pr閟ents dans les expressions ou dans des mots
            newString = transformTerm(newString, "to", "^");
            newString = transformTerm(newString, "\\-\\>", "\\to\\");
            
            
            
            
            in = newString;
            //return newString;
        }





    public String transformTerm(String toTransform, String oldTerm,  String newTerm){
            String[] temporaire = toTransform.split(oldTerm);
            toTransform = temporaire[0];
            int i;
            for (i=1;i<temporaire.length;i++){
                toTransform = toTransform + newTerm + temporaire[i];
            }
            return toTransform;
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
